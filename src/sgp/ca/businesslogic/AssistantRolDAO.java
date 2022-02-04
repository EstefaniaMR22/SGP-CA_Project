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
import sgp.ca.domain.AssistantRol;
import sgp.ca.domain.Meeting;

public class AssistantRolDAO implements IAssistantRolDAO{
    private final ConnectionDatabase CONNECTION = new ConnectionDatabase();

    @Override
    public void addAssistantRol(Connection connection, Meeting meeting) {
        meeting.getAssistantsRol().forEach( assistantRol -> {
            try{
                PreparedStatement sentenceQuery = connection.prepareStatement(
                    "INSERT INTO IntegrantMeeting (integrantMeetingKey, assistantName, "
                    + "meetingKey, role, assistantNumber) VALUES (? , ?, ? , ? , ?)"
                );
                sentenceQuery.setInt(1, assistantRol.getAssistantRolKey());
                sentenceQuery.setString(2, assistantRol.getAssistantName());
                sentenceQuery.setInt(3, meeting.getMeetingKey());
                sentenceQuery.setString(4, assistantRol.getRoleAssistant());
                sentenceQuery.setInt(5, assistantRol.getAssistantNumber());
                sentenceQuery.executeUpdate();
            }catch(SQLException sqlException){
                try{
                    connection.rollback();
                    CONNECTION.closeConnection();
                    Logger.getLogger(AssistantRol.class.getName()).log(Level.SEVERE, null, sqlException);
                }catch(SQLException ex){
                    Logger.getLogger(AssistantRolDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    @Override
    public List<AssistantRol> getAssistantsRolByMeeting(int meetingKey){
        List<AssistantRol> assistantRolList = new ArrayList();
        try{
            PreparedStatement sentenceQuery = CONNECTION.getConnectionDatabase().prepareStatement(
                    "SELECT * FROM IntegrantMeeting WHERE meetingKey = ?;"
            );
            sentenceQuery.setInt(1, meetingKey);
            ResultSet queryResult = sentenceQuery.executeQuery();
            while(queryResult.next()){
                AssistantRol newAssistantRol = new AssistantRol(
                    queryResult.getInt("integrantMeetingKey"),
                    queryResult.getString("assistantName"),
                    queryResult.getString("role"),
                    queryResult.getInt("assistantNumber")
                );
                assistantRolList.add(newAssistantRol);
            }
        }catch(SQLException sqlException){
            Logger.getLogger(AssistantRolDAO.class.getName()).log(Level.SEVERE, null, sqlException);
        }finally{
            return assistantRolList;
        }
    }
}
