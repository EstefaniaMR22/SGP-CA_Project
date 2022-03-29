package utils;

import assets.utils.Autentication;
import controller.exceptions.LimitReachedException;
import controller.exceptions.UserNotFoundException;
import org.junit.Assert;
import org.junit.Test;

import java.net.SocketException;
import java.sql.SQLException;

public class AutenticationTest {
    @Test
    public void logInExistingAccountTest() throws UserNotFoundException, SocketException, LimitReachedException, SQLException {
        String email = "angeladriancamalgarcia@hotmail.com";
        String password = "hola";
        boolean isLogged = Autentication.getInstance().logIn(email, password);
        Assert.assertTrue(isLogged);
    }

    @Test
    public void generateRecoveryCodeByExistingEmailTest() throws SQLException {
        String email = "angeladriancamalgarcia@hotmail.com";
        boolean isGenerated = Autentication.getInstance().generateRecoveryCode(email);
        Assert.assertTrue(isGenerated);
    }

    @Test
    public void generateRecoveryCodeByNoExistingEmailTest() throws SQLException {
        String email = "noexistingemail@hotgmail.com";
        boolean isGenerated = Autentication.getInstance().generateRecoveryCode(email);
        Assert.assertFalse(isGenerated);
    }

    @Test
    public void getRecoveryCodeTest() throws SQLException {
        String code = Autentication.getInstance().getRecoveryCode("angeladriancamalgarcia@hotmail.com");
        Assert.assertNotNull(code);
    }

    @Test
    public void resetPasswordTest() throws SQLException {
        String newPasssword = "hola";
        int idUser = 1;
        boolean isPasswordChanged = Autentication.getInstance().resetPassword(newPasssword, idUser);
        Assert.assertTrue(isPasswordChanged);
    }

    @Test
    public void resetPasswordByNoExistingUserTest() throws SQLException {
        String newPassword = "hola";
        int idUser = -1;
        boolean isPasswordChanged = Autentication.getInstance().resetPassword(newPassword, idUser);
        Assert.assertFalse(isPasswordChanged);
    }

    @Test
    public void resetPasswordByUnloggedUser() throws SQLException {
        String email = "angeladriancamalgarcia@hotmail.com";
        String password = "hola";
        boolean isPasswordChanged = Autentication.getInstance().resetPasswordByUnloggedUser(email, password);
        Assert.assertTrue(isPasswordChanged);
    }



}
