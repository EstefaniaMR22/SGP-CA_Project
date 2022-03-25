package controller.academicgroup;

import controller.AlertController;
import controller.ValidatorController;
import controller.validator.Validator;
import controller.validator.ValidatorComboBoxBase;
import controller.validator.ValidatorComboBoxBaseWithConstraints;
import controller.validator.ValidatorTextInputControl;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import model.dao.MiembroDAO;
import model.domain.CivilStatus;
import model.domain.Member;
import model.domain.ParticipationType;
import model.domain.StudyGrade;
import utils.DateFormatter;
import utils.SQLStates;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddMemberController extends ValidatorController implements Initializable {
    private Member registeredMember;
    @FXML private TextField aditionalEmailTextField;
    @FXML private TextField appointmentTextField;
    @FXML private ComboBox<CivilStatus> civilStatusComboBox;
    @FXML private ComboBox<String> educationalProgramComboBox;
    @FXML private TextField curpTextField;
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
    @FXML private DatePicker admissionDateDatePicker;
    @FXML private DatePicker birthDateDatePicker;
    @FXML private ComboBox<StudyGrade> studyGradeComboBox;
    @FXML private TextField studyAreaTextField;
    @FXML private Label systemLabel;
    @FXML private VBox memberDataVBox;
    @FXML private Button registerButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getCivilStatesFromDatabase();
        getStudyGradesFromDatabase();
        getEducationProgramFromDatabase();
        initValidatorToTextInput();
    }

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/AddMemberView.fxml"), this);
        stage.showAndWait();
    }

    public Member getRegisteredMember() {
        return registeredMember;
    }

    @FXML
    void cancelButtonPressed(ActionEvent event) {
        if (AlertController.showCancelationConfirmationAlert()) {
            stage.close();
        }
    }

    @FXML
    void AddMemberOnAction(ActionEvent event) {
        if (validateInputs()) {
            if (!validatePersonalNumber()) {
                addMember();
            } else {
                systemLabel.setText("¡Al parecer ya existe un miembro con ese numero de personal!");
            }
        } else {
            systemLabel.setText("Algunos campos son inválidos, por favor verifíquelos");
        }
    }

    private boolean validatePersonalNumber() {
        try {
            return new MiembroDAO().checkMember(personalNumberTextField.getText());
        } catch (SQLException sqlException) {
            Logger.getLogger(AddMemberController.class.getName()).log(Level.SEVERE, null, sqlException);
        }
        return false;
    }

    private void addMember() {
        Member member = getMemberFromInputs();
        try {
            member.setId(new MiembroDAO().addMember(member, "hola"));
            registeredMember = member;
            systemLabel.setText("¡Se ha registrado de manera exitosa!");
            disableMemberInput(true);
            pause();
        } catch (SQLException sqlException) {
            deterMinateSQLState(sqlException);
        }
    }

    private void addColaborator() {
        Member colaborator = getMemberFromInputs();
        try {
            colaborator.setId(new MiembroDAO().addMember(colaborator));
            registeredMember = colaborator;
            systemLabel.setText("¡Se ha registrado de manera exitosa!");
            disableMemberInput(true);
            pause();
        } catch (SQLException sqlException) {
            deterMinateSQLState(sqlException);
        }
    }

    private void getCivilStatesFromDatabase() {
        List<CivilStatus> civilStatusList = new ArrayList<>();
        try {
            civilStatusList = new MiembroDAO().getCivilStatus();
        } catch (SQLException sqlException) {
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

    private void getEducationProgramFromDatabase() {
        List<String> educationalProgramList = new ArrayList<>();
        try {
            educationalProgramList = new MiembroDAO().getAllEducationProgram();
        } catch (SQLException sqlException ) {
            deterMinateSQLState(sqlException);
        }
        ObservableList<String> educationalProgramObservableList = FXCollections.observableArrayList(educationalProgramList);
        educationalProgramComboBox.setItems(educationalProgramObservableList);
    }

    private void deterMinateSQLState(SQLException sqlException) {
        Logger.getLogger(AddMemberController.class.getName()).log(Level.SEVERE, null, sqlException);
        if (sqlException.getSQLState().equals(SQLStates.SQL_NO_CONNECTION.getSqlState())) {
            AlertController.showConnectionErrorAlert();
        }
        AlertController.showActionFailedAlert(sqlException.getLocalizedMessage());
    }

    private void pause() {
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> stage.close());
        pause.play();
    }

    private void initValidatorToTextInput() {
        Function<Object, Boolean> validateBirthDate = a -> {
            LocalDate now = LocalDate.now();
            now = now.minusYears(Validator.MIN_YEARS_OLD);
            return now.compareTo((LocalDate) a) >= 0;
        };

        Function<Object, Boolean> validateAdmissionDate = a -> DateFormatter.compareActualDateToLocalDate((LocalDate) a) >= 0;
        disableMemberInput(false);
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
        addComponentToValidator(new ValidatorComboBoxBase(educationalProgramComboBox, this), false);
        addComponentToValidator(new ValidatorTextInputControl(stateTextField, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);
        addComponentToValidator(new ValidatorComboBoxBaseWithConstraints(birthDateDatePicker, this, validateBirthDate), false);
        addComponentToValidator(new ValidatorComboBoxBaseWithConstraints(admissionDateDatePicker, this, validateAdmissionDate), false);
        addComponentToValidator(new ValidatorTextInputControl(homePhoneNumberTextField, Validator.PATTERN_TELEPHONE, Validator.LENGTH_TELEPHONE, this), false);
        addComponentToValidator(new ValidatorTextInputControl(workTelephoneTextField, Validator.PATTERN_TELEPHONE, Validator.LENGTH_TELEPHONE, this), false);
        addComponentToValidator(new ValidatorTextInputControl(aditionalEmailTextField, Validator.PATTERN_EMAIL, Validator.LENGTH_EMAIL, this), false);
        addComponentToValidator(new ValidatorTextInputControl(appointmentTextField, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);
        addComponentToValidator(new ValidatorTextInputControl(studyAreaTextField, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);
        addComponentToValidator(new ValidatorComboBoxBase(studyGradeComboBox, this), false);
        initListenerToControls();
    }

    private void disableMemberInput(boolean state) {
        nameTextField.setDisable(state);
        paternalLastnameTextField.setDisable(state);
        maternalLastnameTextField.setDisable(state);
        nationalityTextField.setDisable(state);
        civilStatusComboBox.setDisable(state);
        curpTextField.setDisable(state);
        telephoneTextField.setDisable(state);
        rfcTextField.setDisable(state);
        personalNumberTextField.setDisable(state);
        uvEmailTextField.setDisable(state);
        educationalProgramComboBox.setDisable(state);
        stateTextField.setDisable(state);
        birthDateDatePicker.setDisable(state);
        admissionDateDatePicker.setDisable(state);
        workTelephoneTextField.setDisable(state);
        homePhoneNumberTextField.setDisable(state);
        aditionalEmailTextField.setDisable(state);
        appointmentTextField.setDisable(state);
        studyAreaTextField.setDisable(state);
        studyGradeComboBox.setDisable(state);
        registerButton.setDisable(state);
    }

    private Member getMemberFromInputs() {
        Member member = new Member();
        member.setName(nameTextField.getText());
        member.setPaternalLastname(paternalLastnameTextField.getText());
        member.setMaternalLastname(maternalLastnameTextField.getText());
        member.setNationality(nationalityTextField.getText());
        member.setCivilStatus(civilStatusComboBox.getValue());
        member.setCurp(curpTextField.getText());
        member.setTelephone(telephoneTextField.getText());
        member.setRfc(rfcTextField.getText());
        member.setBirthState(stateTextField.getText());
        member.setPersonalNumber(personalNumberTextField.getText());
        member.setUvEmail(uvEmailTextField.getText());
        member.setEducationalProgram(educationalProgramComboBox.getValue());
        member.setHomeTelephone(homePhoneNumberTextField.getText());
        member.setWorkTelephone(workTelephoneTextField.getText());
        member.setAditionalEmail(aditionalEmailTextField.getText());
        member.setAppointment(appointmentTextField.getText());
        member.setAdmissionDate(DateFormatter.getDateFromDatepickerValue(admissionDateDatePicker.getValue()));
        member.setBirthDate(DateFormatter.getDateFromDatepickerValue(birthDateDatePicker.getValue()));
        member.setMaxStudyGrade(studyGradeComboBox.getValue());
        member.setBirthDate(DateFormatter.getDateFromDatepickerValue(birthDateDatePicker.getValue()));
        member.setStudyArea(studyAreaTextField.getText());
        return member;
    }

}
