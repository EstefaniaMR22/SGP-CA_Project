package model.dao;

import controller.exceptions.LimitReachedException;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;

public class AccountDAOTest {

    @Test
    public void changePasswordByIdUser() throws SQLException {
        String newPassword = "hello";
        int idUser = 1;
        boolean isPasswordChanged = new CuentaDAO().changePasswordByIdUser(newPassword, idUser);
        Assert.assertTrue(isPasswordChanged);
    }

    @Test
    public void changePasswordByNoExistingUser() throws SQLException {
        String newPassword = "hello";
        int idUser = -1;
        boolean isPasswordChanged = new CuentaDAO().changePasswordByIdUser(newPassword, idUser);
        Assert.assertFalse(isPasswordChanged);
    }

    @Test
    public void generatePasswordRecoveryCodeByEmail() throws SQLException {
        String email = "angeladriancamalgarcia@hotmail.com";
        boolean isCodeGenerated = new CuentaDAO().generatePasswordRecoveryCodeByEmail(email);
        Assert.assertTrue(isCodeGenerated);
    }

    @Test
    public void generatePasswordRecoveryCodeByNoExistingEmail() throws SQLException {
        String email = "adsiofasjo";
        boolean isCodeGenerated = new CuentaDAO().generatePasswordRecoveryCodeByEmail(email);
        Assert.assertFalse(isCodeGenerated);
    }

    @Test
    public void changePasswordByEmail() throws SQLException {
        String email = "angeladriancamalgarcia@hotmail.com";
        String password = "hola123";
        boolean isPasswordChanged = new CuentaDAO().changePasswordByEmail(email, password);
        Assert.assertTrue(isPasswordChanged);
    }

    @Test
    public void changePasswordByNoExistingEmail() throws SQLException {
        String email = "noexist@gmailhotmail.com";
        String password = "hola123";
        boolean isPasswordChanged = new CuentaDAO().changePasswordByEmail(email,password);
        Assert.assertFalse(isPasswordChanged);
    }

    @Test
    public void getAttemptsByMacAddress() throws SQLException {
        String macAddress = "38:56:a4:g5:46:5g";
        int actual = new CuentaDAO().getAttemptsByMacAddress(macAddress);
        int expected = 5;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getAttemtsByNoExistingMacAddress() throws SQLException {
        String macaddress = "noexist";
        int actual = new CuentaDAO().getAttemptsByMacAddress(macaddress);
        int expected = -1;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void sendActualMacAddress() throws SQLException, LimitReachedException {
        String macaddress = "test";
        boolean isSent = new CuentaDAO().sendActualMacAddress(macaddress);
        Assert.assertTrue(isSent);
    }

    @Test
    public void resetAttemptsByAddress() throws SQLException {
        String macaddress = "45:50:fg:a2:46:4g";
        boolean isAttemptsReset = new CuentaDAO().resetAttempts(macaddress);
        Assert.assertTrue(isAttemptsReset);
    }




}