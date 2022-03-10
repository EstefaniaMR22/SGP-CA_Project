package controller.academicgroup;

import com.sun.xml.internal.ws.wsdl.writer.document.Part;
import controller.AdministratorController;
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
import model.domain.*;

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

    }

    @FXML
    void cancelButtonPressed(ActionEvent event) {
        stage.close();
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
        stateTextField.setText(memberSelected.getState());
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

    private void updateMember(Member member) {
        if(member.getParticipationType() == ParticipationType.RESPONSABLE) {
            
        } else if(member.getParticipationType() == ParticipationType.INTEGRANT) {

        } else if(member.getParticipationType() == ParticipationType.COLABORATOR) {

        } else {

        }
    }

}
