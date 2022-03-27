package controller;

import controller.exceptions.LimitReachedException;
import controller.exceptions.UserNotFoundException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import model.dao.AcademicGroupDAO;
import model.domain.AcademicGroup;
import model.domain.ParticipationType;
import utils.Autentication;
import java.net.SocketException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController extends Controller implements Initializable {
    @FXML private TextField userTextField;
    @FXML private PasswordField passwordPasswordField;
    @FXML private Label systemLabel;
    @FXML private ListView<AcademicGroup> academicGroupProgramListView;
    @FXML private Button cancelButton;
    @FXML private Button loginButton;
    @FXML private VBox formVBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getAllAcademicGroupPrograms();
    }

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/LoginView.fxml"), this);
        stage.show();
    }

    @FXML
    void signInOnAction(ActionEvent event) {
       if(validateInputs()) {
           if(login()) {
               openMemberWindow(Autentication.getInstance().getParticipation().getParticipationType());
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
        }catch (Exception addProjectInvestigationException) {
            AlertController alertView = new AlertController();
            alertView.showActionFailedAlert(" No se pudo abrir la ventana " +
                    "ProyectsInvestigation. Causa: " + addProjectInvestigationException);
        }
    }

    @FXML
    void responsableOnAction(ActionEvent event) {
        stage.hide();
        ResponsableController sessionController = new ResponsableController();
        sessionController.showStage();
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
        if (participationType == ParticipationType.INTEGRANT) {
            IntegrantController integrantController = new IntegrantController();
            integrantController.showStage();
        } else if (participationType == ParticipationType.RESPONSABLE) {
            ResponsableController responsableController = new ResponsableController();
            responsableController.showStage();
        } else if(participationType == ParticipationType.COLABORATOR ) {
            systemLabel.setText("¡Estás asignado para este Cuerpo Académico como Colaborador!");
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
        AcademicGroup academicGroupSelected = academicGroupProgramListView.getSelectionModel().getSelectedItem();
        if(academicGroupSelected != null ) {
            try {
                isLogged = Autentication.getInstance().logIn(emailInput, passwordInput, academicGroupSelected.getId());
            } catch (UserNotFoundException | LimitReachedException e) {
                systemLabel.setText( e.getMessage() );
                Logger.getLogger( LoginController.class.getName() ).log(Level.FINE, null, e);
            } catch (SQLException | SocketException e) {
                Logger.getLogger( LoginController.class.getName() ).log(Level.WARNING, null, e);
            }
        } else {
            try {
                isLogged = Autentication.getInstance().logIn(emailInput, passwordInput);
            } catch(UserNotFoundException | LimitReachedException e ) {
                systemLabel.setText( e.getMessage() );
                Logger.getLogger( LoginController.class.getName() ).log(Level.FINE, null, e);
            } catch (SQLException | SocketException e) {
                Logger.getLogger( LoginController.class.getName() ).log(Level.WARNING, null, e);
            }
        }
        return isLogged;
    }

    private void getAllAcademicGroupPrograms() {
        try{
            ObservableList<AcademicGroup> academicGroupProgramObservableList = FXCollections.observableArrayList(new AcademicGroupDAO().getAllAcademicGroupPrograms());
            academicGroupProgramListView.setItems(academicGroupProgramObservableList);
        } catch(SQLException sqlException) {
            Logger.getLogger(ResponsableController.class.getName()).log(Level.SEVERE, null, sqlException);
        }
    }

}

