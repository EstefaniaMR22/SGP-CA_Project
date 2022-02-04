/**
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package businesslogic.testmeetingdao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Test;
import sgp.ca.businesslogic.MeetingAgendaDAO;
import sgp.ca.dataaccess.ConnectionDatabase;
import sgp.ca.domain.MeetingAgenda;

public class TopicDeleteTest{
    private final ConnectionDatabase CONNECTION = new ConnectionDatabase();
    
    @Test
    public void testCleanAndCompleteTopicsDeleteByMeetingAgenda(){
        try{
            Connection connection = CONNECTION.getConnectionDatabaseNotAutoCommit();
            MeetingAgendaDAO meetingAgendaDAO = new MeetingAgendaDAO();
            MeetingAgenda meetingAgendaRetrieved = meetingAgendaDAO.getMeetingAgendaByMeeting(46);
            meetingAgendaDAO.deleteTopic(connection, meetingAgendaRetrieved);
            connection.commit();
            connection.setAutoCommit(true);
            meetingAgendaRetrieved = meetingAgendaDAO.getMeetingAgendaByMeeting(46);
            int expectedTopicsNum = 0;
            int retrievedTopicsNum = meetingAgendaRetrieved.getTopics().size();
            Assert.assertEquals(expectedTopicsNum, retrievedTopicsNum);
        }catch(SQLException sqlException){
            Logger.getLogger(TopicDeleteTest.class.getName()).log(Level.SEVERE, null, sqlException);
        }finally{
            CONNECTION.closeConnection();
        }
    }
}
