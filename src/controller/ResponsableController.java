package controller;

import controller.academicgroup.AcademicGroupProgramDetailsController;
import controller.academicgroup.AddAcademicGroupController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import model.dao.AcademicGroupDAO;
import model.domain.AcademicGroup;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResponsableController extends Controller implements Initializable {
    @FXML private ListView<AcademicGroup> academicGroupProgramListView;
    @FXML private Label totalAcademicGroupProgramLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getAllAcademicGroupPrograms();
    }

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/ResponsableView.fxml"), this);
        stage.showAndWait();
    }

    @FXML
    void getAcademicGroupProgramDetailsOnAction(ActionEvent event) {
        AcademicGroup academicGroupProgramSelected = academicGroupProgramListView.getSelectionModel().getSelectedItem();
        if(academicGroupProgramSelected != null ) {
            AcademicGroupProgramDetailsController academicGroupProgramDetailsController = new AcademicGroupProgramDetailsController(academicGroupProgramSelected);
            academicGroupProgramDetailsController.showStage();
        }
    }

    @FXML
    void registerAcademicGroupProgramOnAction(ActionEvent event) {
        AddAcademicGroupController addAcademicGroupProgramController = new AddAcademicGroupController();
        addAcademicGroupProgramController.showStage();
        if(addAcademicGroupProgramController.getAcademicGroupProgramRegistered() != null ) {
            academicGroupProgramListView.getItems().add(addAcademicGroupProgramController.getAcademicGroupProgramRegistered());
        }
    }

    private void getAllAcademicGroupPrograms() {
        try{
            ObservableList<AcademicGroup> academicGroupProgramObservableList = FXCollections.observableArrayList(new AcademicGroupDAO().getAllAcademicGroupPrograms());
            academicGroupProgramListView.setItems(academicGroupProgramObservableList);
        } catch(SQLException sqlException) {
            Logger.getLogger(ResponsableController.class.getName()).log(Level.SEVERE, null, sqlException);
        }
    }

}
