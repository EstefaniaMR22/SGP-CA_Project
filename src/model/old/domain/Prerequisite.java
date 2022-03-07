/**
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package model.old.domain;

public class Prerequisite{
    private int prerequisiteKey;
    private String responsiblePrerequisite;
    private String descrptionPrerequisite;

    public Prerequisite(int prerequisiteKey, String responsiblePrerequisite, 
    String descrptionPrerequisite){
        this.prerequisiteKey = prerequisiteKey;
        this.responsiblePrerequisite = responsiblePrerequisite;
        this.descrptionPrerequisite = descrptionPrerequisite;
    }

    public Prerequisite(){
        
    }

    public int getPrerequisiteKey(){
        return prerequisiteKey;
    }

    public void setPrerequisiteKey(int prerequisiteKey){
        this.prerequisiteKey = prerequisiteKey;
    }

    public String getResponsiblePrerequisite(){
        return responsiblePrerequisite;
    }

    public void setResponsiblePrerequisite(String responsiblePrerequisite){
        this.responsiblePrerequisite = responsiblePrerequisite;
    }

    public String getDescrptionPrerequisite(){
        return descrptionPrerequisite;
    }

    public void setDescrptionPrerequisite(String descrptionPrerequisite){
        this.descrptionPrerequisite = descrptionPrerequisite;
    }
    
}
