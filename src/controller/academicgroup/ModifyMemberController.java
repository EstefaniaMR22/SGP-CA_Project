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
import javafx.stage.Modality;
import model.dao.MiembroDAO;
import model.domain.*;
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
    @FXML private Label systemLabel;

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

    public Member getMemberSelected() {
        return memberSelected;
    }

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/ModifyMemberView.fxml"), this);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    @FXML
    void ModifyMemberOnAction(ActionEvent event) {
        try {
            if (validateInputs()) {
                updateMember();
            } else {
                systemLabel.setText("Algunos campos son inválidos, por favor verifíquelos");
            }
        } catch (Exception e) {
            systemLabel.setText(e.getLocalizedMessage());
        }
    }

    @FXML
    void cancelButtonPressed(ActionEvent event) {
        stage.close();
    }

    private void updateMember() {
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
        try {
            if( new MiembroDAO().updateMember(member) ) {
                memberSelected = member;
            }
        } catch (SQLException sqlException) {
            Logger.getLogger(ModifyMemberController.class.getName()).log(Level.SEVERE, null, sqlException);
        }
    }

    private void getCivilStatesFromDatabase() {
        List<CivilStatus> civilStatusList = new ArrayList<>();
        try {
            civilStatusList = new MiembroDAO().getCivilStatus();
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        ObservableList<CivilStatus> civilStatusObservableList = FXCollections.observableArrayList(civilStatusList);
        civilStatusComboBox.setItems(civilStatusObservableList);
    }

    private void deterMinateSQLState(SQLException sqlException) {
        Logger.getLogger(ModifyMemberController.class.getName()).log(Level.SEVERE, null, sqlException);
        if (sqlException.getSQLState().equals(SQLStates.SQL_NO_CONNECTION.getSqlState())) {
            AlertController.showConnectionErrorAlert();
        }
        AlertController.showActionFailedAlert(sqlException.getLocalizedMessage());
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
        homePhoneNumberTextField.setText(memberSelected.getHomeTelephone());
        workTelephoneTextField.setText(memberSelected.getWorkTelephone());
        aditionalEmailTextField.setText(memberSelected.getAditionalEmail());
        appointmentTextField.setText(memberSelected.getAppointment());
        studyGradeComboBox.getSelectionModel().select(memberSelected.getMaxStudyGrade());
        studyAreaTextField.setText(memberSelected.getStudyArea());
    }

    private void initValidatorToTextInput() {
        Function<Object, Boolean> validateBirthDate = a -> {
            LocalDate now = LocalDate.now();
            now = now.minusYears(Validator.MIN_YEARS_OLD);
            return now.compareTo((LocalDate) a) >= 0;
        };

        Function<Object, Boolean> validateAdmissionDate = a -> {
            return DateFormatter.compareActualDateToLocalDate((LocalDate) a) >= 0;
        };
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

}
