/**
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package model.domain;

public class Prototype extends Evidence {
    private String features;

    public Prototype(String urlFile, String projectName, String evidenceTitle, 
    String country, String publicationDate, boolean impactAB, String registrationDate,
    String registrationResponsible, String studyDegree, String bodyAcademyKey, String features){
        super(
            urlFile, projectName, evidenceTitle,country, 
            publicationDate, impactAB, registrationDate,
            registrationResponsible, studyDegree, bodyAcademyKey
        );
        this.features = features;
    }
    
    public Prototype(String urlFile, String projectName, boolean impactAB, String evidenceType, String evidenceTitle, 
            String registrationResponsible, String registrationDate, String studyDegree,  String publicationDate, 
            String country){
        super(
            urlFile, projectName, evidenceTitle, country, 
            publicationDate, impactAB, registrationDate, 
            registrationResponsible, studyDegree, evidenceType
        );
    }

    public Prototype(String evidenceType, String evidenceTitle, 
    boolean impactAB, String registrationResponsible, String registrationDate, String urlFile){
        super(evidenceType, evidenceTitle, impactAB, registrationResponsible, registrationDate, urlFile);
    }

    public Prototype(){
        
    }

    public String getFeatures(){
        return features;
    }

    public void setFeatures(String features){
        this.features = features;
    }
    
    @Override
    public String toString(){
        return "Prototipo";
    }

    @Override
    public Evidence getSpecificEvidenceInstance(String evidenceType){
        Evidence evidence = null;
        if(this.toString().equalsIgnoreCase(evidenceType)){
            evidence = new Prototype();
        }
        return evidence;
    }

    @Override
    public Evidence getSpecificEvidenceInstance(String evidenceType, String evidenceTitle,
                                                boolean impactAB, String registrationResponsible, String registrationDate, String urlFile){
        Evidence evidence = null;
        if(this.toString().equalsIgnoreCase(evidenceType)){
            evidence = new Prototype(
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
