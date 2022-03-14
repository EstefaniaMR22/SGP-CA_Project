package controller.academicgroup;

import controller.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.dao.interfaces.AcademicGroupProgramDAO;
import model.domain.AcademicGroupProgram;
import model.domain.ConsolidationGrade;
import model.domain.LGAC;
import utils.DateFormatter;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddAcademicGroupProgramController extends Controller implements Initializable {
    private List<LGAC> listAddedToProgram;
    @FXML private TableView<LGAC> lgacAvailableTableView;
    @FXML private TableColumn<LGAC, Integer> identificatorTableColumn;
    @FXML private TableColumn<LGAC, String> descriptionTableColumn;
    @FXML private ListView<LGAC> lgacProgramListView;
    @FXML private TextField activeMembersTextField;
    @FXML private TextField adscriptionAreaTextField;
    @FXML private TextField adscriptionUnitTextField;
    @FXML private ComboBox<ConsolidationGrade> consolidationGradeComboBox;
    @FXML private TableColumn<?, ?> emailTableColumn;
    @FXML private TextArea generalObjetiveTextArea;
    @FXML private TextField idTextField;
    @FXML private DatePicker lastEvaluationDatePicker;
    @FXML private TableView<?> membersAvailableTableView;
    @FXML private ListView<?> membersInProgramListView;
    @FXML private TextArea misionTextArea;
    @FXML private TextField nameTextField;
    @FXML private TableColumn<?, ?> nameTableColumn;
    @FXML private TableColumn<?, ?> positionTableColumn;
    @FXML private TableColumn<?, ?> telephoneNumberTableColumn;
    @FXML private DatePicker registerDateDatePicker;
    @FXML private Label totalLGACInProgramLabel;
    @FXML private Label totalMembersInProgramLabel;
    @FXML private TextArea visionTextArea;

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/AddAcademicGroupProgramView.fxml"), this);
        stage.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setTableComponents();
        getConsolidationGradeFromDatabase();
        getAlllgacsFromDatabase();
    }

    @FXML
    void addAcademicGroupProgramOnAction(ActionEvent event) {
        AcademicGroupProgram academicGroupProgram = new AcademicGroupProgram();
        academicGroupProgram.setId(idTextField.getText());
        academicGroupProgram.setAdscriptionArea(adscriptionAreaTextField.getText());
        academicGroupProgram.setName(nameTextField.getText());
        academicGroupProgram.setAdscriptionUnit(adscriptionUnitTextField.getText());
        academicGroupProgram.setConsolidationGrade(consolidationGradeComboBox.getSelectionModel().getSelectedItem());
        academicGroupProgram.setRegisterDate(DateFormatter.getDateFromDatepickerValue(registerDateDatePicker.getValue()));
        academicGroupProgram.setLastEvaluationDate(DateFormatter.getDateFromDatepickerValue(lastEvaluationDatePicker.getValue()));
        academicGroupProgram.setGeneralObjetive(generalObjetiveTextArea.getText());
        academicGroupProgram.setMission(misionTextArea.getText());
        academicGroupProgram.setVision(visionTextArea.getText());
        academicGroupProgram.setLgacList(lgacProgramListView.getItems());

        try{
            System.out.println(new AcademicGroupProgramDAO().addAcademicGroupProgram(academicGroupProgram));
        } catch(SQLException sqlException) {
            Logger.getLogger(AddAcademicGroupProgramController.class.getName()).log(Level.SEVERE, null, sqlException);
        }
    }

    @FXML
    void addLGACToProgramOnAction(ActionEvent event) {
        LGAC lgacSelected = lgacAvailableTableView.getSelectionModel().getSelectedItem();
        if(lgacSelected != null ) {
            lgacAvailableTableView.getSelectionModel().clearSelection();
            lgacProgramListView.getItems().add(lgacSelected);
            lgacAvailableTableView.getItems().remove(lgacSelected);
        }
    }

    @FXML
    void addMemberToProgramOnAction(ActionEvent event) {

    }

    @FXML
    void removeMemberFromProgramOnAction(ActionEvent event) {

    }

    @FXML
    void cancelOnAction(ActionEvent event) {
        stage.close();
    }

    @FXML
    void removeLGACFromProgramOnAction(ActionEvent event) {
        LGAC lgacSelected = lgacProgramListView.getSelectionModel().getSelectedItem();
        if(lgacSelected != null ) {
            lgacProgramListView.getItems().remove(lgacSelected);
            lgacProgramListView.getSelectionModel().clearSelection();
            lgacAvailableTableView.getItems().add(lgacSelected);
        }
    }


    private void getConsolidationGradeFromDatabase() {
        List<ConsolidationGrade> grades = null;
        try{
            grades = new AcademicGroupProgramDAO().getConsolidationGrades();
            ObservableList<ConsolidationGrade> gradeObservableList = FXCollections.observableArrayList(grades);
            consolidationGradeComboBox.setItems(gradeObservableList);
        } catch (SQLException sqlException) {
            Logger.getLogger(AddAcademicGroupProgramController.class.getName()).log(Level.SEVERE, null, sqlException);
        }
    }

    private void getAlllgacsFromDatabase() {
        List<LGAC> lgacs = null;
        try {
            lgacs = new AcademicGroupProgramDAO().getAlllgacs();
            ObservableList<LGAC> lgacObservableList = FXCollections.observableArrayList(lgacs);
            lgacAvailableTableView.setItems(lgacObservableList);
        } catch(SQLException sqlException) {
            Logger.getLogger(AddAcademicGroupProgramController.class.getName()).log(Level.SEVERE, null, sqlException);
        }
    }

    private void setTableComponents() {
        identificatorTableColumn.setCellValueFactory(new PropertyValueFactory<>("identification"));
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        lgacProgramListView.getItems().addListener(new ListChangeListener<LGAC>() {
            @Override
            public void onChanged(Change<? extends LGAC> c) {
                totalLGACInProgramLabel.setText(( String.valueOf(lgacProgramListView.getItems().size())));
            }
        });
    }
}
