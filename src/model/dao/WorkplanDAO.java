package model.dao;

import assets.utils.Database;
import assets.utils.DateFormatter;
import model.dao.interfaces.IWorkplanDAO;
import model.domain.AcademicGroup;
import model.domain.Workplan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WorkplanDAO implements IWorkplanDAO {
    private final Database database;

    public WorkplanDAO() {
        this.database = new Database();
    }

    /***
     * Add a workplan to database
     * @param workplan the objecto to save in database
     * @return String representing the id of workplan
     */
    @Override
    public String addWorkPlan(Workplan workplan) throws SQLException {
        return null;
    }

    /***
     * Get all workplan of academic group.
     * @param academicGroupID the academic group to get all workplans
     * @return List containin all the workplans registered in database
     */
    @Override
    public List<Workplan> getAllWorkplan(String academicGroupID) throws SQLException {
        List<Workplan> list;
        try( Connection conn = database.getConnection() ) {
            String statement = "SELECT * FROM PlanDeTrabajo";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            ResultSet resultSet = preparedStatement.executeQuery();
            list = new ArrayList<>();
            while(resultSet.next()) {
                Workplan workplan = new Workplan();
                workplan.setId(resultSet.getString("id"));
                workplan.setYearsDuration(resultSet.getInt("duracion_anios"));
                workplan.setStartDate(DateFormatter.convertSQLDateToUtilDate(resultSet.getDate("fecha_inicio")));
                workplan.setEndDate(DateFormatter.convertSQLDateToUtilDate(resultSet.getDate("fecha_final")));
                workplan.setGeneralObjetive(resultSet.getString("objetivo_general"));
                list.add(workplan);
            }
        }
        return list;
    }

    /***
     * Get all details (actions and goals) of workplan of database.
     * @param id the id of the workplan to get details
     * @return Workplan with details (actions and goals)
     */
    @Override
    public Workplan getWorkplanDetails(int id) throws SQLException {
        return null;
    }

    /***
     * Update a workplan including goals and actions.
     * @param workplan all the new workplan to update
     * @return true if it was updated otherwise false.
     */
    @Override
    public boolean updateWorkplan(Workplan workplan) throws SQLException {
        return false;
    }

    /***
     * Remove a workplan from database.
     * @param id the workplan id
     * @return true if was removed otherwise false
     */
    @Override
    public boolean removeWorkplan(int id) throws SQLException {
        return false;
    }
}
