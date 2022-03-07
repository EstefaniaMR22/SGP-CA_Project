/**
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package model.old.dataaccess;

import java.sql.Connection;
import java.util.List;
import model.old.domain.AssistantRol;
import model.old.domain.Meeting;

public interface IAssistantRolDAO{
    public void addAssistantRol(Connection connection, Meeting meeting);
    public List<AssistantRol> getAssistantsRolByMeeting(int meetingKey);
}
