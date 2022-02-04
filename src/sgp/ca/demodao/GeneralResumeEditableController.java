/*
 * @author Josué 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package sgp.ca.demodao;

import com.jfoenix.controls.JFXDatePicker;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import sgp.ca.businesslogic.GeneralResumeDAO;
import sgp.ca.businesslogic.IntegrantDAO;
import sgp.ca.domain.GeneralResume;
import sgp.ca.domain.Integrant;
import sgp.ca.domain.Lgac;

public class GeneralResumeEditableController implements Initializable{
    
    @FXML
    private Button btnAddLgac;
    @FXML
    private Button btnDeleteLgac;
    @FXML
    private TextArea txtAreaGeneralTarget;
    @FXML
    private TextArea txtAreaMission;
    @FXML
    private TextArea txtAreaVision;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnSignUpBodyAcademy;
    @FXML
    private Button btnCancelChanges;
    @FXML
    private Button btnCancelRegistration;
    @FXML
    private TextField txtFieldBodyAcademyName;
    @FXML
    private TextField txtFieldBodyAcademyKey;
    @FXML
    private TextField txtFieldAdscriptionArea;
    @FXML
    private TextField txtFieldAdsciptionUnit;
    @FXML
    private ComboBox<String> cboBoxConsolidationDegree;
    @FXML
    private Label lbUserName;
    @FXML
    private HBox hbGeneralResumeOptions;
    @FXML
    private JFXDatePicker dtpRegistrationDate;
    @FXML
    private JFXDatePicker dtpLastEvaluationDate;
    @FXML
    private ListView<String> lvLgac;
    @FXML
    private TextArea txtAreaLgacTitle;
    @FXML
    private TextArea txtAreaLgacDescription;
    @FXML
    private HBox hbLgacTable;
    
    private final GeneralResumeDAO GENERAL_RESUME_DAO = new GeneralResumeDAO();
    private final IntegrantDAO INTEGRANT_DAO = new IntegrantDAO();
    private Integrant token;
    private List<TextField> listFields;
    private List<TextArea> listTextAreas;
    private GeneralResume generalResume;
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        this.hbGeneralResumeOptions.getChildren().removeAll(btnCancelChanges, btnSignUpBodyAcademy, btnUpdate, btnCancelRegistration);
        cboBoxConsolidationDegree.getItems().addAll("En formación", "En consolidación", "Consolidado");
        listTextAreas = Arrays.asList(txtAreaGeneralTarget, txtAreaMission, txtAreaVision);
        listFields = Arrays.asList(txtFieldAdsciptionUnit, txtFieldAdscriptionArea, txtFieldBodyAcademyName);
        listTextAreas.forEach(txtArea -> txtArea.setWrapText(true));
    }
    
    public void showGeneralResumeInsertForm(Integrant responsible){
        this.hbGeneralResumeOptions.getChildren().addAll(btnSignUpBodyAcademy, btnCancelRegistration);
        this.token = responsible;
        this.lbUserName.setText(this.token.getFullName());
        this.generalResume = new GeneralResume();
        this.txtFieldBodyAcademyKey.setDisable(false);
    }
    
    public void showGeneralResumeUpdateForm(Integrant responsible){
        this.hbGeneralResumeOptions.getChildren().addAll(btnUpdate, btnCancelChanges);
        this.token = responsible;
        this.lbUserName.setText(this.token.getFullName());
        this.generalResume = GENERAL_RESUME_DAO.getGeneralResumeByKey(this.token.getBodyAcademyKey());
        this.setGeneralResumeDataIntoInterface();
    }

    @FXML
    private void signUpBodyAcademy(ActionEvent event){
        try{
            checkGeneralResumeForm();
            checkExistBodyAcademyKey();
            this.getOutGeneralResumeFormData();
            if(GENERAL_RESUME_DAO.addGeneralResume(generalResume)){
                this.addResponsibleInGeneralResumeRegistered(generalResume, event);
            }else{
                GenericWindowDriver.getGenericWindowDriver().showErrorAlert(event, "Error del sistema, favor de contactar a soporte técnico");
                FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("Login.fxml", btnCancelChanges);
            }
        }catch(InvalidFormException ex){
            GenericWindowDriver.getGenericWindowDriver().showErrorAlert(event, ex.getMessage());
        }
    }
    
    private void addResponsibleInGeneralResumeRegistered(GeneralResume generalResume, Event event){
        this.token = (Integrant) INTEGRANT_DAO.getMemberByUVmail(token.getEmailUV());
        this.token.setBodyAcademyKey(generalResume.getBodyAcademyKey());
        if(INTEGRANT_DAO.updateMember(token, token.getEmailUV())){
            GenericWindowDriver.getGenericWindowDriver().showInfoAlert(event, "El currículum general ha sido registrado con éxito");
            FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("Start.fxml", btnCancelRegistration);
            StartController controller = loader.getController();
            controller.receiveIntegrantToken(token);
        }else{
            FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("Login.fxml", btnCancelRegistration);
        }
    }
    
    @FXML
    private void updateGeneralResume(ActionEvent event){
        try{
            checkGeneralResumeForm();
            this.getOutGeneralResumeFormData();
            if(GENERAL_RESUME_DAO.updateGeneralResume(generalResume, this.token.getBodyAcademyKey())){
                this.token.setBodyAcademyKey(generalResume.getBodyAcademyKey());
                GenericWindowDriver.getGenericWindowDriver().showInfoAlert(event, "El currículum ha sido actualizado exitosamente");
            }else{
                GenericWindowDriver.getGenericWindowDriver().showErrorAlert(event, "Error del sistema, favor de contactar a soporte técnico");
            }
            FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("GeneralResumeRequest.fxml", btnCancelChanges);
            GeneralResumeRequestController controller = loader.getController();
            controller.showGeneralResume(this.token);
        }catch(InvalidFormException ex){
            GenericWindowDriver.getGenericWindowDriver().showErrorAlert(event, ex.getMessage());
        }
    }
    
    @FXML
    private void cancelChanges(ActionEvent event){
        Optional<ButtonType> action = GenericWindowDriver.getGenericWindowDriver().showConfirmacionAlert(event, "¿Seguro que deseas cancelar la actualización?");
        if(action.get() == ButtonType.OK){
            FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("Start.fxml", btnCancelChanges);
            StartController controller = loader.getController();
            controller.receiveIntegrantToken(this.token);
        }
    }
    
    @FXML
    private void cancelSignUpBodyAcademy(ActionEvent event){
        Optional<ButtonType> action = GenericWindowDriver.getGenericWindowDriver().showConfirmacionAlert(event, "¿Seguro que deseas cancelar el registro?");
        if(action.get() == ButtonType.OK){
            FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("Login.fxml", btnCancelChanges);
        }
    }
    
    @FXML
    private void addLgac(ActionEvent event){
        try{
            this.checkLgaFrom();
            GeneralResume bodyAcademy = new GeneralResume();
            bodyAcademy.setBodyAcademyKey(this.token.getBodyAcademyKey());
            this.generalResume.addLgac(new Lgac(
                txtAreaLgacTitle.getText(), 
                txtAreaLgacDescription.getText(), 
                bodyAcademy
            ));
            this.lvLgac.getItems().clear();
            this.generalResume.getLgacList().forEach(lgac -> this.lvLgac.getItems().add(lgac.getTitle()));
        }catch(InvalidFormException ex){
            GenericWindowDriver.getGenericWindowDriver().showErrorAlert(event, ex.getMessage());
        }
    }

    @FXML
    private void removeLgac(ActionEvent event){
        if(this.lvLgac.getSelectionModel().getSelectedItem() != null){
            this.generalResume.removeLgac(this.lvLgac.getSelectionModel().getSelectedItem());
            this.lvLgac.getItems().remove(this.lvLgac.getSelectionModel().getSelectedItem());
            this.lvLgac.getItems().clear();
            this.generalResume.getLgacList().forEach(lgac -> this.lvLgac.getItems().add(lgac.getTitle()));
        }
    }
    
    private void checkGeneralResumeForm() throws InvalidFormException{
        ValidatorForm.checkAlaphabeticalFields(listFields, 5, 100);
        ValidatorForm.chechkAlphabeticalField(txtFieldBodyAcademyKey, 6, 13);
        ValidatorForm.checkAlaphabeticalTextAreas(listTextAreas, 5, 450);
        ValidatorForm.checkNotEmptyDateField(dtpRegistrationDate);
        ValidatorForm.checkNotEmptyDateField(dtpLastEvaluationDate);
        ValidatorForm.isComboBoxSelected(cboBoxConsolidationDegree);
    }
    
    private void checkLgaFrom() throws InvalidFormException{
        ValidatorForm.chechkAlphabeticalArea(txtAreaLgacTitle, 5, 100);
        ValidatorForm.chechkAlphabeticalArea(txtAreaLgacDescription, 5, 450);
        for(String lgacDescription : this.lvLgac.getItems()){
            if(lgacDescription.equalsIgnoreCase(this.txtAreaLgacTitle.getText())){
                throw new InvalidFormException("Esta Lgac ya existe");
            }
        }
    }
    
    private void checkExistBodyAcademyKey() throws InvalidFormException{
        if(GENERAL_RESUME_DAO.isBodyAcademyRegistered(this.txtFieldBodyAcademyKey.getText())){
            this.txtFieldBodyAcademyKey.setStyle("-fx-border-color: red;");
            throw new InvalidFormException("Ya hay un cuerpo académico con la misma clave");
        }
    }
    
    private void setGeneralResumeDataIntoInterface(){
        if(this.generalResume != null){
            this.txtFieldBodyAcademyKey.setText(this.generalResume.getBodyAcademyKey());
            this.txtFieldBodyAcademyName.setText(this.generalResume.getBodyAcademyName());
            this.txtFieldAdsciptionUnit.setText(this.generalResume.getAscriptionUnit());
            this.cboBoxConsolidationDegree.setValue(this.generalResume.getConsolidationDegree());
            this.dtpRegistrationDate.setValue(LocalDate.parse(this.generalResume.getRegistrationDate()));
            this.txtFieldAdscriptionArea.setText(this.generalResume.getAscriptionArea());
            this.dtpLastEvaluationDate.setValue(LocalDate.parse(this.generalResume.getLastEvaluation()));
            this.txtAreaGeneralTarget.setText(this.generalResume.getGeneralTarjet());
            this.txtAreaMission.setText(this.generalResume.getMission());
            this.txtAreaVision.setText(this.generalResume.getVision());
        }
        this.generalResume.getLgacList().forEach(lgac -> this.lvLgac.getItems().add(lgac.getTitle()));
    }
    
    private void getOutGeneralResumeFormData(){
        this.generalResume.setBodyAcademyKey(txtFieldBodyAcademyKey.getText());
        this.generalResume.setBodyAcademyName(txtFieldBodyAcademyName.getText()); 
        this.generalResume.setAscriptionArea(txtFieldAdscriptionArea.getText());
        this.generalResume.setAscriptionUnit(txtFieldAdsciptionUnit.getText());
        this.generalResume.setConsolidationDegree(cboBoxConsolidationDegree.getValue());
        this.generalResume.setVision(txtAreaVision.getText());
        this.generalResume.setMission(txtAreaMission.getText());
        this.generalResume.setGeneralTarjet(txtAreaGeneralTarget.getText());
        this.generalResume.setRegistrationDate(ValidatorForm.convertJavaDateToSQlDate(dtpRegistrationDate));
        this.generalResume.setLastEvaluation(ValidatorForm.convertJavaDateToSQlDate(dtpLastEvaluationDate));
    }

    @FXML
    private void selectLgac(MouseEvent event){
        String lgacTitle = this.lvLgac.getSelectionModel().getSelectedItem();
        if(lgacTitle != null){
            this.txtAreaLgacTitle.setText(lgacTitle);
            this.txtAreaLgacDescription.setText(this.generalResume.getLgacDescriptionByTitle(lgacTitle));
            this.btnDeleteLgac.setDisable(false);
        }
    }
}
