/**
* @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
*/

package sgp.ca.domain;

public class Comment{
    
    private int commentKey;
    private String commentDescription;
    private String commentator;
    private String commentTime;
    private String commentDate;

    public Comment(int commentKey, String commentDescription, String commentator, 
    String commentTime, String commentDate){
        this.commentKey = commentKey;
        this.commentDescription = commentDescription;
        this.commentator = commentator;
        this.commentTime = commentTime;
        this.commentDate = commentDate;
    }

    public Comment(){
        
    }

    public int getCommentKey(){
        return commentKey;
    }

    public void setCommentKey(int commentKey){
        this.commentKey = commentKey;
    }

    public String getCommentDescription(){
        return commentDescription;
    }

    public void setCommentDescription(String commentDescription){
        this.commentDescription = commentDescription;
    }

    public String getCommentator(){
        return commentator;
    }

    public void setCommentatorRFC(String commentatorRFC){
        this.commentator = commentatorRFC;
    }

    public String getCommentTime(){
        return commentTime;
    }

    public void setCommentTime(String commentTime){
        this.commentTime = commentTime;
    }

    public String getCommentDate(){
        return commentDate;
    }

    public void setCommentDate(String commentDate){
        this.commentDate = commentDate;
    }
    
}
