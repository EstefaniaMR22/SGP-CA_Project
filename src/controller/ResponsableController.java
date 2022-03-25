package controller;

import controller.academicgroup.AcademicGroupProgramDetailsController;
import controller.academicgroup.AddAcademicGroupController;
import controller.academicgroup.AddLgacController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import model.dao.AcademicGroupProgramDAO;
import model.dao.LgacDAO;
import model.domain.AcademicGroupProgram;
import model.domain.LGAC;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResponsableController extends Controller implements Initializable {
    @FXML private ListView<AcademicGroupProgram> academicGroupProgramListView;
    @FXML private Label totalAcademicGroupProgramLabel;
    @FXML private ListView<LGAC> lgacListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getAllAcademicGroupPrograms();
        getAlllgacsFromDatabase();
    }

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/ResponsableView.fxml"), this);
        stage.showAndWait();
    }


    @FXML
    void registerLgacOnAction(ActionEvent event) {
        AddLgacController addLgacController = new AddLgacController();
        addLgacController.showStage();
    }

    @FXML
    void removeLgacOnAction(ActionEvent event) {
        LGAC lgacSelected = lgacListView.getSelectionModel().getSelectedItem();
        if( lgacSelected != null ) {
            try {
                System.out.println(new LgacDAO().removeLgac(lgacSelected.getId()));
            } catch(SQLException sqlException) {
                Logger.getLogger(ResponsableController.class.getName()).log(Level.SEVERE, null, sqlException);
            }
        }
    }

    @FXML
    void getAcademicGroupProgramDetailsOnAction(ActionEvent event) {
        AcademicGroupProgram academicGroupProgramSelected = academicGroupProgramListView.getSelectionModel().getSelectedItem();
        if(academicGroupProgramSelected != null ) {
            AcademicGroupProgramDetailsController academicGroupProgramDetailsController = new AcademicGroupProgramDetailsController(academicGroupProgramSelected);
            academicGroupProgramDetailsController.showStage();
        }
    }

    @FXML
    void registerAcademicGroupProgramOnAction(ActionEvent event) {
        AddAcademicGroupController addAcademicGroupProgramController = new AddAcademicGroupController();
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

    private void getAlllgacsFromDatabase() {
        List<LGAC> lgacs = null;
        try {
            lgacs = new LgacDAO().getAlllgacs();
            ObservableList<LGAC> lgacObservableList = FXCollections.observableArrayList(lgacs);
            lgacListView.setItems(lgacObservableList);
        } catch(SQLException sqlException) {
            Logger.getLogger(ResponsableController.class.getName()).log(Level.SEVERE, null, sqlException);
        }
    }

}
