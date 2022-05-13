package model.dao.interfaces;

import javafx.collections.ObservableList;
import model.domain.ActivityStateLGAC;
import model.domain.LGAC;

import java.sql.SQLException;
import java.util.List;

public interface ILgacDAO {
    /***
     * Add a LGAC to database to specific Academic Group.
     * <p>
     * Add a LGAC into database with id and description.
     * </p>
     * @param lgac the object lgac.
     * @param academicGroupID the academic group id.
     * @return id representing the lgac's id in database.
     */
    int addLgac(LGAC lgac, String academicGroupID) throws SQLException;
    /***
     * Remove LGAC
     * <p>
     * Remove an LGAC from database.
     * </p>
     * @param idLgac the lgac's id.
     * @return true if it was removed otherwise false.
     */
    boolean removeLgac(int idLgac) throws SQLException;
    /***
     * Get all LGACs of specific AcademicGroup
     * <p>
     * Get all the registered LGACS in database.
     * </p>
     * @return List that contain all LGACS.
     */
    ObservableList<LGAC> getAllLgacsByIdAcademicGroup(String academicGroupID) throws SQLException;
    /***
     * Get a specific LGAC.
     * @param idLGAC LGAC's ID.
     * @return LGAC object that contains all data of LGAC.
     */
    LGAC getLGACById(int idLGAC) throws SQLException;

    /***
     * Change the activity state status of lgac
     * @param identificator identificador of lgac of academic group
     * @param academicGroupID the id of academic group
     * @return true if updated otherwise false.
     */
    boolean updateActivityState(String identificator, String academicGroupID, ActivityStateLGAC state) throws SQLException;
}
