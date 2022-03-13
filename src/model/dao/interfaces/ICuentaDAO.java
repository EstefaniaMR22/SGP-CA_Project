package model.dao.interfaces;

import controller.exceptions.LimitReachedException;

import java.sql.SQLException;

public interface ICuentaDAO {
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
    boolean changePasswordByIdUser(String password, int idUser) throws SQLException;

    /***
     * Generate a code by email
     * <p>
     * This method it's used when someone has lost his password and need to recover it
     * generating a new recovery code in database
     * </p>
     * @param emailRecovery the email to generate the code
     * @return true if any row was affected and false if any row was not affected.
     */
    boolean generatePasswordRecoveryCodeByEmail(String emailRecovery) throws SQLException;

    /***
     * Change password by email
     * <p>
     * This method it's used when someone has not logged in and needs to change his password
     * </p>
     * @param password the new password to replace de oldpassword
     * @param email the account's email to change it password.
     * @return true if any row was affected and false if any row was no affected.
     */
    boolean changePasswordByEmail(String password, String email) throws SQLException;

    /***
     * Get the recovery code by Email
     * <p>
     * This method is used to validate the code sent against the code entered by the user
     * </p>
     * @param emailRecovery the account's email to get the recovery code.
     * @return String representing the recovery code.
     */
    String getRecoveryCodeByEmail(String emailRecovery) throws SQLException;
    /***
     * Get attempts done by mac address.
     * <p>
     * This method get the number of attempts done by a specified mac address
     * or user.
     * </p>
     * @param address the host that is using the system.
     * @return int representing the attemps done by host.
     */
    int getAttemptsByMacAddress(String address) throws SQLException;
    /***
     * Send the host's address
     * <p>
     * This method send the host's address when he is trying to log in
     * to the system. Furthermore, tracks the number of attempts.
     * </p>
     * @param address the host's address
     * @return boolean if any row was affected on database and false if any row was no affected.
     */
    boolean sendActualMacAddress(String address) throws SQLException, LimitReachedException;
    /***
     * Reset the attempt's done by host.
     * <p>
     * This method reset the attempt's done by host. The objective is reset the attempt's done
     * when the user was able to log in to the system.
     * </p>
     * @param address the host's address
     * @return true if any row was affected on database and false if any row was not affected.
     */
    boolean resetAttempts(String address) throws SQLException;
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
    int getMemberIDByEmailAndPassword(String email, String password) throws SQLException;
}
