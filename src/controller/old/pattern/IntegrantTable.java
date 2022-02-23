/*
* @author Johann
* @versión v1.0
* Last modification date: 17-06-2021
*/

package controller.old.pattern;

import javafx.scene.control.RadioButton;

public class IntegrantTable {
    private String integrantRfc;
    private String integrantName;
    private RadioButton participation;

    public IntegrantTable(String integrantRfc, String integrantName){
        this.integrantRfc = integrantRfc;
        this.integrantName = integrantName;
        this.participation = new RadioButton();
    }

    public String getIntegrantName(){
        return integrantName;
    }

    public void setIntegrantName(String integrantName){
        this.integrantName = integrantName;
    }

    public RadioButton getParticipation(){
        return participation;
    }

    public void setParticipation(RadioButton assistant){
        this.participation = participation;
    }

    public String getIntegrantRfc(){
        return integrantRfc;
    }

    public void setIntegrantRfc(String integrantRfc){
        this.integrantRfc = integrantRfc;
    }
}
