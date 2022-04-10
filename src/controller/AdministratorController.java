package controller;

import controller.academicgroup.ConsultAcademicGroupsController;
import controller.academicgroup.ConsultMembersController;
import controller.control.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AdministratorController extends Controller {
    @FXML private Label educationalProgramLabel;
    @FXML private Label emailLabel;
    @FXML private Label nameLabel;
    @FXML private Label nationalityLabel;
    @FXML private Label personalNumberLabel;
    @FXML private Label telephoneLabel;

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
