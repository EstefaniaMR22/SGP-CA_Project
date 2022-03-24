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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
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
    @FXML private VBox memberDataVBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setValuesToToggleButtons();
        getCivilStatesFromDatabase();
        getStudyGradesFromDatabase();
        initMemberTypeListener();
        disableMemberInput(true);
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
        try {
            if (validateInputs()) {
                if (!validatePersonalNumber()) {
                    ParticipationType participationType = (ParticipationType) typeParticipationToggleGroup.getSelectedToggle().getUserData();
                    if (participationType == ParticipationType.COLABORATOR) {
                        addColaborator();
                    } else if (participationType == ParticipationType.OTHER) {
                        System.out.println("FUNCTIONALITY NOT IMPLEMENT YET");
                    } else {
                        addMember();
                    }
                } else {
                    systemLabel.setText("¡Al parecer ya existe un miembro con ese numero de personal!");
                }
            } else {
                systemLabel.setText("Algunos campos son inválidos, por favor verifíquelos");
            }
        } catch (Exception e) {
            systemLabel.setText(e.getLocalizedMessage());
        }
    }

    private boolean validatePersonalNumber() throws SQLException {
        return new MiembroDAO().checkMember(personalNumberTextField.getText());
    }

    private void addMember() {
        Member member = getMemberFromInputs();
        try {
            member.setId(new MiembroDAO().addMember(member, "hola"));
        } catch (SQLException sqlException) {
            deterMinateSQLState(sqlException);
        }
        registeredMember = member;
        systemLabel.setText("¡Se ha registrado con exito el nuevo miembro");
        disableMemberInput(true);
        disableToggleButton();
        pause();
    }

    private void addColaborator() {
        Member colaborator = getMemberFromInputs();
        try {
            colaborator.setId(new MiembroDAO().addMember(colaborator));
        } catch (SQLException sqlException) {
            deterMinateSQLState(sqlException);
        }
        registeredMember = colaborator;
        systemLabel.setText("¡Se ha registrado con exito el nuevo miembro");
        disableMemberInput(true);
        disableToggleButton();
        pause();
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

    private void deterMinateSQLState(SQLException sqlException) {
        Logger.getLogger(AddMemberController.class.getName()).log(Level.SEVERE, null, sqlException);
        if (sqlException.getSQLState().equals(SQLStates.SQL_NO_CONNECTION.getSqlState())) {
            AlertController.showConnectionErrorAlert();
        }
        AlertController.showActionFailedAlert(sqlException.getLocalizedMessage());
    }

    private void initMemberTypeListener() {
        typeParticipationToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                clearMap();
                disableMemberInput(true);
                initValidatorToTextInput();
            }
        });
    }

    private void pause() {
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> stage.close());
        pause.play();
    }

    private void setValuesToToggleButtons() {
        responsableToggleButton.setUserData(ParticipationType.RESPONSABLE);
        integrantToggleButton.setUserData(ParticipationType.INTEGRANT);
        colaboratorToggleButton.setUserData(ParticipationType.COLABORATOR);
    }

    private void initValidatorToTextInput() {
        Function<Object, Boolean> validateBirthDate = a -> {
            LocalDate now = LocalDate.now();
            now = now.minusYears(Validator.MIN_YEARS_OLD);
            return now.compareTo((LocalDate) a) >= 0;
        };

        Function<Object, Boolean> validateAdmissionDate = a -> DateFormatter.compareActualDateToLocalDate((LocalDate) a) >= 0;
        disableMemberInput(false);
        //addComponentToValidator(new ValidatorToggleGroup(typeParticipationToggleGroup, this), false);
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
        educationalProgramTextField.setDisable(state);
        stateTextField.setDisable(state);
        birthDateDatePicker.setDisable(state);
        admissionDateDatePicker.setDisable(state);
        workTelephoneTextField.setDisable(state);
        homePhoneNumberTextField.setDisable(state);
        aditionalEmailTextField.setDisable(state);
        appointmentTextField.setDisable(state);
        studyAreaTextField.setDisable(state);
        studyGradeComboBox.setDisable(state);
    }

    private void disableToggleButton() {
        integrantToggleButton.setDisable(true);
        responsableToggleButton.setDisable(true);
        colaboratorToggleButton.setDisable(true);
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
        member.setEducationalProgram(educationalProgramTextField.getText());
        member.setHomeTelephone(homePhoneNumberTextField.getText());
        member.setWorkTelephone(workTelephoneTextField.getText());
        member.setAditionalEmail(aditionalEmailTextField.getText());
        member.setAppointment(appointmentTextField.getText());
        member.setParticipationType((ParticipationType) typeParticipationToggleGroup.getSelectedToggle().getUserData());
        member.setAdmissionDate(DateFormatter.getDateFromDatepickerValue(admissionDateDatePicker.getValue()));
        member.setBirthDate(DateFormatter.getDateFromDatepickerValue(birthDateDatePicker.getValue()));
        member.setMaxStudyGrade(studyGradeComboBox.getValue());
        member.setBirthDate(DateFormatter.getDateFromDatepickerValue(birthDateDatePicker.getValue()));
        member.setStudyArea(studyAreaTextField.getText());
        return member;
    }

}
