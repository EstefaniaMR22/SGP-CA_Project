/*
* @author Johann
* @versión v1.0
* Last modification date: 17-06-2021
*/

package model.dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import controller.old.pattern.ValidatorForm;
import model.domain.Project;
import utils.ConnectionDatabase;

public class ProjectDAO implements IProjectDAO{
    
    private final ConnectionDatabase QUERY = new ConnectionDatabase();
    
    @Override
    public List<Project> getProjectList(){
        List<Project> listProjects = new ArrayList<>();
        try{
            Statement instructionQuery = QUERY.getConnectionDatabase().createStatement();;
            ResultSet queryResult = instructionQuery.executeQuery("Select projectName, durationProjectInMonths, status, startDate, endDate FROM Project");
            while(queryResult.next()){
                listProjects.add(new Project(
                    queryResult.getString("projectName"),
                    queryResult.getInt("durationProjectInMonths"),
                    queryResult.getString("status"),
                    queryResult.getString("startDate"),
                    queryResult.getString(ValidatorForm.convertSQLDateToJavaDate("endDate")))); 
            }
        }catch(SQLException ex){
            Logger.getLogger(ProjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            QUERY.closeConnection();
            return listProjects;
        }
    }
    
    public List<String> getProjectNameListForEvidence(){
        List<String> projectNamesList = new ArrayList<>();
        try{
            Statement instructionQuery = QUERY.getConnectionDatabase().createStatement();
            ResultSet queryResult = instructionQuery.executeQuery("Select projectName FROM Project");
            while(queryResult.next()){
                String projectName = queryResult.getString("projectName");
                projectNamesList.add(projectName);
            }
        }catch(SQLException ex){
            Logger.getLogger(ProjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            QUERY.closeConnection();
            return projectNamesList;
        }
    }
    
    @Override
    public void addProject(Project newProject){
        Connection connection = QUERY.getConnectionDatabaseNotAutoCommit();
        try{
            PreparedStatement sentenceQuery = QUERY.getConnectionDatabase().prepareStatement(
                "INSERT INTO Project VALUES(?, ?, ?, ?, ?, ?, ?, ?)"
            );
            sentenceQuery.setString(1, newProject.getProjectName());
            sentenceQuery.setString(2, newProject.getBodyAcademyKey());
            sentenceQuery.setInt(3, newProject.getDurationProjectInMonths());
            sentenceQuery.setString(4, newProject.getStatus());
            sentenceQuery.setString(5, newProject.getStartDate());
            sentenceQuery.setString(6, newProject.getEndDate());
            sentenceQuery.setString(7, newProject.getEstimatedEndDate());
            sentenceQuery.setString(8, newProject.getDescription());
            sentenceQuery.executeUpdate();
        }catch(SQLException sqlEsception){
            try{
                connection.rollback();
                Logger.getLogger(ProjectDAO.class.getName()).log(Level.SEVERE, null, sqlEsception);
            }catch(SQLException ex){
                Logger.getLogger(ProjectDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }finally{
            QUERY.closeConnection();
        }
    }
    
    @Override
    public Project getProjectbyName(String projectName) {
        Project project = new Project();
        Connection connection = QUERY.getConnectionDatabaseNotAutoCommit();
        try{
            PreparedStatement instructionQuery = connection.prepareStatement("SELECT *"
                    + " FROM Project WHERE projectName = ? ;");
            instructionQuery.setString(1, projectName);
            ResultSet queryResult = instructionQuery.executeQuery();
            connection.commit();
            if(queryResult.next()){
                project = new Project(
                    queryResult.getString("projectName"),
                    queryResult.getString("bodyAcademyKey"),
                    queryResult.getInt("durationProjectInMonths"),
                    queryResult.getString("status"),
                    queryResult.getString("startDate"),
                    queryResult.getString("endDate"),
                    queryResult.getString("estimatedEndDate"),
                    queryResult.getString("description")
                );
            }
        }catch(SQLException sqlException){
            Logger.getLogger(ProjectDAO.class.getName()).log(Level.SEVERE, null, sqlException);
        }finally{
            QUERY.closeConnection();
            return project;
        }
    }
    
    @Override
    public boolean updateProject(Project project, String oldProjectName){
        boolean check = false;
        Connection connection = QUERY.getConnectionDatabaseNotAutoCommit();
        try{
            PreparedStatement sentenceQuery = QUERY.getConnectionDatabase().prepareStatement(
                "UPDATE Project SET projectName = ?, bodyAcademyKey = ?, durationProjectInMonths = ?, status = ?,"
                + " startDate = ?, endDate = ?, estimatedEndDate = ?, description= ? WHERE projectName = ?;"
            );
            sentenceQuery.setString(1, project.getProjectName());
            sentenceQuery.setString(2, project.getBodyAcademyKey());
            sentenceQuery.setInt(3, project.getDurationProjectInMonths());
            sentenceQuery.setString(4, project.getStatus());
            sentenceQuery.setString(5, project.getStartDate());
            sentenceQuery.setString(6, project.getEndDate());
            sentenceQuery.setString(7, project.getEstimatedEndDate());
            sentenceQuery.setString(8, project.getDescription());
            sentenceQuery.setString(9, oldProjectName);
            sentenceQuery.executeUpdate();
            check = true;
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(ProjectDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }finally{
            QUERY.closeConnection();
            return check;
        }
    }
    
    @Override
    public boolean projectRegistered(String projectName){
        boolean isRegistered = false;
        try{
            PreparedStatement sentenceQuery = QUERY.getConnectionDatabase().prepareStatement(
                "SELECT projectName FROM Project WHERE projectName = ?;"
            );
            sentenceQuery.setString(1, projectName);
            ResultSet queryResult = sentenceQuery.executeQuery();
            if(queryResult.next()){
                isRegistered = true;                
            }
        }catch(SQLException sqlException){
            Logger.getLogger(ProjectDAO.class.getName()).log(Level.SEVERE, null, sqlException);
        }finally{
            QUERY.closeConnection();
            return isRegistered;
        }
    }
}
