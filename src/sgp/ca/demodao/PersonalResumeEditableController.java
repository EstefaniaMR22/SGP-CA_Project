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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import sgp.ca.businesslogic.IntegrantDAO;
import sgp.ca.domain.Integrant;
import sgp.ca.domain.Member;
import sgp.ca.domain.Schooling;

public class PersonalResumeEditableController implements Initializable {

    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnAddSchooling;
    @FXML
    private Button btnRemoveSchooling;
    @FXML
    private HBox hboxNewSchooling;
    @FXML
    private TextField txtFieldRfc;
    @FXML
    private TextField txtFieldFullName;
    @FXML
    private TextField txtFieldEmailUv;
    @FXML
    private TextField txtFieldStatus;
    @FXML
    private TextField txtFieldCurp;
    @FXML
    private TextField txtFieldNationality;
    @FXML
    private TextField txtFieldEducationalProgram;
    @FXML
    private TextField txtFieldStaffNumber;
    @FXML
    private TextField txtFieldCellPhone;
    @FXML
    private TextField txtFieldWorkPhone;
    @FXML
    private TextField txtFieldHomePhone;
    @FXML
    private TextField txtFieldBodyAcademyKey;
    @FXML
    private TextField txtFieldAppoinment;
    @FXML
    private TextField txtFieldParticipationType;
    @FXML
    private TextField txtFieldAditionalMail;
    @FXML
    private PasswordField passFieldIntegrantPassword;
    @FXML
    private TextField txtFieldStudyDegree;
    @FXML
    private TextField txtFieldStudyName;
    @FXML
    private TextField txtFieldInstitutionSchooling;
    @FXML
    private TextField txtFieldState;
    @FXML
    private TextField txtFieldCeduleNumber;
    @FXML
    private TextField txtFieldStudyArea;
    @FXML
    private TextField txtFieldDiscipline;
    @FXML
    private Label lbUserName;
    @FXML
    private JFXDatePicker dtpStudyDegreeDate;
    @FXML
    private TableView<Schooling> tvSchooling;
    @FXML
    private TableColumn<Schooling, String> colSchoolingDegree;
    @FXML
    private TableColumn<Schooling, String> colSchoolingName;
    @FXML
    private TableColumn<Schooling, String> colRegistrationStudyDate;
    @FXML
    private TableColumn<Schooling, String> colInstitution;
    @FXML
    private TableColumn<Schooling, String> colState;
    @FXML
    private TableColumn<Schooling, String> colCeduleNumber;
    @FXML
    private TableColumn<Schooling, String> colSchoolingArea;
    @FXML
    private TableColumn<Schooling, String> colDiscipline;
    @FXML
    private JFXDatePicker dtpRegistrationDate;
    
    private Integrant token;
    private Integrant integrantUpdated;
    private final IntegrantDAO INTEGRANT_DAO = new IntegrantDAO();
    private List<TextField> listSchoolingFields;
    private List<TextField> listPersonalResumeFields;
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        this.preprareSchoolingTable();
        listSchoolingFields = Arrays.asList(
            txtFieldStudyDegree, txtFieldStudyName, 
            txtFieldInstitutionSchooling, txtFieldState, 
            txtFieldStudyArea, txtFieldDiscipline
        );
        listPersonalResumeFields = Arrays.asList(
            txtFieldFullName, txtFieldEmailUv,
            txtFieldNationality, txtFieldEducationalProgram,
            txtFieldAditionalMail
        );
    }

    public void receiveIntegrantToken(Integrant integrant){
        this.token = integrant;
        this.lbUserName.setText(this.token.getFullName());
        this.setIntegrantDataIntoInterface();
    }

    @FXML
    private void updatePersonalResume(ActionEvent event){
        try{
            this.checkPersonalResumeForm();
            this.getOutIntegrantData();
            if(INTEGRANT_DAO.updateMember(this.integrantUpdated, token.getEmailUV())){
                GenericWindowDriver.getGenericWindowDriver().showInfoAlert(event, "Currículum personal actualizado exitosamente");
            }else{
                GenericWindowDriver.getGenericWindowDriver().showErrorAlert(event, "Error en el sistema, favor de ponerse en contacto con soporte técnico");
                this.integrantUpdated = this.token;
            }
            FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("PersonalResumeRequest.fxml", btnCancel);
            PersonalResumeRequestController controller = loader.getController();
            controller.receiveIntegrantToken(this.integrantUpdated);
        }catch(InvalidFormException ex){
            GenericWindowDriver.getGenericWindowDriver().showErrorAlert(event, ex.getMessage());
        }
    }
    
    @FXML
    private void selectSchooling(MouseEvent event){
        if(tvSchooling.getSelectionModel().getSelectedItem() != null){
            this.btnRemoveSchooling.setDisable(false);
        }else{
            this.btnRemoveSchooling.setDisable(true);
        }
    }
    
    @FXML
    private void cancelUpdate(ActionEvent event){
        Optional<ButtonType> action = GenericWindowDriver.getGenericWindowDriver().showConfirmacionAlert(event, "¿Seguro que deseas cancelar la operción?");
        if(action.get() == ButtonType.OK){
            FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("PersonalResumeRequest.fxml", btnCancel);
            PersonalResumeRequestController controller = loader.getController();
            controller.receiveIntegrantToken(this.token);
        }
    }

    @FXML
    private void addSchooling(ActionEvent event){
        try {
            this.checkShooling();
            tvSchooling.getItems().add(this.getOutSchooling());
        } catch (InvalidFormException ex) {
            GenericWindowDriver.getGenericWindowDriver().showErrorAlert(event, ex.getMessage());
        }
    }

    @FXML
    private void removeSchooling(ActionEvent event){
        tvSchooling.getItems().remove(
            tvSchooling.getSelectionModel().getSelectedItem()
        );
    }
    
    private void preprareSchoolingTable(){
        colSchoolingDegree.setCellValueFactory(new PropertyValueFactory<>("levelOfStudy"));
        colSchoolingName.setCellValueFactory(new PropertyValueFactory<>("studyName"));
        colRegistrationStudyDate.setCellValueFactory(new PropertyValueFactory<>("dateOfObteiningStudies"));
        colInstitution.setCellValueFactory(new PropertyValueFactory<>("studiesInstitution"));
        colState.setCellValueFactory(new PropertyValueFactory<>("studiesSatate"));
        colCeduleNumber.setCellValueFactory(new PropertyValueFactory<>("idProfessionalStudies"));
        colSchoolingArea.setCellValueFactory(new PropertyValueFactory<>("studyArea"));
        colDiscipline.setCellValueFactory(new PropertyValueFactory<>("studiesDiscipline"));
    }
    
    private void setIntegrantDataIntoInterface(){
        Integrant integrant = (Integrant) INTEGRANT_DAO.getMemberByUVmail(token.getEmailUV());
        if(integrant != null){
            this.txtFieldAditionalMail.setText(integrant.getAditionalMail());
            this.txtFieldBodyAcademyKey.setText(integrant.getBodyAcademyKey());
            this.txtFieldAppoinment.setText(integrant.getAppointmentMember());
            this.txtFieldBodyAcademyKey.setText(integrant.getBodyAcademyKey());
            this.txtFieldCellPhone.setText(integrant.getCellphone());
            this.txtFieldCurp.setText(integrant.getCurp());
            this.txtFieldEducationalProgram.setText(integrant.getEducationalProgram());
            this.txtFieldEmailUv.setText(integrant.getEmailUV());
            this.txtFieldFullName.setText(integrant.getFullName());
            this.txtFieldHomePhone.setText(integrant.getHomePhone());
            this.txtFieldNationality.setText(integrant.getNationality());
            this.txtFieldParticipationType.setText(integrant.getParticipationType());
            this.passFieldIntegrantPassword.setText(integrant.getPassword());
            this.txtFieldRfc.setText(integrant.getRfc());
            this.txtFieldStaffNumber.setText(String.valueOf(integrant.getStaffNumber()));
            this.txtFieldStatus.setText(integrant.getParticipationStatus());
            this.txtFieldWorkPhone.setText(integrant.getWorkPhone());
            this.dtpRegistrationDate.setValue(LocalDate.parse(integrant.getDateOfAdmission()));
            ObservableList<Schooling> list = FXCollections.observableArrayList();
            list.addAll(integrant.getSchooling());
            tvSchooling.setItems(list);
        }else{
            GenericWindowDriver.getGenericWindowDriver().showErrorAlert(new ActionEvent(), "Error en el sistema, favor de contactar con soporte técnico");
            FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("Start.fxml", btnCancel);
            StartController controller = loader.getController();
            controller.receiveIntegrantToken(this.token);
        }
    }
    
    private void getOutIntegrantData(){
        this.integrantUpdated = new Integrant(
            this.txtFieldRfc.getText(), 
            this.txtFieldFullName.getText(), 
            this.txtFieldEmailUv.getText(), 
            "Activo", 
            this.passFieldIntegrantPassword.getText(), 
            this.txtFieldCurp.getText(), 
            this.txtFieldParticipationType.getText(), 
            this.txtFieldNationality.getText(), 
            this.dtpRegistrationDate.getValue().toString(), 
            this.txtFieldEducationalProgram.getText(), 
            Integer.parseInt(this.txtFieldStaffNumber.getText()), 
            this.txtFieldCellPhone.getText(), 
            this.txtFieldBodyAcademyKey.getText(), 
            this.txtFieldAppoinment.getText(), 
            this.txtFieldAditionalMail.getText(), 
            this.txtFieldHomePhone.getText(), 
            this.txtFieldWorkPhone.getText()
        );
        this.integrantUpdated.setSchooling(tvSchooling.getItems());
    }
    
    private Schooling getOutSchooling(){
        Schooling schooling = new Schooling(
            this.txtFieldStudyDegree.getText(), 
            this.txtFieldStudyName.getText(), 
            this.dtpStudyDegreeDate.getValue().toString(), 
            ValidatorForm.convertJavaDateToSQlDate(dtpStudyDegreeDate), 
            this.txtFieldState.getText(), 
            this.txtFieldCeduleNumber.getText(), 
            this.txtFieldStudyArea.getText(), 
            this.txtFieldDiscipline.getText()
        );
        return schooling;
    }
    
    private void checkPersonalResumeForm() throws InvalidFormException{
        int smallestCharacterSize = 3;
        int largestCharacterSize = 100;
        ValidatorForm.chechkAlphabeticalField(this.txtFieldRfc, smallestCharacterSize, 18);
        ValidatorForm.chechkAlphabeticalField(this.txtFieldCurp, smallestCharacterSize, 19);
        ValidatorForm.checkAlaphabeticalFields(this.listPersonalResumeFields, smallestCharacterSize, largestCharacterSize);
        ValidatorForm.isNumberData(this.txtFieldCellPhone, 10);
        ValidatorForm.isIntegerNumberData(this.txtFieldStaffNumber, smallestCharacterSize);
        ValidatorForm.checkNotEmptyDateField(this.dtpRegistrationDate);
        ValidatorForm.chechkPasswordField(passFieldIntegrantPassword, smallestCharacterSize, 50);
        ValidatorForm.chechkAlphabeticalField(this.txtFieldAppoinment, smallestCharacterSize, 50);
        ValidatorForm.isNumberData(this.txtFieldWorkPhone, 20);
        ValidatorForm.isNumberData(this.txtFieldHomePhone, 10);
    }
    
    private void checkShooling() throws InvalidFormException{
        int smallestCharacterSize = 2;
        int largestCharacterSize = 100;
        ValidatorForm.chechkAlphabeticalField(txtFieldCeduleNumber, smallestCharacterSize, 9);
        ValidatorForm.checkAlaphabeticalFields(listSchoolingFields, smallestCharacterSize, largestCharacterSize);
        ValidatorForm.checkNotEmptyDateField(dtpStudyDegreeDate);
        for(Schooling schooling : tvSchooling.getItems()){
            if(schooling.getIdProfessionalStudies().equalsIgnoreCase(txtFieldCeduleNumber.getText())){
                throw new InvalidFormException("El número de cédula ya existe");
            }
        }
        checkExistEmailUser();
    }
    
    private void checkExistEmailUser() throws InvalidFormException{
        boolean isRegistred = false;
        Member integrantRetrieved = INTEGRANT_DAO.getMemberByUVmail(this.txtFieldEmailUv.getText());
        if(!this.txtFieldEmailUv.getText().equalsIgnoreCase(this.token.getEmailUV())){
            if(integrantRetrieved.getFullName() != null){
                isRegistred = true;
            }
        }
        if(isRegistred){
            throw new InvalidFormException("Usuario repetido en el sistema");
        }
    }
    
}
