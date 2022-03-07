/*
 * @author Josué 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package model.old.domain;

public class Lgac{
    
    private String identifierLgac;
    private String title;
    private String description;
    private GeneralResume bodyAcademyRelated;

    public Lgac(String identifierLgac, String title, String description, GeneralResume bodyAcademyRelated){
        this.identifierLgac = identifierLgac;
        this.title = title;
        this.description = description;
        this.bodyAcademyRelated = bodyAcademyRelated;
    }

    public Lgac(String title, String description, GeneralResume bodyAcademyRelated){
        this.title = title;
        this.description = description;
        this.bodyAcademyRelated = bodyAcademyRelated;
    }

    public Lgac(String title) {
        this.title = title;
    }
    
    public Lgac(){
    }

    public String getIdentifierLgac(){
        return identifierLgac;
    }

    public void setIdentifierLgac(String identifierLgac){
        this.identifierLgac = identifierLgac;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public GeneralResume getBodyAcademyRelated(){
        return bodyAcademyRelated;
    }

    public void setBodyAcademyRelated(GeneralResume bodyAcademyRelated){
        this.bodyAcademyRelated = bodyAcademyRelated;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    @Override
    public boolean equals(Object obj){
        boolean isEqual = false;
        if(this.description.equals(((Lgac)obj).getDescription())){
            isEqual = true;
        }
        return isEqual;
    } 
    
}
