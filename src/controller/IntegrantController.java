package controller;


import controller.exceptions.AlertException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class IntegrantController extends Controller {
    @FXML
    private Label lbBienvenida;
    @FXML
    private Button btExit;
    @FXML
    private Button btProjectsInvestigation;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/IntegrantView.fxml"), this);

        stage.show();
    }

    @FXML
    void consultProjectsInvestigation(ActionEvent event) {
        try{
            stage.close();
            ProjectsInvestigationController projectsInvestigationController = new ProjectsInvestigationController();
            projectsInvestigationController.showStage();

        }catch(Exception integrantOnActionExeception){
            Alert alertView;
            alertView = AlertException.builderAlert("Error FXML", "No se encuentra "
                    + "el FXML por: " + integrantOnActionExeception, Alert.AlertType.ERROR);
            alertView.showAndWait();
        }
    }

    @FXML
    public void returnView(ActionEvent actionEvent) {
        try{
            stage.close();
            LoginController viewReturn = new LoginController();
            viewReturn.showStage();

        }catch(Exception integrantOnActionExeception){
            Alert alertView;
            alertView = AlertException.builderAlert("Error FXML", "No se encuentra "
                    + "el FXML por: " + integrantOnActionExeception, Alert.AlertType.ERROR);
            alertView.showAndWait();
        }
    }

}
