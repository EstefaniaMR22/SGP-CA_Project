
/**
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package model.old.domain;

import java.util.ArrayList;
import java.util.List;

public abstract class Evidence{
    private String urlFile;
    private String evidenceTitle;
    private String country; 
    private String publicationDate;
    private boolean impactAB;
    private String registrationDate;
    private String registrationResponsible;
    private String studyDegree;
    private String projectName;
    private String evidenceType;
    private List<String> students;
    private List<Integrant> integrants;
    private List<Collaborator> collaborators;


    public Evidence(String urlFile, String projectName, String evidenceTitle, 
    String country, String publicationDate, boolean impactAB, String registrationDate,
    String registrationResponsible, String studyDegree, String evidenceType){
        this.urlFile = urlFile;
        this.projectName = projectName;
        this.evidenceTitle = evidenceTitle;
        this.country = country;
        this.publicationDate = publicationDate;
        this.impactAB = impactAB;
        this.registrationDate = registrationDate;
        this.registrationResponsible = registrationResponsible;
        this.studyDegree = studyDegree;
        this.evidenceType = evidenceType;
        this.students = new ArrayList<>();
        this.integrants = new ArrayList<>();
        this.collaborators = new ArrayList<>();
    }
    
    public Evidence(String evidenceType, String evidenceTitle, 
    boolean impactAB, String registrationResponsible, String registrationDate, String urlFile){
        this.evidenceType = evidenceType;
        this.evidenceTitle = evidenceTitle;
        this.impactAB = impactAB;
        this.registrationResponsible = registrationResponsible;
        this.registrationDate = registrationDate;
        this.urlFile = urlFile;
        this.students = new ArrayList<>();
        this.integrants = new ArrayList<>();
        this.collaborators = new ArrayList<>();
    }
    
    public Evidence(){
        this.students = new ArrayList<>();
        this.integrants = new ArrayList<>();
        this.collaborators = new ArrayList<>();
    }

    public String getUrlFile(){
        return urlFile;
    }

    public void setUrlFile(String urlFile){
        this.urlFile = urlFile;
    }

    public String getEvidenceTitle(){
        return evidenceTitle;
    }

    public void setEvidenceTitle(String evidenceTitle){
        this.evidenceTitle = evidenceTitle;
    }

    public String getCountry(){
        return country;
    }

    public void setCountry(String country){
        this.country = country;
    }

    public String getPublicationDate(){
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate){
        this.publicationDate = publicationDate;
    }

    public boolean getImpactAB(){
        return impactAB;
    }

    public void setImpactAB(boolean impactAB){
        this.impactAB = impactAB;
    }

    public String getRegistrationDate(){
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate){
        this.registrationDate = registrationDate;
    }

    public String getRegistrationResponsible(){
        return registrationResponsible;
    }

    public void setRegistrationResponsible(String registrationResponsible){
        this.registrationResponsible = registrationResponsible;
    }

    public String getStudyDegree(){
        return studyDegree;
    }

    public void setStudyDegree(String studyDegree){
        this.studyDegree = studyDegree;
    }

    public String getProjectName(){
        return projectName;
    }

    public void setProjectName(String projectName){
        this.projectName = projectName;
    }

    public String getEvidenceType(){
        return evidenceType;
    }

    public void setEvidenceType(String evidenceType){
        this.evidenceType = evidenceType;
    }

    public List<String> getStudents(){
        return students;
    }

    public void setStudents(List<String> students){
        this.students = students;
    }

    public List<Integrant> getIntegrants(){
        return integrants;
    }

    public void setIntegrants(List<Integrant> integrants){
        this.integrants = integrants;
    }

    public List<Collaborator> getCollaborators(){
        return collaborators;
    }

    public void setCollaborators(List<Collaborator> collaborators){
        this.collaborators = collaborators;
    }
    
    @Override
    public abstract String toString();
    public abstract Evidence getSpecificEvidenceInstance(String evidenceType, String evidenceTitle, 
    boolean impactAB, String registrationResponsible, String registrationDate, String urlFile);
    public abstract Evidence getSpecificEvidenceInstance(String evidenceType);

}