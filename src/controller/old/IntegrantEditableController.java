/*
 * @author Josué 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package controller.old;

import com.jfoenix.controls.JFXDatePicker;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import model.domain.Integrant;
import model.domain.Member;
import model.dataaccess.GeneralResumeDAO;
import model.dataaccess.IntegrantDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import controller.old.pattern.InvalidFormException;
import controller.old.pattern.ValidatorForm;

public class IntegrantEditableController implements Initializable{

    @FXML
    private Button btnIntegrantRegistrer;
    @FXML
    private Button btnResponsibleRegistrer;
    @FXML
    private Button btnUpdateIntegrant;
    @FXML
    private Button btnCancelIntegrantChanges;
    @FXML
    private Button btnCancelResponsibleResgistration;
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
    private TextField txtFieldMemberBodyAcademyKey;
    @FXML
    private PasswordField passFieldMemberPassword;
    @FXML
    private CheckBox chBoxIsIntoBodyAcademy;
    @FXML
    private JFXDatePicker dtpAdmisionDate;
    
    private List<TextField> fields;
    private final IntegrantDAO INTEGRANT_DAO = new IntegrantDAO();
    private final GeneralResumeDAO GENERAL_RESUME = new GeneralResumeDAO();
    private Integrant token = null;
    private Integrant integrant;
    private String oldEmailUvForUpdate = "";    

    @Override
    public void initialize(URL url, ResourceBundle rb){
        this.integrant = new Integrant();
        fields = Arrays.asList(
            txtFieldMemberEducationalProgram, txtFieldMemberEmailUv,
            txtFieldMemberFullName, txtFieldMemberNationality
        );
        hbIntegrantOptions.getChildren().removeAll(
            btnIntegrantRegistrer, btnCancelIntegrantChanges, 
            btnCancelResponsibleResgistration, 
            btnUpdateIntegrant, btnResponsibleRegistrer
        );
    }    
    
    public void showResponsibleInscriptionForm(){
        this.lbParticipationType.setText("Responsable");
        hbIntegrantOptions.getChildren().addAll(btnResponsibleRegistrer, btnCancelResponsibleResgistration);
    }
    
    public void showIntegrantInscriptionForm(Integrant responsibleToken){
        this.token = responsibleToken;
        this.lbParticipationType.setText("Integrante");
        hbIntegrantOptions.getChildren().addAll(btnIntegrantRegistrer, btnCancelIntegrantChanges);
        txtFieldMemberBodyAcademyKey.setVisible(false);
        chBoxIsIntoBodyAcademy.setVisible(false);
    }
    
    public void showIntegrantUpdateForm(Integrant integrantToken, String emailUV){
        this.token = integrantToken;
        this.integrant = (Integrant) INTEGRANT_DAO.getMemberByUVmail(emailUV);
        this.oldEmailUvForUpdate = this.integrant.getEmailUV();
        setIntegrantDataIntoInterface();
        hbIntegrantOptions.getChildren().addAll(btnUpdateIntegrant,  btnCancelIntegrantChanges);
        txtFieldMemberBodyAcademyKey.setDisable(true);
        chBoxIsIntoBodyAcademy.setDisable(true);
    }

    @FXML
    private void confirmBodyAcademyRegistered(ActionEvent event){
        if(chBoxIsIntoBodyAcademy.isSelected()){
            this.txtFieldMemberBodyAcademyKey.setDisable(false);
        }else{
            this.txtFieldMemberBodyAcademyKey.setDisable(true);
        }
    }

    @FXML
    private void addNewIntegrant(ActionEvent event){
        try{
            validateForm();
            checkExistEmailUser();
            this.integrant.setBodyAcademyKey(this.token.getBodyAcademyKey());
            this.getOutIntegrantDataFromInterface();
            if(INTEGRANT_DAO.addMember(this.integrant)){
                WindowControllerB.getWindowController().showInfoAlert(event, "Integrante registrado con éxito");
            }else{
                WindowControllerB.getWindowController().showErrorAlert(event, "Error en el sistema, favor de ponerse en contactor con soporte técnico");
            }
            FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("GeneralResumeRequest.fxml", btnCancelIntegrantChanges);
            GeneralResumeRequestController controller = loader.getController();
            controller.showGeneralResume(this.token);
        }catch(InvalidFormException ex){
            WindowControllerB.getWindowController().showErrorAlert(event, ex.getMessage());
        }
    }

    @FXML
    private void updateIntegrant(ActionEvent event){
        try{
            validateForm();
            checkExistEmailUser();
            this.getOutIntegrantDataFromInterface();
            this.integrant.setBodyAcademyKey(this.token.getBodyAcademyKey());
            if(INTEGRANT_DAO.updateMember(this.integrant, oldEmailUvForUpdate)){
                WindowControllerB.getWindowController().showInfoAlert(event, "Integrante actualizado con éxito");
            }else{
                WindowControllerB.getWindowController().showErrorAlert(event, "Error en el sistema, favor de ponerse en contactor con soporte técnico");
            }
            FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("GeneralResumeRequest.fxml", btnCancelIntegrantChanges);
            GeneralResumeRequestController controller = loader.getController();
            controller.showGeneralResume(this.token);
        }catch(InvalidFormException ex){
            WindowControllerB.getWindowController().showErrorAlert(event, ex.getMessage());
        }
    }
    
    @FXML
    private void addNewResponsible(ActionEvent event){
        try{
            validateForm();
            checkExistEmailUser();
            this.getOutIntegrantDataFromInterface();
            if(INTEGRANT_DAO.addMember(this.integrant)){
                WindowControllerB.getWindowController().showInfoAlert(event, "Integrante registrado con éxito");
            }else{
                WindowControllerB.getWindowController().showErrorAlert(event, "Error en el sistema, favor de ponerse en contactor con soporte técnico");
            }
            FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("Login.fxml", btnResponsibleRegistrer);
            
        }catch(InvalidFormException ex){
            WindowControllerB.getWindowController().showErrorAlert(event, ex.getMessage());
        }
    }

    @FXML
    private void cancelIntegrantChanges(ActionEvent event){
        Optional<ButtonType> action = WindowControllerB.getWindowController().showConfirmacionAlert(event, "¿Seguro que deseas cancelar la actualización?");
        if(action.get() == ButtonType.OK){
            FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("GeneralResumeRequest.fxml", btnCancelIntegrantChanges);
            GeneralResumeRequestController controller = loader.getController();
            controller.showGeneralResume(this.token);
        }
    }

    @FXML
    private void cancelResponsibleSignUp(ActionEvent event){
        FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("Login.fxml", btnCancelResponsibleResgistration);
    }
    
    private void validateForm() throws InvalidFormException{
        int smallestCharacterSize = 5;
        int largestCharacterSize = 100;
        ValidatorForm.chechkAlphabeticalField(this.txtFieldMemberRfc, smallestCharacterSize, 18);
        ValidatorForm.chechkAlphabeticalField(this.txtFieldMemberCurp, smallestCharacterSize, 19);
        ValidatorForm.checkAlaphabeticalFields(this.fields, smallestCharacterSize, largestCharacterSize);
        ValidatorForm.isNumberData(this.txtFieldMemberCellNumber, 10);
        ValidatorForm.isIntegerNumberData(this.txtFieldMemberStaffNumber, smallestCharacterSize);
        ValidatorForm.checkNotEmptyDateField(this.dtpAdmisionDate);
        ValidatorForm.chechkPasswordField(passFieldMemberPassword, smallestCharacterSize, 50);
        if(chBoxIsIntoBodyAcademy.isVisible() && chBoxIsIntoBodyAcademy.isSelected()){
            if(!GENERAL_RESUME.isBodyAcademyRegistered(txtFieldMemberBodyAcademyKey.getText())){
                txtFieldMemberBodyAcademyKey.setStyle("-fx-border-color: red;");
                throw new InvalidFormException("El cuerpo Académico no existe");
            }
        }
    }
    
    private void setIntegrantDataIntoInterface(){
        this.passFieldMemberPassword.setText(integrant.getPassword());
        this.txtFieldMemberBodyAcademyKey.setText(integrant.getBodyAcademyKey());
        this.txtFieldMemberCellNumber.setText(integrant.getCellphone());
        this.txtFieldMemberCurp.setText(integrant.getCurp());
        this.txtFieldMemberEducationalProgram.setText(integrant.getEducationalProgram());
        this.txtFieldMemberEmailUv.setText(integrant.getEmailUV());
        this.txtFieldMemberFullName.setText(integrant.getFullName());
        this.txtFieldMemberNationality.setText(integrant.getNationality());
        this.txtFieldMemberRfc.setText(integrant.getRfc());
        this.dtpAdmisionDate.setValue(LocalDate.parse(integrant.getDateOfAdmission()));
        this.txtFieldMemberStaffNumber.setText(String.valueOf(integrant.getStaffNumber()));
        this.lbParticipationType.setText(integrant.getParticipationType());
    }
    
    private void getOutIntegrantDataFromInterface(){
        this.integrant.setRfc(this.txtFieldMemberRfc.getText());
        this.integrant.setFullName(this.txtFieldMemberFullName.getText());
        this.integrant.setEmailUV(this.txtFieldMemberEmailUv.getText());
        this.integrant.setCurp(this.txtFieldMemberCurp.getText());
        this.integrant.setNationality(this.txtFieldMemberNationality.getText());
        this.integrant.setCellphone(this.txtFieldMemberCellNumber.getText());
        this.integrant.setParticipationType(this.lbParticipationType.getText());
        this.integrant.setParticipationStatus("Activo");
        if(chBoxIsIntoBodyAcademy.isVisible() && this.chBoxIsIntoBodyAcademy.isSelected()){
            this.integrant.setBodyAcademyKey(this.txtFieldMemberBodyAcademyKey.getText());
        }
        this.integrant.setEducationalProgram(this.txtFieldMemberEducationalProgram.getText());
        this.integrant.setStaffNumber(Integer.parseInt(this.txtFieldMemberStaffNumber.getText()));
        this.integrant.setDateOfAdmission(ValidatorForm.convertJavaDateToSQlDate(this.dtpAdmisionDate));
        this.integrant.setPassword(this.passFieldMemberPassword.getText());
    }
    
    private void checkExistEmailUser() throws InvalidFormException{
        boolean isRegistred = false;
        Member integrantRetrieved = INTEGRANT_DAO.getMemberByUVmail(this.txtFieldMemberEmailUv.getText());
        if(!this.txtFieldMemberEmailUv.getText().equalsIgnoreCase(this.oldEmailUvForUpdate)){
            if(integrantRetrieved.getFullName() != null){
                isRegistred = true;
            }
        }
        if(isRegistred){
            throw new InvalidFormException("Usuario repetido en el sistema");
        }
    }
    
}
