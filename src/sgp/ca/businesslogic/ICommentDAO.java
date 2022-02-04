/**
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package sgp.ca.businesslogic;

import java.sql.Connection;
import java.util.List;
import sgp.ca.domain.Comment;
import sgp.ca.domain.Meeting;

public interface ICommentDAO{
    public void addComment(Connection connection, Meeting meeting);
    public List<Comment> getCommentsByMeeting(int meetingKey);
}
