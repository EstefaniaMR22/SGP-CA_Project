package controller.academicgroup;

import controller.AlertController;
import controller.ValidatorController;
import controller.validator.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.dao.MiembroDAO;
import model.domain.CivilStatus;
import model.domain.Colaborator;
import model.domain.Integrant;
import model.domain.ParticipationType;
import model.domain.Responsable;
import model.domain.StudyGrade;
import utils.DateFormatter;
import utils.SQLStates;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AddMemberController extends ValidatorController implements Initializable {
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
    @FXML private ToggleButton responsableToggleButton;
    @FXML private ToggleGroup typeParticipationToggleGroup;
    @FXML private ToggleButton integrantToggleButton;
    @FXML private ToggleButton colaboratorToggleButton;
    @FXML private DatePicker admissionDateDatePicker;
    @FXML private DatePicker birthDateDatePicker;
    @FXML private ComboBox<StudyGrade> studyGradeComboBox;
    @FXML private TextField studyAreaTextField;
    @FXML private Label systemLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getCivilStatesFromDatabase();
        getStudyGradesFromDatabase();
        initValidatorToTextInput();
        responsableToggleButton.setUserData(ParticipationType.RESPONSABLE);
        integrantToggleButton.setUserData(ParticipationType.INTEGRANT);
        colaboratorToggleButton.setUserData(ParticipationType.COLABORATOR);
    }

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/AddMemberView.fxml"), this);
        stage.showAndWait();
    }

    @FXML
    void cancelButtonPressed(ActionEvent event) {
        if(AlertController.showCancelationConfirmationAlert()) {
            stage.close();
        }
    }

    @FXML
    void AddMemberOnAction(ActionEvent event) {
        try {
            if(validateInputs()) {
                ParticipationType participationType = (ParticipationType) typeParticipationToggleGroup.getSelectedToggle().getUserData();
                if(participationType == ParticipationType.INTEGRANT ) {
                    addIntegrant();
                } else if( participationType == ParticipationType.RESPONSABLE){
                    addResponsable();
                } else if(participationType == ParticipationType.COLABORATOR) {
                    addColaborator();
                }
            }
        } catch (Exception e) {
           systemLabel.setText(e.getLocalizedMessage());
        }
    }

    private void addIntegrant() {
        Integrant integrante = new Integrant();
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
        integrante.setAdmissionDate(DateFormatter.getDateFromDatepickerValue(admissionDateDatePicker.getValue()));
        integrante.setBirthDate(DateFormatter.getDateFromDatepickerValue(birthDateDatePicker.getValue()));
        try {
            integrante.setId(new MiembroDAO().addMember(integrante, "hola"));
        } catch(SQLException sqlException) {
            deterMinateSQLState(sqlException);
        }
    }

    private void addResponsable() {
        Responsable responsable = new Responsable();
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
        responsable.setParticipationType(ParticipationType.INTEGRANT);
        responsable.setAdmissionDate(DateFormatter.getDateFromDatepickerValue(admissionDateDatePicker.getValue()));
        responsable.setBirthDate(DateFormatter.getDateFromDatepickerValue(birthDateDatePicker.getValue()));
        try {
            responsable.setId(new MiembroDAO().addMember(responsable, "hola"));
        } catch(SQLException sqlException) {
            deterMinateSQLState(sqlException);
        }
    }

    private void addColaborator() {
        Colaborator colaborator = new Colaborator();
        colaborator.setName(nameTextField.getText());
        colaborator.setPaternalLastname(paternalLastnameTextField.getText());
        colaborator.setMaternalLastname(maternalLastnameTextField.getText());
        colaborator.setNationality(nationalityTextField.getText());
        colaborator.setCivilStatus(civilStatusComboBox.getValue());
        colaborator.setCurp(curpTextField.getText());
        colaborator.setTelephone(telephoneTextField.getText());
        colaborator.setRfc(rfcTextField.getText());
        colaborator.setBirthState(stateTextField.getText());
        colaborator.setPersonalNumber(personalNumberTextField.getText());
        colaborator.setUvEmail(uvEmailTextField.getText());
        colaborator.setEducationalProgram(educationalProgramTextField.getText());
        colaborator.setStudyArea(studyAreaTextField.getText());
        colaborator.setMaxStudyGrade(studyGradeComboBox.getValue());
        colaborator.setAdmissionDate(DateFormatter.getDateFromDatepickerValue(admissionDateDatePicker.getValue()));
        colaborator.setBirthDate(DateFormatter.getDateFromDatepickerValue(birthDateDatePicker.getValue()));

        try {
            colaborator.setId(new MiembroDAO().addMember(colaborator));
        } catch (SQLException sqlException) {
            deterMinateSQLState(sqlException);
        }

    }

    private void getCivilStatesFromDatabase() {
        List<CivilStatus> civilStatusList = new ArrayList<>();
        try {
            civilStatusList = new MiembroDAO().getCivilStatus();
        } catch(SQLException sqlException) {
            deterMinateSQLState(sqlException);
        }
        ObservableList<CivilStatus> civilStatusObservableList = FXCollections.observableArrayList(civilStatusList);
        civilStatusComboBox.setItems(civilStatusObservableList);
    }

    private void getStudyGradesFromDatabase() {
        List<StudyGrade> studyGradeList = new ArrayList<>();
        try {
            studyGradeList = new MiembroDAO().getStudyGrades();
        } catch (SQLException sqlException) {
            deterMinateSQLState(sqlException);
        }
        ObservableList<StudyGrade> studyGradeObservableList = FXCollections.observableArrayList(studyGradeList);
        studyGradeComboBox.setItems(studyGradeObservableList);
    }

    private void deterMinateSQLState(SQLException sqlException) {
        Logger.getLogger(AddMemberController.class.getName()).log(Level.SEVERE, null, sqlException);
        if(sqlException.getSQLState().equals(SQLStates.SQL_NO_CONNECTION.getSqlState())) {
            AlertController.showConnectionErrorAlert();
        }
        AlertController.showActionFailedAlert(sqlException.getLocalizedMessage());
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
