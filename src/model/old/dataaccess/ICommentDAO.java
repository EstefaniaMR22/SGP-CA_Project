/**
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package model.old.dataaccess;

import java.sql.Connection;
import java.util.List;

import model.old.domain.Comment;
import model.old.domain.Meeting;

public interface ICommentDAO{
    public void addComment(Connection connection, Meeting meeting);
    public List<Comment> getCommentsByMeeting(int meetingKey);
}
