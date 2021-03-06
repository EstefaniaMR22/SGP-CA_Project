package controller;

import controller.control.AlertController;
import controller.control.Controller;
import controller.control.exceptions.LimitReachedException;
import controller.control.exceptions.UserNotFoundException;
import controller.control.listcell.AcademicGroupListCell;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.dao.AcademicGroupDAO;
import model.domain.AcademicGroup;
import model.domain.ParticipationType;
import assets.utils.Autentication;
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
    @FXML private Label academicGroupSelectedLabel;
    @FXML private VBox academicSelectionVBox;
    @FXML private HBox loginVBox;
    @FXML private Label academicGroupIDLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginVBox.setVisible(false);
        academicSelectionVBox.setVisible(true);
        initializeListView();
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
       loginVBox.setVisible(false);
       academicSelectionVBox.setVisible(true);
       academicGroupProgramListView.getSelectionModel().clearSelection();
       systemLabel.setText("");
    }


    @FXML
    void responsableOnAction(ActionEvent event) {
        stage.hide();
        AcademicGroup academicGroupSelected = academicGroupProgramListView.getSelectionModel().getSelectedItem();
        ResponsableController sessionController = new ResponsableController(academicGroupSelected.getId());
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

    @FXML
    void administrationOnAction(ActionEvent event) {
        clearInputs();
        academicGroupSelectedLabel.setText("");
        academicGroupIDLabel.setText("");
        academicGroupProgramListView.getSelectionModel().clearSelection();
        academicSelectionVBox.setVisible(false);
        loginVBox.setVisible(true);
    }

    private boolean validateInputs() {
        boolean isValidInput = true;
        if(userTextField.getText().equals("") || passwordPasswordField.getText().equals("") ) {
            isValidInput = false;
            systemLabel.setText("??Debes ingresar los datos en ambos campos de texto!");
        }
        return true;
    }

    private void openMemberWindow(ParticipationType participationType) {
        stage.hide();
        if (participationType == ParticipationType.INTEGRANT) {
            AcademicGroup academicGroupSelected = academicGroupProgramListView.getSelectionModel().getSelectedItem();
            IntegrantController integrantController = new IntegrantController(academicGroupSelected.getId());
            integrantController.showStage();
        } else if (participationType == ParticipationType.RESPONSABLE) {
            AcademicGroup academicGroupSelected = academicGroupProgramListView.getSelectionModel().getSelectedItem();
            ResponsableController responsableController = new ResponsableController(academicGroupSelected.getId());
            responsableController.showStage();
        } else if(participationType == ParticipationType.COLABORATOR ) {
            systemLabel.setText("??Est??s asignado para este Cuerpo Acad??mico como Colaborador!");
        } else if(participationType == ParticipationType.OTHER) {
            AdministratorController administratorController = new AdministratorController();
            administratorController.showStage();
        }
        stage.show();
    }

    private boolean login() {
        boolean isLogged = false;
        systemLabel.setText("");
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
                AlertController.getInstance().determinateAlertBySQLException(e);
            }
        }
        return isLogged;
    }

    private void getAllAcademicGroupPrograms() {
        try{
            ObservableList<AcademicGroup> academicGroupProgramObservableList = FXCollections.observableArrayList(new AcademicGroupDAO().getAllAcademicGroup());
            academicGroupProgramListView.setItems(academicGroupProgramObservableList);
        } catch(SQLException sqlException) {
            AlertController.getInstance().determinateAlertBySQLException(sqlException);
        }
    }

    private void initializeListView() {
        academicGroupProgramListView.setCellFactory( item -> new AcademicGroupListCell());
        academicGroupProgramListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AcademicGroup>() {
            @Override
            public void changed(ObservableValue<? extends AcademicGroup> observable, AcademicGroup oldValue, AcademicGroup newValue) {
                if(newValue != null ) {
                    clearInputs();
                    academicSelectionVBox.setVisible(false);
                    loginVBox.setVisible(true);
                    academicGroupSelectedLabel.setText(newValue.getName());
                    academicGroupIDLabel.setText(newValue.getId());
                }
            }
        });
    }

    private void clearInputs() {
        userTextField.setText("");
        passwordPasswordField.setText("");
    }

}

