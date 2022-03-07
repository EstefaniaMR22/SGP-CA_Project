/**
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package model.old.dataaccess;

import java.sql.Connection;
import model.old.domain.Meeting;
import model.old.domain.MeetingAgenda;

public interface IMeetingAgendaDAO{
    public void addMeetingAgenda(Connection connection, Meeting meeting);
    public MeetingAgenda getMeetingAgendaByMeeting(int meetingKey);
    public void deleteTopic(Connection connection, MeetingAgenda meetingAgenda);
    public void deletePrerequisite(Connection connection, MeetingAgenda meetingAgenda);
}
