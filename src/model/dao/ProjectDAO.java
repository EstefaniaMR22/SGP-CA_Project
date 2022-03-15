/*
 * @author V Manuel Arrys
 * @versión v1.0
 * Last modification date: 15-03-2022
 */

package model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import controller.exceptions.AlertException;
import javafx.scene.control.Alert;
import model.dao.interfaces.IProjectDAO;
import model.domain.Project;
import utils.Database;

public class ProjectDAO implements IProjectDAO{

    private final Database databaseConection;

    public ProjectDAO() {
        this.databaseConection = new Database();
    }

    @Override
    public List<Project> getProjectList() {
        List<Project> projectList = null;
        try(Connection conn = databaseConection.getConnection()) {
            String statement = "SELECT * FROM Project";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            ResultSet resultSet = preparedStatement.executeQuery();
            projectList = new ArrayList<>();
            while(resultSet.next()) {
                Project projectDataTable = new Project();
                projectDataTable.setIdProject(resultSet.getInt("id"));
                projectDataTable.setProjectName(resultSet.getString("projectName"));
                projectDataTable.setIdBodyAcademyProgram(resultSet.getString("bodyAcademyKey"));
                projectDataTable.setDurationProjectInMonths(resultSet.getInt("durationProjectInMonths"));
                projectDataTable.setStatus(resultSet.getString("status"));
                projectDataTable.setStartDate(resultSet.getString("startDate"));
                projectDataTable.setEndDate(resultSet.getString("endDate"));
                projectDataTable.setEstimatedEndDate(resultSet.getString("estimatedEndDate"));
                projectDataTable.setDescription(resultSet.getString("description"));
                projectList.add(projectDataTable);
            }
        }catch(Exception addProjectException){
            Alert alertView;
            alertView = AlertException.builderAlert("Error", "Al momento de 'Consultar Proyectos'" +
                    " se presento un error debido a: " + addProjectException, Alert.AlertType.ERROR);
            alertView.showAndWait();
        }finally{
            databaseConection.disconnect();
        }
        return projectList;
    }

    /***
     * Add Project to database
     * <p>
     *
     * </p>
     * @param newProject The information from a new investigation project
     * @return result representing that the project has been saved correctly.
     */

    @Override
    public int addProject(Project newProject){
        int result = 0;

        try(Connection conn = databaseConection.getConnection() ) {
            conn.setAutoCommit(false);
            String statement = "CALL agregarProyecto(?, ?, ?, ?, ?, ?, ?, ?)";
            CallableStatement callableStatement = conn.prepareCall(statement);

            callableStatement.setString(1, newProject.getProjectName());
            callableStatement.setString(2, newProject.getIdBodyAcademyProgram());
            callableStatement.setInt(3, newProject.getDurationProjectInMonths());
            callableStatement.setString(4, newProject.getStatus());
            callableStatement.setString(5, newProject.getStartDate());
            callableStatement.setString(6, newProject.getEndDate());
            callableStatement.setString(7, newProject.getEstimatedEndDate());
            callableStatement.setString(8, newProject.getDescription());
;
            result = callableStatement.executeUpdate();

        }catch(Exception addProjectException){
            Alert alertView;
            alertView = AlertException.builderAlert("Error", "Al momento de guardar se presento"
                    + " un error debido a: " + addProjectException, Alert.AlertType.ERROR);
            alertView.showAndWait();
        }finally{
            databaseConection.disconnect();
        }
        return result;
    }

    /***
     * Get Project to database
     * <p>
     *
     * </p>
     * @param idProject The id from investigation project
     * @return projectDetails representing that the information from the project
     */

    @Override
    public Project getProjectDetails(int idProject) {
        Project projectDetails = new Project();
        try(Connection conn = databaseConection.getConnection()) {
            String statement = "SELECT * FROM Project WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1, idProject);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                projectDetails = new Project();
                        projectDetails.setIdProject(resultSet.getInt("id"));
                        projectDetails.setProjectName(resultSet.getString("projectName"));
                        projectDetails.setIdBodyAcademyProgram(resultSet.getString("bodyAcademyKey"));
                        projectDetails.setDurationProjectInMonths(resultSet.getInt("durationProjectInMonths"));
                        projectDetails.setStatus(resultSet.getString("status"));
                        projectDetails.setStartDate(resultSet.getString("startDate"));
                        projectDetails.setEndDate(resultSet.getString("endDate"));
                        projectDetails.setEstimatedEndDate(resultSet.getString("estimatedEndDate"));
                        projectDetails.setDescription(resultSet.getString("description"));

            }
        }catch(Exception addProjectException){
            Alert alertView;
            alertView = AlertException.builderAlert("Error", "Al momento de 'Consultar el proyecto' se presento"
                    + " un error debido a: " + addProjectException, Alert.AlertType.ERROR);
            alertView.showAndWait();
        }finally{
            databaseConection.disconnect();
        }
        return projectDetails;
    }


    /***
     * Update Project to database
     * <p>
     *
     * </p>
     * @param updateProject The information from update investigation project
     * @param idProject The id from a investigation project.
     * @return int representing that the project has been updated correctly.
     */

    @Override
    public int updateProject(int idProject, Project updateProject){
        int result = 0;

        try(Connection conn = databaseConection.getConnection() ) {
            conn.setAutoCommit(false);
            String updateStatement = "UPDATE Project SET projectName = ?, bodyAcademyKey = ?, durationProjectInMonths = ?, " +
                    "status = ?, startDate = ?, endDate = ?, estimatedEndDate = ?, description= ? " +
                    "WHERE projectName = ?;";
            CallableStatement callableStatement = conn.prepareCall(updateStatement);

            callableStatement.setString(1, updateProject.getProjectName());
            callableStatement.setString(2, updateProject.getIdBodyAcademyProgram());
            callableStatement.setInt(3, updateProject.getDurationProjectInMonths());
            callableStatement.setString(4, updateProject.getStatus());
            callableStatement.setString(5, updateProject.getStartDate());
            callableStatement.setString(6, updateProject.getEndDate());
            callableStatement.setString(7, updateProject.getEstimatedEndDate());
            callableStatement.setString(8, updateProject.getDescription());
            ;
            result = callableStatement.executeUpdate();

        }catch(Exception addProjectException){
            Alert alertView;
            alertView = AlertException.builderAlert("Error", "Al momento de guardar se presento"
                    + " un error debido a: " + addProjectException, Alert.AlertType.ERROR);
            alertView.showAndWait();
        }finally{
            databaseConection.disconnect();
        }
        return result;
    }



}
