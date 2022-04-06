package controller;

import controller.academicgroup.AcademicGroupDetailsController;
import controller.academicgroup.AddAcademicGroupController;
import controller.listcell.AcademicGroupListCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.dao.AcademicGroupDAO;
import model.domain.AcademicGroup;
import model.domain.Member;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResponsableController extends Controller implements Initializable {
    @FXML private ListView<AcademicGroup> academicGroupProgramListView;
    @FXML private Label totalAcademicGroupProgramLabel;
    @FXML private TextField searchTextField;
    private FilteredList<AcademicGroup> filteredData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getAllAcademicGroupPrograms();
        initializeListView();
        updateTotalAcademicGroup();
        initializeFilterSearchInputMembers();
    }

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/ResponsableController.fxml"), this);
        stage.showAndWait();
    }

    @FXML
    void closeOnAction(ActionEvent event) {
        stage.close();
    }

    @FXML
    void getAcademicGroupProgramDetailsOnAction(ActionEvent event) {
        String lastText = searchTextField.getText();
        AcademicGroup selected = academicGroupProgramListView.getSelectionModel().getSelectedItem();
        searchTextField.setText("");
        academicGroupProgramListView.getSelectionModel().select(selected);
        int indexOf = academicGroupProgramListView.getSelectionModel().getSelectedIndex();
        searchTextField.setText(lastText);
        if (selected != null) {
            AcademicGroupDetailsController academicGroupProgramDetailsController = new AcademicGroupDetailsController(selected);
            academicGroupProgramDetailsController.showStage();
            if(!academicGroupProgramDetailsController.getAcademicGroupProgramSelected().equals(selected)) {
                searchTextField.setText("");
                ObservableList<AcademicGroup> newList = FXCollections.observableArrayList(new ArrayList<>());
                newList.addAll(academicGroupProgramListView.getItems());
                newList.remove(selected);
                newList.add(indexOf, academicGroupProgramDetailsController.getAcademicGroupProgramSelected());
                setNewItemsToListview(newList);
                searchTextField.setText(lastText);
            }
        }
    }

    @FXML
    void registerAcademicGroupProgramOnAction(ActionEvent event) {
        AddAcademicGroupController addAcademicGroupProgramController = new AddAcademicGroupController();
        addAcademicGroupProgramController.showStage();
        if (addAcademicGroupProgramController.getAcademicGroupProgramRegistered() != null) {
            String lastText = searchTextField.getText();
            searchTextField.setText("");
            ObservableList<AcademicGroup> newList = FXCollections.observableArrayList(new ArrayList<>());
            newList.addAll(academicGroupProgramListView.getItems());
            newList.add(addAcademicGroupProgramController.getAcademicGroupProgramRegistered());
            setNewItemsToListview(newList);
            searchTextField.setText(lastText);
        }
    }

    private void initializeListView() {
        academicGroupProgramListView.setCellFactory(item -> new AcademicGroupListCell());
    }

    private void updateTotalAcademicGroup() {
        totalAcademicGroupProgramLabel.setText(String.valueOf(academicGroupProgramListView.getItems().size()));
    }

    private void getAllAcademicGroupPrograms() {
        try {
            ObservableList<AcademicGroup> academicGroupProgramObservableList = FXCollections.observableArrayList(new AcademicGroupDAO().getAllAcademicGroup());
            academicGroupProgramListView.setItems(academicGroupProgramObservableList);
        } catch (SQLException sqlException) {
            Logger.getLogger(ResponsableController.class.getName()).log(Level.SEVERE, null, sqlException);
        }
    }

    private void setNewItemsToListview(ObservableList<AcademicGroup> newList) {
        searchTextField.setText("");
        academicGroupProgramListView.setItems(newList);
        initializeFilterSearchInputMembers();
    }

    private void initializeFilterSearchInputMembers() {
        filteredData = new FilteredList<>(academicGroupProgramListView.getItems(), p -> true);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(object -> {
                if (newValue == null | newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (String.valueOf(object.getName()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(object.getId()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
            SortedList<AcademicGroup> sortedList = new SortedList<>(filteredData);
            academicGroupProgramListView.setItems(sortedList);
        });
        updateTotalAcademicGroup();
    }
}
