package controller.academicgroup;

import assets.utils.SQLStates;
import controller.control.AlertController;
import controller.control.Controller;
import controller.control.listcell.AdministratorMemberListCell;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.dao.MemberDAO;
import model.domain.Member;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsultMembersController extends Controller implements Initializable {
    private FilteredList<Member> filteredData;
    @FXML private ListView<Member> membersListView;
    @FXML private TextField searchTextField;
    @FXML private Label totalMembersLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeCellFactoryListView();
        getMembersFromDatabase();
        initializeCountMembersListViewListener();
        initializeFilterSearchInputMembers();
    }

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/ConsultMemberRegisteredView.fxml"), this);
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
            String lastText = searchTextField.getText();
            searchTextField.setText("");
            ObservableList<Member> newList = FXCollections.observableArrayList(new ArrayList<>());
            newList.addAll(membersListView.getItems());
            newList.add(addMemberController.getRegisteredMember());
            setNewItemsToListview(newList);
            searchTextField.setText(lastText);
        }
    }

    @FXML
    void lookMemberDetailsOnAction(ActionEvent event) {
        Member selected = membersListView.getSelectionModel().getSelectedItem();
        String lastText = searchTextField.getText();
        searchTextField.setText("");
        membersListView.getSelectionModel().select(selected);
        int indexOf = membersListView.getSelectionModel().getSelectedIndex();
        searchTextField.setText(lastText);
        if(selected != null) {
            MemberDetailsController memberDetailsController = new MemberDetailsController(selected);
            memberDetailsController.showStage();
            if(!memberDetailsController.getMemberSelected().equals(selected)) {
                searchTextField.setText("");
                ObservableList<Member> newList = FXCollections.observableArrayList(new ArrayList<>());
                newList.addAll(membersListView.getItems());
                newList.remove(selected);
                newList.add(indexOf, memberDetailsController.getMemberSelected());
                setNewItemsToListview(newList);
                searchTextField.setText(lastText);
            }
        }
    }

    @FXML
    void removeMemberOnAction(ActionEvent event) {
        Member memberSelected = membersListView.getSelectionModel().getSelectedItem();
        if(memberSelected != null ) {
           if(AlertController.getInstance().showConfirmationAlert()) {
               try {
                   if(new MemberDAO().removeMember(memberSelected.getId())) {
                       String lastText = searchTextField.getText();
                       searchTextField.setText("");
                       ObservableList<Member> newList = FXCollections.observableArrayList(new ArrayList<>());
                       newList.addAll(membersListView.getItems());
                       newList.remove(memberSelected);
                       setNewItemsToListview(newList);
                       searchTextField.setText(lastText);
                   }
               } catch (SQLException sqlException) {
                   Logger.getLogger(ConsultMembersController.class.getName()).log(Level.WARNING, null, sqlException);
                   if(sqlException.getSQLState().equals(SQLStates.SQL_INTEGRITY_CONSTRAINT_VIOLATION.getSqlState())) {
                        AlertController.getInstance().showActionFailedAlert("El miembro a eliminar ya ha tenido actividad en el sistema.");
                   }
               }
           }
        }
    }

    private void getMembersFromDatabase() {
        List<Member> memberList = null;
        try {
            memberList = new MemberDAO().getAllMembers();
            ObservableList<Member> observableList = FXCollections.observableArrayList(memberList);
            countIntegrants(observableList);
            membersListView.setItems(observableList);
        } catch(SQLException sqlException) {
            Logger.getLogger(ConsultMembersController.class.getName()).log(Level.SEVERE, null, sqlException);
        };
    }

    private void initializeCountMembersListViewListener() {
        membersListView.getItems().addListener((ListChangeListener<Member>) c -> countIntegrants(membersListView.getItems()));
    }

    private void countIntegrants(ObservableList<Member> members) {
        totalMembersLabel.setText(String.valueOf(members.size()));
    }

    private void initializeCellFactoryListView() {
        membersListView.setCellFactory( item -> new AdministratorMemberListCell());
    }

    private void setNewItemsToListview(ObservableList<Member> newList) {
        searchTextField.setText("");
        membersListView.setItems(newList);
        initializeFilterSearchInputMembers();
    }

    private void initializeFilterSearchInputMembers() {
        filteredData = new FilteredList<>(membersListView.getItems(), p -> true);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(object -> {
                if (newValue == null | newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (String.valueOf(object.getFullName()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(object.getPersonalNumber()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
            SortedList<Member> sortedList = new SortedList<>(filteredData);
            membersListView.setItems(sortedList);
        });
        initializeCountMembersListViewListener();
    }
}
