package controller;

import assets.utils.Autentication;
import controller.control.AlertController;
import controller.control.Controller;
import controller.meets.MeetsController;
import controller.projects.ProjectsInvestigationController;
import controller.receptionalWorks.ReceptionalWorksController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.domain.Member;

import java.net.URL;
import java.util.ResourceBundle;

public class IntegrantController extends Controller implements Initializable {
    @FXML private Button exitButton;
    @FXML private Button projectsInvestigationButton;
    @FXML private Button receptionalWorksButton;
    @FXML private Button meetsButton;
    @FXML private Label educationalProgramLabel;
    @FXML private Label emailLabel;
    @FXML private Label nameLabel;
    @FXML private Label nationalityLabel;
    @FXML private Label personalNumberLabel;
    @FXML private Label telephoneLabel;

    String idAcademicGroup;


    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/IntegrantView.fxml"), this);
        stage.showAndWait();
    }

    public IntegrantController(String idAcademicGroup) {
        this.idAcademicGroup = idAcademicGroup;
    }

    @FXML
    void consultProjectsInvestigationOnAction(ActionEvent event) {
        try {
            ProjectsInvestigationController projectsInvestigationController = new ProjectsInvestigationController(idAcademicGroup);
            projectsInvestigationController.showStage();

        } catch (Exception addProjectInvestigationException) {
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" No se pudo abrir la ventana " +
                    "ProjectsInvestigation. Causa: " + addProjectInvestigationException);

        }
    }

    @FXML
    void consultReceptionalWorksOnAction(ActionEvent event) {
        try {
            ReceptionalWorksController receptionalWorksController = new ReceptionalWorksController(idAcademicGroup);
            receptionalWorksController.showStage();

        } catch (Exception addProjectInvestigationException) {
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" No se pudo abrir la ventana " +
                    "ReceptionalWorks. Causa: " + addProjectInvestigationException);
        }

    }

    @FXML
    void consultMeetOnAction(ActionEvent event) {
        try {
            MeetsController meetsController = new MeetsController(idAcademicGroup);
            meetsController.showStage();

        } catch (Exception addProjectInvestigationException) {
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" No se pudo abrir la ventana " +
                    "Meets. Causa: " + addProjectInvestigationException);
        }

    }

    @FXML
    void returnViewOnAction(ActionEvent actionEvent) {
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (Autentication.getInstance().getParticipation() != null) {
            Member member = Autentication.getInstance().getParticipation().getMember();
            educationalProgramLabel.setText(member.getEducationalProgram());
            emailLabel.setText(member.getUvEmail());
            nameLabel.setText(member.getFullName());
            nationalityLabel.setText(member.getNationality());
            personalNumberLabel.setText(member.getPersonalNumber());
            telephoneLabel.setText(member.getTelephone());
        }
    }
}
