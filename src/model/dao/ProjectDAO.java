/*
 * @author V Manuel Arrys
 * @versión v1.0
 * Last modification date: 15-03-2022
 */

package model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import controller.AlertController;
import controller.exceptions.AlertException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    public ObservableList<Project> getProjectList() {
        ObservableList<Project> projectList;
        projectList = FXCollections.observableArrayList();

        try(Connection conn = databaseConection.getConnection()) {
            String statement = "SELECT * FROM ProyectoInvestigacion;";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Project projectDataTable = new Project();
                projectDataTable.setIdProject(resultSet.getInt("id"));
                projectDataTable.setProjectName(resultSet.getString("nombre"));
                projectDataTable.setDescription(resultSet.getString("descripcion"));
                projectDataTable.setStatus(resultSet.getString("estado"));
                projectDataTable.setDurationProjectInMonths(resultSet.getInt("duracion_meses"));
                String startDate = String.valueOf(resultSet.getDate("fecha_inicio"));
                projectDataTable.setStartDate(startDate);
                String estimatedEndDate = String.valueOf(resultSet.getDate("fecha_fin_estimada"));
                projectDataTable.setEstimatedEndDate(estimatedEndDate);

                    if(resultSet.wasNull()){
                        String endDate = String.valueOf(resultSet.getDate("fecha_real"));
                        projectDataTable.setEndDate(endDate);
                    }else {
                        projectDataTable.setEndDate("Pendiente");
                    }
                projectDataTable.setIdLGCA(resultSet.getInt("identificador_lgca"));
                projectList.add(projectDataTable);

            }

        }catch(Exception listProjectsException){

                AlertController alertView = new AlertController();
                alertView.showActionFailedAlert(" " + listProjectsException);

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
            String statement = "INSERT INTO ProyectoInvestigacion(?, ?, ?, ?, ?, ?, ?, ?)";
            CallableStatement callableStatement = conn.prepareCall(statement);

            callableStatement.setString(1, newProject.getProjectName());
            callableStatement.setString(2, newProject.getDescription());
            callableStatement.setString(3, newProject.getStatus());
            callableStatement.setInt(4, newProject.getDurationProjectInMonths());
            callableStatement.setString(5, newProject.getStartDate());
            callableStatement.setString(6, newProject.getEstimatedEndDate());
            callableStatement.setString(7, newProject.getEndDate());
            callableStatement.setInt(8, newProject.getIdLGCA());

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
            String statement = "SELECT * FROM ProyectoInvestigacion WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1, idProject);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                projectDetails = new Project();
                        projectDetails.setIdProject(resultSet.getInt("id"));
                        projectDetails.setProjectName(resultSet.getString("name"));
                        projectDetails.setIdLGCA(resultSet.getInt("identificador_lgca"));
                        projectDetails.setDurationProjectInMonths(resultSet.getInt("duracion_meses"));
                        projectDetails.setStatus(resultSet.getString("estado"));
                        projectDetails.setStartDate(resultSet.getString("fecha_incio"));
                        projectDetails.setEndDate(resultSet.getString("fecha_real"));
                        projectDetails.setEstimatedEndDate(resultSet.getString("fecha_fin_estimada"));
                        projectDetails.setDescription(resultSet.getString("descripcion"));

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
            String updateStatement = "UPDATE ProyectoInvestigacion SET name = ?, descripcion = ?, estado = ?, " +
                    "duracion_meses = ?, fecha_inicio = ?, fecha_fin_estimada = ?, fecha_real = ?, " +
                    "identificador_lgca= ? WHERE id = ?;";
            CallableStatement callableStatement = conn.prepareCall(updateStatement);

            callableStatement.setString(1, updateProject.getProjectName());
            callableStatement.setString(2, updateProject.getDescription());
            callableStatement.setString(3, updateProject.getStatus());
            callableStatement.setInt(4, updateProject.getDurationProjectInMonths());
            callableStatement.setString(5, updateProject.getStartDate());
            callableStatement.setString(6, updateProject.getEstimatedEndDate());
            callableStatement.setString(7, updateProject.getEndDate());
            callableStatement.setInt(8, updateProject.getIdLGCA());
            callableStatement.setInt(9, updateProject.getIdProject());

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
