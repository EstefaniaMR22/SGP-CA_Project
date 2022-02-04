/**
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package sgp.ca.businesslogic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sgp.ca.dataaccess.ConnectionDatabase;
import sgp.ca.domain.Meeting;

public class MeetingDAO implements IMeetingDAO{
    private final ConnectionDatabase CONNECTION = new ConnectionDatabase();
    private MeetingAgendaDAO meetingAgendaDAO = new MeetingAgendaDAO();
    private AgreementDAO agreementDAO = new AgreementDAO();
    private CommentDAO commentDAO = new CommentDAO();
    private AssistantRolDAO assisntantRolDAO = new AssistantRolDAO();

    @Override
    public List<Meeting> getAllMeetings(){
        List<Meeting> meetingsList = new ArrayList<>();
        try{
            PreparedStatement sentenceQuery = CONNECTION.getConnectionDatabase().prepareStatement(
                "SELECT meetingKey, meetingDate, meetingTime, issueMeeting, integrantResponsible FROM `Meeting`;"
            );
            
            ResultSet queryResult = sentenceQuery.executeQuery();
            while(queryResult.next()){
                Meeting meeting = new Meeting ();
                meeting.setMeetingKey(queryResult.getInt("meetingKey"));
                meeting.setMeetingDate(queryResult.getDate("meetingDate").toString());
                meeting.setMeetingTime(queryResult.getTime("meetingTime").toString());
                meeting.setIssueMeeting(queryResult.getString("issueMeeting"));
                meeting.setIntegrantResponsible(queryResult.getString("integrantResponsible"));
                meetingsList.add(meeting);
            }
        }catch(SQLException sqlException){
            meetingsList = null;
            Logger.getLogger(Meeting.class.getName()).log(Level.SEVERE, null, sqlException);
        }finally{
            CONNECTION.closeConnection();
            return meetingsList;
        }
    }

    @Override
    public Meeting getMeeting(int meetingKey){
        Meeting meeting = new Meeting();
        try{
            PreparedStatement sentenceQuery = CONNECTION.getConnectionDatabase().prepareStatement(
                "SELECT * FROM Meeting WHERE meetingKey = ?;"
            );
            
            sentenceQuery.setInt(1, meetingKey);
            ResultSet queryResult = sentenceQuery.executeQuery();
            if(queryResult.next()){meeting = new Meeting (
                queryResult.getInt("meetingKey"),
                queryResult.getDate("meetingDate").toString(),
                queryResult.getTime("meetingTime").toString(),
                queryResult.getString("meetingProject"),
                queryResult.getDate("meetingRegistrationDate").toString(),
                queryResult.getString("statusMeeting"),
                queryResult.getString("placeMeeting"),
                queryResult.getString("issueMeeting"),
                queryResult.getString("meetingNote"),
                queryResult.getString("meetingPending"),
                queryResult.getString("integrantResponsible")
            );}
            meeting.setMeetingAgenda(meetingAgendaDAO.getMeetingAgendaByMeeting(meetingKey));
            meeting.setAgreements(agreementDAO.getAgreementListByMeeting(meetingKey));
            meeting.setComments(commentDAO.getCommentsByMeeting(meetingKey));
            meeting.setAssistantsRol(assisntantRolDAO.getAssistantsRolByMeeting(meetingKey));
        }catch(SQLException sqlException){
            meeting = null;
            Logger.getLogger(Meeting.class.getName()).log(Level.SEVERE, null, sqlException);
        }finally{
            CONNECTION.closeConnection();
            return meeting;
        }
    }

    @Override
    public boolean addMeeting(Meeting newMeeting){
        boolean addedMeeting = false;
        Connection connection = CONNECTION.getConnectionDatabaseNotAutoCommit();
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "INSERT INTO Meeting (meetingDate, meetingTime, meetingProject, "
                + "meetingRegistrationDate, statusMeeting, placeMeeting, issueMeeting, "
                + "meetingNote, meetingPending, integrantResponsible) VALUES(?,?,?,?,?,?,?,?,?,?);",
                PreparedStatement.RETURN_GENERATED_KEYS
            );
            sentenceQuery.setString(1, newMeeting.getMeetingDate());
            sentenceQuery.setString(2, newMeeting.getMeetingTime());
            sentenceQuery.setString(3, newMeeting.getMeetingProject());
            sentenceQuery.setString(4, newMeeting.getMeetingRegistrationDate());
            sentenceQuery.setString(5, newMeeting.getStatusMeeting());
            sentenceQuery.setString(6, newMeeting.getPlaceMeeting());
            sentenceQuery.setString(7, newMeeting.getIssueMeeting());
            sentenceQuery.setString(8, newMeeting.getMeetingNote());
            sentenceQuery.setString(9, newMeeting.getMeetingPending());
            sentenceQuery.setString(10, newMeeting.getIntegrantResponsible());
            sentenceQuery.executeUpdate();
            this.updateMeetingWithKeyGenerated(sentenceQuery, newMeeting);
            meetingAgendaDAO.addMeetingAgenda(connection, newMeeting);
            agreementDAO.addAgreements(connection, newMeeting);
            commentDAO.addComment(connection, newMeeting);
            assisntantRolDAO.addAssistantRol(connection, newMeeting);
            connection.commit();
            connection.setAutoCommit(true);
            addedMeeting = true;
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                Logger.getLogger(Meeting.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(MeetingDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }finally{
            CONNECTION.closeConnection();
            return addedMeeting;
        }
    }

    @Override
    public boolean updateMeeting(Meeting meeting, Meeting oldMeeting){
        boolean uptadedMeeting = false;
        Connection connection = CONNECTION.getConnectionDatabaseNotAutoCommit();
        try{
            this.deleteMeetingAgenda(connection, oldMeeting);
            this.deleteAgreement(connection, oldMeeting);
            this.deleteComment(connection, oldMeeting);
            this.deleteAssistantRol(connection, oldMeeting);
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "UPDATE Meeting SET meetingDate = ?, meetingTime = ?, meetingProject = ?, "
                + "meetingRegistrationDate = ?, statusMeeting = ?, placeMeeting = ?, "
                + "issueMeeting = ?, meetingNote = ?, meetingPending = ?, integrantResponsible = ? "
                + "WHERE meetingKey = ?;"
            );
            sentenceQuery.setString(1, meeting.getMeetingDate());
            sentenceQuery.setString(2, meeting.getMeetingTime());
            sentenceQuery.setString(3, meeting.getMeetingProject());
            sentenceQuery.setString(4, meeting.getMeetingRegistrationDate());
            sentenceQuery.setString(5, meeting.getStatusMeeting());
            sentenceQuery.setString(6, meeting.getPlaceMeeting());
            sentenceQuery.setString(7, meeting.getIssueMeeting());
            sentenceQuery.setString(8, meeting.getMeetingNote());
            sentenceQuery.setString(9, meeting.getMeetingPending());
            sentenceQuery.setString(10, meeting.getIntegrantResponsible());
            sentenceQuery.setInt(11, oldMeeting.getMeetingKey());
            sentenceQuery.executeUpdate();
            meeting.setMeetingKey(oldMeeting.getMeetingKey());
            meetingAgendaDAO.addMeetingAgenda(connection, meeting);
            agreementDAO.addAgreements(connection, meeting);
            commentDAO.addComment(connection, meeting);
            assisntantRolDAO.addAssistantRol(connection, meeting);
            connection.commit();
            connection.setAutoCommit(true);
            uptadedMeeting = true;
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                Logger.getLogger(Meeting.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(MeetingDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }finally{
            CONNECTION.closeConnection();
            return uptadedMeeting;
        }
    }

    @Override
    public boolean deleteMeeting(Meeting meeting){
        boolean deletedMeeting = false;
        Connection connection = CONNECTION.getConnectionDatabaseNotAutoCommit();
        try{
            this.deleteMeetingAgenda(connection, meeting);
            this.deleteAgreement(connection, meeting);
            this.deleteComment(connection, meeting);
            this.deleteAssistantRol(connection, meeting);
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "DELETE FROM Meeting WHERE meetingKey = ?;"
            );
            sentenceQuery.setInt(1, meeting.getMeetingKey());
            sentenceQuery.executeUpdate();
            connection.commit();
            connection.setAutoCommit(true);
            deletedMeeting = true;
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                Logger.getLogger(MeetingDAO.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(MeetingDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }finally{
            CONNECTION.closeConnection();
            return deletedMeeting;
        }
    }
    
    private void deleteMeetingAgenda(Connection connection, Meeting meeting){
        try{
            meetingAgendaDAO.deleteTopic(connection, meeting.getMeetingAgenda());
            meetingAgendaDAO.deletePrerequisite(connection, meeting.getMeetingAgenda());
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "DELETE FROM MeetingAgenda WHERE meetingKey = ?;"
            );
            sentenceQuery.setInt(1, meeting.getMeetingKey());
            sentenceQuery.executeUpdate();
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                connection.close();
                Logger.getLogger(MeetingDAO.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(MeetingDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void deleteAgreement(Connection connection, Meeting meeting){
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "DELETE FROM Agreement WHERE meetingKey = ?;"
            );
            sentenceQuery.setInt(1, meeting.getMeetingKey());
            sentenceQuery.executeUpdate();
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                Logger.getLogger(MeetingDAO.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(MeetingDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void deleteComment(Connection connection, Meeting meeting){
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "DELETE FROM Comment WHERE meetingKey = ?;"
            );
            sentenceQuery.setInt(1, meeting.getMeetingKey());
            sentenceQuery.executeUpdate();
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                Logger.getLogger(Meeting.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(MeetingDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void deleteAssistantRol(Connection connection, Meeting meeting){
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "DELETE FROM IntegrantMeeting WHERE meetingKey = ?;"
            );
            sentenceQuery.setInt(1, meeting.getMeetingKey());
            sentenceQuery.executeUpdate();
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                Logger.getLogger(Meeting.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(MeetingDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Override
    public void addMeetingNote(String newMeetingNote, Meeting meeting){
        try{
            PreparedStatement sentenceQuery = CONNECTION.getConnectionDatabase().prepareStatement(
                "UPDATE Meeting SET meetingNote= ? WHERE meetingKey = ? ;"
            );
            sentenceQuery.setString(1, newMeetingNote);
            sentenceQuery.setInt(2, meeting.getMeetingKey());
            sentenceQuery.executeUpdate();
        }catch(SQLException ex){
            Logger.getLogger(MeetingDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            CONNECTION.closeConnection();
        }
    }
    
    @Override
    public void addMeetingPending(String newMeetingPending, Meeting meeting){
        try{
            PreparedStatement sentenceQuery = CONNECTION.getConnectionDatabase().prepareStatement(
                "UPDATE Meeting SET meetingNote= ? WHERE meetingKey = ?;"
            );
            sentenceQuery.setString(1, newMeetingPending);
            sentenceQuery.setInt(2, meeting.getMeetingKey());
            sentenceQuery.executeUpdate();
        }catch(SQLException ex){
            Logger.getLogger(MeetingDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            CONNECTION.closeConnection();
        }
    }
    
    private void updateMeetingWithKeyGenerated(PreparedStatement statement, Meeting meeting){
        try{
            ResultSet result = statement.getGeneratedKeys();
            if(result.next()){
                meeting.setMeetingKey(result.getInt(1));
            }
        }catch(SQLException ex){
            Logger.getLogger(MeetingDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
