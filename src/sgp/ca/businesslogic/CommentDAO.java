/**
 * @author Estefanía
 * @versión v1.0
 * Last modification date format: 17-06-2021
 */

package sgp.ca.businesslogic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sgp.ca.dataaccess.ConnectionDatabase;
import sgp.ca.domain.Comment;
import sgp.ca.domain.Meeting;

public class CommentDAO implements ICommentDAO{
    private final ConnectionDatabase CONNECTION = new ConnectionDatabase();

    @Override
    public void addComment(Connection connection, Meeting meeting) {
        meeting.getComments().forEach(comment ->{
            try{
                PreparedStatement sentenceQuery = connection.prepareStatement(
                        "INSERT INTO Comment (meetingKey, commentDescription, "
                        + "commentator, commentTime, commentDate) "
                        + "VALUES (?, ?, ?, ?, ?);"
                );
                sentenceQuery.setInt(1, meeting.getMeetingKey());
                sentenceQuery.setString(2, comment.getCommentDescription());
                sentenceQuery.setString(3, comment.getCommentator());
                sentenceQuery.setString(4, comment.getCommentTime());
                sentenceQuery.setString(5, comment.getCommentDate());
                sentenceQuery.executeUpdate();
            }catch(SQLException sqlException){
                try{
                    connection.rollback();
                    CONNECTION.closeConnection();
                    Logger.getLogger(Meeting.class.getName()).log(Level.SEVERE, null, sqlException);
                }catch(SQLException ex){
                    Logger.getLogger(MeetingDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    @Override
    public List<Comment> getCommentsByMeeting(int meetingKey) {
        List<Comment> commentList = new ArrayList();
        try{
            PreparedStatement sentenceQuery = CONNECTION.getConnectionDatabase().prepareStatement(
                "SELECT * FROM Comment WHERE meetingKey = ?;"
            );
            sentenceQuery.setInt(1, meetingKey);
            ResultSet queryResult = sentenceQuery.executeQuery();
            while(queryResult.next()){
                Comment newComment = new Comment(
                    queryResult.getInt("commentKey"),
                    queryResult.getString("commentDescription"),
                    queryResult.getString("commentator"),
                    queryResult.getTime("commentTime").toString(),
                    queryResult.getDate("commentDate").toString()
                );
                commentList.add(newComment);
            }
        }catch(SQLException sqlException){
            Logger.getLogger(MeetingDAO.class.getName()).log(Level.SEVERE, null, sqlException);
        }finally{
            return commentList;
        }
    } 
}
