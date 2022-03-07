/**
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package model.old.domain;

import java.util.ArrayList;
import java.util.List;

public class MeetingAgenda{
    private int meetingAgendaKey;
    private String totalTime;
    private String estimatedTotalTime;
    private int totaltopics;
    private List<Topic> topics;
    private List<Prerequisite> prerequisites;

    public MeetingAgenda(int meetingAgendaKey, String totalTime, String estimatedTotalTime, int totaltopics){
        this.meetingAgendaKey = meetingAgendaKey;
        this.totalTime = totalTime;
        this.estimatedTotalTime = estimatedTotalTime;
        this.totaltopics = totaltopics;
        this.topics = new ArrayList<>();
        this.prerequisites = new ArrayList<>();
    }

    public MeetingAgenda(){
        this.topics = new ArrayList<>();
        this.prerequisites = new ArrayList<>();
    }

    public int getMeetingAgendaKey(){
        return meetingAgendaKey;
    }

    public void setMeetingAgendaKey(int meetingAgendaKey){
        this.meetingAgendaKey = meetingAgendaKey;
    }

    public String getTotalTime(){
        return totalTime;
    }

    public void setTotalTime(String totalTime){
        this.totalTime = totalTime;
    }

    public String getEstimatedTotalTime(){
        return estimatedTotalTime;
    }

    public void setEstimatedTotalTime(String estimatedTotalTime){
        this.estimatedTotalTime = estimatedTotalTime;
    }

    public int getTotaltopics(){
        return totaltopics;
    }

    public void setTotaltopics(int totaltopics){
        this.totaltopics = totaltopics;
    }

    public List<Topic> getTopics(){
        return topics;
    }

    public void setTopics(List<Topic> topics){
        this.topics = topics;
    }

    public List<Prerequisite> getPrerequisites(){
        return prerequisites;
    }

    public void setPrerequisites(List<Prerequisite> prerequisites){
        this.prerequisites = prerequisites;
    }

    public Topic getTopicByNumber(int numberTopic){
        Topic topicReturn = new Topic();
        for(Topic topic : this.topics){
            if(topic.getNumberTopic() == numberTopic){
                topicReturn = topic;
            }
        }
        return topicReturn;
    }
    
    public Prerequisite getPrerequisiteByKey(int prerequisiteKey){
        Prerequisite prerequisiteReturn = new Prerequisite();
        for(Prerequisite prerequisite : this.prerequisites){
            if(prerequisite.getPrerequisiteKey() == prerequisiteKey){
                prerequisiteReturn = prerequisite;
            }
        }
        return prerequisiteReturn;
    }
    
    public void addTopic(Topic newtopic){
        topics.add(newtopic);
    }
    
    public void removeTopic(Topic topic){
        topics.remove(topic);
    }
    
    public void addPrerequisite(Prerequisite newprerequisite){
        prerequisites.add(newprerequisite);
    }
    
    public void removePrerequisite(Prerequisite prerequisite){
        prerequisites.remove(prerequisite);
    }
}
