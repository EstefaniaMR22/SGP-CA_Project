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
import sgp.ca.domain.MeetingAgenda;
import sgp.ca.domain.Topic;

public class TopicDAO implements ITopicDAO{
    private final ConnectionDatabase CONNECTION = new ConnectionDatabase();

    @Override
    public void addTopic(Connection connection, MeetingAgenda meetingAgenda){
        meetingAgenda.getTopics().forEach(topic -> {
            try{
                PreparedStatement sentenceQuery = connection.prepareStatement(
                    "INSERT INTO Topic (meetingAgendaKey, startTime, endTime, "
                    + "plannedTime, realTime, descriptionTopic, discissionLeader, statusTopic) "
                    + "VALUES(?,?,?,?,?,?,?,?);"    
                );
                sentenceQuery.setInt(1, meetingAgenda.getMeetingAgendaKey());
                sentenceQuery.setString(2, topic.getStartTime());
                sentenceQuery.setString(3, topic.getEndTime());
                sentenceQuery.setString(4, topic.getPlannedTime());
                sentenceQuery.setString(5, topic.getRealTime());
                sentenceQuery.setString(6, topic.getDescriptionTopic());
                sentenceQuery.setString(7, topic.getDiscissionLeader());
                sentenceQuery.setString(8, topic.getStatusTopic());
                sentenceQuery.executeUpdate();
            }catch(SQLException sqlException){
                try{
                    connection.rollback();
                    CONNECTION.closeConnection();
                    Logger.getLogger(Topic.class.getName()).log(Level.SEVERE, null, sqlException);
                }catch(SQLException ex){
                    Logger.getLogger(TopicDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    @Override
    public List<Topic> getTopicsByAgendaMeeting(int meetingAgendaKey){
        List<Topic> topicList = new ArrayList<>();
        try{
            PreparedStatement sentenceQuery = CONNECTION.getConnectionDatabase().prepareStatement(
                "SELECT * FROM Topic WHERE meetingAgendaKey = ?;"
            );
            sentenceQuery.setInt(1, meetingAgendaKey);
            ResultSet queryResult = sentenceQuery.executeQuery();
            while(queryResult.next()){
                Topic newTopic = new Topic(
                     queryResult.getInt("numberTopic"),
                     queryResult.getTime("startTime").toString(),
                     queryResult.getTime("endTime").toString(),
                     queryResult.getTime("plannedTime").toString(),
                     queryResult.getTime("realTime").toString(),
                     queryResult.getString("descriptionTopic"),
                     queryResult.getString("discissionLeader"),
                     queryResult.getString("statusTopic")
                );
                topicList.add(newTopic);
            }
        }catch(SQLException sqlException){
            Logger.getLogger(TopicDAO.class.getName()).log(Level.SEVERE, null, sqlException);
        }finally{
            return topicList;
        }
    }
}
