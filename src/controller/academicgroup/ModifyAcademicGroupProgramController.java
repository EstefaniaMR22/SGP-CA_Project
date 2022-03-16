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
import model.dao.AcademicGroupProgramDAO;
import model.dao.LgacDAO;
import model.domain.AcademicGroupProgram;
import model.domain.ConsolidationGrade;
import model.domain.LGAC;
import model.domain.Member;
import utils.DateFormatter;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModifyAcademicGroupProgramController extends Controller implements Initializable {
    AcademicGroupProgram academicGroupProgramSelected;
    @FXML private TextField adscriptionAreaTextField;
    @FXML private TableView<LGAC> lgacAvailableTableView;
    @FXML private TableColumn<LGAC, String> descriptionLgacTableColumn;
    @FXML private TableColumn<LGAC, String> identificatorLgacTableColumn;
    @FXML private TextField adscriptionUnitTextField;
    @FXML private ComboBox<ConsolidationGrade> consolidationGradeComboBox;
    @FXML private TextArea generalObjetiveTextArea;
    @FXML private TextField idTextField;
    @FXML private DatePicker lastEvaluationDatePicker;
    @FXML private ListView<LGAC> lgacProgramListView;
    @FXML private ListView<Member> membersInProgramListView;
    @FXML private TextArea misionTextArea;
    @FXML private TableView<Member> membersAvailableTableView;
    @FXML private TableColumn<Member, String> nameTableColumn;
    @FXML private TableColumn<Member, String> positionTableColumn;
    @FXML private TableColumn<Member, String> telephoneNumberTableColumn;
    @FXML private TableColumn<Member, String> emailTableColumn;
    @FXML private TextField nameTextField;
    @FXML private DatePicker registerDateDatePicker;
    @FXML private Label totalLGACInProgramLabel;
    @FXML private Label totalMembersInProgramLabel;
    @FXML private TextArea visionTextArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setTableComponents();
        setAcademicGroupProgramDetailsIntoTextFields();
        getAlllgacsFromDatabase();
    }

    public ModifyAcademicGroupProgramController(AcademicGroupProgram academicGroupProgramSelected) {
        this.academicGroupProgramSelected = academicGroupProgramSelected;
    }

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/ModifyAcademicGroupProgramView.fxml"), this);
        stage.showAndWait();
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
    void cancelOnAction(ActionEvent event) {
        stage.close();
    }

    @FXML
    void modifyAcademicGroupProgramOnAction(ActionEvent event) {
        academicGroupProgramSelected.setId(idTextField.getText());
        academicGroupProgramSelected.setAdscriptionArea(adscriptionAreaTextField.getText());
        academicGroupProgramSelected.setName(nameTextField.getText());
        academicGroupProgramSelected.setAdscriptionUnit(adscriptionUnitTextField.getText());
        academicGroupProgramSelected.setConsolidationGrade(consolidationGradeComboBox.getSelectionModel().getSelectedItem());
        academicGroupProgramSelected.setRegisterDate(DateFormatter.getDateFromDatepickerValue(registerDateDatePicker.getValue()));
        academicGroupProgramSelected.setLastEvaluationDate(DateFormatter.getDateFromDatepickerValue(lastEvaluationDatePicker.getValue()));
        academicGroupProgramSelected.setGeneralObjetive(generalObjetiveTextArea.getText());
        academicGroupProgramSelected.setMission(misionTextArea.getText());
        academicGroupProgramSelected.setVision(visionTextArea.getText());
        academicGroupProgramSelected.setLgacList(lgacProgramListView.getItems());
        System.out.println(academicGroupProgramSelected);
    }

    @FXML
    void removeLGACFromProgramOnAction(ActionEvent event) {
        LGAC lgacSelected = lgacProgramListView.getSelectionModel().getSelectedItem();
        if(lgacSelected != null ) {
            lgacProgramListView.getItems().remove(lgacSelected);
            lgacAvailableTableView.getItems().add(lgacSelected);
            lgacProgramListView.getSelectionModel().clearSelection();
        }
    }

    @FXML
    void removeMemberFromProgramOnAction(ActionEvent event) {

    }


    private void setAcademicGroupProgramDetailsIntoTextFields() {
        idTextField.setText(academicGroupProgramSelected.getId());
        adscriptionAreaTextField.setText(academicGroupProgramSelected.getAdscriptionArea());
        nameTextField.setText(academicGroupProgramSelected.getName());
        adscriptionUnitTextField.setText(academicGroupProgramSelected.getAdscriptionUnit());
        consolidationGradeComboBox.getSelectionModel().select(academicGroupProgramSelected.getConsolidationGrade());
        registerDateDatePicker.setValue(DateFormatter.getLocalDateFromUtilDate(academicGroupProgramSelected.getRegisterDate()));
        lastEvaluationDatePicker.setValue(DateFormatter.getLocalDateFromUtilDate(academicGroupProgramSelected.getLastEvaluationDate()));
        generalObjetiveTextArea.setText(academicGroupProgramSelected.getGeneralObjetive());
        misionTextArea.setText(academicGroupProgramSelected.getMission());
        visionTextArea.setText(academicGroupProgramSelected.getVision());
        lgacProgramListView.setItems(FXCollections.observableArrayList(academicGroupProgramSelected.getLgacList()));
        totalLGACInProgramLabel.setText(String.valueOf(lgacProgramListView.getItems().size()));
        // GET MEMBERS ASSOCIATED TO A WORKPLAN -> ACADEMIC GROUP PROGRAM
        // GET WORK PLAN ASSIGNED TO ACADEMIC GROUP PROGRAM
    }

    private void getAlllgacsFromDatabase() {
        List<LGAC> lgacs = null;
        try {
            lgacs = new LgacDAO().getAlllgacs();
            System.out.println(lgacs);
            ObservableList<LGAC> lgacObservableList = FXCollections.observableArrayList(lgacs);
            for(int i = 0; i < lgacProgramListView.getItems().size(); i++) {
                if(lgacProgramListView.getItems().get(i).getId() == lgacObservableList.get(i).getId()) {
                    lgacObservableList.remove(i);
                }
            }
            lgacAvailableTableView.setItems(lgacObservableList);
        } catch(SQLException sqlException) {
            Logger.getLogger(ModifyAcademicGroupProgramController.class.getName()).log(Level.SEVERE, null, sqlException);
        }
    }

    private void setTableComponents() {
        identificatorLgacTableColumn.setCellValueFactory(new PropertyValueFactory<>("identification"));
        descriptionLgacTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        lgacProgramListView.getItems().addListener(new ListChangeListener<LGAC>() {
            @Override
            public void onChanged(Change<? extends LGAC> c) {
                System.out.println("CHANGE");
            }
        });

//        lgacProgramListView.getItems().addListener((ListChangeListener<LGAC>) c -> {
//            System.out.println("Cambiooooo");
//            totalLGACInProgramLabel.setText((String.valueOf(lgacProgramListView.getItems().size())));
//        });
    }

}
