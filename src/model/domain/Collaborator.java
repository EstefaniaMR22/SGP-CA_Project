/*
 * @author Josué 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package model.domain;

public class Collaborator extends Member {
    
    private String studyArea;
    private String nameBACollaborator;
    private String highestDegreeStudies;

    public Collaborator(String rfc, String fullName, String emailUV, String participationStatus, 
    String curp, String participationType, String nationality, String dateOfAdmission, 
    String educationalProgram, int staffNumber, String cellphone, String bodyAcademyKey, String studyArea, 
    String nameBACollaborator, String highestDegreeStudies){
        super(
            rfc, fullName, emailUV, participationStatus, curp, participationType, nationality, dateOfAdmission, 
            educationalProgram, staffNumber, cellphone, bodyAcademyKey
        );
        this.studyArea = studyArea;
        this.nameBACollaborator = nameBACollaborator;
        this.highestDegreeStudies = highestDegreeStudies;
    }
    
    public Collaborator(String participationType, String fullName, String emailUV, String cellphone){
        super(participationType, fullName, emailUV, cellphone);
    }
    
    public Collaborator(){
        
    }

    public String getStudyArea(){
        return studyArea;
    }

    public void setStudyArea(String studyArea){
        this.studyArea = studyArea;
    }

    public String getNameBACollaborator(){
        return nameBACollaborator;
    }

    public void setNameBACollaborator(String nameBACollaborator){
        this.nameBACollaborator = nameBACollaborator;
    }

    public String getHighestDegreeStudies(){
        return highestDegreeStudies;
    }
    
    public Collaborator(String rfc, String fullName){
        super(rfc, fullName);
    }

    public void setHighestDegreeStudies(String highestDegreeStudies){
        this.highestDegreeStudies = highestDegreeStudies;
    }
    
    @Override
    public String toString() {
        return super.toString();
    }
}
