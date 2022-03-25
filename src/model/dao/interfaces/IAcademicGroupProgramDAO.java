package model.dao.interfaces;

import model.domain.AcademicGroupProgram;
import model.domain.ConsolidationGrade;
import model.domain.LGAC;

import java.sql.SQLException;
import java.util.List;

public interface IAcademicGroupProgramDAO {
    /***
     * Add an Academic Group Program to database.
     * <p>
     * Add an AGP to database. This is so important for the activities.
     * </p>
     * @param academicGroupProgram the AGP to register.
     * @return String represeting the academic group ID in database.
     */
    String addAcademicGroupProgram(AcademicGroupProgram academicGroupProgram) throws SQLException;
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
    List<AcademicGroupProgram> getAllAcademicGroupPrograms() throws SQLException;
    /***
     * Get all details from Academic Group Programs
     * <p>
     * Get details from Academic Group Program.
     * </p>
     * @param id The id of Academic Group Program.
     * @return List that contain all Academic Group Programs
     */
    AcademicGroupProgram getAcademicGroupProgramDetails(String id) throws SQLException;
}
