/*
 * @author Josué 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package sgp.ca.demodao;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import sgp.ca.domain.Integrant;

public class MemberSelectionController implements Initializable{

    @FXML
    private Button btnCancel;
    @FXML
    private Button btnIntegrantRegistrer;
    @FXML
    private Button btnCollaboratorRegistrer;

    private Integrant token;
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
    }    
    
    public void receiveResponsibeleToken(Integrant integrantToken){
        this.token = integrantToken;
    }

    @FXML
    private void cancelRegistrerSelection(ActionEvent event){
        FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("GeneralResumeRequest.fxml", btnCancel);
        GeneralResumeRequestController controller = loader.getController();
        controller.showGeneralResume(token);
    }

    @FXML
    private void addNewIntegrant(ActionEvent event){
        FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("IntegrantEditable.fxml", btnCancel);
        IntegrantEditableController controller = loader.getController();
        controller.showIntegrantInscriptionForm(token);
    }

    @FXML
    private void addNewCollaborator(ActionEvent event){
        FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("CollaboratorEditable.fxml", btnCancel);
        CollaboratorEditableController controller = loader.getController();
        controller.showCollaboratorRegistrationForm(token);
    }
    
}
