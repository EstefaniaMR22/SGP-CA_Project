package controller.academicgroup;

import controller.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.dao.MiembroDAO;
import model.domain.CivilStatus;
import model.domain.Integrant;
import model.domain.Responsable;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AddMemberController extends Controller implements Initializable {
    @FXML
    private TextField aditionalEmailTextField;

    @FXML
    private TextField appointmentTextField;

    @FXML
    private ComboBox<CivilStatus> civilStatusComboBox;

    @FXML
    private TextField curpTextField;

    @FXML
    private TextField educationalProgramTextField;

    @FXML
    private TextField homeTextField;

    @FXML
    private TextField maternalLastnameTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField nationalityTextField;

    @FXML
    private TextField paternalLastnameTextField;

    @FXML
    private TextField personalNumberTextField;

    @FXML
    private TextField rfcTextField;

    @FXML
    private TextField stateTextField;

    @FXML
    private TextField telephoneTextField;

    @FXML
    private TextField uvEmailTextField;

    @FXML
    private TextField workTelephoneTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getCivilStatesFromDatabase();
    }

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/AddMemberView.fxml"), this);
        stage.showAndWait();
    }

    @FXML
    void cancelButtonPressed(ActionEvent event) {
        stage.close();
    }

    @FXML
    void AddMemberOnAction(ActionEvent event) {
        // CHECK USER TYPE
    }


    private void addIntengrat() {
        Responsable integrante = new Responsable();
        integrante.setName(nameTextField.getText());
        integrante.setPaternalLastname(paternalLastnameTextField.getText());
        integrante.setMaternalLastname(maternalLastnameTextField.getText());
        integrante.setNationality(nationalityTextField.getText());
        integrante.setCivilStatus(civilStatusComboBox.getValue());
        integrante.setCurp(curpTextField.getText());
        integrante.setTelephone(telephoneTextField.getText());
        integrante.setRfc(rfcTextField.getText());
        integrante.setState(stateTextField.getText());
        integrante.setPersonalNumber(personalNumberTextField.getText());
        integrante.setUvEmail(uvEmailTextField.getText());
        integrante.setEducationalProgram(educationalProgramTextField.getText());
        integrante.setHomeTelephone(homeTextField.getText());
        integrante.setWorkTelephone(workTelephoneTextField.getText());
        integrante.setAditionalEmail(aditionalEmailTextField.getText());
        integrante.setAppointment(appointmentTextField.getText());
        try {
            integrante.setId(new MiembroDAO().addMember(integrante, "hola"));
        } catch(SQLException sqlException) {
            System.out.println(sqlException.getMessage());
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
        responsable.setState(stateTextField.getText());
        responsable.setPersonalNumber(personalNumberTextField.getText());
        responsable.setUvEmail(uvEmailTextField.getText());
        responsable.setEducationalProgram(educationalProgramTextField.getText());
        responsable.setHomeTelephone(homeTextField.getText());
        responsable.setWorkTelephone(workTelephoneTextField.getText());
        responsable.setAditionalEmail(aditionalEmailTextField.getText());
        responsable.setAppointment(appointmentTextField.getText());
        try {
            responsable.setId(new MiembroDAO().addMember(responsable, "hola"));
        } catch(SQLException sqlException) {
            Logger.getLogger(AddMemberController.class.getName()).log(Level.SEVERE, null, sqlException);
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

}
