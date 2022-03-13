package controller.academicgroup;

import controller.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Modality;
import model.dao.MiembroDAO;
import model.domain.CivilStatus;
import model.domain.Integrant;
import model.domain.Member;
import model.domain.ParticipationType;
import model.domain.Responsable;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModifyMemberController extends Controller implements Initializable {
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

    public ModifyMemberController(Member member) {
        this.memberSelected = member;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

        } else {

        }
    }
}
