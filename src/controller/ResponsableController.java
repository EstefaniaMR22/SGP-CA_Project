package controller;

import assets.utils.Autentication;
import controller.academicgroup.ConsultAcademicGroupsController;
import controller.control.AlertController;
import controller.control.Controller;
import controller.evidences.EvidencesController;
import controller.meets.MeetsController;
import controller.workplan.ConsultWorkplanController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import model.domain.Member;

import java.net.URL;
import java.util.ResourceBundle;

public class ResponsableController extends Controller implements Initializable {
    @FXML private Label educationalProgramLabel;
    @FXML private Label emailLabel;
    @FXML private Label nameLabel;
    @FXML private Label nationalityLabel;
    @FXML private Label personalNumberLabel;
    @FXML private Label telephoneLabel;

    private String idAcademicGroup;
    private int idMember;

    public ResponsableController(String idAcademicGroup) {
        this.idAcademicGroup = idAcademicGroup;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(Autentication.getInstance().getParticipation() != null ) {
            Member member = Autentication.getInstance().getParticipation().getMember();
            educationalProgramLabel.setText(member.getEducationalProgram());
            emailLabel.setText(member.getUvEmail());
            nameLabel.setText(member.getFullName());
            nationalityLabel.setText(member.getNationality());
            personalNumberLabel.setText(member.getPersonalNumber());
            telephoneLabel.setText(member.getTelephone());
            this.idMember = member.getId();
        }
    }

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/ResponsableView.fxml"), this);
        stage.showAndWait();
    }

    @FXML
    void workPlanOnAction(ActionEvent event) {
        new ConsultWorkplanController().showStage();
    }

    @FXML
    void meetsOnAction(ActionEvent event) {
        try {
            MeetsController meetsController = new MeetsController(idAcademicGroup, idMember);
            meetsController.showStage();

        } catch (Exception addProjectInvestigationException) {
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" No se pudo abrir la ventana " +
                    "Meets. Causa: " + addProjectInvestigationException);
        }
    }

    @FXML
    void evidencesOnAction(ActionEvent event) {
        try {
            System.out.println(idAcademicGroup + " " + idMember);
            EvidencesController evidencesController = new EvidencesController(idAcademicGroup, idMember);
            evidencesController.showStage();

        } catch (Exception addProjectInvestigationException) {
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" No se pudo abrir la ventana " +
                    "Evidences. Causa: " + addProjectInvestigationException);
        }
    }

    @FXML
    void academicGroupOnAction(ActionEvent event) {
        new ConsultAcademicGroupsController().showStage();
    }

}
