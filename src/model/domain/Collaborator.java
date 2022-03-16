/*
 * @author Josué 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package model.domain;


import java.util.Date;

public class Collaborator extends Member {
    
    private String studyArea;
    private String nameBACollaborator;
    private String highestDegreeStudies;

    public Collaborator(int id, String name, String maternalLastname, String paternalLastname, String nationality,
                        String educationalProgram, String personalNumber, Date admissionDate, String rfc, String telephone,
                        String curp, CivilStatus civilStatus, String uvEmail, String birthState, Date birthDate,
                        ParticipationType participationType){
        super(
                id, name, maternalLastname, paternalLastname, nationality, educationalProgram, personalNumber,
                admissionDate, rfc, telephone, curp, civilStatus, uvEmail, birthState, birthDate, participationType
        );

        this.studyArea = studyArea;
        this.nameBACollaborator = nameBACollaborator;
        this.highestDegreeStudies = highestDegreeStudies;
    }
    
    public Collaborator(ParticipationType participationType, String name, String maternalLastname, String paternalLastname,
                        String uvEmail, String telephone){
        super(participationType, name, maternalLastname, paternalLastname, uvEmail, telephone);
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
    
    public Collaborator(String rfc, String name, String maternalLastname, String paternalLastnam){
        super(rfc, name, maternalLastname, paternalLastnam);
    }

    public void setHighestDegreeStudies(String highestDegreeStudies){
        this.highestDegreeStudies = highestDegreeStudies;
    }
    
    @Override
    public String toString() {
        return super.toString();
    }
}
