package controller.academicgroup;

import controller.Controller;
import controller.ValidatorController;
import controller.validator.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Modality;
import model.dao.MiembroDAO;
import model.domain.*;
import utils.DateFormatter;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModifyMemberController extends ValidatorController implements Initializable {
    private Member memberSelected;
    @FXML private TextField aditionalEmailTextField;
    @FXML private TextField appointmentTextField;
    @FXML private ComboBox<CivilStatus> civilStatusComboBox;
    @FXML private TextField curpTextField;
    @FXML private TextField educationalProgramTextField;
    @FXML private TextField homePhoneNumberTextField;
    @FXML private TextField maternalLastnameTextField;
    @FXML private TextField nameTextField;
    @FXML private TextField nationalityTextField;
    @FXML private TextField paternalLastnameTextField;
    @FXML private TextField personalNumberTextField;
    @FXML private TextField rfcTextField;
    @FXML private TextField stateTextField;
    @FXML private TextField telephoneTextField;
    @FXML private TextField uvEmailTextField;
    @FXML private TextField workTelephoneTextField;
    @FXML private ToggleButton colaboratorToggleButton;
    @FXML private ToggleButton integrantToggleButton;
    @FXML private ToggleButton responsableToggleButton;
    @FXML private ToggleGroup typeParticipationToggleGroup;
    @FXML private DatePicker admissionDateDatePicker;
    @FXML private DatePicker birthDateDatePicker;
    @FXML private ComboBox<StudyGrade> studyGradeComboBox;
    @FXML private TextField studyAreaTextField;

    public ModifyMemberController(Member member) {
        this.memberSelected = member;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initValidatorToTextInput();
        responsableToggleButton.setUserData(ParticipationType.RESPONSABLE);
        integrantToggleButton.setUserData(ParticipationType.INTEGRANT);
        colaboratorToggleButton.setUserData(ParticipationType.COLABORATOR);
        setMemberDataIntoFields();
        getCivilStatesFromDatabase();
    }

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/ModifyMemberView.fxml"), this);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    @FXML
    void ModifyMemberOnAction(ActionEvent event) {
        if(memberSelected.getParticipationType() == ParticipationType.INTEGRANT ) {
            updateIntegrant();
        } else if( memberSelected.getParticipationType() == ParticipationType.RESPONSABLE){
            updateResponsable();
        } else if(memberSelected.getParticipationType() == ParticipationType.COLABORATOR) {
            // Example
        } else {
            // Example
        }
    }

    @FXML
    void cancelButtonPressed(ActionEvent event) {
        stage.close();
    }

    private void updateIntegrant() {
        boolean isUpdated = false;
        Integrant integrante = new Integrant();
        integrante.setId(memberSelected.getId());
        integrante.setName(nameTextField.getText());
        integrante.setPaternalLastname(paternalLastnameTextField.getText());
        integrante.setMaternalLastname(maternalLastnameTextField.getText());
        integrante.setNationality(nationalityTextField.getText());
        integrante.setCivilStatus(civilStatusComboBox.getValue());
        integrante.setCurp(curpTextField.getText());
        integrante.setTelephone(telephoneTextField.getText());
        integrante.setRfc(rfcTextField.getText());
        integrante.setBirthState(stateTextField.getText());
        integrante.setPersonalNumber(personalNumberTextField.getText());
        integrante.setUvEmail(uvEmailTextField.getText());
        integrante.setEducationalProgram(educationalProgramTextField.getText());
        integrante.setHomeTelephone(homePhoneNumberTextField.getText());
        integrante.setWorkTelephone(workTelephoneTextField.getText());
        integrante.setAditionalEmail(aditionalEmailTextField.getText());
        integrante.setAppointment(appointmentTextField.getText());
        integrante.setParticipationType(ParticipationType.INTEGRANT);
        integrante.setBirthDate(DateFormatter.getDateFromDatepickerValue(birthDateDatePicker.getValue()));
        integrante.setAdmissionDate(DateFormatter.getDateFromDatepickerValue(admissionDateDatePicker.getValue()));
        try {
            isUpdated = new MiembroDAO().updateMember(integrante);
        } catch(SQLException sqlException) {
            Logger.getLogger(ModifyMemberController.class.getName()).log(Level.SEVERE, null, sqlException);
        }
        System.out.println(isUpdated);
    }

    private void updateResponsable() {
        boolean isUpdated = false;
        Responsable responsable = new Responsable();
        responsable.setId(memberSelected.getId());
        responsable.setName(nameTextField.getText());
        responsable.setPaternalLastname(paternalLastnameTextField.getText());
        responsable.setMaternalLastname(maternalLastnameTextField.getText());
        responsable.setNationality(nationalityTextField.getText());
        responsable.setCivilStatus(civilStatusComboBox.getValue());
        responsable.setCurp(curpTextField.getText());
        responsable.setTelephone(telephoneTextField.getText());
        responsable.setRfc(rfcTextField.getText());
        responsable.setBirthState(stateTextField.getText());
        responsable.setPersonalNumber(personalNumberTextField.getText());
        responsable.setUvEmail(uvEmailTextField.getText());
        responsable.setEducationalProgram(educationalProgramTextField.getText());
        responsable.setHomeTelephone(homePhoneNumberTextField.getText());
        responsable.setWorkTelephone(workTelephoneTextField.getText());
        responsable.setAditionalEmail(aditionalEmailTextField.getText());
        responsable.setAppointment(appointmentTextField.getText());
        responsable.setParticipationType(ParticipationType.RESPONSABLE);
        responsable.setBirthDate(DateFormatter.getDateFromDatepickerValue(birthDateDatePicker.getValue()));
        responsable.setAdmissionDate(DateFormatter.getDateFromDatepickerValue(admissionDateDatePicker.getValue()));
        try {
            isUpdated = new MiembroDAO().updateMember(responsable);
        } catch (SQLException sqlException) {
            Logger.getLogger(ModifyMemberController.class.getName()).log(Level.SEVERE, null, sqlException);
        }
        System.out.println(isUpdated);
    }

    private void getCivilStatesFromDatabase() {
        List<CivilStatus> civilStatusList = new ArrayList<>();
        try {
            civilStatusList = new MiembroDAO().getCivilStatus();
        } catch(SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        ObservableList<CivilStatus> civilStatusObservableList = FXCollections.observableArrayList(civilStatusList);
        civilStatusComboBox.setItems(civilStatusObservableList);
    }

    private void setMemberDataIntoFields() {
        nameTextField.setText(memberSelected.getName());
        paternalLastnameTextField.setText(memberSelected.getPaternalLastname());
        maternalLastnameTextField.setText(memberSelected.getMaternalLastname());
        nationalityTextField.setText(memberSelected.getNationality());
        civilStatusComboBox.getSelectionModel().select(memberSelected.getCivilStatus());
        curpTextField.setText(memberSelected.getCurp());
        telephoneTextField.setText(memberSelected.getTelephone());
        rfcTextField.setText(memberSelected.getRfc());
        personalNumberTextField.setText(memberSelected.getPersonalNumber());
        uvEmailTextField.setText(memberSelected.getUvEmail());
        educationalProgramTextField.setText(memberSelected.getEducationalProgram());
        stateTextField.setText(memberSelected.getBirthState());
        birthDateDatePicker.setValue(DateFormatter.getLocalDateFromUtilDate(memberSelected.getBirthDate()));
        admissionDateDatePicker.setValue(DateFormatter.getLocalDateFromUtilDate(memberSelected.getAdmissionDate()));
        //typeParticipationToggleGroup.
        if(memberSelected.getParticipationType() == ParticipationType.INTEGRANT) {
            homePhoneNumberTextField.setText(((Integrant) memberSelected).getHomeTelephone());
            workTelephoneTextField.setText(((Integrant) memberSelected).getWorkTelephone());
            aditionalEmailTextField.setText(((Integrant) memberSelected).getAditionalEmail());
            appointmentTextField.setText(((Integrant) memberSelected).getAppointment());
        } else if(memberSelected.getParticipationType() == ParticipationType.RESPONSABLE) {
            homePhoneNumberTextField.setText(((Responsable) memberSelected).getHomeTelephone());
            workTelephoneTextField.setText(((Responsable) memberSelected).getWorkTelephone());
            aditionalEmailTextField.setText(((Responsable) memberSelected).getAditionalEmail());
            appointmentTextField.setText(((Responsable) memberSelected).getAppointment());
        } else if(memberSelected.getParticipationType() == ParticipationType.COLABORATOR) {
            studyGradeComboBox.getSelectionModel().select( ((Colaborator) memberSelected).getMaxStudyGrade());
            studyAreaTextField.setText(((Colaborator) memberSelected).getStudyArea());
        }
    }

    private void initValidatorToTextInput() {
        Function<Object, Boolean> validateBirthDate = a -> {
            LocalDate now = LocalDate.now();
            now = now.minusYears(Validator.MIN_YEARS_OLD);
            return now.compareTo( (LocalDate) a) >= 0;
        };

        Function<Object, Boolean> validateAdmissionDate = a -> {
            return DateFormatter.compareActualDateToLocalDate((LocalDate) a) >= 0;
        };
        addComponentToValidator(new ValidatorToggleGroup(typeParticipationToggleGroup, this), false);
        addComponentToValidator(new ValidatorTextInputControl(nameTextField, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);
        addComponentToValidator(new ValidatorTextInputControl(paternalLastnameTextField, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);
        addComponentToValidator(new ValidatorTextInputControl(maternalLastnameTextField, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);
        addComponentToValidator(new ValidatorTextInputControl(nationalityTextField, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);
        addComponentToValidator(new ValidatorComboBoxBase(civilStatusComboBox, this), false);
        addComponentToValidator(new ValidatorTextInputControl(curpTextField, Validator.PATTERN_CURP, Validator.LENGTH_CURP, this), false);
        addComponentToValidator(new ValidatorTextInputControl(telephoneTextField, Validator.PATTERN_TELEPHONE, Validator.LENGTH_TELEPHONE, this), false);
        addComponentToValidator(new ValidatorTextInputControl(rfcTextField, Validator.PATTERN_RFC, Validator.LENGTH_RFC, this), false);
        addComponentToValidator(new ValidatorTextInputControl(personalNumberTextField, Validator.PATTERN_NUMBERS_AND_LETTERS, Validator.LENGTH_GENERAL, this), false);
        addComponentToValidator(new ValidatorTextInputControl(uvEmailTextField, Validator.PATTERN_EMAIL, Validator.LENGTH_EMAIL, this), false);
        addComponentToValidator(new ValidatorTextInputControl(educationalProgramTextField, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);
        addComponentToValidator(new ValidatorTextInputControl(stateTextField, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);
        addComponentToValidator(new ValidatorComboBoxBaseWithConstraints(birthDateDatePicker, this, validateBirthDate), false);
        addComponentToValidator(new ValidatorComboBoxBaseWithConstraints(admissionDateDatePicker, this, validateAdmissionDate),false);
        addComponentToValidator(new ValidatorTextInputControl(homePhoneNumberTextField, Validator.PATTERN_TELEPHONE, Validator.LENGTH_TELEPHONE, this), false);
        addComponentToValidator(new ValidatorTextInputControl(workTelephoneTextField, Validator.PATTERN_TELEPHONE, Validator.LENGTH_TELEPHONE, this), false);
        addComponentToValidator(new ValidatorTextInputControl(aditionalEmailTextField, Validator.PATTERN_EMAIL, Validator.LENGTH_EMAIL, this), false);
        addComponentToValidator(new ValidatorTextInputControl(appointmentTextField, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);
        addComponentToValidator(new ValidatorTextInputControl(studyAreaTextField, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);
        addComponentToValidator(new ValidatorComboBoxBase(studyGradeComboBox, this), false);
        initListenerToControls();
    }

}
