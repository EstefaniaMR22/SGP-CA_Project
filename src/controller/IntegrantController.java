package controller;

import controller.control.AlertController;
import controller.control.Controller;
import controller.meets.MeetsController;
import controller.projects.ProjectsInvestigationController;
import controller.receptionalWorks.ReceptionalWorksController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class IntegrantController extends Controller {
    @FXML
    private Label lbBienvenida;
    @FXML
    private Button exitButton;
    @FXML
    private Button projectsInvestigationButton;
    @FXML
    private Button receptionalWorksButton;
    @FXML
    private Button meetsButton;

    String idAcademicGroup;

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/IntegrantView.fxml"), this);
        stage.show();
    }

    public IntegrantController(String idAcademicGroup){
        this.idAcademicGroup = idAcademicGroup;
    }

    @FXML
    void consultProjectsInvestigationOnAction(ActionEvent event) {
        try{
            stage.close();
            ProjectsInvestigationController projectsInvestigationController = new ProjectsInvestigationController(idAcademicGroup);
            projectsInvestigationController.showStage();

        }catch (Exception addProjectInvestigationException) {
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" No se pudo abrir la ventana " +
                    "ProjectsInvestigation. Causa: " + addProjectInvestigationException);

        }
    }

    @FXML
    void consultReceptionalWorksOnAction(ActionEvent event) {
        try{
            stage.close();
            ReceptionalWorksController receptionalWorksController = new ReceptionalWorksController(idAcademicGroup);
            receptionalWorksController.showStage();

        }catch (Exception addProjectInvestigationException) {
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" No se pudo abrir la ventana " +
                    "ReceptionalWorks. Causa: " + addProjectInvestigationException);

        }

    }

    @FXML
    void consultMeetOnAction(ActionEvent event) {
        try{
            stage.close();
            MeetsController meetsController = new MeetsController(idAcademicGroup);
            meetsController.showStage();

        }catch (Exception addProjectInvestigationException) {
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" No se pudo abrir la ventana " +
                    "Meets. Causa: " + addProjectInvestigationException);

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
