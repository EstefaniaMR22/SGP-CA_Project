package model.dao;

import controller.exceptions.LimitReachedException;
import model.dao.interfaces.ICuentaDAO;
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
        try( Connection conn = database.getConnection() ) {
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
        try( Connection conn = database.getConnection() ) {
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
        int rowsAffected = 0;
        try( Connection conn = database.getConnection() ) {
            conn.setAutoCommit(false);
            String statement = "UPDATE Cuenta SET contrasena = ? WHERE email = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, password);
            preparedStatement.setString(2, email);
            rowsAffected = preparedStatement.executeUpdate();
            conn.commit();
        }
        return rowsAffected > 0;
    }

    @Override
    public String getRecoveryCodeByEmail(String emailRecovery) throws SQLException {
        String code = null;
        try( Connection conn = database.getConnection() ) {
            conn.setAutoCommit(false);
            String statement = "SELECT codigo_recuperacion FROM Cuenta WHERE email = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, emailRecovery);
            ResultSet resultSet = preparedStatement.executeQuery();
            if( resultSet.next() ) {
                code = resultSet.getString("codigo_recuperacion");
            }
            conn.commit();
        }
        return code;
    }

    @Override
    public int getAttemptsByMacAddress(String address) throws SQLException {
        int attempts = -1;
        try(Connection conn = database.getConnection() ){
            conn.setAutoCommit(false);
            String statement = "SELECT intentos FROM Host WHERE mac_address = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, address);
            ResultSet resultSet = preparedStatement.executeQuery();
            if( resultSet.next() ) {
                attempts = resultSet.getInt("intentos");
            }
            conn.commit();
        }
        return attempts;
    }

    @Override
    public boolean sendActualMacAddress(String address) throws SQLException, LimitReachedException {
        boolean executed;
        try(Connection conn = database.getConnection() ){
            conn.setAutoCommit(false);
            String statement = "SELECT mac_address, intentos FROM Host WHERE mac_address = ? LIMIT 1";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, address);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
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
        try(Connection conn = database.getConnection() ){
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
     * @return int the member's id in database. Returns -1 if not exist in database.
     */
    @Override
    public int getMemberIDByEmailAndPassword(String email, String password) throws SQLException {
       int idMember = -1;
        try(Connection conn = database.getConnection() ){
            conn.setAutoCommit(false);
            String statement = "SELECT id_miembro FROM Cuenta WHERE email = ? AND contrasena = SHA2(?,512)";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if( resultSet.next() ) {
                idMember = resultSet.getInt("id_miembro");
            }
            conn.commit();
        }
       return idMember;
    }
}
