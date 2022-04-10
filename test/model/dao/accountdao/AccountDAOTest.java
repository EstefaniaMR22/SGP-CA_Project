package model.dao.accountdao;

import controller.control.exceptions.LimitReachedException;
import model.dao.AccountDAO;
import model.domain.Participation;
import org.junit.Assert;
import org.junit.Test;
import java.sql.SQLException;

public class AccountDAOTest {

    @Test
    public void changePasswordByIdUser() throws SQLException {
        String newPassword = "hola";
        int idUser = 1;
        boolean isPasswordChanged = new AccountDAO().changePasswordByIdUser(newPassword, idUser);
        Assert.assertTrue(isPasswordChanged);
    }

    @Test
    public void changePasswordByNoExistingUser() throws SQLException {
        String newPassword = "hello";
        int idUser = -1;
        boolean isPasswordChanged = new AccountDAO().changePasswordByIdUser(newPassword, idUser);
        Assert.assertFalse(isPasswordChanged);
    }

    @Test
    public void generatePasswordRecoveryCodeByEmail() throws SQLException {
        String email = "angeladriancamalgarcia@hotmail.com";
        boolean isCodeGenerated = new AccountDAO().generatePasswordRecoveryCodeByEmail(email);
        Assert.assertTrue(isCodeGenerated);
    }

    @Test
    public void generatePasswordRecoveryCodeByNoExistingEmail() throws SQLException {
        String email = "adsiofasjo";
        boolean isCodeGenerated = new AccountDAO().generatePasswordRecoveryCodeByEmail(email);
        Assert.assertFalse(isCodeGenerated);
    }

    @Test
    public void changePasswordByEmail() throws SQLException {
        String email = "angeladriancamalgarcia@hotmail.com";
        String password = "hola";
        boolean isPasswordChanged = new AccountDAO().changePasswordByEmail(password, email);
        Assert.assertTrue(isPasswordChanged);
    }

    @Test
    public void changePasswordByNoExistingEmail() throws SQLException {
        String email = "noexist@gmailhotmail.com";
        String password = "hola123";
        boolean isPasswordChanged = new AccountDAO().changePasswordByEmail(email,password);
        Assert.assertFalse(isPasswordChanged);
    }

    @Test
    public void getAttemptsByMacAddress() throws SQLException {
        String macAddress = "38:a8:p5:a94:c4";
        int actual = new AccountDAO().getAttemptsByMacAddress(macAddress);
        int expected = 5;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getAttemtsByNoExistingMacAddress() throws SQLException {
        String macaddress = "noexist";
        int actual = new AccountDAO().getAttemptsByMacAddress(macaddress);
        int expected = -1;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void sendActualMacAddress() throws SQLException {
        String macaddress = "test";
        LimitReachedException expected = null;
        try {
            boolean isSent = new AccountDAO().sendActualMacAddress(macaddress);
        } catch (LimitReachedException e) {
            expected = e;
        }
        Assert.assertNotNull(expected);
    }

    @Test
    public void resetAttemptsByAddress() throws SQLException {
        String macaddress = "45:50:fg:a2:46:4g";
        boolean isAttemptsReset = new AccountDAO().resetAttempts(macaddress);
        Assert.assertTrue(isAttemptsReset);
    }

    @Test
    public void getMemberIDByEmailAndPassword() throws SQLException {
        String email = "angeladriancamalgarcia@hotmail.com";
        String password = "hola";
        Participation actual = new AccountDAO().getMemberByEmailAndPassword(email,password);
        Assert.assertNotNull(actual);
    }
}