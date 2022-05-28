
/**
 * @author V Manuel Arrys
 * @versión v1.0
 * Last modification date: 14-03-2022
 */

package model.domain;

import java.util.ArrayList;
import java.util.List;

public abstract class Evidence{
    private String evidenceTitle;
    private String country; 
    private String publicationDate;
    private String registrationResponsable;
    private String editorialBook;
    private String editionNumberBook;
    private String isbnBook;
    private String reference;
    private String pagesBook;
    private String titleBook;
    private int idAcademicBody;
    private int idProject;
    private int idResponsable;
    private String projectName;
    private String evidenceType;


    public Evidence(){
    }

    public String getEvidenceTitle() {
        return evidenceTitle;
    }

    public void setEvidenceTitle(String evidenceTitle) {
        this.evidenceTitle = evidenceTitle;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getRegistrationResponsable() {
        return registrationResponsable;
    }

    public void setRegistrationResponsable(String registrationResponsable) {
        this.registrationResponsable = registrationResponsable;
    }

    public String getEditorialBook() {
        return editorialBook;
    }

    public void setEditorialBook(String editorialBook) {
        this.editorialBook = editorialBook;
    }

    public String getEditionNumberBook() {
        return editionNumberBook;
    }

    public void setEditionNumberBook(String editionNumberBook) {
        this.editionNumberBook = editionNumberBook;
    }

    public String getIsbnBook() {
        return isbnBook;
    }

    public void setIsbnBook(String isbnBook) {
        this.isbnBook = isbnBook;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getPagesBook() {
        return pagesBook;
    }

    public void setPagesBook(String pagesBook) {
        this.pagesBook = pagesBook;
    }

    public String getTitleBook() {
        return titleBook;
    }

    public void setTitleBook(String titleBook) {
        this.titleBook = titleBook;
    }

    public int getIdAcademicBody() {
        return idAcademicBody;
    }

    public void setIdAcademicBody(int idAcademicBody) {
        this.idAcademicBody = idAcademicBody;
    }

    public int getIdProject() {
        return idProject;
    }

    public void setIdProject(int idProject) {
        this.idProject = idProject;
    }

    public int getIdResponsable() {
        return idResponsable;
    }

    public void setIdResponsable(int idResponsable) {
        this.idResponsable = idResponsable;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getEvidenceType() {
        return evidenceType;
    }

    public void setEvidenceType(String evidenceType) {
        this.evidenceType = evidenceType;
    }

    @Override
    public abstract String toString();
    public abstract Evidence getSpecificEvidenceInstance(String evidenceType, String evidenceTitle, 
    boolean impactAB, String registrationResponsible, String registrationDate, String urlFile);
    public abstract Evidence getSpecificEvidenceInstance(String evidenceType);

}