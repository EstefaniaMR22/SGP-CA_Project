/*
 * @author Josué 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package model.old.dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.old.domain.WorkPlan;
import model.old.ConnectionDatabase;

public class WorkPlanDAO implements IWorkPlanDAO{

    private final ConnectionDatabase CONNECTION = new ConnectionDatabase();
    private final GoalDAO GOAL_DAO = new GoalDAO();
    
    @Override
    public List<WorkPlan> getWorkPlanPeriots(String bodyAcademyKey){
        List<WorkPlan> workPlanPeriots = new ArrayList<>();
        try{
            PreparedStatement sentenceQuery = CONNECTION.getConnectionDatabase().prepareStatement(
                "SELECT startDate, endDate FROM `WorkPlan` WHERE bodyAcademyKey = ? ORDER BY startDate DESC;"
            );
            sentenceQuery.setString(1, bodyAcademyKey);
            ResultSet queryResult = sentenceQuery.executeQuery();
            while(queryResult.next()){
                WorkPlan workplan = new WorkPlan();
                workplan.setStartDatePlan(queryResult.getDate("startDate").toString());
                workplan.setEndDatePlan(queryResult.getDate("endDate").toString());
                workPlanPeriots.add(workplan);
            }
        }catch(SQLException sqlException){
            workPlanPeriots = null;
            Logger.getLogger(WorkPlan.class.getName()).log(Level.SEVERE, null, sqlException);
        }finally{
            CONNECTION.closeConnection();
            return workPlanPeriots;
        }
    }
    
    @Override
    public WorkPlan getWorkPlan(String endDatePlan, String bodyAcademyKey){
        WorkPlan workPlan = new WorkPlan();
        Connection connection = CONNECTION.getConnectionDatabaseNotAutoCommit();
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "SELECT * FROM WorkPlan WHERE endDate = ? AND bodyAcademyKey = ?;"
            );
            sentenceQuery.setString(1, endDatePlan);
            sentenceQuery.setString(2, bodyAcademyKey);
            ResultSet queryResult = sentenceQuery.executeQuery();
            if(queryResult.next()){workPlan = new WorkPlan(
                queryResult.getInt("workplanKey"),
                queryResult.getInt("durationInYears"),
                queryResult.getString("generalTarjet"),
                queryResult.getDate("startDate").toString(),
                queryResult.getDate("endDate").toString(),
                queryResult.getString("bodyAcademyKey")
            );}
            workPlan.setGoals(GOAL_DAO.getGoalListByWorkPlan(connection, workPlan.getWorkplanKey()));
            connection.commit();
            connection.setAutoCommit(true);
        }catch(SQLException sqlException){
            connection.rollback();
            workPlan = null;
            Logger.getLogger(WorkPlan.class.getName()).log(Level.SEVERE, null, sqlException);
        }finally{
            CONNECTION.closeConnection();
            return workPlan;
        }
    }

    @Override
    public boolean addWorkPlan(WorkPlan newWorkPlan){
        Connection connection = CONNECTION.getConnectionDatabaseNotAutoCommit();
        boolean correctInsertion = false;
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "INSERT INTO WorkPlan (bodyAcademyKey, startDate, endDate, generalTarjet, durationInYears) VALUES(?, ?, ?, ?, ?);",
                PreparedStatement.RETURN_GENERATED_KEYS
            );
            sentenceQuery.setString(1, newWorkPlan.getBodyAcademyKey());
            sentenceQuery.setString(2, newWorkPlan.getStartDatePlan());
            sentenceQuery.setString(3, newWorkPlan.getEndDatePlan());
            sentenceQuery.setString(4, newWorkPlan.getGeneralTarget());
            sentenceQuery.setInt(5, newWorkPlan.getDurationInYears());
            sentenceQuery.executeUpdate();
            this.updateWorkPlanWithKeyGenerated(sentenceQuery, newWorkPlan);
            GOAL_DAO.addGoals(connection, newWorkPlan);
            connection.commit();
            connection.setAutoCommit(true);
            correctInsertion = true;
        }catch(SQLException sqlException){
            try{
                correctInsertion = false;
                connection.rollback();
                Logger.getLogger(WorkPlan.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(WorkPlanDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }finally{
            CONNECTION.closeConnection();
            return correctInsertion;
        }
    }

    @Override
    public boolean updateWorkPlan(WorkPlan workPlan,  WorkPlan oldWorkPlan){
        Connection connection = CONNECTION.getConnectionDatabaseNotAutoCommit();
        boolean correctUpdate = false;
        try{
            this.deleteGoals(connection, oldWorkPlan);
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "UPDATE WorkPlan SET bodyAcademyKey = ?, startDate = ?, endDate = ?, generalTarjet = ?, durationInYears = ? WHERE workplanKey = ?;"
            );
            sentenceQuery.setString(1, workPlan.getBodyAcademyKey());
            sentenceQuery.setString(2, workPlan.getStartDatePlan());
            sentenceQuery.setString(3, workPlan.getEndDatePlan());
            sentenceQuery.setString(4, workPlan.getGeneralTarget());
            sentenceQuery.setInt(5, workPlan.getDurationInYears());
            sentenceQuery.setInt(6, oldWorkPlan.getWorkplanKey());
            sentenceQuery.executeUpdate();
            workPlan.setWorkplanKey(oldWorkPlan.getWorkplanKey());
            GOAL_DAO.addGoals(connection, workPlan);
            connection.commit();
            connection.setAutoCommit(true);
            correctUpdate = true;
        }catch(SQLException sqlException){
            try{
                correctUpdate = false;
                connection.rollback();
                Logger.getLogger(WorkPlan.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(WorkPlanDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }finally{
            CONNECTION.closeConnection();
            return correctUpdate;
        }
    }

    @Override
    public boolean deleteWorkPlan(WorkPlan workPlan, String bodyAcademyKey){
        Connection connection = CONNECTION.getConnectionDatabaseNotAutoCommit();
        boolean correctDelete = false;
        try{
            this.deleteGoals(connection, workPlan);
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "DELETE FROM WorkPlan WHERE workplanKey = ?;"
            );
            sentenceQuery.setInt(1, workPlan.getWorkplanKey());
            sentenceQuery.executeUpdate();
            connection.commit();
            connection.setAutoCommit(true);
            correctDelete = true;
        }catch(SQLException sqlException){
            try{
                correctDelete = false;
                connection.rollback();
                Logger.getLogger(WorkPlanDAO.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(WorkPlanDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }finally{
            CONNECTION.closeConnection();
            return correctDelete;
        }    
    }
    
    public void deleteGoals(Connection connection, WorkPlan workPlan){
        try{
            GOAL_DAO.deleteActions(connection, workPlan.getGoals());
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "DELETE FROM Goal WHERE workplanKey = ?;"
            );
            sentenceQuery.setInt(1, workPlan.getWorkplanKey());
            sentenceQuery.executeUpdate();
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                connection.close();
                Logger.getLogger(WorkPlanDAO.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(WorkPlanDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public boolean updateWorkPlanWithKeyGenerated(PreparedStatement statement, WorkPlan workPlan){
        boolean correctUpdate = false;
        try{
            ResultSet result = statement.getGeneratedKeys();
            if(result.next()){
                workPlan.setWorkplanKey(result.getInt(1));
            }
            correctUpdate = true;
        }catch(SQLException ex){
            Logger.getLogger(WorkPlanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            return correctUpdate;
        }
    }
    
}
