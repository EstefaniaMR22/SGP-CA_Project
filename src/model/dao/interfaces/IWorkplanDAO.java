package model.dao.interfaces;

import model.domain.Workplan;
import java.sql.SQLException;
import java.util.List;

public interface IWorkplanDAO {
    /***
     * Add a workplan to database
     * @param workplan the objecto to save in database
     * @return String representing the id of workplan
     */
    String addWorkPlan(Workplan workplan) throws SQLException;
    /***
     * Get all workplan of academic group.
     * @param academicGroupID the academic group to get all workplans
     * @return List containin all the workplans registered in database
     */
    List<Workplan> getAllWorkplan(String academicGroupID) throws SQLException;
    /***
     * Get all details (actions and goals) of workplan of database.
     * @param id the id of the workplan to get details
     * @return Workplan with details (actions and goals)
     */
    Workplan getWorkplanDetails(int id) throws SQLException;
    /***
     * Update a workplan including goals and actions.
     * @param workplan all the new workplan to update
     * @return true if it was updated otherwise false.
     */
    boolean updateWorkplan(Workplan workplan) throws SQLException;
    /***
     * Remove a workplan from database.
     * @param id the workplan id
     * @return true if was removed otherwise false
     */
    boolean removeWorkplan(int id) throws SQLException;
}
