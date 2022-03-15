package controller;


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

        stage.showAndWait();
    }

    @FXML
    public void consultProjectsInvestigation(ActionEvent event) {
        stage.hide();
        ProjectsInvestigationController projectsView = new ProjectsInvestigationController();
        projectsView.showStage();
        stage.show();
        }

    @FXML
    public void returnView(ActionEvent actionEvent) {

    }

}
