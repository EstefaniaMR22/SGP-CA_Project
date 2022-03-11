package controller;

import controller.academicgroup.AcademicGroupProgramDetailsController;
import controller.academicgroup.AddAcademicGroupProgramController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import model.dao.interfaces.AcademicGroupProgramDAO;
import model.domain.AcademicGroupProgram;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResponsableController extends Controller implements Initializable {
    @FXML private ListView<AcademicGroupProgram> academicGroupProgramListView;
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
        AcademicGroupProgramDetailsController academicGroupProgramDetailsController = new AcademicGroupProgramDetailsController(academicGroupProgramListView.getSelectionModel().getSelectedItem());
        academicGroupProgramDetailsController.showStage();
    }

    @FXML
    void registerAcademicGroupProgramOnAction(ActionEvent event) {
        AddAcademicGroupProgramController addAcademicGroupProgramController = new AddAcademicGroupProgramController();
        addAcademicGroupProgramController.showStage();
    }

    private void getAllAcademicGroupPrograms() {
        try{
            ObservableList<AcademicGroupProgram> academicGroupProgramObservableList = FXCollections.observableArrayList(new AcademicGroupProgramDAO().getAllAcademicGroupPrograms());
            academicGroupProgramListView.setItems(academicGroupProgramObservableList);
        } catch(SQLException sqlException) {
            Logger.getLogger(ResponsableController.class.getName()).log(Level.SEVERE, null, sqlException);
        }
    }

}
