package controller.academicgroup;

import controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class ModifyMemberController extends Controller {

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/ModifyMemberView.fxml"), this);
        stage.showAndWait();
    }

    @FXML
    private TextField aditionalEmailTextField;

    @FXML
    private TextField appointmentTextField;

    @FXML
    private ComboBox<?> civilStatusComboBox;

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

    @FXML
    void ModifyMemberOnAction(ActionEvent event) {
        // DAO
    }

    @FXML
    void cancelButtonPressed(ActionEvent event) {
        stage.close();
    }

}
