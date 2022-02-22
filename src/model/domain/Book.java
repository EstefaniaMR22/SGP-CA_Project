/**
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package model.domain;

public class Book extends Evidence {
    private String publisher;
    private int editionsNumber;
    private String isbn;

    public Book(String urlFile, String projectName, boolean impactAB, String evidenceType, 
    String evidenceTitle, String registrationResponsible, String registrationDate, 
    String studyDegree, String publicationDate, String country, String publisher, 
    int editionsNumber, String isbn){
        super(
            urlFile, projectName, evidenceTitle, country, publicationDate,
            impactAB,registrationDate, registrationResponsible, studyDegree, 
            evidenceType
        );
        this.publisher = publisher;
        this.editionsNumber = editionsNumber;
        this.isbn = isbn;
    }

    public Book(String evidenceType, String evidenceTitle, 
    boolean impactAB, String registrationResponsible, String registrationDate, String urlFile){
        super(evidenceType, evidenceTitle, impactAB, registrationResponsible, registrationDate, urlFile);
    }

    public Book(){
    
    }

    public String getPublisher(){
        return publisher;
    }

    public void setPublisher(String publisher){
        this.publisher = publisher;
    }

    public int getEditionsNumber(){
        return editionsNumber;
    }

    public void setEditionsNumber(int editionsNumber){
        this.editionsNumber = editionsNumber;
    }

    public String getIsbn(){
        return isbn;
    }

    public void setIsbn(String isbn){
        this.isbn = isbn;
    }
    
    @Override
    public String toString(){
        return "Libro";
    }

    @Override
    public Evidence getSpecificEvidenceInstance(String evidenceType){
        Evidence evidence = null;
        if(this.toString().equalsIgnoreCase(evidenceType)){
            evidence = new Book();
        }
        return evidence;
    }

    @Override
    public Evidence getSpecificEvidenceInstance(String evidenceType, String evidenceTitle,
                                                boolean impactAB, String registrationResponsible, String registrationDate, String urlFile){
        Evidence evidence = null;
        if(this.toString().equalsIgnoreCase(evidenceType)){
            evidence = new Book(
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
