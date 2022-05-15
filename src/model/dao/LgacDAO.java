package model.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.dao.interfaces.ILgacDAO;
import model.domain.ActivityStateLGAC;
import model.domain.LGAC;
import assets.utils.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LgacDAO implements ILgacDAO {
    private final Database database;

    public LgacDAO() {
        this.database = new Database();
    }

    /***
     * Add a LGAC to database to specific Academic Group.
     * <p>
     * Add a LGAC into database with id and description.
     * </p>
     * @param lgac the object lgac.
     * @param academicGroupID the academic group id.
     * @return id representing the lgac's id in database.
     */
    @Override
    public int addLgac(LGAC lgac, String academicGroupID) throws SQLException {
        int idLgac = -1;
        try (Connection conn = database.getConnection()) {
            conn.setAutoCommit(false);
            String statement = "INSERT INTO LGAC(identificador, descripcion, id_programa_cuerpo_academico, estado_actividad) VALUES(?,?,?,1)";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, lgac.getIdentification());
            preparedStatement.setString(2, lgac.getDescription());
            preparedStatement.setString(3, academicGroupID);
            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected > 0) {
                statement = "SELECT LAST_INSERT_ID()";
                preparedStatement = conn.prepareStatement(statement);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()) {
                    idLgac = resultSet.getInt(1);
                }
            }
            conn.commit();
        }
        return idLgac;
    }

    /***
     * Remove LGAC
     * <p>
     * Remove an LGAC from database.
     * </p>
     * @param idLgac the lgac's id.
     * @return true if it was removed otherwise false.
     */
    @Override
    public boolean removeLgac(int idLgac) throws SQLException {
        boolean isRemoved = false;
        try (Connection conn = database.getConnection()) {
            String statement = "DELETE FROM LGAC WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1, idLgac);
            int rowsAffected = preparedStatement.executeUpdate();
            isRemoved = rowsAffected > 0;
        }
        return isRemoved;
    }

    /***
     * Get all LGACs of specific AcademicGroup
     * <p>
     * Get all the registered LGACS in database.
     * </p>
     * @return List that contain all LGACS.
     */
    @Override
    public ObservableList<LGAC> getAllLgacsByIdAcademicGroup(String academicGroupID) throws SQLException {
            ObservableList<LGAC> list;
            list = FXCollections.observableArrayList();
        try (Connection conn = database.getConnection()) {
            String statement = "SELECT * FROM LGAC WHERE id_programa_cuerpo_academico = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, academicGroupID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                LGAC lgac = new LGAC();
                lgac.setId(resultSet.getInt("id"));
                lgac.setIdentification(resultSet.getString("identificador"));
                lgac.setDescription(resultSet.getString("descripcion"));
                list.add(lgac);
            }
        }
        database.disconnect();
        return list;
    }
    /***
     * Get a specific LGAC.
     * @param idLGAC LGAC's ID.
     * @return LGAC object that contains all data of LGAC.
     */
    @Override
    public LGAC getLGACById(int idLGAC) throws SQLException {
        LGAC lgac = null;
        try (Connection conn = database.getConnection()) {
            conn.setAutoCommit(false);
            String statement = "SELECT * FROM LGAC where id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1, idLGAC);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                lgac = new LGAC();
                lgac.setDescription(resultSet.getString("descripcion"));
                lgac.setId(resultSet.getInt("id"));
                lgac.setIdentification(resultSet.getString("identificador"));
            }
        }
        database.disconnect();
        return lgac;
    }


    /***
     * Check if can remove a lgac without constraints by id
     * @param idLgac id of lgac
     * @return return true if can remove otherwise false
     */
    public boolean checkLgacConstraints(int idLgac) {
        boolean canRemove = false;
        try(Connection conn = database.getConnection()) {
            conn.setAutoCommit(false);
            String statement = "DELETE From LGAC WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1, idLgac);
            preparedStatement.execute();
            conn.rollback();
            canRemove = true;
        } catch(SQLException sqlException) {
           canRemove = false;
        }
        return canRemove;
    }

    /***
     * Check if can remove a lgac without constraints by identificator and academic group id
     * @param identification the id of lgac of academic group
     * @param idAcademicGroup the id of academic group
     * @return return true if can remove otherwise false.
     */
    public boolean checkLgacConstraints(String identification, String idAcademicGroup) {
        boolean canRemove = false;
        try(Connection conn = database.getConnection()) {
            conn.setAutoCommit(false);
            String statement = "DELETE From LGAC WHERE identificador = ? AND id_programa_cuerpo_academico = ? ";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, identification);
            preparedStatement.setString(2, idAcademicGroup);
            preparedStatement.execute();
            conn.rollback();
            canRemove = true;
        } catch(SQLException sqlException) {
            canRemove = false;
        }
        return canRemove;
    }


    /***
     * Change the activity state status of lgac
     * @param identificator id of lgac of academic group
     * @param academicGroupID the id of academic group
     * @return true if updated otherwise false.
     */
    @Override
    public boolean updateActivityState(String identificator, String academicGroupID, ActivityStateLGAC state) throws SQLException {
        boolean isUpdated = false;
        try(Connection conn = database.getConnection() ) {
            conn.setAutoCommit(false);
            String statement = "UPDATE LGAC SET estado_actividad = ? WHERE identificador = ? AND id_programa_cuerpo_academico = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt( 1, (state == ActivityStateLGAC.ACTIVE ? 1 : 2));
            preparedStatement.setString( 2, identificator);
            preparedStatement.setString( 3, academicGroupID);
            isUpdated = preparedStatement.executeUpdate() > 0;
            conn.commit();
        }
        return isUpdated;
    }
}
