package model.dao;

import controller.control.exceptions.LimitReachedException;
import model.dao.interfaces.IAccountDAO;
import model.domain.Member;
import model.domain.Participation;
import model.domain.ParticipationType;
import assets.utils.Database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO implements IAccountDAO {
    private Database database;

    public AccountDAO() {
        database = new Database();
    }

    /***
     * Change a password to person
     * <p>
     * This method it's used when someone needs to change a password
     * to their account.
     * </p>
     * @param password the password to set
     * @param idUser the id user to change password
     * @return true if any row was affected otherwise it returns false
     */
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
    /***
     * Generate a code by email
     * <p>
     * This method it's used when someone has lost his password and need to recover it
     * generating a new recovery code in database
     * </p>
     * @param emailRecovery the email to generate the code
     * @return true if any row was affected and false if any row was not affected.
     */
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
    /***
     * Change password by email
     * <p>
     * This method it's used when someone has not logged in and needs to change his password
     * </p>
     * @param password the new password to replace de oldpassword
     * @param email the account's email to change it password.
     * @return true if any row was affected and false if any row was no affected.
     */
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
    /***
     * Get the recovery code by Email
     * <p>
     * This method is used to validate the code sent against the code entered by the user
     * </p>
     * @param emailRecovery the account's email to get the recovery code.
     * @return String representing the recovery code.
     */
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
    /***
     * Get attempts done by mac address.
     * <p>
     * This method get the number of attempts done by a specified mac address
     * or user.
     * </p>
     * @param address the host that is using the system.
     * @return int representing the attemps done by host.
     */
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
    /***
     * Send the host's address
     * <p>
     * This method send the host's address when he is trying to log in
     * to the system. Furthermore, tracks the number of attempts.
     * </p>
     * @param address the host's address
     * @return boolean if any row was affected on database and false if any row was no affected.
     */
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
    /***
     * Reset the attempt's done by host.
     * <p>
     * This method reset the attempt's done by host. The objective is reset the attempt's done
     * when the user was able to log in to the system.
     * </p>
     * @param address the host's address
     * @return true if any row was affected on database and false if any row was not affected.
     */
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
            if (resultSet.next()) {
                idMember = resultSet.getInt("id_miembro");
            }
            if(idMember != -1 ) {
                participation = new Participation();
                participation.setParticipationType(getParticipationType(resultSet.getString("tipo_participacion")));
                Member member = new MemberDAO().getMember(idMember);
                participation.setMember(member);
            }
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
                participation.setMember(new MemberDAO().getMember(idMember));
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
