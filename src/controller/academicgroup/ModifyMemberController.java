package controller.academicgroup;

import controller.control.AlertController;
import controller.control.ValidatorController;
import controller.control.validator.Validator;
import controller.control.validator.ValidatorComboBoxBase;
import controller.control.validator.ValidatorComboBoxBaseWithConstraints;
import controller.control.validator.ValidatorTextInputControl;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.util.Duration;
import model.dao.MemberDAO;
import model.domain.CivilStatus;
import model.domain.Member;
import model.domain.StudyGrade;
import assets.utils.DateFormatter;
import assets.utils.SQLStates;

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
    @FXML private TextField homePhoneNumberTextField;
    @FXML private TextField maternalLastnameTextField;
    @FXML private ComboBox<String> educationalProgramComboBox;
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
    @FXML private Button modifyButton;

    public ModifyMemberController(Member member) {
        this.memberSelected = member;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initValidatorToTextInput();
        setMemberDataIntoFields();
        getCivilStatesFromDatabase();
        getStudyGradesFromDatabase();
        getEducationProgramFromDatabase();
    }

    public Member getMemberSelected() {
        return memberSelected;
    }

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/ModifyMemberView.fxml"), this);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Modificar miembro");
        stage.showAndWait();
    }

    @FXML
    void modifyMemberOnAction(ActionEvent event) {
        if (validateInputs()) {
            if(!validateEmail()) {
                updateMember();
            }
        } else {
            systemLabel.setText("Algunos campos son inv??lidos, por favor verif??quelos");
        }
    }

    @FXML
    void cancelButtonPressed(ActionEvent event) {
        if (AlertController.getInstance().showCancelationConfirmationAlert()) {
            stage.close();
        }
    }

    private boolean validateEmail() {
        boolean existMember = false;
        try {
            existMember = new MemberDAO().checkMemberByEmail(uvEmailTextField.getText(), memberSelected.getId());
            if(existMember) {
                systemLabel.setText("??Al parecer el correo electronico ya est?? siendo usado!");
            }
        } catch (SQLException sqlException) {
            Logger.getLogger(AddMemberController.class.getName()).log(Level.SEVERE, null, sqlException);
            AlertController.getInstance().determinateAlertBySQLException(sqlException);
        }
        return existMember;
    }

    private boolean validatePersonalNumber() {
        boolean existMember = false;
        try {
            existMember =  new MemberDAO().checkMember(personalNumberTextField.getText());
            if(existMember ) {
                systemLabel.setText("??Al parecer ya existe un miembro con ese n??mero de personal!");
            }
        } catch (SQLException sqlException) {
            Logger.getLogger(AddMemberController.class.getName()).log(Level.SEVERE, null, sqlException);
            AlertController.getInstance().determinateAlertBySQLException(sqlException);
        }
        return false;
    }

    private void pause() {
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> stage.close());
        pause.play();
    }

    private void getCivilStatesFromDatabase() {
        List<CivilStatus> civilStatusList = new ArrayList<>();
        try {
            civilStatusList = new MemberDAO().getCivilStatus();
        } catch (SQLException sqlException) {
            Logger.getLogger(ModifyMemberController.class.getName()).log(Level.SEVERE, null, sqlException);
            AlertController.getInstance().determinateAlertBySQLException(sqlException);
        }
        ObservableList<CivilStatus> civilStatusObservableList = FXCollections.observableArrayList(civilStatusList);
        civilStatusComboBox.setItems(civilStatusObservableList);
    }

    private void getStudyGradesFromDatabase() {
        List<StudyGrade> studyGradeList = new ArrayList<>();
        try {
            studyGradeList = new MemberDAO().getStudyGrades();
        } catch (SQLException sqlException) {
            Logger.getLogger(ModifyMemberController.class.getName()).log(Level.SEVERE, null, sqlException);
            AlertController.getInstance().determinateAlertBySQLException(sqlException);
        }
        ObservableList<StudyGrade> studyGradeObservableList = FXCollections.observableArrayList(studyGradeList);
        studyGradeComboBox.setItems(studyGradeObservableList);
    }

    private void getEducationProgramFromDatabase() {
        List<String> educationalProgramList = new ArrayList<>();
        try {
            educationalProgramList = new MemberDAO().getAllEducationProgram();
        } catch (SQLException sqlException ) {
            Logger.getLogger(ModifyMemberController.class.getName()).log(Level.SEVERE, null, sqlException);
            AlertController.getInstance().determinateAlertBySQLException(sqlException);
        }
        ObservableList<String> educationalProgramObservableList = FXCollections.observableArrayList(educationalProgramList);
        educationalProgramComboBox.setItems(educationalProgramObservableList);
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
        member.setEducationalProgram(educationalProgramComboBox.getSelectionModel().getSelectedItem());
        member.setHomeTelephone(homePhoneNumberTextField.getText());
        member.setWorkTelephone(workTelephoneTextField.getText());
        member.setAditionalEmail(aditionalEmailTextField.getText());
        member.setAppointment(appointmentTextField.getText());
        member.setAdmissionDate(DateFormatter.getDateFromDatepickerValue(admissionDateDatePicker.getValue()));
        member.setBirthDate(DateFormatter.getDateFromDatepickerValue(birthDateDatePicker.getValue()));
        member.setMaxStudyGrade(studyGradeComboBox.getValue());
        member.setBirthDate(DateFormatter.getDateFromDatepickerValue(birthDateDatePicker.getValue()));
        member.setStudyArea(studyAreaTextField.getText());
        member.setId(memberSelected.getId());
        try {
            if( new MemberDAO().updateMember(member) ) {
                memberSelected = member;
                systemLabel.setText("??Se ha actualizado de manera exitosa!");
                disableMemberInput(true);
                pause();
            } else {
                systemLabel.setText("??No se ha podido modificar!");
            }
        } catch (SQLException sqlException) {
            Logger.getLogger(ModifyMemberController.class.getName()).log(Level.SEVERE, null, sqlException);
            AlertController.getInstance().determinateAlertBySQLException(sqlException);
        }
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
        educationalProgramComboBox.getSelectionModel().select(memberSelected.getEducationalProgram());
        stateTextField.setText(memberSelected.getBirthState());
        birthDateDatePicker.setValue(DateFormatter.getLocalDateFromUtilDate(memberSelected.getBirthDate()));
        admissionDateDatePicker.setValue(DateFormatter.getLocalDateFromUtilDate(memberSelected.getAdmissionDate()));
        homePhoneNumberTextField.setText(memberSelected.getHomeTelephone());
        workTelephoneTextField.setText(memberSelected.getWorkTelephone());
        aditionalEmailTextField.setText(memberSelected.getAditionalEmail());
        appointmentTextField.setText(memberSelected.getAppointment());
        studyGradeComboBox.getSelectionModel().select(memberSelected.getMaxStudyGrade());
        studyAreaTextField.setText(memberSelected.getStudyArea());
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
        modifyButton.setDisable(state);
    }

    private void initValidatorToTextInput() {
        Function<Object, Boolean> validateBirthDate = a -> {
            LocalDate now = LocalDate.now();
            now = now.minusYears(Validator.MIN_YEARS_OLD);
            return now.compareTo((LocalDate) a) >= 0;
        };
        Function<Object, Boolean> validateAdmissionDate = a -> DateFormatter.compareActualDateToLocalDate((LocalDate) a) >= 0;
        addComponentToValidator(new ValidatorTextInputControl(nameTextField, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);
        addComponentToValidator(new ValidatorTextInputControl(paternalLastnameTextField, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);
        addComponentToValidator(new ValidatorTextInputControl(maternalLastnameTextField, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);
        addComponentToValidator(new ValidatorTextInputControl(nationalityTextField, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);
        addComponentToValidator(new ValidatorComboBoxBase(civilStatusComboBox, this), false);
        addComponentToValidator(new ValidatorTextInputControl(curpTextField, Validator.PATTERN_CURP, Validator.LENGTH_CURP, this), false);
        addComponentToValidator(new ValidatorTextInputControl(telephoneTextField, Validator.PATTERN_TELEPHONE, Validator.LENGTH_TELEPHONE, this), false);
        addComponentToValidator(new ValidatorTextInputControl(rfcTextField, Validator.PATTERN_RFC, Validator.LENGTH_RFC, this), false);
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
}
