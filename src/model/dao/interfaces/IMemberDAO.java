package model.dao.interfaces;

import javafx.collections.ObservableList;
import model.domain.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Observable;

public interface IMemberDAO {

    /***
     * Add Member to database including Account to system access.
     * <p>
     * Add an Member as Responsalbe to database, include an Access Account.
     * </p>
     * @param member The member of the Academic Group Program
     * @param password the account password.
     * @return id representing the user's id in database.
     */
    int addMember(Member member, String password) throws SQLException;
    /***
     * Add Colaborator to database
     * <p>
     * Add an Member as Colaborator to database.
     * </p>
     * @param colaborator The member of the Academic Group Program
     * @return int representing the user's id in database.
     */
    int addMember(Member colaborator) throws SQLException;
    /***
     * Get all the civil status
     * <p>
     * Get all the civil status like Single, Concubinate, Married, and so on...
     * </p>
     * @return List that contain all the available civil status.
     */
    List<CivilStatus> getCivilStatus() throws SQLException;
    /***
     * Get all members registered in database
     * <p>
     * Get all the members including colaborators, responsables and integrants
     * </p>
     * @return List that contain all the registered members
     */
    List<Member> getAllMembers() throws SQLException;
    /***
     * Get all members registered on academicbody in database
     * <p>
     * Get all the members including colaborators, responsables and integrants from academicBody
     * </p>
     * @return List that contain all the registered members
     */
    ObservableList<Member> getAllMembersByIdAcademicBody(String idAcademicBody) throws SQLException;

    /***
     * Update member
     * <p>
     * Update the member data by newer information.
     * </p>
     * @param member that contains all the data
     * @return true if integrant was updated otherwise it returns false
     */
    boolean updateMember(Member member) throws SQLException;
    /***
     * Remove Member
     * <p>
     * Remove all the member data in the database.
     * </p>
     * @param idMember member to be removed.
     * @return true if it was removed otherwise false.
     */
    boolean removeMember(int idMember) throws SQLException;

    /***
     * Get Member
     * <p>
     * Get member data by id
     * </p>
     * @param id the member's id registered in database.
     * @return Member that contains all the information about member.
     */
    Member getMember(int id) throws SQLException;
    /***
     * Get member by personal number
     * @param personalNumber the member's id.
     * @return int that represents the member's id.
     */
    int getMemberIdByPersonalNumber(String personalNumber) throws SQLException;
    /***
     * Get all the study grades
     * <p>
     * Get all the study grades like PRIMARY, SECONDARY, HIGH SCHOOL...
     * </p>
     * @return List that contain all the available Study grades;
     */
    List<StudyGrade> getStudyGrades() throws SQLException;
    /***
     * Check if Member already exists in database.
     * <p>
     * From a Personal Number check if member exist in database
     * </p>
     * @param personalNumber the string code.
     * @return true if member already existe in database otherwise false.
     */
    boolean checkMember(String personalNumber) throws SQLException;

    /***
     * Check if Member email already exists in database.
     * <p>
     * From a UV Email check if member exist in database
     * </p>
     * @param emailUV the string code.
     * @param id member id.
     * @return true if member already existe in database otherwise false.
     */
    boolean checkMemberByEmail(String emailUV, int id) throws SQLException;

    /***
     * Get all educational Programs.
     * <p>
     *  Get a list with all registered educational program in database.
     * </p>
     * @return List that contains strings of educational program.
     */
    List<String> getAllEducationProgram() throws SQLException;
}
