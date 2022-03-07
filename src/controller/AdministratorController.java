package controller;

import controller.general.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class AdministratorController extends Controller {

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/AdministratorView.fxml"), this);
        stage.showAndWait();
    }

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

    @FXML
    void addMemberOnAction(ActionEvent event) {

    }

    @FXML
    void closeOnAction(ActionEvent event) {

    }

    @FXML
    void lookMemberDetailsOnAction(ActionEvent event) {

    }

    @FXML
    void removeMemberOnAction(ActionEvent event) {

    }


}
