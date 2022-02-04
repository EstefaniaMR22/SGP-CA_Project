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
import java.util.logging.Level;
import java.util.logging.Logger;
import sgp.ca.dataaccess.ConnectionDatabase;
import sgp.ca.domain.Meeting;
import sgp.ca.domain.MeetingAgenda;

public class MeetingAgendaDAO implements IMeetingAgendaDAO{
    private final ConnectionDatabase CONNECTION = new ConnectionDatabase();
    private TopicDAO topicDAO = new TopicDAO();
    private PrerequisiteDAO prerequisiteDAO = new PrerequisiteDAO();

    @Override
    public void addMeetingAgenda(Connection connection, Meeting meeting){
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "INSERT INTO MeetingAgenda (meetingKey, totalTime, "
                + "estimatedTotalTime, numberTopics) VALUES (?, ?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS
            );
            sentenceQuery.setInt(1, meeting.getMeetingKey());
            sentenceQuery.setString(2, meeting.getMeetingAgenda().getTotalTime());
            sentenceQuery.setString(3, meeting.getMeetingAgenda().getEstimatedTotalTime());
            sentenceQuery.setInt(4, meeting.getMeetingAgenda().getTotaltopics());
            sentenceQuery.executeUpdate();
            this.updateMeetingAgendaWithKeyGenerated(sentenceQuery, meeting.getMeetingAgenda());
            topicDAO.addTopic(connection, meeting.getMeetingAgenda());
            prerequisiteDAO.addPrerequisite(connection, meeting.getMeetingAgenda());
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                CONNECTION.closeConnection();
                Logger.getLogger(MeetingAgenda.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(MeetingAgendaDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public MeetingAgenda getMeetingAgendaByMeeting(int meetingKey){
        MeetingAgenda newMeetingAgenda = new MeetingAgenda();
        try{
            PreparedStatement sentenceQuery = CONNECTION.getConnectionDatabase().prepareStatement(
                "SELECT * FROM MeetingAgenda WHERE meetingKey = ?;"
            );
            sentenceQuery.setInt(1, meetingKey);
            ResultSet queryResult = sentenceQuery.executeQuery();
            if(queryResult.next()){newMeetingAgenda = new MeetingAgenda(
                queryResult.getInt("meetingAgendaKey"),
                queryResult.getTime("totalTime").toString(),
                queryResult.getTime("estimatedTotalTime").toString(),
                queryResult.getInt("numberTopics")
            );}
            newMeetingAgenda.setTopics(topicDAO.getTopicsByAgendaMeeting(newMeetingAgenda.getMeetingAgendaKey()));
            newMeetingAgenda.setPrerequisites(prerequisiteDAO.getPrerequisiteByAgendaMeeting(newMeetingAgenda.getMeetingAgendaKey()));
        }catch(SQLException sqlException){
            Logger.getLogger(MeetingAgenda.class.getName()).log(Level.SEVERE, null, sqlException);
        }finally{
            return newMeetingAgenda;
        }
    }

    @Override
    public void deleteTopic(Connection connection, MeetingAgenda meetingAgenda){
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "DELETE FROM Topic WHERE meetingAgendaKey = ?;"
            );
            sentenceQuery.setInt(1, meetingAgenda.getMeetingAgendaKey());
            sentenceQuery.executeUpdate();
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                connection.close();
                Logger.getLogger(MeetingAgendaDAO.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(MeetingAgendaDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void deletePrerequisite(Connection connection, MeetingAgenda meetingAgenda){
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "DELETE FROM Prerequisite WHERE meetingAgendaKey = ?;"
            );
            sentenceQuery.setInt(1, meetingAgenda.getMeetingAgendaKey());
            sentenceQuery.executeUpdate();
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                connection.close();
                Logger.getLogger(MeetingAgendaDAO.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(MeetingAgendaDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void updateMeetingAgendaWithKeyGenerated(PreparedStatement statement, MeetingAgenda meetingAgenda){
        try{
            ResultSet result = statement.getGeneratedKeys();
            if(result.next()){
                meetingAgenda.setMeetingAgendaKey(result.getInt(1));
            }
        }catch(SQLException ex){
            Logger.getLogger(MeetingAgendaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
