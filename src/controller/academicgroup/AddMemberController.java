package controller.academicgroup;

import controller.AlertController;
import controller.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.dao.MiembroDAO;
import model.domain.*;
import utils.DateFormatter;
import utils.SQLStates;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AddMemberController extends Controller implements Initializable {
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getCivilStatesFromDatabase();
        getStudyGradesFromDatabase();
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
        ParticipationType participationType = (ParticipationType) typeParticipationToggleGroup.getSelectedToggle().getUserData();
        if(participationType == ParticipationType.INTEGRANT ) {
            addIntegrant();
        } else if( participationType == ParticipationType.RESPONSABLE){
            addResponsable();
        } else if(participationType == ParticipationType.COLABORATOR) {
            addColaborator();
        } else {
            // FUNCTIONALITY NOT SUPPORTED YET
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


}
