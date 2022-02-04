/**
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package sgp.ca.businesslogic;

import java.util.List;
import sgp.ca.domain.Meeting;

public interface IMeetingDAO{
    public List<Meeting> getAllMeetings();
    public Meeting getMeeting(int meetingKey);
    public boolean addMeeting(Meeting newMeeting);
    public boolean updateMeeting(Meeting meeting, Meeting oldMeeting);
    public boolean deleteMeeting(Meeting meeting);
    public void addMeetingNote(String newMeetingNote, Meeting meeting);
    public void addMeetingPending(String newMeetingPending, Meeting meeting);
}
