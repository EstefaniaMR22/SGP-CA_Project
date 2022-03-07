/**
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package model.old.dataaccess;

import java.sql.Connection;
import java.util.List;
import model.old.domain.MeetingAgenda;
import model.old.domain.Topic;

public interface ITopicDAO{
    public void addTopic(Connection connection, MeetingAgenda meetingAgenda);
    public List<Topic> getTopicsByAgendaMeeting(int meetingAgendaKey);
}
