package assets.utils;

import controller.exceptions.LimitReachedException;
import controller.exceptions.UserNotFoundException;
import model.dao.CuentaDAO;
import model.domain.Participation;

import java.net.SocketException;
import java.sql.SQLException;

public class Autentication {
    private static Autentication instance;
    private Participation participation;

    public static Autentication getInstance() {
        if(instance == null) {
            instance = new Autentication();
        }
        return instance;
    }

    /***
     * Logout from actual current logged user.
     */
    public void logOut() {
        participation = null;
    }

    public Participation getParticipation() {
        return participation;
    }

    /***
     * Log in an account.
     * <p>
     * This method it is used to accesss to the system.
     * </p>
     * @param email the user's email
     * @param password the password to log in.
     * @param idAcademicGroup the academic group to login.
     * @return boolean true if login was successful and false if login was not successful
     * @throws UserNotFoundException if user was not found in database.
     * @throws LimitReachedException if user has reached the 5 attempts.
     * @throws SQLException if it happened a error in database.
     */
    public boolean logIn(String email, String password, String idAcademicGroup) throws UserNotFoundException, LimitReachedException, SQLException, SocketException {
        checkAttemptsLimit();
        sendMacAddress();
        participation = getMemberParticipation(email, password, idAcademicGroup);
        resetAttempts();
        return true;
    }
    /***
     * Log in an account.
     * <p>
     * This method it is used to accesss to admin.
     * </p>
     * @param email the user's email
     * @param password the password to log in.
     * @return boolean true if login was successful and false if login was not successful
     * @throws UserNotFoundException if user was not found in database.
     * @throws LimitReachedException if user has reached the 5 attempts.
     * @throws SQLException if it happened a error in database.
     */
    public boolean logIn(String email, String password) throws UserNotFoundException, LimitReachedException, SQLException, SocketException {
        checkAttemptsLimit();
        sendMacAddress();
        participation = getMemberAdminParticipation(email, password);
        resetAttempts();
        return true;
    }

    /**+
     * Generate a recovery code.
     * <p>
     * This method generate a random string in database.
     * The purpose is that user can change his password.
     * </p>
     * @param email representing the user's email
     * @return boolean if recovery code was generated and false if was not generated.
     * @throws SQLException if any error happened in database.
     */
    public boolean generateRecoveryCode(String email) throws SQLException {
        boolean isCodeGenerated = new CuentaDAO().generatePasswordRecoveryCodeByEmail( email );
        return isCodeGenerated;
    }

    /***
     * Return the recovery code from database.
     * <p>
     * This method returns the recovery code generated in database.
     * The purpose is that user can change his password
     * </p>
     * @param email representing the user's email
     * @return String representing the recovery code generated in database.
     * @throws SQLException if any error happened in database.
     */
    public String getRecoveryCode(String email) throws SQLException {
        String recoveryCode = new CuentaDAO().getRecoveryCodeByEmail(email);
        return recoveryCode;
    }

    /***
     * Change password from the current logged user.
     * <p>
     * This method it's used by the logged user when he needs to change his password by
     * any reason.
     * </p>
     * @param newPassword to be set it in database.
     * @param idUser the id of Member in database.
     * @return boolean true if password was reset and false if was not reset.
     * @throws SQLException if any error happened in database.
     */
    public boolean resetPassword(String newPassword, int idUser) throws SQLException {
        boolean isPasswordChanged = new CuentaDAO().changePasswordByIdUser(newPassword, idUser);
        return isPasswordChanged;
    }

    /***
     * Change password when it was forgotten.
     * <p>
     * This method is executed when a user is trying to recover their password
     * </p>
     * @param email the user's email
     * @param newPassword the new password to be set it
     * @return boolean true if password was changed and false if it was not changed
     * @throws SQLException representing a error in connection to database
     */
    public boolean resetPasswordByUnloggedUser(String email, String newPassword) throws SQLException {
        boolean isPasswordChanged = new CuentaDAO().changePasswordByEmail(newPassword, email);
        return isPasswordChanged;
    }

    private void checkAttemptsLimit() throws LimitReachedException, SQLException, SocketException {
        final int ATTEMPTS_LIMIT = 5;
        boolean isAttempsLimitReached = new CuentaDAO().getAttemptsByMacAddress( getMacAddress() ) == ATTEMPTS_LIMIT;
        if(isAttempsLimitReached) {
            throw new LimitReachedException("¡Limite de intentos alcanzado! ¡Espera 10 minutos!");
        }
    }

    private String getMacAddress() throws SocketException {
        String macAddress = null;
            macAddress = NetworkAddress.getLocalAdress();
            macAddress = Cryptography.cryptSHA2( (macAddress != null) ? macAddress : "" );

        return macAddress;
    }

    private boolean sendMacAddress() throws SQLException, LimitReachedException, SocketException {
        boolean isMacAddressSent;
        isMacAddressSent = new CuentaDAO().sendActualMacAddress( getMacAddress() );
        return isMacAddressSent;
    }

    private boolean resetAttempts() throws SQLException, SocketException {
        boolean isAttemptsReset;
        isAttemptsReset = new CuentaDAO().resetAttempts( getMacAddress() );
        return isAttemptsReset;
    }

    private Participation getMemberParticipation(String email, String password, String academicGroupID) throws SQLException, UserNotFoundException {
        Participation participation = new CuentaDAO().getMemberByEmailPasswordParticipation(email,password,academicGroupID);
        if(participation == null) {
            throw new UserNotFoundException("¡Usuario o contraseña incorrecta!");
        }
        return participation;
    }

    private Participation getMemberAdminParticipation(String email, String password) throws SQLException, UserNotFoundException {
        Participation participation = new CuentaDAO().getMemberByEmailAndPassword(email,password);
        if(participation == null) {
            throw new UserNotFoundException("¡Usuario o contraseña incorrecta!");
        }
        return participation;
    }

    private void Autentication() {
    }
}
