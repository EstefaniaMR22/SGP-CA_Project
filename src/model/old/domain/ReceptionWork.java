/*
* @author Johann
* @versión v1.0
* Last modification date: 17-06-2021
*/

package model.old.domain;

import java.util.ArrayList;
import java.util.List;

public class ReceptionWork extends Evidence {
    
    private String description;
    private String status; 
    private String modality;
    private int actualDurationInMonths;
    private int estimatedDurationInMonths;
    private List<String> requirements;
    
    public ReceptionWork(String urlFile, String projectName, boolean impactAB, String evidenceType, String evidenceTitle, 
            String registrationResponsible, String registrationDate, String studyDegree,  String publicationDate, 
            String country, String description, String status, int actualDurationInMonths, 
            int estimatedDurationInMonths, String modality){
        super(
            urlFile, projectName, evidenceTitle, country, 
            publicationDate, impactAB, registrationDate, 
            registrationResponsible, studyDegree, evidenceType
        );
        this.description = description;
        this.status = status;
        this.actualDurationInMonths = actualDurationInMonths;
        this.estimatedDurationInMonths = estimatedDurationInMonths;
        this.modality = modality;
        this.requirements = new ArrayList();        
    }

    public ReceptionWork(String evidenceType, String evidenceTitle, 
    boolean impactAB, String registrationResponsible, String registrationDate, String urlFile){
        super(evidenceType, evidenceTitle, impactAB, registrationResponsible, registrationDate, urlFile);
    }
    
    public ReceptionWork(String urlFile, String projectName, boolean impactAB, String evidenceType, String evidenceTitle, 
            String registrationResponsible, String registrationDate, String studyDegree,  String publicationDate, 
            String country){
        super(
            urlFile, projectName, evidenceTitle, country, 
            publicationDate, impactAB, registrationDate, 
            registrationResponsible, studyDegree, evidenceType
        );
        this.requirements = new ArrayList();  
    }
    
    public ReceptionWork(){
        this.requirements = new ArrayList();
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getModality(){
        return modality;
    }

    public void setModality(String modality){
        this.modality = modality;
    }

    public int getActualDurationInMonths(){
        return actualDurationInMonths;
    }

    public void setActualDurationInMonths(int actualDurationInMonths){
        this.actualDurationInMonths = actualDurationInMonths;
    }

    public int getEstimatedDurationInMonths(){
        return estimatedDurationInMonths;
    }

    public void setEstimatedDurationInMonths(int estimatedDurationInMonths){
        this.estimatedDurationInMonths = estimatedDurationInMonths;
    }
    
    public List<String> getRequirements(){
        return requirements;
    }

    public void setRequirements(List<String> requirements){
        this.requirements = requirements;
    }
    
    @Override
    public String toString(){
        return "Trabajo recepcional";
    }

    @Override
    public Evidence getSpecificEvidenceInstance(String evidenceType){
        Evidence evidence = null;
        if(this.toString().equalsIgnoreCase(evidenceType)){
            evidence = new ReceptionWork();
        }
        return evidence;
    }

    @Override
    public Evidence getSpecificEvidenceInstance(String evidenceType, String evidenceTitle,
                                                boolean impactAB, String registrationResponsible, String registrationDate, String urlFile){
        Evidence evidence = null;
        if(this.toString().equalsIgnoreCase(evidenceType)){
            evidence = new ReceptionWork(
                evidenceType,
                evidenceTitle,
                impactAB,
                registrationResponsible,
                registrationDate,
                urlFile
            );
        }
        return evidence;
    }
}
