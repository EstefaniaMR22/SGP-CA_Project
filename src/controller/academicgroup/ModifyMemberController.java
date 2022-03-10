package controller.academicgroup;

import controller.AdministratorController;
import controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.dao.MiembroDAO;
import model.domain.CivilStatus;
import model.domain.Member;
import model.domain.ParticipationType;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModifyMemberController extends Controller implements Initializable {
    private Member memberSelected;

    public ModifyMemberController(Member member) {
        this.memberSelected = member;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/ModifyMemberView.fxml"), this);
        stage.showAndWait();
    }
    @FXML private TextField aditionalEmailTextField;
    @FXML private TextField appointmentTextField;
    @FXML private ComboBox<CivilStatus> civilStatusComboBox;
    @FXML private TextField curpTextField;
    @FXML private TextField educationalProgramTextField;
    @FXML private TextField homeTextField;
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

    @FXML
    void ModifyMemberOnAction(ActionEvent event) {

    }

    @FXML
    void cancelButtonPressed(ActionEvent event) {
        stage.close();
    }


}
