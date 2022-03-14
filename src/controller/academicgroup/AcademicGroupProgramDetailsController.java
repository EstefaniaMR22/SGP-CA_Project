package controller.academicgroup;

import controller.Controller;
import controller.ResponsableController;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.dao.interfaces.AcademicGroupProgramDAO;
import model.domain.AcademicGroupProgram;
import model.domain.LGAC;
import model.domain.Member;
import model.domain.Workplan;

import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AcademicGroupProgramDetailsController extends Controller implements Initializable {
    private AcademicGroupProgram academicGroupProgramSelected;


    @FXML private TableView<Member> memberTableView;
    @FXML private TableColumn<Member, String> telephoneMemberTableColumn;
    @FXML private TableColumn<Member, String> emailMemberTableColumn;
    @FXML private TableColumn<Member, String> nameMemberTableColumn;
    @FXML private TableColumn<Member, String> positionMemberTableColumn;
    @FXML private Label adscriptionAreaLabel;
    @FXML private Label adscriptionUnitLabel;
    @FXML private Label consolidationGradeLabel;
    @FXML private TableView<Workplan> workPlanTableView;
    @FXML private TableColumn<Workplan, String> generalObjetiveWorkPlanTableColumn;
    @FXML private TableColumn<Workplan, String> idWorkPlanTableColumn;
    @FXML private TableColumn<Workplan, Date> endDateWorkPlanTableColumn;
    @FXML private TableColumn<Workplan, Date> initialDateWorkPlanTableColumn;
    @FXML private Label generalObjetiveLabel;
    @FXML private Label keyLabel;
    @FXML private Label lastEvaluationLabel;
    @FXML private Label misionLabel;
    @FXML private Label nameLabel;
    @FXML private Label registerDateLabel;
    @FXML private TextField searchInputTextField;
    @FXML private Label totalLgacsLabel;
    @FXML private Label totalMembersLabel;
    @FXML private Label visionLabel;
    @FXML private Label totalWorkPlanLabel;
    @FXML private TableView<LGAC> lgacTableView;
    @FXML private TableColumn<LGAC, String> identificationLgacTableColumn;
    @FXML private TableColumn<LGAC, String> descriptionLgacTableColumn;


    @FXML
    void searchLGACOnAction(ActionEvent event) {

    }

    @FXML
    void searchMemberOnAction(ActionEvent event) {

    }


    @FXML
    void updateOnAction(ActionEvent event) {
        ModifyAcademicGroupProgramController modifyAcademicGroupProgramController = new ModifyAcademicGroupProgramController(academicGroupProgramSelected);
        modifyAcademicGroupProgramController.showStage();
    }


    @FXML
    void cancelOnAction(ActionEvent event) {
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setTableComponents();
        getAcademicGroupProgramDetails();
    }

    public AcademicGroupProgramDetailsController(AcademicGroupProgram academicGroupProgramSelected) {
        this.academicGroupProgramSelected = academicGroupProgramSelected;
    }

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/AcademicGroupProgramDetailsView.fxml"), this);
        stage.showAndWait();
    }

    private void getAcademicGroupProgramDetails() {
        try{
            academicGroupProgramSelected = new AcademicGroupProgramDAO().getAcademicGroupProgramDetails(academicGroupProgramSelected.getId());

        }catch(SQLException sqlException) {
            Logger.getLogger(AcademicGroupProgramDetailsController.class.getName()).log(Level.SEVERE, null, sqlException);
        }
        setAcademicGroupProgramDetailsIntoTextFields(academicGroupProgramSelected);
    }

    private void setAcademicGroupProgramDetailsIntoTextFields(AcademicGroupProgram academicGroupProgram) {
        keyLabel.setText(academicGroupProgramSelected.getId());
        adscriptionAreaLabel.setText(academicGroupProgramSelected.getAdscriptionArea());
        nameLabel.setText(academicGroupProgramSelected.getName());
        adscriptionUnitLabel.setText(academicGroupProgramSelected.getAdscriptionUnit());
        consolidationGradeLabel.setText(academicGroupProgramSelected.getConsolidationGrade().getConsolidationGrade());
        registerDateLabel.setText(academicGroupProgramSelected.getRegisterDate().toString());
        lastEvaluationLabel.setText(academicGroupProgramSelected.getLastEvaluationDate().toString());
        generalObjetiveLabel.setText(academicGroupProgramSelected.getGeneralObjetive());
        misionLabel.setText(academicGroupProgramSelected.getMission());
        visionLabel.setText(academicGroupProgramSelected.getVision());

        lgacTableView.setItems(FXCollections.observableArrayList(academicGroupProgram.getLgacList()));
        totalLgacsLabel.setText(String.valueOf( lgacTableView.getItems().size()));
        // GET MEMBERS ASSOCIATED TO A WORKPLAN -> ACADEMIC GROUP PROGRAM
        // GET WORK PLAN ASSIGNED TO ACADEMIC GROUP PROGRAM

    }


    private void setTableComponents() {
        descriptionLgacTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        identificationLgacTableColumn.setCellValueFactory(new PropertyValueFactory<>("identification"));

        positionMemberTableColumn.setCellValueFactory(new PropertyValueFactory<>("participationType"));
        nameMemberTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailMemberTableColumn.setCellValueFactory(new PropertyValueFactory<>("uvEmail"));
        telephoneMemberTableColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));

        idWorkPlanTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        generalObjetiveWorkPlanTableColumn.setCellValueFactory(new PropertyValueFactory<>("generalObjetive"));
        initialDateWorkPlanTableColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateWorkPlanTableColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));


    }



}