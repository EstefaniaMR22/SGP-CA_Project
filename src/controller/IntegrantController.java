package controller;

import controller.control.AlertController;
import controller.control.Controller;
import controller.projects.ProjectsInvestigationController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class IntegrantController extends Controller {
    @FXML
    private Label lbBienvenida;
    @FXML
    private Button exitButton;
    @FXML
    private Button projectsInvestigationButton;

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/IntegrantView.fxml"), this);
        stage.showAndWait();
    }

    @FXML
    void consultProjectsInvestigationOnAction(ActionEvent event) {
        try{
            stage.close();
            ProjectsInvestigationController projectsInvestigationController = new ProjectsInvestigationController();
            projectsInvestigationController.showStage();

        }catch (Exception addProjectInvestigationException) {
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" No se pudo abrir la ventana " +
                    "ProjectsInvestigation. Causa: " + addProjectInvestigationException);

        }
    }

    @FXML
    void returnViewOnAction(ActionEvent actionEvent) {
        try{
            stage.close();
            LoginController viewReturn = new LoginController();
            viewReturn.showStage();

        }catch(Exception returnViewOnActionExeception){
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" Error en el metodo returnViewOnActionExeception:  " + returnViewOnActionExeception);

        }
    }

}
