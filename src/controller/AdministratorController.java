package controller;

import assets.utils.Autentication;
import controller.academicgroup.ConsultAcademicGroupsController;
import controller.academicgroup.ConsultMembersController;

import controller.control.ValidatorController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import model.domain.Member;

import java.net.URL;
import java.util.ResourceBundle;

public class AdministratorController extends ValidatorController implements Initializable {
    @FXML private Label educationalProgramLabel;
    @FXML private Label emailLabel;
    @FXML private Label nameLabel;
    @FXML private Label nationalityLabel;
    @FXML private Label personalNumberLabel;
    @FXML private Label telephoneLabel;

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
        }
    }

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/AdministratorView.fxml"), this);
        stage.showAndWait();
    }

    @FXML
    void academicGroupOnAction(ActionEvent event) {
        ConsultAcademicGroupsController controller = new ConsultAcademicGroupsController();
        controller.showStage();
    }

    @FXML
    void membersOnAction(ActionEvent event) {
        ConsultMembersController controller = new ConsultMembersController();
        controller.showStage();
    }

}
