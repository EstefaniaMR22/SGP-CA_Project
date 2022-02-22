/*
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package controller;

import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import controller.pattern.GenericWindowDriver;
import controller.pattern.InvalidFormException;
import controller.pattern.ValidatorForm;
import model.dataaccess.MeetingDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.domain.Agreement;
import model.domain.AssistantRol;
import model.domain.Comment;
import model.domain.Meeting;
import model.domain.Prerequisite;
import model.domain.Topic;
import model.domain.Integrant;

public class MeetingRequestController implements Initializable{
    
    @FXML
    private Label lbUserName;
    @FXML
    private Button btnCloseMeetingWindow;
    @FXML
    private Button btnDownloadMinute;
    @FXML
    private Button btnCancelMeeting;
    @FXML
    private Label lbCanceledMetting;
    @FXML
    private TextField txtFieldMeetingRecordDate;
    @FXML
    private TextField txtFieldMeetingProject;
    @FXML
    private TextField txtFieldMeetingIssue;
    @FXML
    private TextField txtFieldMeetingPlace;
    @FXML
    private TextField txtFieldEstimatedTotalTime;
    @FXML
    private TextField txtFieldMeetingDate;
    @FXML
    private TextField txtFieldMeetingTime;
    @FXML
    private TextField txtFieldTotalTime;
    @FXML
    private Button btnUpdateMeeting;
    @FXML
    private Button btnStartMeeting;
    @FXML
    private TableView<Comment> tvComments;
    @FXML
    private TableColumn<Comment, String> colCommentator;
    @FXML
    private TableColumn<Comment, String> colCommentDescription;
    @FXML
    private TextArea txtAreaComment;
    @FXML
    private Button btnDeleteComment;
    @FXML
    private Button btnAddNewComment;
    @FXML
    private TableView<AssistantRol> tvAssistantRol;
    @FXML
    private TableColumn<AssistantRol, String> colAssistantName;
    @FXML
    private TableColumn<AssistantRol, String> colRolAssistant;
    @FXML
    private TableView<Prerequisite> tvPrerequisite;
    @FXML
    private TableColumn<Prerequisite, String> colDescriptionPrerequisite;
    @FXML
    private TableColumn<Prerequisite, String> colResponsiblePrerequisite;
    @FXML
    private TableView<Topic> tvMeetingAgenda;
    @FXML
    private TableColumn<Topic, String> colStartTime;
    @FXML
    private TableColumn<Topic, String> colEndTime;
    @FXML
    private TableColumn<Topic, String> colPlannedTime;
    @FXML
    private TableColumn<Topic, String> colRealTime;
    @FXML
    private TableColumn<Topic, String> colDescriptionTopic;
    @FXML
    private TableColumn<Topic, String> colDiscussionLeader;
    @FXML
    private TableView<Agreement> tvAgreement;
    @FXML
    private TableColumn<Agreement, String> colDescriptionAgreement;
    @FXML
    private TableColumn<Agreement, String> colDeliveryDate;
    @FXML
    private TableColumn<Agreement, String> colResponsibleAgreement;
    @FXML
    private TextArea txtAreaNoteMeeting;
    @FXML
    private TextArea txtAreaPendingMeeting;
    
    private final MeetingDAO MEETING_DAO = new MeetingDAO();
    private Meeting meeting;
    Calendar date = new GregorianCalendar();
    private Integrant token;

    public void reciveMeeting (int meetingKey){
        this.meeting = MEETING_DAO.getMeeting(meetingKey);
        this.comproveMeetingDifferentNull(meeting);
        this.setMeetingInformation();
        this.preparedCommentTable();
        this.preparedAssistantTable();
        this.preparedPrerequisiteTable();
        this.preparedAgendaMeetingTable();
        this.preparedAgreementTable();
        windowPermissions(meeting, token);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        
    }
    
    public void receiveToken(Integrant integrantToken){
        this.token = integrantToken;
        this.lbUserName.setText(token.getFullName());
    }
    
    private void windowPermissions(Meeting meeting, Integrant token){
        if(meeting.getStatusMeeting().compareTo("Cancelada") == 0){
            this.lbCanceledMetting.setVisible(true);
        }
        if(meeting.getStatusMeeting().compareTo("Realizada") == 0){
            this.btnDownloadMinute.setDisable(false);
            this.btnCancelMeeting.setDisable(true);
        }
        if(token.getRfc().compareTo(meeting.getIntegrantResponsible()) != 0){
            this.btnCancelMeeting.setDisable(true);
            this.btnUpdateMeeting.setDisable(true);   
        }
    }
    
    private void setMeetingInformation(){
        this.txtFieldMeetingRecordDate.setText(meeting.getMeetingRegistrationDate());
        this.txtFieldMeetingProject.setText(meeting.getMeetingProject());
        this.txtFieldMeetingIssue.setText(meeting.getIssueMeeting());
        this.txtFieldMeetingPlace.setText(meeting.getPlaceMeeting());
        this.txtFieldEstimatedTotalTime.setText(meeting.getMeetingAgenda().getEstimatedTotalTime());
        this.txtFieldMeetingDate.setText(meeting.getMeetingDate());
        this.txtFieldMeetingTime.setText(meeting.getMeetingTime());
        this.txtFieldTotalTime.setText(meeting.getMeetingAgenda().getTotalTime());
        this.txtAreaNoteMeeting.setText(meeting.getMeetingNote());
        this.txtAreaPendingMeeting.setText(meeting.getMeetingPending());
    }

    @FXML
    private void cancelMeeting(ActionEvent event){
        if(meeting.getStatusMeeting().compareTo("Pendiente") == 0){
            Optional<ButtonType> action = GenericWindowDriver.getGenericWindowDriver().showConfirmacionAlert(event,
            "¿Seguro que deseas cancelar la reunión? Ya no se podrá iniciar.");
            if(action.get() == ButtonType.OK){
                meeting.setStatusMeeting("Cancelada");
                this.btnStartMeeting.setDisable(true);
                this.lbCanceledMetting.setVisible(true);
            }
        }else{
            GenericWindowDriver.getGenericWindowDriver().showInfoAlert(event, "Esta reunión no se puede cancelar");
        }
    }
    
    private void validateFieldComments()throws InvalidFormException {
        ValidatorForm.chechkAlphabeticalArea(txtAreaComment, 2 , 300);
    }

    @FXML
    private void addNewComment(ActionEvent event){
        try{
            validateFieldComments();
            meeting.getComments().add(getOutCommentInformation());
            this.preparedCommentTable();
            this.txtAreaComment.clear();
        }catch(InvalidFormException ex){
            GenericWindowDriver.getGenericWindowDriver().showErrorAlert(event, ex.getMessage());
        }
        
    }
    
    private Comment getOutCommentInformation(){
        String commentDescription = this.txtAreaComment.getText();
        String actualTime = date.get(Calendar.HOUR_OF_DAY) + ":" + date.get(Calendar.MINUTE) + ":" + date.get(Calendar.SECOND);
        String actualDate = date.get(Calendar.YEAR) + "-" + (date.get(Calendar.MONTH) + 1) + "-" + date.get(Calendar.DAY_OF_MONTH);
        Comment newComment = new Comment(0, commentDescription, token.getFullName(), actualTime, actualDate);
        return newComment;
    }

    @FXML
    private void closeMeetingWindow(ActionEvent event){
        MEETING_DAO.updateMeeting(meeting, meeting);
        FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("MeetingHistory.fxml", this.btnCloseMeetingWindow);
        MeetingHistoryController controller = loader.getController();
        controller.receiveToken(token);
    }

    @FXML
    private void deleteComment(ActionEvent event){
        Comment commentDeleted =  tvComments.getSelectionModel().getSelectedItem();
        if(this.token.getFullName().equals(commentDeleted.getCommentator())){
            meeting.removeComment(commentDeleted);
            this.preparedCommentTable();
        }else{
            GenericWindowDriver.getGenericWindowDriver().showInfoAlert(event, "No puede eliminar los comentarios de otros integrantes.");
        }
    }

    @FXML
    private void downloadMinute(ActionEvent event){
        GenericWindowDriver.getGenericWindowDriver().showInfoAlert(event, "Lo sentimos. La minuta no esta disponible por el momento");
    }

    @FXML
    private void startMeeting(ActionEvent event) {
        if(meeting.getStatusMeeting().compareTo("Pendiente") == 0){
            
        }else{
            GenericWindowDriver.getGenericWindowDriver().showInfoAlert(event, "Esta reunión ya no se puede iniciar");
        }
    }

    @FXML
    private void updateMeeting(ActionEvent event){
        String statusMeeting = meeting.getStatusMeeting();
        if((statusMeeting.compareTo("Pendiente") == 0) || (statusMeeting.compareTo("Cancelada") == 0)){
            MEETING_DAO.updateMeeting(meeting, meeting);
            FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("MeetingEdit.fxml", this.btnCloseMeetingWindow); 
            MeetingEditController controller = loader.getController();
            controller.reciveMeeting(meeting);
            controller.receiveToken(token);
            controller.addNewMeeting(false);
        }
        if(statusMeeting.compareTo("Realizada") == 0){
            MEETING_DAO.updateMeeting(meeting, meeting);
            FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("MeetingRealizedEdit.fxml", this.btnCloseMeetingWindow);
            MeetingRealizedEditController controller = loader.getController();
            controller.reciveMeeting(meeting);
            controller.receiveToken(token);
        }

    }
    
    private void preparedCommentTable(){
        colCommentator.setCellValueFactory(new PropertyValueFactory<Comment, String>("commentator"));
        colCommentDescription.setCellValueFactory(new PropertyValueFactory<Comment, String>("commentDescription"));
        tvComments.setItems(makeItemsForCommentTableView());
    
    }
    
    private void preparedAssistantTable(){
        colAssistantName.setCellValueFactory(new PropertyValueFactory<AssistantRol, String>("assistantName"));
        colRolAssistant.setCellValueFactory(new PropertyValueFactory<AssistantRol, String>("roleAssistant"));
        tvAssistantRol.setItems(makeItemsForAssistantRolTableView());
    }
    
    private void preparedPrerequisiteTable(){
        colDescriptionPrerequisite.setCellValueFactory(new PropertyValueFactory<Prerequisite, String>("descrptionPrerequisite"));
        colResponsiblePrerequisite.setCellValueFactory(new PropertyValueFactory<Prerequisite, String>("responsiblePrerequisite"));
        tvPrerequisite.setItems(makeItemsForPrerequisiteTableView());
    }
    
    private void preparedAgendaMeetingTable() {
        colStartTime.setCellValueFactory(new PropertyValueFactory<Topic, String>("startTime"));
        colEndTime.setCellValueFactory(new PropertyValueFactory<Topic, String>("endTime"));
        colPlannedTime.setCellValueFactory(new PropertyValueFactory<Topic, String>("plannedTime"));
        colRealTime.setCellValueFactory(new PropertyValueFactory<Topic, String>("realTime"));
        colDescriptionTopic.setCellValueFactory(new PropertyValueFactory<Topic, String>("descriptionTopic"));
        colDiscussionLeader.setCellValueFactory(new PropertyValueFactory<Topic, String>("discissionLeader"));
        tvMeetingAgenda.setItems(makeItemsForMeetingAgendaTableView());
    }
    
    private void preparedAgreementTable(){
        colDescriptionAgreement.setCellValueFactory(new PropertyValueFactory<Agreement, String>("descriptionAgreement"));
        colDeliveryDate.setCellValueFactory(new PropertyValueFactory<Agreement, String>("deliveryDate"));
        colResponsibleAgreement.setCellValueFactory(new PropertyValueFactory<Agreement, String>("responsibleAgreement"));
        tvAgreement.setItems(makeItemsForAgreementTableView());
    }
    
    private ObservableList<Comment> makeItemsForCommentTableView(){
        ObservableList<Comment> itemsComment = FXCollections.observableArrayList();
        List commentsList = meeting.getComments();
        itemsComment.addAll(commentsList);
        return itemsComment;
    }
    
    private ObservableList<AssistantRol> makeItemsForAssistantRolTableView(){
        ObservableList<AssistantRol> itemsAssisntantRol = FXCollections.observableArrayList();
        List assistantsList = meeting.getAssistantsRol();
        itemsAssisntantRol.addAll(assistantsList);
        return itemsAssisntantRol;
    }
    
    private ObservableList<Prerequisite> makeItemsForPrerequisiteTableView(){
        ObservableList<Prerequisite> itemsPrerequisite = FXCollections.observableArrayList();
        List prerequisitesList = meeting.getMeetingAgenda().getPrerequisites();
        itemsPrerequisite.addAll(prerequisitesList);
        return itemsPrerequisite;
    }
    
    private ObservableList<Topic> makeItemsForMeetingAgendaTableView(){
        ObservableList<Topic>itemsTopic = FXCollections.observableArrayList();
        List topicsList = meeting.getMeetingAgenda().getTopics();
        itemsTopic.addAll(topicsList);
        return itemsTopic;
    }
    
    private ObservableList<Agreement> makeItemsForAgreementTableView(){
        ObservableList<Agreement> itemsAgreement = FXCollections.observableArrayList();
        List agreementsList = meeting.getAgreements();
        itemsAgreement.addAll(agreementsList);
        return itemsAgreement;
    }
    
    private void comproveMeetingDifferentNull(Meeting meeting){ 
        if (meeting == null){
            GenericWindowDriver.getGenericWindowDriver().showErrorAlert(new ActionEvent(), "Lo sentimos,no se pudo encontrar la información de la reunión");
            FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("MeetingHistory.fxml", this.btnCloseMeetingWindow);
            MeetingHistoryController controller = loader.getController();
            controller.receiveToken(token);
        }
    }
}
