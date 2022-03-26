package model.dao.interfaces;

import model.domain.AcademicGroup;
import model.domain.ConsolidationGrade;

import java.sql.SQLException;
import java.util.List;

public interface IAcademicGroupDAO {
    /***
     * Add an Academic Group Program to database.
     * <p>
     * Add an AGP to database. This is so important for the activities.
     * </p>
     * @param academicGroupProgram the AGP to register.
     * @return String represeting the academic group ID in database.
     */
    String addAcademicGroupProgram(AcademicGroup academicGroupProgram) throws SQLException;
    /***
     * Get all consolidation grades from database
     * <p>
     * Get all the consolidation grades: Consolidated, in consolidation and in formation
     * </p>
     * @return List that contain all consolidation grades
     */
    List<ConsolidationGrade> getConsolidationGrades() throws SQLException;

    /***
     * Get all Academic Group Programs
     * <p>
     * Get all the Academic Group Programs with basic information.
     * </p>
     * @return List that contain all Academic Group Programs
     */
    List<AcademicGroup> getAllAcademicGroupPrograms() throws SQLException;
    /***
     * Get all details from Academic Group.
     * <p>
     * Get details from Academic Group..
     * The details are Members and LGAS assigned to AG.
     * </p>
     * @param id The id of Academic Group.
     * @return List that contain all Academic Group.
     */
    AcademicGroup getAcademicGroupProgramDetails(String id) throws SQLException;

    /***
     * Update a registered Academic Group.
     * <p>
     *     Including his LGACS and Members.
     * </p>
     * @param academicGroup the new data to be registered in database.
     * @return true if it was registered otherwise false.
     */
    boolean updateAcademicGroup(AcademicGroup academicGroup) throws SQLException;
}
