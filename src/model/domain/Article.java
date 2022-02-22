/**
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package model.domain;

public class Article extends Evidence{
    private String isnn = "";
    private String magazineName = "";
    private String magazineEditorial = "";

    public Article(String urlFile, String projectName, boolean impactAB, String evidenceType, 
    String evidenceTitle, String registrationResponsible, String registrationDate, 
    String studyDegree, String publicationDate, String country, String isnn, 
    String magazineName, String magazineEditorial){
        super(
            urlFile, projectName, evidenceTitle, country, 
            publicationDate, impactAB, registrationDate, 
            registrationResponsible, studyDegree, evidenceType
        );
        this.isnn = isnn;
        this.magazineName = magazineName;
        this.magazineEditorial = magazineEditorial;
        
    }

    public Article(String evidenceType, String evidenceTitle, 
    boolean impactAB, String registrationResponsible, String registrationDate, String urlFile) {
        super(evidenceType, evidenceTitle, impactAB, registrationResponsible, registrationDate, urlFile);
    }

    public Article(){
    }

    public String getIsnn(){
        return isnn;
    }

    public void setIsnn(String isnn){
        this.isnn = isnn;
    }

    public String getMagazineName(){
        return magazineName;
    }

    public void setMagazineName(String magazineName){
        this.magazineName = magazineName;
    }

    public String getMagazineEditorial(){
        return magazineEditorial;
    }

    public void setMagazineEditorial(String magazineEditorial){
        this.magazineEditorial = magazineEditorial;
    }
    
    @Override
    public String toString(){
        return "Artículo";
    }

    @Override
    public Evidence getSpecificEvidenceInstance(String evidenceType){
        Evidence evidence = null;
        if(this.toString().equalsIgnoreCase(evidenceType)){
            evidence = new Article();
        }
        return evidence;
    }

    @Override
    public Evidence getSpecificEvidenceInstance(String evidenceType, String evidenceTitle, 
    boolean impactAB, String registrationResponsible, String registrationDate, String urlFile){
        Evidence evidence = null;
        if(this.toString().equalsIgnoreCase(evidenceType)){
            evidence = new Article(
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
