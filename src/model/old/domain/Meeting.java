/*
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
*/

package model.old.domain;

import java.util.ArrayList;
import java.util.List;


public class Meeting {
    private int meetingKey;
    private String  meetingDate;
    private String meetingTime;
    private String meetingProject;
    private String meetingRegistrationDate;
    private String statusMeeting;
    private String placeMeeting;
    private String issueMeeting;
    private String meetingNote;
    private String meetingPending;
    private String integrantResponsible;
    private MeetingAgenda meetingAgenda;
    private List<Agreement> agreements;
    private List <Comment> comments;
    private List <AssistantRol> assistantsRol;

    public Meeting(int meetingKey, String meetingDate, String meetingTime, String meetingProject, 
    String meetingRegistrationDate, String statusMeeting, String placeMeeting, String issueMeeting, 
    String meetingNote, String meetingPending, String integrantResponsible){
        this.meetingKey = meetingKey;
        this.meetingDate = meetingDate;
        this.meetingTime = meetingTime;
        this.meetingProject = meetingProject;
        this.meetingRegistrationDate = meetingRegistrationDate;
        this.statusMeeting = statusMeeting;
        this.placeMeeting = placeMeeting;
        this.issueMeeting = issueMeeting;
        this.meetingNote = meetingNote;
        this.meetingPending = meetingPending;
        this.integrantResponsible  = integrantResponsible;
        this.meetingAgenda = new MeetingAgenda();
        this.agreements = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.assistantsRol = new ArrayList<>();
    }

    public Meeting(){
        this.meetingAgenda = new MeetingAgenda();
        this.agreements = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.assistantsRol = new ArrayList<>();
    }

    public int getMeetingKey(){
        return meetingKey;
    }

    public void setMeetingKey(int meetingKey){
        this.meetingKey = meetingKey;
    }

    public String getMeetingDate(){
        return meetingDate;
    }

    public void setMeetingDate(String meetingDate){
        this.meetingDate = meetingDate;
    }

    public String getMeetingTime(){
        return meetingTime;
    }

    public void setMeetingTime(String meetingTime){
        this.meetingTime = meetingTime;
    }

    public String getMeetingProject(){
        return meetingProject;
    }

    public void setMeetingProject(String meetingProject){
        this.meetingProject = meetingProject;
    }

    public String getMeetingRegistrationDate(){
        return meetingRegistrationDate;
    }

    public void setMeetingRegistrationDate(String meetingRegistrationDate){
        this.meetingRegistrationDate = meetingRegistrationDate;
    }

    public String getStatusMeeting(){
        return statusMeeting;
    }

    public void setStatusMeeting(String statusMeeting){
        this.statusMeeting = statusMeeting;
    }

    public String getPlaceMeeting(){
        return placeMeeting;
    }

    public void setPlaceMeeting(String placeMeeting){
        this.placeMeeting = placeMeeting;
    }

    public String getIssueMeeting(){
        return issueMeeting;
    }

    public void setIssueMeeting(String issueMeeting){
        this.issueMeeting = issueMeeting;
    }

    public String getMeetingNote(){
        return meetingNote;
    }

    public void setMeetingNote(String meetingNote){
        this.meetingNote = meetingNote;
    }

    public String getMeetingPending(){
        return meetingPending;
    }

    public void setMeetingPending(String meetingPending){
        this.meetingPending = meetingPending;
    }

    public String getIntegrantResponsible(){
        return integrantResponsible;
    }

    public void setIntegrantResponsible(String integrantResponsible){
        this.integrantResponsible = integrantResponsible;
    }

    public MeetingAgenda getMeetingAgenda(){
        return meetingAgenda;
    }

    public void setMeetingAgenda(MeetingAgenda meetingAgenda){
        this.meetingAgenda = meetingAgenda;
    }

    public List<Agreement> getAgreements() {
        return agreements;
    }

    public void setAgreements(List<Agreement> agreements){
        this.agreements = agreements;
    }

    public List<Comment> getComments(){
        return comments;
    }

    public void setComments(List<Comment> comments){
        this.comments = comments;
    }

    public List<AssistantRol> getAssistantsRol(){
        return assistantsRol;
    }

    public void setAssistantsRol(List<AssistantRol> assistantsRol){
        this.assistantsRol = assistantsRol;
    }
    
    public Agreement getAgreementByNumber(int agreementNumber){
        Agreement agreementReturn = new Agreement();
        for(Agreement agreement : this.agreements){
            if(agreement.getAgreementNumber() == agreementNumber){
                agreementReturn = agreement;
            }
        }
        return agreementReturn;
    }
    
    public Comment getCommentByKey(int commentKey){
        Comment commentReturn = new Comment();
        for(Comment comment : this.comments){
            if(comment.getCommentKey() == commentKey){
                commentReturn = comment;
            }
        }
        return commentReturn;
    }
    
    public AssistantRol getAssistantRolByAssistantRolKey(int assistantRolKey){
        AssistantRol assistantRolReturn = new AssistantRol();
        for(AssistantRol assistantRol : this.assistantsRol){
            if(assistantRol.getAssistantRolKey() == assistantRolKey){
                assistantRolReturn = assistantRol;
            }
        }
        return assistantRolReturn;
    }
    
    public void addAgreement(Agreement newAgreement){
        agreements.add(newAgreement);
    }
    
    public void removeAgreement(Agreement agreement){
        agreements.remove(agreement);
    }
    
    public void addComment(Comment comment){
        comments.add(comment);
    }
    
    public void removeComment(Comment comment){
        comments.remove(comment);
    }
    
    public void addAssistantRol(AssistantRol assistantRol){
        assistantsRol.add(assistantRol);
    }
    
    public void removeAssistantRol(AssistantRol assistantRol){
        assistantsRol.remove(assistantRol);
    }
    
    public boolean equals(Object obj){
        boolean sameMeetings = true;
        Meeting meeting = (Meeting)obj;
        if((this.hashCode() != meeting.hashCode()) || (this.agreements.size() != meeting.getAgreements().size())){
            sameMeetings = false;
        }
        return sameMeetings;
    }
}
