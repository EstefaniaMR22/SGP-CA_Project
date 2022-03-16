package controller;

import controller.exceptions.AlertException;
import controller.exceptions.LimitReachedException;
import controller.exceptions.UserNotFoundException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import model.domain.ParticipationType;
import utils.Autentication;

import java.net.SocketException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController extends Controller {
    @FXML private TextField userTextField;
    @FXML private PasswordField passwordPasswordField;
    @FXML private Label systemLabel;
    @FXML private Button btIntegrant;

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/LoginView.fxml"), this);
        stage.show();
    }

    @FXML
    void signInOnAction(ActionEvent event) {
       if(validateInputs()) {
           if(login()) {
               openMemberWindow(Autentication.getInstance().getMember().getParticipationType());
           }
       }
    }

    @FXML
    void cancelOnAction(ActionEvent event) {
       stage.close();
    }

    @FXML
    void integranOnAction(ActionEvent event) {
        try{
            stage.close();
            IntegrantController integrantController = new IntegrantController();
            integrantController.showStage();

        }catch(Exception integrantOnActionExeception){
            Alert alertView;
            alertView = AlertException.builderAlert("Error FXML", "No se encuentra "
                    + "el FXML por: " + integrantOnActionExeception, Alert.AlertType.ERROR);
            alertView.showAndWait();
        }
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

    private boolean validateInputs() {
        boolean isValidInput = true;
        if(userTextField.getText().equals("") || passwordPasswordField.getText().equals("") ) {
            isValidInput = false;
            systemLabel.setText("¡Debes ingresar los datos en ambos campos de texto!");
        }
        return true;
    }

    private void openMemberWindow(ParticipationType participationType) {
        stage.hide();
        if(participationType == ParticipationType.INTEGRANT) {
            IntegrantController integrantController = new IntegrantController();
            integrantController.showStage();
        } else if(participationType == ParticipationType.RESPONSABLE) {
            ResponsableController responsableController = new ResponsableController();
            responsableController.showStage();
        } else if(participationType == ParticipationType.OTHER) {
            AdministratorController administratorController = new AdministratorController();
            administratorController.showStage();
        }
        stage.show();
    }

    private boolean login() {
        boolean isLogged = false;
        String emailInput = userTextField.getText();
        String passwordInput = passwordPasswordField.getText();
        try {
            isLogged = Autentication.getInstance().logIn(emailInput, passwordInput);
        } catch (UserNotFoundException | LimitReachedException e) {
            systemLabel.setText( e.getMessage() );
            Logger.getLogger( LoginController.class.getName() ).log(Level.FINE, null, e);
        } catch (SQLException | SocketException e) {
            Logger.getLogger( LoginController.class.getName() ).log(Level.WARNING, null, e);
        }
        return isLogged;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}

