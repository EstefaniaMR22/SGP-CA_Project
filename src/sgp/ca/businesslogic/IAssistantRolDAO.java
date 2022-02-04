/**
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package sgp.ca.businesslogic;

import java.sql.Connection;
import java.util.List;
import sgp.ca.domain.AssistantRol;
import sgp.ca.domain.Meeting;

public interface IAssistantRolDAO{
    public void addAssistantRol(Connection connection, Meeting meeting);
    public List<AssistantRol> getAssistantsRolByMeeting(int meetingKey);
}
