/*
 * @author Josué 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package sgp.ca.demodao;

import com.jfoenix.controls.JFXDatePicker;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import sgp.ca.businesslogic.IntegrantDAO;
import sgp.ca.domain.Integrant;

public class IntegrantRequestController implements Initializable {

    @FXML
    private Button btnUnsubscribe;
    @FXML
    private Button btnSubscribe;
    @FXML
    private Button btnIntegrantUpdate;
    @FXML
    private Button btnExit;
    @FXML
    private HBox hbIntegrantOptions;
    @FXML
    private Label lbParticipationType;
    @FXML
    private TextField txtFieldMemberRfc;
    @FXML
    private TextField txtFieldMemberFullName;
    @FXML
    private TextField txtFieldMemberEmailUv;
    @FXML
    private TextField txtFieldMemberCurp;
    @FXML
    private TextField txtFieldMemberNationality;
    @FXML
    private TextField txtFieldMemberEducationalProgram;
    @FXML
    private TextField txtFieldMemberCellNumber;
    @FXML
    private TextField txtFieldMemberStaffNumber;
    @FXML
    private PasswordField passFieldMemberPassword;
    @FXML
    private TextField txtFieldParticipationStatus;
    @FXML
    private JFXDatePicker dtpMemberRegistration;
    
    private Integrant token;
    private Integrant integrant;
    private final IntegrantDAO INTEGRANT_DAO = new IntegrantDAO();
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        hbIntegrantOptions.getChildren().removeAll(btnUnsubscribe, btnSubscribe, btnIntegrantUpdate, btnExit);
    }
    
    public void showIntegrantByEmail(Integrant integrantToken, String emailUV){
        this.token = integrantToken;
        this.integrant = (Integrant) INTEGRANT_DAO.getMemberByUVmail(emailUV);
        if(integrant != null){
            setIntegrantDataIntoInterface();
            this.checkPrivileges();
        }else{
            GenericWindowDriver.getGenericWindowDriver().showErrorAlert(new ActionEvent(), "Lo sentimos, parece que el sistema no está disponible por el momento");
            FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("GeneralResumeRequest.fxml", btnExit);
            GeneralResumeRequestController controller = loader.getController();
            controller.showGeneralResume(token);
        }
    }

    @FXML
    private void unsubscribeIntegrant(ActionEvent event){
        if(INTEGRANT_DAO.unsubscribeMemberByEmailUV(integrant.getEmailUV())){
            GenericWindowDriver.getGenericWindowDriver().showInfoAlert(event, "El integrante ha sido dado de baja");
        }else{
            GenericWindowDriver.getGenericWindowDriver().showErrorAlert(event, "Error del sistema, favor de contactas a soporte técnico");
        }
        FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("GeneralResumeRequest.fxml", btnExit);
        GeneralResumeRequestController controller = loader.getController();
        controller.showGeneralResume(this.token);
    }

    @FXML
    private void subscribeIntegrant(ActionEvent event){
        if(INTEGRANT_DAO.subscribeMemberByEmailUV(integrant.getEmailUV())){
            GenericWindowDriver.getGenericWindowDriver().showInfoAlert(event, "El integrante ha sido dado de alta");
        }else{
            GenericWindowDriver.getGenericWindowDriver().showErrorAlert(event, "Error del sistema, favor de contactas a soporte técnico");
        }
        FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("GeneralResumeRequest.fxml", btnExit);
        GeneralResumeRequestController controller = loader.getController();
        controller.showGeneralResume(this.token);
    }

    @FXML
    private void updateIntegrant(ActionEvent event){
        FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("IntegrantEditable.fxml", btnExit);
        IntegrantEditableController controller = loader.getController();
        controller.showIntegrantUpdateForm(this.token, integrant.getEmailUV());
    }

    @FXML
    private void exit(ActionEvent event){
        FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("GeneralResumeRequest.fxml", btnExit);
        GeneralResumeRequestController controller = loader.getController();
        controller.showGeneralResume(this.token);
    }
    
    private void setIntegrantDataIntoInterface(){
        this.passFieldMemberPassword.setText(this.integrant.getPassword());
        this.txtFieldMemberCellNumber.setText(this.integrant.getCellphone());
        this.txtFieldMemberCurp.setText(this.integrant.getCurp());
        this.txtFieldMemberEducationalProgram.setText(this.integrant.getEducationalProgram());
        this.txtFieldMemberEmailUv.setText(this.integrant.getEmailUV());
        this.txtFieldMemberFullName.setText(this.integrant.getFullName());
        this.txtFieldMemberNationality.setText(this.integrant.getNationality());
        this.txtFieldMemberRfc.setText(this.integrant.getRfc());
        this.dtpMemberRegistration.setValue(LocalDate.parse(this.integrant.getDateOfAdmission()));
        this.txtFieldMemberStaffNumber.setText(String.valueOf(this.integrant.getStaffNumber()));
        this.lbParticipationType.setText(this.integrant.getParticipationType());
        this.txtFieldParticipationStatus.setText(this.integrant.getParticipationStatus());
    }    
    
    private void checkPrivileges(){
        if(this.token.getParticipationType().equalsIgnoreCase("Responsable") && 
        (!this.token.getEmailUV().equalsIgnoreCase(this.integrant.getEmailUV()))){
            hbIntegrantOptions.getChildren().addAll(btnUnsubscribe, btnSubscribe, btnIntegrantUpdate, btnExit);
            if(this.integrant.getParticipationStatus().equalsIgnoreCase("Activo")){
                hbIntegrantOptions.getChildren().remove(btnSubscribe);
            }else{
                hbIntegrantOptions.getChildren().remove(btnUnsubscribe);
            }
        }else{
            hbIntegrantOptions.getChildren().addAll(btnExit);
        }
    }
}
