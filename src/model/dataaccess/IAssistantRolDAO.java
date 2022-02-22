/**
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package model.dataaccess;

import java.sql.Connection;
import java.util.List;
import model.domain.AssistantRol;
import model.domain.Meeting;

public interface IAssistantRolDAO{
    public void addAssistantRol(Connection connection, Meeting meeting);
    public List<AssistantRol> getAssistantsRolByMeeting(int meetingKey);
}
