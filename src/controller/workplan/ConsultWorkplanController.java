package controller.workplan;

import assets.utils.Autentication;
import controller.academicgroup.ConsultAcademicGroupsController;
import controller.control.AlertController;
import controller.control.Controller;
import controller.control.listcell.WorkplanListCell;
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
import model.dao.WorkplanDAO;
import model.domain.Workplan;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsultWorkplanController extends Controller implements Initializable {
    @FXML private Label academicGroupLabel;
    @FXML private TextField searchTextField;
    @FXML private Label totalWorkplanLabel;
    @FXML private ListView<Workplan> workplanListView;
    private FilteredList<Workplan> filteredData;

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/ConsultWorkplanView.fxml"), this);
        stage.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getAllAcademicGroupPrograms();
        initializeListView();
        initializeFilterSearchInput();
        academicGroupLabel.setText(Autentication.getInstance().getIdAcademicGroup());
    }

    @FXML
    void registerOnAction(ActionEvent event) {
        new AddWorkplanController().showStage();
    }

    @FXML
    void closeOnAction(ActionEvent event) {
        stage.close();
    }

    @FXML
    void consultWPOnAction(ActionEvent event) {
        String lastText = searchTextField.getText();
        Workplan selected = workplanListView.getSelectionModel().getSelectedItem();
        searchTextField.setText("");
        workplanListView.getSelectionModel().select(selected);
        int indexOf = workplanListView.getSelectionModel().getSelectedIndex();
        searchTextField.setText(lastText);
        if(selected != null ) {
            WorkplanDetailsController workplanDetailsController = new WorkplanDetailsController(selected);
            workplanDetailsController.showStage();
            if(!workplanDetailsController.getWorkplanSelected().equals(selected)) {
                searchTextField.setText("");
                ObservableList<Workplan> newList = FXCollections.observableArrayList(new ArrayList<>());
                newList.addAll(workplanListView.getItems());
                newList.remove(selected);
                newList.add(indexOf, workplanDetailsController.getWorkplanSelected());
                setNewItemsToListview(newList);
                searchTextField.setText(lastText);
            }
        }
    }

    private void initializeListView() {
        workplanListView.setCellFactory(item -> new WorkplanListCell());
    }

    private void getAllAcademicGroupPrograms() {
        try {
            ObservableList<Workplan> workplanObservableList = FXCollections.observableArrayList(new WorkplanDAO().getAllWorkplan(Autentication.getInstance().getIdAcademicGroup()));
            workplanListView.setItems(workplanObservableList);
        } catch (SQLException sqlException) {
            Logger.getLogger(ConsultAcademicGroupsController.class.getName()).log(Level.SEVERE, null, sqlException);
            AlertController.getInstance().determinateAlertBySQLException(sqlException);
        }
    }

    private void setNewItemsToListview(ObservableList<Workplan> newList) {
        searchTextField.setText("");
        workplanListView.setItems(newList);
        initializeFilterSearchInput();
    }

    private void initializeFilterSearchInput() {
        filteredData = new FilteredList<>(workplanListView.getItems(), p -> true);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(object -> {
                if (newValue == null | newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (String.valueOf(object.getId()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
            SortedList<Workplan> sortedList = new SortedList<>(filteredData);
            workplanListView.setItems(sortedList);
        });
    }

}
