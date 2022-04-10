package controller;

import controller.academicgroup.ConsultAcademicGroupsController;
import controller.control.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ResponsableController extends Controller {
    @FXML private Label educationalProgramLabel;
    @FXML private Label emailLabel;
    @FXML private Label nameLabel;
    @FXML private Label nationalityLabel;
    @FXML private Label personalNumberLabel;
    @FXML private Label telephoneLabel;

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/ResponsableView.fxml"), this);
        stage.showAndWait();
    }

    @FXML
    void academicGroupOnAction(ActionEvent event) {
        new ConsultAcademicGroupsController().showStage();
    }

}
