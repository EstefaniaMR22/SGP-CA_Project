/*
* @author Johann
* @versión v1.0
* Last modification date: 17-06-2021
*/

package controller.old.pattern;

import javafx.scene.control.RadioButton;

public class CollaboratorTable {
    private String collaboratorRfc;
    private String collaboratorName;
    private RadioButton participation;

    public CollaboratorTable(String collaboratorRfc, String collaboratorName){
        this.collaboratorRfc = collaboratorRfc;
        this.collaboratorName = collaboratorName;
        this.participation = new RadioButton();
    }

    public String getCollaboratorName(){
        return collaboratorName;
    }

    public void setCollaboratorName(String collaboratorName){
        this.collaboratorName = collaboratorName;
    }

    public RadioButton getParticipation(){
        return participation;
    }

    public void setParticipation(RadioButton assistant){
        this.participation = participation;
    }

    public String getCollaboratorRfc(){
        return collaboratorRfc;
    }

    public void setCollaboratorRfc(String collaboratorRfc){
        this.collaboratorRfc = collaboratorRfc;
    }
    
}
