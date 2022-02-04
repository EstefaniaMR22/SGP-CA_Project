/*
 * @author Josué 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package sgp.ca.domain;

import java.util.ArrayList;
import java.util.List;


public class GeneralResume{
    
    private String bodyAcademyKey;
    private String bodyAcademyName; 
    private String ascriptionArea;
    private String ascriptionUnit;
    private String consolidationDegree;
    private String vision;
    private String mission;
    private String generalTarjet;
    private String registrationDate;
    private String lastEvaluation;
    private List<Member> listMembers;
    private List<Lgac> listLgac;
    private List<WorkPlan> listWorkplans;

    public GeneralResume(String bodyAcademyKey, String bodyAcademyName, 
    String ascriptionArea, String ascriptionUnit, String consolidationDegree, 
    String vision, String mission, String generalTarjet, 
    String registrationDate, String lastEvaluation){
        this.bodyAcademyKey = bodyAcademyKey;
        this.bodyAcademyName = bodyAcademyName;
        this.ascriptionArea = ascriptionArea;
        this.ascriptionUnit = ascriptionUnit;
        this.consolidationDegree = consolidationDegree;
        this.vision = vision;
        this.mission = mission;
        this.generalTarjet = generalTarjet;
        this.registrationDate = registrationDate;
        this.lastEvaluation = lastEvaluation;
        listMembers = new ArrayList<>();
        listLgac = new ArrayList<>();
        listWorkplans = new ArrayList<>();
    }
    
    public GeneralResume(){
        listMembers = new ArrayList<>();
        listLgac = new ArrayList<>();
        listWorkplans = new ArrayList<>();
    }
    
    public void addMember(Member newMember){
        listMembers.add(newMember);
    }
    
    public void removeMember(Member member){
        listMembers.remove(member);
    }
    
    public void addLgac(Lgac newLgac){
        listLgac.add(newLgac);
    }
    
    public void removeLgac(String lgacTitle){
        for(Lgac lgac : this.listLgac){
            if(lgac.getTitle().equalsIgnoreCase(lgacTitle)){
                this.listLgac.remove(lgac);
                break;
            }
        }
    }

    public String getBodyAcademyKey(){
        return bodyAcademyKey;
    }

    public void setBodyAcademyKey(String bodyAcademyKey){
        this.bodyAcademyKey = bodyAcademyKey;
    }

    public String getLastEvaluation(){
        return lastEvaluation;
    }

    public void setLastEvaluation(String lastEvaluation){
        this.lastEvaluation = lastEvaluation;
    }
   

    public String getBodyAcademyName(){
        return bodyAcademyName;
    }

    public void setBodyAcademyName(String bodyAcademyName){
        this.bodyAcademyName = bodyAcademyName;
    }

    public String getAscriptionArea(){
        return ascriptionArea;
    }

    public void setAscriptionArea(String ascriptionArea){
        this.ascriptionArea = ascriptionArea;
    }

    public String getAscriptionUnit(){
        return ascriptionUnit;
    }

    public void setAscriptionUnit(String ascriptionUnit){
        this.ascriptionUnit = ascriptionUnit;
    }

    public String getConsolidationDegree(){
        return consolidationDegree;
    }

    public void setConsolidationDegree(String consolidationDegree){
        this.consolidationDegree = consolidationDegree;
    }

    public String getVision(){
        return vision;
    }

    public void setVision(String vision){
        this.vision = vision;
    }

    public String getMission(){
        return mission;
    }

    public void setMission(String mission){
        this.mission = mission;
    }

    public String getGeneralTarjet(){
        return generalTarjet;
    }

    public void setGeneralTarjet(String generalTarjet){
        this.generalTarjet = generalTarjet;
    }

    public String getRegistrationDate(){
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate){
        this.registrationDate = registrationDate;
    }
    
    public void addLgacList(List<Lgac> listLgac){
        this.listLgac = listLgac;
    }
    
    public List<Lgac> getLgacList(){
        return listLgac;
    }
    
    public String getLgacDescriptionByTitle(String lgacTitle){
        String description = null;
        for(Lgac lgac : listLgac){
            if(lgac.getTitle().equalsIgnoreCase(lgacTitle)){
                description = lgac.getDescription();
            }            
        }
        return description;
    }
    
    public void addMembers(List<Member> members){
        for(Member member : members){
            this.listMembers.add(member);
        }
    }
    
    public List<Integrant> getIntegrants(String status){
        List<Integrant> integrants = new ArrayList<>();
        for(Member member : this.listMembers){
            if(!member.getParticipationType().equalsIgnoreCase("Colaborador") && member.getParticipationStatus().equalsIgnoreCase(status)){
                integrants.add((Integrant) member);
            }            
        }
        return integrants;
    }
    
    public List<Collaborator> getCollaborators(String status){
        List<Collaborator> collaborators = new ArrayList<>();
        for(Member member : this.listMembers){
            if(member.getParticipationType().equalsIgnoreCase("Colaborador") && member.getParticipationStatus().equalsIgnoreCase(status)){
                collaborators.add((Collaborator) member);
            }            
        }
        return collaborators;
    }
    
    @Override
    public boolean equals(Object obj){
        GeneralResume generalResume = ((GeneralResume)obj);
        boolean isEqual = false;
        if(generalResume.getBodyAcademyKey().equalsIgnoreCase(this.bodyAcademyKey)){
            isEqual = true;
        }
        return isEqual;
    }
    
}
