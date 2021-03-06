package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import controller.control.AlertController;
import controller.projects.AddProjectsInvestigationController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.dao.interfaces.IProjectDAO;
import model.domain.Project;
import assets.utils.Database;
import assets.utils.DateFormatter;

public class ProjectDAO implements IProjectDAO{

    private final Database databaseConection;

    public ProjectDAO() {
        this.databaseConection = new Database();
    }

    /***
     * Get investigation projects to database
     * <p>
     * Get all the investigation projects
     * </p>
     * @return List that contain all the registered investigation projects.
     */

    @Override
    public ObservableList<Project> getProjectList(String academicGroupID) throws SQLException{
        ObservableList<Project> projectList;
        projectList = FXCollections.observableArrayList();

        try(Connection conn = databaseConection.getConnection()) {
            String statement = "Select * FROM ProyectoInvestigacion INNER JOIN LGACProyectoInvestigacion ON " +
                    "ProyectoInvestigacion.id = LGACProyectoInvestigacion.id_proyecto_investigacion " +
                    "INNER JOIN LGAC ON LGACProyectoInvestigacion.id_lgac = LGAC.id " +
                    "where id_programa_cuerpo_academico = ?;";

            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, academicGroupID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Project projectDataTable = new Project();
                projectDataTable.setIdProject(resultSet.getInt("ProyectoInvestigacion.id"));
                projectDataTable.setProjectName(resultSet.getString("ProyectoInvestigacion.nombre"));
                projectDataTable.setDescription(resultSet.getString("ProyectoInvestigacion.descripcion"));
                projectDataTable.setStatus(resultSet.getString("ProyectoInvestigacion.estado"));
                projectDataTable.setDurationProjectInMonths(resultSet.getInt("ProyectoInvestigacion.duracion_meses"));

                projectDataTable.setStartDate(DateFormatter.convertSQLDateToUtilDate(resultSet.getDate("ProyectoInvestigacion.fecha_inicio")));
                projectDataTable.setStartDateString(DateFormatter.getParseDate(projectDataTable.getStartDate()));

                projectDataTable.setEstimatedEndDate(DateFormatter.convertSQLDateToUtilDate(resultSet.getDate("ProyectoInvestigacion.fecha_fin_estimada")));
                projectDataTable.setEstimatedEndDateString(DateFormatter.getParseDate(projectDataTable.getStartDate()));

                if(resultSet.getDate("ProyectoInvestigacion.fecha_real")==null){
                    projectDataTable.setEndDate(projectDataTable.getEstimatedEndDate());
                    projectDataTable.setEndDateString("Pendiente");

                }else {
                    projectDataTable.setEndDate(DateFormatter.convertSQLDateToUtilDate(resultSet.getDate("ProyectoInvestigacion.fecha_real")));
                    projectDataTable.setEndDateString(DateFormatter.getParseDate(projectDataTable.getEndDate()));
                }

                projectDataTable.setIdLGCA(getIdLGAC(projectDataTable.getIdProject()));

                projectList.add(projectDataTable);

            }

        }
            databaseConection.disconnect();
        return projectList;
    }

    /***
     * Add Project to database
     * <p>
     *  Add a new project
     * </p>
     * @param newProject The information from a new investigation project
     * @return result representing that the project has been saved correctly.
     */

    @Override
    public boolean addProject(Project newProject) throws SQLException{
        boolean wasAdded = false;

        try(Connection conn = databaseConection.getConnection() ) {
            conn.setAutoCommit(false);
            String statement = "INSERT INTO ProyectoInvestigacion(nombre, descripcion, estado, duracion_meses, fecha_inicio, fecha_fin_estimada, fecha_real) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);

            preparedStatement.setString(1, newProject.getProjectName());
            preparedStatement.setString(2, newProject.getDescription());
            preparedStatement.setString(3, newProject.getStatus());
            preparedStatement.setInt(4, newProject.getDurationProjectInMonths());
            preparedStatement.setDate(5, DateFormatter.convertUtilDateToSQLDate(newProject.getStartDate()));
            preparedStatement.setDate(6, DateFormatter.convertUtilDateToSQLDate(newProject.getEstimatedEndDate()));
            preparedStatement.setDate(7, null);
            wasAdded = preparedStatement.executeUpdate() > 0;

            conn.commit();

        }
        databaseConection.disconnect();

            if(wasAdded == true) {
                try {
                    newProject.setIdProject(getIdProject(newProject.getProjectName()));
                    addProjectLGAC(newProject.getIdProject(), newProject.getIdLGCA());
                } catch (Exception addProjectLGAC) {
                    Logger.getLogger(AddProjectsInvestigationController.class.getName()).log(Level.SEVERE, null, addProjectLGAC);
                    AlertController alertView = AlertController.getInstance();
                    alertView.showActionFailedAlert(" No se pudo guardar la relaci??n entre LGAC y Proyecto." +
                            " Causa: " + addProjectLGAC);

                }


            }

        return wasAdded;
    }

    /***
     * Add Project to database
     * <p>
     *  Add a new project
     * </p>
     * @param idProject The id from a new investigation project
     * @param idLGAC the id lgac for a new investigation project
     * @return result representing that the project has been saved correctly.
     */

    @Override
    public int addProjectLGAC(int idProject, int idLGAC) throws SQLException{
        int result = 0;

        try(Connection conn = databaseConection.getConnection() ) {
            conn.setAutoCommit(false);
            String statement = "INSERT INTO LGACProyectoInvestigacion(id_proyecto_investigacion, id_lgac) values(?, ?)";
            PreparedStatement preparedStatement = conn.prepareCall(statement);

            preparedStatement.setInt(1, idProject);
            preparedStatement.setInt(2,idLGAC);

            result = preparedStatement.executeUpdate();
            conn.commit();

        }
        databaseConection.disconnect();

        return result;
    }

    /***
     * Get idProject to database
     * <p>
     *
     * </p>
     * @param nameProject The name from investigation project
     * @return idProject representing id from the project
     */

    @Override
    public int getIdProject(String nameProject) throws SQLException{
        int idProject = 0;

        try(Connection conn = databaseConection.getConnection()) {
            String statement = "SELECT id FROM ProyectoInvestigacion WHERE nombre = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, nameProject);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                idProject = resultSet.getInt("id");
                System.out.println(idProject);
            }
        }
        databaseConection.disconnect();
        return idProject;
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
    public Project getProjectDetails(int idProject) throws SQLException{
        Project projectDetails = new Project();
        try(Connection conn = databaseConection.getConnection()) {
            String statement = "SELECT * FROM ProyectoInvestigacion WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1, idProject);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                projectDetails = new Project();

                projectDetails.setIdProject(resultSet.getInt("id"));
                projectDetails.setProjectName(resultSet.getString("nombre"));
                projectDetails.setDescription(resultSet.getString("descripcion"));
                projectDetails.setStatus(resultSet.getString("estado"));
                projectDetails.setDurationProjectInMonths(resultSet.getInt("duracion_meses"));


                projectDetails.setStartDate(DateFormatter.convertSQLDateToUtilDate(resultSet.getDate("fecha_inicio")));
                projectDetails.setStartDateString(DateFormatter.getParseDate(projectDetails.getStartDate()));

                projectDetails.setEstimatedEndDate(DateFormatter.convertSQLDateToUtilDate(resultSet.getDate("fecha_fin_estimada")));
                projectDetails.setEstimatedEndDateString(DateFormatter.getParseDate(projectDetails.getStartDate()));

                if(resultSet.getDate("fecha_real")==null){
                    projectDetails.setEndDate(projectDetails.getEstimatedEndDate());
                    projectDetails.setEndDateString("Pendiente");

                }else {
                    projectDetails.setEndDate(DateFormatter.convertSQLDateToUtilDate(resultSet.getDate("ProyectoInvestigacion.fecha_real")));
                    projectDetails.setEndDateString(DateFormatter.getParseDate(projectDetails.getEndDate()));
                }

                projectDetails.setIdLGCA(getIdLGAC(projectDetails.getIdProject()));

            }
        }
            databaseConection.disconnect();
        return projectDetails;
    }

    /***
     * Check if Project exist in database
     * <p>
     *
     * </p>
     * @param projectName The name from investigation project
     * @return projectAlreadyExist representing Project exist in database
     */
    @Override
    public boolean checkProject(String projectName) throws SQLException {
        boolean projectAlreadyExist = false;
        try(Connection conn = databaseConection.getConnection()) {
            String statement = "SELECT * FROM ProyectoInvestigacion WHERE nombre = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, projectName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                projectAlreadyExist = true;
            }
        }
        return projectAlreadyExist;
    }

    /***
     * Check if Project exist in database
     * <p>
     *
     * </p>
     * @param projectName The name from investigation project
     * @return projectAlreadyExist representing Project exist in database
     */
    @Override
    public boolean checkProjectUpdated(String projectName, int idProject) throws SQLException {
        boolean projectAlreadyExist = false;
        try(Connection conn = databaseConection.getConnection()) {
            String statement = "SELECT * FROM ProyectoInvestigacion WHERE nombre = ? & id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, projectName);
            preparedStatement.setInt(2, idProject);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                projectAlreadyExist = true;
            }
        }
        return projectAlreadyExist;
    }

    /***
     * Get id LGAC from the Project
     * <p>
     *
     * </p>
     * @param idProject The id from investigation project
     * @return idLGAC from Project in database
     */
    @Override
    public int getIdLGAC(int idProject) throws SQLException{
        int idLGAC = 0;

        try(Connection conn = databaseConection.getConnection() ) {
            conn.setAutoCommit(false);
            String statement = "SELECT * FROM LGACProyectoInvestigacion where id_proyecto_investigacion = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1, idProject);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                idLGAC = resultSet.getInt("id_lgac");
            }

        }
        databaseConection.disconnect();

        return idLGAC;
    }


    /***
     * Update Project to database
     * <p>
     *
     * </p>
     * @param updateProject The information from update investigation project
     * @return int representing that the project has been updated correctly.
     */

    @Override
    public boolean updateProject(Project updateProject) throws SQLException{
        boolean wasUpdated = false;

        try(Connection conn = databaseConection.getConnection() ) {
            int rowsAffected = 0;
            conn.setAutoCommit(false);
            String updateStatement = "UPDATE ProyectoInvestigacion SET nombre = ?, descripcion = ?, estado = ?, fecha_real = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(updateStatement);

            preparedStatement.setString(1, updateProject.getProjectName());
            preparedStatement.setString(2, updateProject.getDescription());
            preparedStatement.setString(3, updateProject.getStatus());
            preparedStatement.setDate(4, DateFormatter.convertUtilDateToSQLDate(updateProject.getEndDate()));
            preparedStatement.setInt(5, updateProject.getIdProject());
            rowsAffected = preparedStatement.executeUpdate();
            wasUpdated = rowsAffected > 0;

            conn.commit();

        }
            databaseConection.disconnect();
        return wasUpdated;
    }



}
