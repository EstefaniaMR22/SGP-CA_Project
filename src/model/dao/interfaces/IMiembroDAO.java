package model.dao.interfaces;

import model.domain.*;

import java.sql.SQLException;
import java.util.List;

public interface IMiembroDAO {

    /***
     * Add Integrant to database
     * <p>
     * Add an Member as Integrant to database, include an Access Account.
     * </p>
     * @param Integrant The member of the Academic Group Program
     * @param String the account password.
     * @return id representing the user's id in database.
     */
    int addMember(Integrant integrant, String password) throws SQLException;
    /***
     * Add Responsable to database
     * <p>
     * Add an Member as Responsalbe to database, include an Access Account.
     * </p>
     * @param Responsable The member of the Academic Group Program
     * @param String the account password.
     * @return id representing the user's id in database.
     */
    int addMember(Responsable responsable, String password) throws SQLException;
    /***
     * Add Colaborator to database
     * <p>
     * Add an Member as Colaborator to database, include an Access Account.
     * </p>
     * @param Colaborator The member of the Academic Group Program
     * @return id representing the user's id in database.
     */
    int addMember(Colaborator colaborator) throws SQLException;
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
    List<Member> getMembers() throws SQLException;
    /***
     * Get Integrant details from database.
     * <p>
     * Get all the details from Integrant.
     * </p>
     * @param id The member ID.
     * @return Integrant that contains some details.
     */
    Integrant getIntegrantDetails(int id) throws SQLException;
    /***
     * Get Responsable details from database.
     * <p>
     * Get all the details from Responsable.
     * </p>
     * @param id The member ID.
     * @return Responsable that contains some details.
     */
    Responsable getResponsableDetails(int id) throws SQLException;

    /***
     * Update integrant.
     * <p>
     * Update the integrant data by newer information.
     * </p>
     * @param Integrant that contains all the data
     * @return true if integrant was updated otherwise it returns false
     */
    boolean updateMember(Integrant integrant) throws SQLException;
    /***
     * Update Responsable
     * <p>
     * Update the responsable data by newer information.
     * </p>
     * @param Responsable that contains all the data
     * @return true if responsable was updated otherwise it returns false
     */
    boolean updateMember(Responsable responsable) throws SQLException;
    /***
     * Update colaborator
     * <p>
     * Update the colaborator data by newer information.
     * </p>
     * @param Colaborator that contains all the data
     * @return true if colaborator was updated otherwise it returns false
     */
    boolean updateMember(Colaborator colaborator) throws SQLException;
}
