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
import java.util.ResourceBundle;

import model.domain.Collaborator;
import model.domain.Integrant;
import model.dataaccess.CollaboratorDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import controller.old.pattern.InvalidFormException;
import controller.old.pattern.ValidatorForm;

public class CollaboratorEditableController implements Initializable{

    @FXML
    private Button btnRegistrerColaborator;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnCollaboratorUpdate;
    @FXML
    private Label lbParticipationType;
    @FXML
    private TextField txtFieldRfcMember;
    @FXML
    private TextField txtFieldFullName;
    @FXML
    private TextField txtFieldEmailUv;
    @FXML
    private TextField txtFieldCurp;
    @FXML
    private TextField txtFieldNationality;
    @FXML
    private TextField txtFieldEducationalProgram;
    @FXML
    private TextField txtFieldCellNumber;
    @FXML
    private TextField txtFieldStaffNumber;
    @FXML
    private TextField txtFieldStudyArea;
    @FXML
    private TextField txtFieldBodyAcademyName;
    @FXML
    private TextField txtFieldLevelStudy;
    @FXML
    private HBox hbCollaboratorOptions;
    @FXML
    private JFXDatePicker dtpAdmissionDate;
    
    private Integrant token;
    private Collaborator collaborator;
    private String oldRfc = "";
    private List<TextField> fields;
    private final CollaboratorDAO COLLABORATOR_DAO = new CollaboratorDAO();
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        collaborator = new Collaborator();
        this.fields = Arrays.asList(
            txtFieldBodyAcademyName, txtFieldEducationalProgram,
            txtFieldEmailUv, txtFieldFullName, txtFieldLevelStudy, txtFieldNationality, 
            txtFieldStudyArea
        );
        hbCollaboratorOptions.getChildren().removeAll(btnCancel, btnCollaboratorUpdate, btnRegistrerColaborator);
    }  
    
    public void showCollaboratorRegistrationForm(Integrant integrantToken){
        this.token = integrantToken;
        hbCollaboratorOptions.getChildren().addAll(btnRegistrerColaborator, btnCancel);
    }
    
    public void showCollaboratorUpdateForm(Integrant integrantToken, String emailUV){
        this.token = integrantToken;
        hbCollaboratorOptions.getChildren().addAll(btnCollaboratorUpdate, btnCancel);
        this.collaborator = (Collaborator) COLLABORATOR_DAO.getMemberByUVmail(emailUV);
        this.oldRfc = collaborator.getRfc();
        this.setIntegrantDataIntoInterface();
    }

    @FXML
    private void addNewCollaborator(ActionEvent event){
        try{
            this.validateForm();
            this.checkCollaboratorRegistered();
            this.getOutIntegrantDataFromInterface();
            collaborator.setParticipationStatus("Activo");
            if(COLLABORATOR_DAO.addMember(this.collaborator)){
                WindowControllerB.getWindowController().showInfoAlert(event, "El colaborador ha sido registrado en el sistema");
            }else{
                WindowControllerB.getWindowController().showErrorAlert(event, "Ocurrió un error en el sistema, favor de ponerse en contacto con soporte técnico");
            }
            FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("GeneralResumeRequest.fxml", btnCancel);
            GeneralResumeRequestController controller = loader.getController();
            controller.showGeneralResume(token);
        }catch(InvalidFormException ex){
            WindowControllerB.getWindowController().showErrorAlert(event, ex.getMessage());
        }
    }

    @FXML
    private void updateCollaborator(ActionEvent event){
        try{
            this.validateForm();
            this.checkCollaboratorRegistered();
            this.getOutIntegrantDataFromInterface();
            if(COLLABORATOR_DAO.updateMember(this.collaborator, this.oldRfc)){
                WindowControllerB.getWindowController().showInfoAlert(event, "El colaborador ha sido registrado en el sistema");
            }else{
                WindowControllerB.getWindowController().showErrorAlert(event, "Ocurrió un error en el sistema, favor de ponerse en contacto con soporte técnico");
            }
            FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("GeneralResumeRequest.fxml", btnCancel);
            GeneralResumeRequestController controller = loader.getController();
            controller.showGeneralResume(token);
        }catch(InvalidFormException ex){
            WindowControllerB.getWindowController().showErrorAlert(event, ex.getMessage());
        }
    }

    @FXML
    private void cancelCollaboratorChanges(ActionEvent event){
        FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("GeneralResumeRequest.fxml", btnCancel);
        GeneralResumeRequestController controller = loader.getController();
        controller.showGeneralResume(token);
    }
    
    private void setIntegrantDataIntoInterface(){
        this.txtFieldCellNumber.setText(collaborator.getCellphone());
        this.txtFieldCurp.setText(collaborator.getCurp());
        this.txtFieldEducationalProgram.setText(collaborator.getEducationalProgram());
        this.txtFieldEmailUv.setText(collaborator.getEmailUV());
        this.txtFieldFullName.setText(collaborator.getFullName());
        this.txtFieldNationality.setText(collaborator.getNationality());
        this.txtFieldRfcMember.setText(collaborator.getRfc());
        this.dtpAdmissionDate.setValue(LocalDate.parse(collaborator.getDateOfAdmission()));
        this.txtFieldStaffNumber.setText(String.valueOf(collaborator.getStaffNumber()));
        this.lbParticipationType.setText(collaborator.getParticipationType());
        this.txtFieldBodyAcademyName.setText(collaborator.getNameBACollaborator());
        this.txtFieldLevelStudy.setText(collaborator.getHighestDegreeStudies());
        this.txtFieldStudyArea.setText(collaborator.getStudyArea());
    }
    
    private void getOutIntegrantDataFromInterface(){
        collaborator.setBodyAcademyKey(this.token.getBodyAcademyKey());
        collaborator.setCellphone(txtFieldCellNumber.getText());
        collaborator.setRfc(this.txtFieldRfcMember.getText());
        collaborator.setFullName(this.txtFieldFullName.getText());
        collaborator.setEmailUV(this.txtFieldEmailUv.getText());
        collaborator.setCurp(this.txtFieldCurp.getText());
        collaborator.setNationality(this.txtFieldNationality.getText());
        collaborator.setParticipationType(this.lbParticipationType.getText());
        collaborator.setParticipationStatus("Activo");
        collaborator.setEducationalProgram(this.txtFieldEducationalProgram.getText());
        collaborator.setStaffNumber(Integer.parseInt(this.txtFieldStaffNumber.getText()));
        collaborator.setDateOfAdmission(ValidatorForm.convertJavaDateToSQlDate(this.dtpAdmissionDate));
        collaborator.setStudyArea(this.txtFieldStudyArea.getText());
        collaborator.setNameBACollaborator(this.txtFieldBodyAcademyName.getText());
        collaborator.setHighestDegreeStudies(this.txtFieldLevelStudy.getText());
        collaborator.setBodyAcademyKey(this.token.getBodyAcademyKey());
        collaborator.setParticipationType("Colaborador");
    }
    
    private void validateForm() throws InvalidFormException{
        int smallestCharacterSize = 5;
        int largestCharacterSize = 100;
        ValidatorForm.chechkAlphabeticalField(this.txtFieldRfcMember, smallestCharacterSize, 18);
        ValidatorForm.chechkAlphabeticalField(this.txtFieldCurp, smallestCharacterSize, 19);
        ValidatorForm.checkAlaphabeticalFields(this.fields, smallestCharacterSize, largestCharacterSize);
        ValidatorForm.isNumberData(this.txtFieldCellNumber, 10);
        ValidatorForm.isIntegerNumberData(this.txtFieldStaffNumber, smallestCharacterSize);
        ValidatorForm.checkNotEmptyDateField(this.dtpAdmissionDate);
    } 
    
    private void checkCollaboratorRegistered() throws InvalidFormException{
        if(!this.txtFieldRfcMember.getText().equalsIgnoreCase(this.oldRfc)){
            if(COLLABORATOR_DAO.searchCollaboratorByRfc(this.txtFieldRfcMember.getText())){
                throw new InvalidFormException("Usuario registrado en el sistema");
            }
        }
    }
}
