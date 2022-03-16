package model.dao.interfaces;

import model.domain.LGAC;

import java.sql.SQLException;
import java.util.List;

public interface ILgacDAO {
    /***
     * Add a LGAC to database.
     * <p>
     * Add a LGAC into database with id and description.
     * </p>
     * @param lgac the object lgac.
     * @return id representing the lgac's id in database.
     */
    int addLgac(LGAC lgac) throws SQLException;
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
     * Get all LGACs
     * <p>
     * Get all the registered LGACS in database.
     * </p>
     * @return List that contain all LGACS.
     */
    List<LGAC> getAlllgacs() throws SQLException;
}
