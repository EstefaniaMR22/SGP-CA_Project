package model.dao;

import controller.exceptions.LimitReachedException;
import model.dao.interfaces.ICuentaDAO;
import model.domain.Member;
import model.domain.Participation;
import model.domain.ParticipationType;
import utils.Database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CuentaDAO implements ICuentaDAO {
    private Database database;

    public CuentaDAO() {
        database = new Database();
    }

    @Override
    public boolean changePasswordByIdUser(String password, int idUser) throws SQLException {
        int rowsAffected = 0;
        try (Connection conn = database.getConnection()) {
            conn.setAutoCommit(false);
            String statement = "UPDATE Cuenta SET contrasena = SHA2(?, 512) WHERE id_miembro = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, password);
            preparedStatement.setInt(2, idUser);
            rowsAffected = preparedStatement.executeUpdate();
            conn.commit();
        }
        return rowsAffected > 0;
    }

    @Override
    public boolean generatePasswordRecoveryCodeByEmail(String emailRecovery) throws SQLException {
        int rowsAffected = 0;
        try (Connection conn = database.getConnection()) {
            conn.setAutoCommit(false);
            String statement = "UPDATE Cuenta SET codigo_recuperacion = SUBSTRING(MD5(RAND()),-8) WHERE email = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, emailRecovery);
            rowsAffected = preparedStatement.executeUpdate();
            conn.commit();
        }
        return rowsAffected > 0;
    }

    @Override
    public boolean changePasswordByEmail(String password, String email) throws SQLException {
        boolean isUpdated = false;
        try (Connection conn = database.getConnection()) {
            conn.setAutoCommit(false);
            String statement = "UPDATE Cuenta SET contrasena = SHA2(?,512) WHERE email = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, password);
            preparedStatement.setString(2, email);
            isUpdated = preparedStatement.executeUpdate() > 0;
            conn.commit();
        }
        return isUpdated;
    }

    @Override
    public String getRecoveryCodeByEmail(String emailRecovery) throws SQLException {
        String code = null;
        try (Connection conn = database.getConnection()) {
            conn.setAutoCommit(false);
            String statement = "SELECT codigo_recuperacion FROM Cuenta WHERE email = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, emailRecovery);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                code = resultSet.getString("codigo_recuperacion");
            }
            conn.commit();
        }
        return code;
    }

    @Override
    public int getAttemptsByMacAddress(String address) throws SQLException {
        int attempts = -1;
        try (Connection conn = database.getConnection()) {
            conn.setAutoCommit(false);
            String statement = "SELECT intentos FROM Host WHERE mac_address = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, address);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                attempts = resultSet.getInt("intentos");
            }
            conn.commit();
        }
        return attempts;
    }

    @Override
    public boolean sendActualMacAddress(String address) throws SQLException, LimitReachedException {
        boolean executed;
        try (Connection conn = database.getConnection()) {
            conn.setAutoCommit(false);
            String statement = "SELECT mac_address, intentos FROM Host WHERE mac_address = ? LIMIT 1";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, address);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                if (resultSet.getInt("intentos") < 5) {
                    String increaseAttempts = "UPDATE Host SET intentos = intentos + 1 WHERE mac_address = ?";
                    preparedStatement = conn.prepareStatement(increaseAttempts);
                    preparedStatement.setString(1, address);
                    executed = preparedStatement.executeUpdate() > 0;
                } else {
                    throw new LimitReachedException("Maximo de intentos alcanzados");
                }
            } else {
                String createAttempt = "INSERT INTO Host(intentos, mac_address ) VALUES(1,?)";
                preparedStatement = conn.prepareStatement(createAttempt);
                preparedStatement.setString(1, address);
                executed = preparedStatement.executeUpdate() > 0;
            }
            conn.commit();
        }
        return executed;
    }

    @Override
    public boolean resetAttempts(String address) throws SQLException {
        boolean executed;
        try (Connection conn = database.getConnection()) {
            conn.setAutoCommit(false);
            String statement = "UPDATE Host SET intentos = 0 WHERE mac_address = ?";
            CallableStatement callableStatement = conn.prepareCall(statement);
            callableStatement.setString(1, address);
            callableStatement.execute();
            executed = !callableStatement.wasNull();
            conn.commit();
        }
        return executed;
    }

    /***
     * Get Member ID
     * <p>
     * This method get a Member ID using an email and a encrypted password.
     * It should be used like login method.
     * </p>
     * @param email the members email.
     * @param password the password in plain text.
     * @param academicGroupID the id of academic group where this user is registered.
     * @return the participation that contains member and typeParticipation.
     */
    @Override
    public Participation getMemberByEmailPasswordParticipation(String email, String password, String academicGroupID) throws SQLException {
        Participation participation = null;
        try (Connection conn = database.getConnection()) {
            conn.setAutoCommit(false);
            String statement = "SELECT PCAM.id_miembro, PCAM.tipo_participacion FROM Cuenta AS CUE INNER JOIN ParticipacionCuerpoAcademicoMiembro AS PCAM ON CUE.id_miembro = PCAM.id_miembro AND CUE.email = ? AND CUE.contrasena = SHA2(?,512) AND PCAM.id_cuerpo_academico = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, academicGroupID);
            ResultSet resultSet = preparedStatement.executeQuery();
            int idMember = -1;
            participation = new Participation();
            if (resultSet.next()) {
                idMember = resultSet.getInt("id_miembro");
                participation.setParticipationType(getParticipationType(resultSet.getString("tipo_participacion")));
            }
            Member member = new MiembroDAO().getMember(idMember);
            participation.setMember(member);
            conn.commit();
        }
        return participation;
    }

    /***
     * Get Member ID
     * <p>
     * This method get a Member ID using an email and a encrypted password.
     * It should be used like login method for user adminsitrator.
     * </p>
     * @param email the members email.
     * @param password the password in plain text.
     * @return the participation that contains member and typeParticipation.
     */
    @Override
    public Participation getMemberByEmailAndPassword(String email, String password) throws SQLException {
        Participation participation = null;
        try(Connection conn = database.getConnection() ){
            conn.setAutoCommit(false);
            String statement = "SELECT id_miembro FROM Cuenta WHERE email = ? AND contrasena = SHA2(?,512) AND tipo_usuario = 2";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            int idMember = -1;
            if( resultSet.next() ) {
                idMember = resultSet.getInt("id_miembro");
            }
            if(idMember != -1) {
                participation = new Participation();
                participation.setMember(new MiembroDAO().getMember(idMember));
                participation.setParticipationType(ParticipationType.OTHER);
            }
            conn.commit();
        }
        return participation;
    }

    private ParticipationType getParticipationType(String type) {
        for (ParticipationType participationType : ParticipationType.values()) {
            if (type.equalsIgnoreCase(participationType.getParticipationType())) {
                return participationType;
            }
        }
        return null;
    }
}
