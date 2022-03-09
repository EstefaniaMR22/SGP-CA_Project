package controller;

import controller.academicgroup.AddMemberController;
import controller.academicgroup.MemberDetailsController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class AdministratorController extends Controller {
    @FXML
    private ListView<?> membersListView;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchTextField;

    @FXML
    private Label totalIntegrantsTextField;

    @FXML
    private Label totalMembersTextField;

    @FXML
    private Label totalResponsablesTextField;

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/AdministratorView.fxml"), this);
        stage.showAndWait();
    }

    @FXML
    void closeOnAction(ActionEvent event) {
        stage.close();
    }

    @FXML
    void addMemberOnAction(ActionEvent event) {
        AddMemberController addMemberController = new AddMemberController();
        addMemberController.showStage();
    }

    @FXML
    void lookMemberDetailsOnAction(ActionEvent event) {
        MemberDetailsController memberDetailsController = new MemberDetailsController();
        memberDetailsController.showStage();
    }

    @FXML
    void removeMemberOnAction(ActionEvent event) {

    }


}
