package controller.academicgroup;

import assets.utils.Autentication;
import controller.control.ValidatorController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.dao.AcademicGroupDAO;
import model.domain.AcademicGroup;
import model.domain.LGAC;
import model.domain.Participation;
import model.domain.ParticipationType;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AcademicGroupDetailsController extends ValidatorController implements Initializable {
    private AcademicGroup academicGroupProgramSelected;
    @FXML private Label adscriptionAreaLabel;
    @FXML private Label adscriptionUnitLabel;
    @FXML private Label consolidationGradeLabel;
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
    @FXML private TableColumn<LGAC, String> stateTableColumn;
    @FXML private Label adscriptionDescriptionLabel;
    @FXML private TableView<Participation> participationsTableView;
    @FXML private TableColumn<Participation, String> personalNumberTableColumn;
    @FXML private TableColumn<Participation, String> nameTableColumn;
    @FXML private TableColumn<Participation, ParticipationType> typeParticipationColumn;
    @FXML private Label totalColaboratorsLabel;
    @FXML private Label totalIntegrantsLabel;
    @FXML private Label totalResponsablesLabel;
    @FXML private Button modifyButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(Autentication.getInstance().getParticipation().getParticipationType() == ParticipationType.RESPONSABLE ) {
            if(!academicGroupProgramSelected.getId().equals(Autentication.getInstance().getIdAcademicGroup())) {
                modifyButton.setVisible(false);
            }
        }
        getAcademicGroupProgramDetails();
        setTableComponents();
    }

    public AcademicGroupDetailsController(AcademicGroup academicGroupProgramSelected) {
        this.academicGroupProgramSelected = academicGroupProgramSelected;
    }

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/AcademicGroupDetailsView.fxml"), this);
        stage.showAndWait();
    }

    public AcademicGroup getAcademicGroupProgramSelected() {
        return academicGroupProgramSelected;
    }

    @FXML
    void updateOnAction(ActionEvent event) {
        ModifyAcademicGroupController modifyAcademicGroupProgramController = new ModifyAcademicGroupController(academicGroupProgramSelected);
        modifyAcademicGroupProgramController.showStage();
        if(!modifyAcademicGroupProgramController.getAcademicGroupProgramSelected().equals(academicGroupProgramSelected)  && !modifyAcademicGroupProgramController.isCanceledOperation()) {
            academicGroupProgramSelected = modifyAcademicGroupProgramController.getAcademicGroupProgramSelected();
            setAcademicGroupProgramDetailsIntoControls();
            totalLgacsLabel.setText(String.valueOf(lgacTableView.getItems().size()));
        } else {
            getAcademicGroupProgramDetails();
        }
        lgacTableView.refresh();
    }

    @FXML
    void cancelOnAction(ActionEvent event) {
        stage.close();
    }


    private void getAcademicGroupProgramDetails() {
        try{
            academicGroupProgramSelected = new AcademicGroupDAO().getAcademicGroupProgramDetails(academicGroupProgramSelected.getId());
            setAcademicGroupProgramDetailsIntoControls();
        }catch(SQLException sqlException) {
            Logger.getLogger(AcademicGroupDetailsController.class.getName()).log(Level.SEVERE, null, sqlException);
        }
    }

    private void setAcademicGroupProgramDetailsIntoControls() {
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
        adscriptionDescriptionLabel.setText(academicGroupProgramSelected.getDescriptionAdscription());
        lgacTableView.setItems(FXCollections.observableArrayList(academicGroupProgramSelected.getLgacList() == null ? new ArrayList<>() : academicGroupProgramSelected.getLgacList()));
        participationsTableView.setItems(FXCollections.observableArrayList(academicGroupProgramSelected.getParticipationList() == null ? new ArrayList<>() : academicGroupProgramSelected.getParticipationList()));
        totalLgacsLabel.setText(String.valueOf(lgacTableView.getItems().size()));
        setNumbersMembersToLabels();
    }

    private void setTableComponents() {
        descriptionLgacTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        identificationLgacTableColumn.setCellValueFactory(new PropertyValueFactory<>("identification"));
        nameTableColumn.setCellValueFactory( cellData -> new SimpleStringProperty(cellData.getValue().getMember().getFullName()));
        personalNumberTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMember().getPersonalNumber()));
        typeParticipationColumn.setCellValueFactory( cellData -> new SimpleObjectProperty<>(cellData.getValue().getParticipationType()));
        participationsTableView.setEditable(false);
        stateTableColumn.setCellValueFactory( cellData -> new SimpleStringProperty(cellData.getValue().getActivityState().getActivityState()));
    }

    private void setNumbersMembersToLabels() {
        int members = participationsTableView.getItems().size();
        int colaborators = 0;
        int integrants = 0;
        int responsables = 0;
        for(Participation participation : participationsTableView.getItems() ) {
            if(participation.getParticipationType() == ParticipationType.INTEGRANT) {
                integrants++;
            } else if (participation.getParticipationType() == ParticipationType.RESPONSABLE) {
                responsables++;
            } else if(participation.getParticipationType() == ParticipationType.COLABORATOR) {
                colaborators++;
            }
        }
        totalMembersLabel.setText(String.valueOf(members));
        totalResponsablesLabel.setText(String.valueOf(responsables));
        totalIntegrantsLabel.setText(String.valueOf(integrants));
        totalColaboratorsLabel.setText(String.valueOf(colaborators));
    }

}