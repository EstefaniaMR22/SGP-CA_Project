package controller.general;

import controller.AdministratorController;
import controller.IntegrantController;
import controller.ResponsableController;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import model.domain.Integrant;
import model.dataaccess.IntegrantDAO;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class LoginController extends Controller {
    @FXML
    private TextField userTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label systemLabel;
    private final IntegrantDAO INTEGRANT_DAO = new IntegrantDAO();
    private Integrant integrantLogger;


    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/Login.fxml"), this);
        stage.show();
    }

    @FXML
    private void signInOnAction(MouseEvent event) {

    }

    @FXML
    void cancelOnAction(ActionEvent event) {
       stage.close();
    }

    private boolean validateInputs() {
        return false;
    }

    private boolean checkLoginInDatabase() {
        return false;
    }


    @FXML
    void integranOnAction(ActionEvent event) {
        stage.hide();
        IntegrantController integrantController = new IntegrantController();
        integrantController.showStage();
        stage.show();
    }

    @FXML
    void responsableOnAction(ActionEvent event) {
        stage.hide();
        ResponsableController responsableController = new ResponsableController();
        responsableController.showStage();
        stage.show();
    }

    @FXML
    void adminOnAction(ActionEvent event) {
        stage.hide();
        AdministratorController administratorController = new AdministratorController();
        administratorController.showStage();
        stage.show();
    }


}

