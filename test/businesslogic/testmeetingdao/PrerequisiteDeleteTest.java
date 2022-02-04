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

public class PrerequisiteDeleteTest{
    private final ConnectionDatabase CONNECTION = new ConnectionDatabase();
    
    @Test
    public void testCleanAndCompletePrerequisitesDeleteByMeetingAgenda(){
        try{
            Connection connection = CONNECTION.getConnectionDatabaseNotAutoCommit();
            MeetingAgendaDAO meetingAgendaDAO = new MeetingAgendaDAO();
            MeetingAgenda meetingAgendaRetrieved = meetingAgendaDAO.getMeetingAgendaByMeeting(47);
            meetingAgendaDAO.deletePrerequisite(connection, meetingAgendaRetrieved);
            connection.commit();
            connection.setAutoCommit(true);
            meetingAgendaRetrieved = meetingAgendaDAO.getMeetingAgendaByMeeting(47);
            int expectedPrerequisitesNum = 0;
            int retrievedPrerequisitesNum = meetingAgendaRetrieved.getPrerequisites().size();
            Assert.assertEquals(expectedPrerequisitesNum, retrievedPrerequisitesNum);
        }catch(SQLException sqlException){
            Logger.getLogger(PrerequisiteDeleteTest.class.getName()).log(Level.SEVERE, null, sqlException);
        }finally{
            CONNECTION.closeConnection();
        }
    }
}
