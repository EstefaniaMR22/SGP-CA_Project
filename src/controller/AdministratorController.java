package controller;

import controller.academicgroup.AddMemberController;
import controller.academicgroup.MemberDetailsController;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.dao.MiembroDAO;
import model.domain.Member;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdministratorController extends Controller implements Initializable {
    @FXML private ListView<Member> membersListView;
    @FXML private Button searchButton;
    @FXML private TextField searchTextField;
    @FXML private Label totalMembersLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getMembersFromDatabase();
        initializeListViewListener();
    }

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
        if(addMemberController.getRegisteredMember() != null ) {
            membersListView.getItems().add(addMemberController.getRegisteredMember());
        }
    }

    @FXML
    void lookMemberDetailsOnAction(ActionEvent event) {
        Member selected = membersListView.getSelectionModel().getSelectedItem();
        if(selected != null) {
            MemberDetailsController memberDetailsController = new MemberDetailsController(selected);
            memberDetailsController.showStage();
        }
    }

    @FXML
    void removeMemberOnAction(ActionEvent event) {
        Member memberSelected = membersListView.getSelectionModel().getSelectedItem();
        if(memberSelected != null ) {
           if(AlertController.showConfirmationAlert()) {
               try {
                   if(new MiembroDAO().removeMember(memberSelected.getId())) {
                       membersListView.getItems().remove(memberSelected);
                   }
               } catch (SQLException sqlException) {
                   Logger.getLogger(AdministratorController.class.getName()).log(Level.SEVERE, null, sqlException);
               }
           }
        }
    }

    private void getMembersFromDatabase() {
        List<Member> memberList = null;
        try {
            memberList = new MiembroDAO().getAllMembers();
            ObservableList<Member> observableList = FXCollections.observableArrayList(memberList);
            countIntegrants(observableList);
            membersListView.setItems(observableList);
        } catch(SQLException sqlException) {
            Logger.getLogger(AdministratorController.class.getName()).log(Level.SEVERE, null, sqlException);
        };
    }

    private void initializeListViewListener() {
        membersListView.getItems().addListener((ListChangeListener<Member>) c -> countIntegrants(membersListView.getItems()));
    }

    private void countIntegrants(ObservableList<Member> members) {
        totalMembersLabel.setText(String.valueOf(members.size()));
    }

}
