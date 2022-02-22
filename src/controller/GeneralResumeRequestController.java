/*
 * @author Josué 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package controller;

import com.jfoenix.controls.JFXTextArea;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import model.domain.Collaborator;
import model.domain.GeneralResume;
import model.domain.Integrant;
import model.domain.Member;
import model.dataaccess.CollaboratorDAO;
import model.dataaccess.GeneralResumeDAO;
import model.dataaccess.IntegrantDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import controller.pattern.GenericWindowDriver;

public class GeneralResumeRequestController implements Initializable{

    @FXML
    private Button brtnProduction;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnExit;
    @FXML
    private Label lbBodyAcademyName;
    @FXML
    private Label lbBodyAcademyKey;
    @FXML
    private Label lbSubscriptionArea;
    @FXML
    private Label lbAdscriptionUnit;
    @FXML
    private Label lbConsolidationDegree;
    @FXML
    private Label lbRegistrationDate;
    @FXML
    private Label lbLastEvaluationDate;
    @FXML
    private Label lbUserName;
    @FXML
    private TextArea txtAreaGeneralTarget;
    @FXML
    private TextArea txtAreaMission;
    @FXML
    private TextArea txtAreaVision;
    @FXML
    private TextField txtFieldMemberNameForSearch;
    @FXML
    private Button btnAddNewMember;
    @FXML
    private Button btnSearchMember;
    @FXML
    private ListView<String> lvLgac;
    @FXML
    private HBox hbGeneralResumeOptions;
    @FXML
    private JFXTextArea txtAreaLgacDescription;
    @FXML
    private RadioButton rdoBtnSubscribeMembers;
    @FXML
    private RadioButton rdoBtnUnsubscribeMembers;
    @FXML
    private TableColumn<Member, String> colParticipationTypeIntegrant;
    @FXML
    private TableColumn<Member, String> colFullNameIntegrant;
    @FXML
    private TableColumn<Member, String> colEmailUVIntegrant;
    @FXML
    private TableColumn<Member, String> colCellPhoneIntegrant;
    @FXML
    private TableView<Member> tvIntegrants;
    @FXML
    private TableView<Member> tvCollaborators;
    @FXML
    private TableColumn<Member, String> colParticipationTypeColllaborator;
    @FXML
    private TableColumn<Member, String> colFullNameCollaborator;
    @FXML
    private TableColumn<Member, String> colEmailUVCollaborator;
    @FXML
    private TableColumn<Member, String> colPhoneCollaborator;
    private final ToggleGroup rbGroup = new ToggleGroup();
    @FXML
    private HBox hbMembersOptions;
    
    private Integrant token;
    private GeneralResume generalResume;
    private final GeneralResumeDAO GENERAL_RESUME_DAO = new GeneralResumeDAO();
    private final String errorMessage = "El sistema no está funcionando correctamente, "
    + "favor de ponerse en contacto con soporte técnico si es que el problema persiste.";
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        this.prepareCollaboratorTable();
        this.prepareIntegratsTable();
        this.rdoBtnSubscribeMembers.setToggleGroup(rbGroup);
        this.rdoBtnUnsubscribeMembers.setToggleGroup(rbGroup);
    }

    public void showGeneralResume(Integrant integrantToken){
        this.token = integrantToken;
        this.lbUserName.setText(this.token.getFullName());
        this.generalResume = GENERAL_RESUME_DAO.getGeneralResumeByKey(integrantToken.getBodyAcademyKey());
        this.generalResume.addMembers(new IntegrantDAO().getMembers(this.token.getBodyAcademyKey()));
        this.generalResume.addMembers(new CollaboratorDAO().getMembers(this.token.getBodyAcademyKey()));
        this.setGeneralResumeDataIntoInterface();
        if(!this.token.getParticipationType().equalsIgnoreCase("Responsable")){
            hbGeneralResumeOptions.getChildren().remove(this.btnEdit);
            hbMembersOptions.getChildren().remove(this.btnAddNewMember);
        }
    }    
    
    private void setGeneralResumeDataIntoInterface(){
        if(this.generalResume != null){
            lbBodyAcademyKey.setText(this.generalResume.getBodyAcademyKey());
            lbBodyAcademyName.setText(this.generalResume.getBodyAcademyName());
            lbAdscriptionUnit.setText(this.generalResume.getAscriptionUnit());
            lbConsolidationDegree.setText(this.generalResume.getConsolidationDegree());
            lbRegistrationDate.setText(this.generalResume.getRegistrationDate());
            lbSubscriptionArea.setText(this.generalResume.getAscriptionArea());
            lbLastEvaluationDate.setText(this.generalResume.getLastEvaluation());
            txtAreaGeneralTarget.setText(this.generalResume.getGeneralTarjet());
            txtAreaMission.setText(this.generalResume.getMission());
            txtAreaVision.setText(this.generalResume.getVision());
            this.filterSubscribeMembersInTable();
            this.generalResume.getLgacList().forEach(lgac -> this.lvLgac.getItems().add(lgac.getTitle()));
        }else{
            GenericWindowDriver.getGenericWindowDriver().showErrorAlert(new ActionEvent(), errorMessage);
            FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("Start.fxml", btnExit);
            StartController controller = loader.getController();
            controller.receiveIntegrantToken(token);
        }
    }

    @FXML
    private void requestEvidencesList(ActionEvent event){
        FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("EvidenceList.fxml", btnExit);
        EvidenceListController controller = loader.getController();
        controller.showGeneralResumeEvidences(token);
    }

    @FXML
    private void editGeneralResume(ActionEvent event){
        FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("GeneralResumeEditable.fxml", btnExit);
        GeneralResumeEditableController controller = loader.getController();
        controller.showGeneralResumeUpdateForm(this.token);
    }

    @FXML
    private void exit(ActionEvent event){
        FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("Start.fxml", btnExit);
        StartController controller = loader.getController();
        controller.receiveIntegrantToken(this.token);
    }

    @FXML
    private void addMember(ActionEvent event){
        FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("MemberSelection.fxml", btnExit);
        MemberSelectionController controller = loader.getController();
        controller.receiveResponsibeleToken(token);
    }
    
    @FXML
    private void showIntegrant(MouseEvent event){
        Integrant integrant = (Integrant) tvIntegrants.getSelectionModel().getSelectedItem();
        if(integrant != null){
            FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("IntegrantRequest.fxml", btnExit);
            IntegrantRequestController controller = loader.getController();
            controller.showIntegrantByEmail(this.token, integrant.getEmailUV());
        }
    }

    @FXML
    private void showCollaborator(MouseEvent event){
        Collaborator collaborator = (Collaborator) tvCollaborators.getSelectionModel().getSelectedItem();
        if(collaborator != null){
            FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("CollaboratorRequest.fxml", btnExit);
            CollaboratorRequestController controller = loader.getController();
            controller.showCollaboratorByEmail(this.token, collaborator.getEmailUV());
        }
    }

    @FXML
    private void searchMember(ActionEvent event){
        if(rdoBtnSubscribeMembers.isSelected()){
            tvIntegrants.getItems().clear();
            tvCollaborators.getItems().clear();
            tvIntegrants.getItems().addAll(this.getIntegrantsFiltered("Activo"));
            tvCollaborators.getItems().addAll(this.getCollaboratorsFiltered("Activo"));
        }else{
            tvIntegrants.getItems().clear();
            tvCollaborators.getItems().clear();
            tvIntegrants.getItems().addAll(this.getIntegrantsFiltered("Dado de baja"));
            tvCollaborators.getItems().addAll(this.getCollaboratorsFiltered("Dado de baja"));
        }
    }

    @FXML
    private void showSubscribeMembers(ActionEvent event){
        this.filterSubscribeMembersInTable();
    }

    @FXML
    private void showUnsubscribeMembers(ActionEvent event){
        this.filterUnsubscribeMembersInTable();
    }

    @FXML
    private void showLgacDescription(MouseEvent event){
        this.txtAreaLgacDescription.setText(this.generalResume.getLgacDescriptionByTitle(lvLgac.getSelectionModel().getSelectedItem()));
    }
    
    private void prepareIntegratsTable(){
        colParticipationTypeIntegrant.setCellValueFactory(new PropertyValueFactory<>("participationType"));
        colFullNameIntegrant.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colEmailUVIntegrant.setCellValueFactory(new PropertyValueFactory<>("emailUV"));
        colCellPhoneIntegrant.setCellValueFactory(new PropertyValueFactory<>("cellphone"));
    }
    
    private void prepareCollaboratorTable(){
        colParticipationTypeColllaborator.setCellValueFactory(new PropertyValueFactory<>("participationType"));
        colFullNameCollaborator.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colEmailUVCollaborator.setCellValueFactory(new PropertyValueFactory<>("emailUV"));
        colPhoneCollaborator.setCellValueFactory(new PropertyValueFactory<>("cellphone"));
    }
    
    private void filterSubscribeMembersInTable(){
        tvCollaborators.getItems().clear();
        tvIntegrants.getItems().clear();
        tvCollaborators.getItems().addAll(this.generalResume.getCollaborators("Activo"));
        tvIntegrants.getItems().addAll(this.generalResume.getIntegrants("Activo"));
    }
    
    private void filterUnsubscribeMembersInTable(){
        tvCollaborators.getItems().clear();
        tvIntegrants.getItems().clear();
        tvCollaborators.getItems().addAll(this.generalResume.getCollaborators("Dado de baja"));
        tvIntegrants.getItems().addAll(this.generalResume.getIntegrants("Dado de baja"));
    } 
    
    private List<Integrant> getIntegrantsFiltered(String status){
        List<Integrant> integrantsWanted = new ArrayList<>();
        for(Integrant integrante : this.generalResume.getIntegrants(status)){
            if(integrante.getFullName().contains(this.txtFieldMemberNameForSearch.getText())){
                integrantsWanted.add(integrante);
            }
        }
        return integrantsWanted;
    }
    
    private List<Collaborator> getCollaboratorsFiltered(String status){
        List<Collaborator> collaboratorsWanted = new ArrayList<>();
        for(Collaborator collaborator : this.generalResume.getCollaborators(status)){
            if(collaborator.getFullName().contains(this.txtFieldMemberNameForSearch.getText())){
                collaboratorsWanted.add(collaborator);
            }
        }
        return collaboratorsWanted;
    }    
}
