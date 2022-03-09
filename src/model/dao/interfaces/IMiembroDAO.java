package model.dao.interfaces;

import model.domain.CivilStatus;
import model.domain.Colaborator;
import model.domain.Integrant;
import model.domain.Responsable;

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

}
