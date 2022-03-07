/*
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package controller.old;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import controller.old.pattern.InvalidFormException;
import model.old.dataaccess.IntegrantDAO;
import model.old.dataaccess.MeetingDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import controller.old.pattern.ValidatorForm;
import model.old.domain.AssistantRol;
import model.old.domain.Integrant;
import model.old.domain.Meeting;
import model.old.domain.Prerequisite;
import model.old.domain.Topic;

public class MeetingEditController implements Initializable{
    
    @FXML
    private Label lbWindowName;
    @FXML
    private Label lblUserName;
    @FXML
    private Button btnUpdateMeeting;
    @FXML
    private Button btnRegisterMeeting;
    @FXML
    private Button btnClose;
    @FXML
    private TextField txtFieldMeetingProject;
    @FXML
    private TextField txtFieldPlaceMeeting;
    @FXML
    private DatePicker dtpMeetingDate;
    @FXML
    private ComboBox<String> cboBoxMeetingHour;
    @FXML
    private ComboBox<String> cboBoxMeetingMinute;
    @FXML
    private TextField txtFieldIssueMeeting;
    @FXML
    private ComboBox<String> cboBoxDiscussionLeader;
    @FXML
    private ComboBox<String> cboBoxTimeTaker;
    @FXML
    private ComboBox<String> cboBoxSecretary;
    @FXML
    private Button btnAddRowPrerequisiteTable;
    @FXML
    private Button btnRemoveRowPrerequisiteTable;
    @FXML
    private TextField txtFieldDescriptionPrerequisite;
    @FXML
    private ComboBox<String> cboBoxResponsiblePrerequisite;
    @FXML
    private TableView<Prerequisite> tvPrerequisite;
    @FXML
    private TableColumn<Prerequisite, String> colDescriptionPrerequisite;
    @FXML
    private TableColumn<Prerequisite, String> colResponsiblePrerequisite;
    @FXML
    private Button btnAddRowMeetingAgendaTable;
    @FXML
    private Button btnRemoveRowMeetingAgendaTable;
    @FXML
    private ComboBox<String> cboBoxHourTopic;
    @FXML
    private ComboBox<String> cboBoxMinuteTopic;
    @FXML
    private TextField txtFieldDescriptionTopic;
    @FXML
    private ComboBox<String> cboBoxDiscissionLeaderTopic;
    @FXML
    private TableView<Topic> tvMeetingAgenda;
    @FXML
    private TableColumn<Topic, String> colEstimatedTime;
    @FXML
    private TableColumn<Topic, String> colDescriptionTopic;
    @FXML
    private TableColumn<Topic, String> colDiscussionLeaderTopic;
    @FXML
    private Label lbUserName;
    
    private final IntegrantDAO INTEGRANT_DAO = new IntegrantDAO();
    private final MeetingDAO MEETING_DAO = new MeetingDAO();
    private Meeting meeting = new Meeting();
    Calendar date = new GregorianCalendar();
    private Integrant token;
    private boolean addNewMeeting;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        preparePrerequisiteTable();
        prepareMeetingAgendaTable();
    } 
    
    public void receiveToken(Integrant integrantToken){
        this.token = integrantToken;
        this.lbUserName.setText(token.getFullName());
        setMeetingFormCampsInformation();
    }
    
    public void reciveMeeting (Meeting meeting){ 
        this.meeting = meeting;
        setMeetingInformation();
        preparePrerequisiteTable();
        prepareMeetingAgendaTable();
    }
    
    private void setMeetingInformation(){
        LocalDate meetingDate = LocalDate.parse(meeting.getMeetingDate());
        this.dtpMeetingDate.setValue(meetingDate);
        this.cboBoxMeetingHour.setValue(meeting.getMeetingTime().substring(0, 2));
        this.cboBoxMeetingMinute.setValue(meeting.getMeetingTime().substring(3, 5));
        this.txtFieldMeetingProject.setText(meeting.getMeetingProject());
        this.txtFieldPlaceMeeting.setText(meeting.getPlaceMeeting());
        this.txtFieldIssueMeeting.setText(meeting.getIssueMeeting());
        setMeetingAssistants();
    }
    
    private void setMeetingAssistants(){
        for(AssistantRol assistant : this.meeting.getAssistantsRol()){
            if(assistant.getRoleAssistant().equals("Líder de discusión")){
                this.cboBoxDiscussionLeader.setValue(assistant.getAssistantName());
            }
            if(assistant.getRoleAssistant().equals("Tomador de tiempo")){
                this.cboBoxTimeTaker.setValue(assistant.getAssistantName());
            }
            if(assistant.getRoleAssistant().equals("Secretario")){
                this.cboBoxSecretary.setValue(assistant.getAssistantName());
            }
        }
    }
    
    
    public void addNewMeeting(Boolean addedNewMeeting){
        this.addNewMeeting = addedNewMeeting;
        if(!addNewMeeting){
            this.btnRegisterMeeting.setVisible(false);
            this.lbWindowName.setText("MODIFICAR REUNIÓN");
        }else{
            this.btnUpdateMeeting.setVisible(false);
        }
    }
    
    private void setMeetingFormCampsInformation(){
        ObservableList<String> itemsIntegrant = makeitemsIntegrantForComboBox();
        this.cboBoxMeetingHour.setItems(makeitemsHoursList());
        this.cboBoxMeetingMinute.setItems(makeitemsMinutesList());
        this.cboBoxDiscussionLeader.setItems(itemsIntegrant);
        this.cboBoxTimeTaker.getItems().addAll(itemsIntegrant);
        this.cboBoxSecretary.setItems(itemsIntegrant);
        this.cboBoxResponsiblePrerequisite.setItems(itemsIntegrant);
        this.cboBoxHourTopic.setItems(makeitemsHoursListForTopic());
        this.cboBoxMinuteTopic.setItems(makeitemsMinutesListForTopic());
        this.cboBoxDiscissionLeaderTopic.setItems(itemsIntegrant);
    }
    
    @FXML
    private void addNewMeeting(ActionEvent event){
        try{
            validateMeetingInformation();
            this.getOutMeetingInformation();
            this.meeting.setMeetingKey(0);
            this.setIntoMeetingAgendaInformation();
            this.meeting.getMeetingAgenda().setMeetingAgendaKey(0);
            if(MEETING_DAO.addMeeting(this.meeting)){
                WindowControllerB.getWindowController().showInfoAlert(event, "Reunión agendada con éxito");
            }else{
                WindowControllerB.getWindowController().showErrorAlert(event, "Error en el sistema, favor de ponerse en contacto con sopoerte técnico");
            }
            FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("MeetingHistory.fxml", btnClose);
            MeetingHistoryController controller = loader.getController();
            controller.receiveToken(token);
        }catch(InvalidFormException ex){
            WindowControllerB.getWindowController().showErrorAlert(event, ex.getMessage());
        }
    }
    
    private void validateMeetingInformation() throws InvalidFormException{
        ValidatorForm.checkNotEmptyDateField(this.dtpMeetingDate);
        validateDate();
        ValidatorForm.chechkAlphabeticalField(this.txtFieldMeetingProject, 1, 80);
        ValidatorForm.isComboBoxSelected(this.cboBoxDiscussionLeader);
        ValidatorForm.isComboBoxSelected(this.cboBoxTimeTaker);
        ValidatorForm.isComboBoxSelected(this.cboBoxSecretary);
        ValidatorForm.isComboBoxSelected(this.cboBoxMeetingHour);
        ValidatorForm.isComboBoxSelected(this.cboBoxMeetingMinute);
        ValidatorForm.chechkAlphabeticalField(this.txtFieldPlaceMeeting, 1 ,  80);
        ValidatorForm.chechkAlphabeticalField(this.txtFieldIssueMeeting,1, 200);
        
    }
    
    private void validateDate() throws InvalidFormException{
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd");
            Date meetingDate = dateFormat.parse(this.dtpMeetingDate.getValue().toString());
            Date actualDate = dateFormat.parse(date.get(Calendar.YEAR) + "-" + (date.get(Calendar.MONTH) + 1) + "-" + date.get(Calendar.DAY_OF_MONTH));
            if(meetingDate.before(actualDate)){
                this.dtpMeetingDate.setStyle("-fx-border-color: red;");
                throw new InvalidFormException("La fecha de reunión no puede ser menor a la fecha actual");
            }else{
                validateTime();
            } 
        } catch (ParseException ex) {
            Logger.getLogger(MeetingEditController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void validateTime() throws InvalidFormException{
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat ("HH:mm");
            Date meetingDate = dateFormat.parse(this.cboBoxMeetingHour.getValue() +  ":" + this.cboBoxMeetingMinute.getValue());
            Date actualDate = dateFormat.parse(date.get(Calendar.HOUR_OF_DAY) + ":" + (date.get(Calendar.MINUTE) + 4));
            if(meetingDate.before(actualDate)){
                this.dtpMeetingDate.setStyle("-fx-border-color: red;");
                throw new InvalidFormException("La hora de reunión no puede ser anterior a la hora actual");
            } 
        }catch(ParseException ex){
            Logger.getLogger(MeetingEditController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void getOutMeetingInformation(){
        String hourMeeting = this.cboBoxMeetingHour.getValue();
        String minuteMeting = this.cboBoxMeetingMinute.getValue();
        this.meeting.setMeetingDate(this.dtpMeetingDate.getValue().toString());
        this.meeting.setMeetingTime(hourMeeting + ":" + minuteMeting + ":00");
        this.meeting.setMeetingProject(this.txtFieldMeetingProject.getText());
        this.meeting.setMeetingRegistrationDate(
            date.get(Calendar.YEAR) + "-" + (date.get(Calendar.MONTH) + 1) + "-" + date.get(Calendar.DAY_OF_MONTH)
        );
        this.meeting.setStatusMeeting("Pendiente");
        this.meeting.setPlaceMeeting(this.txtFieldPlaceMeeting.getText());
        this.meeting.setIssueMeeting(this.txtFieldIssueMeeting.getText());
        this.meeting.setIntegrantResponsible(token.getRfc());
        this.meeting.setMeetingNote("");
        this.meeting.setMeetingPending("");
        getMeetingAssistants();
    }
    
    private void getMeetingAssistants(){
        String leaderDiscussion = this.cboBoxDiscussionLeader.getSelectionModel().getSelectedItem();
        AssistantRol assistantLeader = new AssistantRol(0, leaderDiscussion, "Líder de discusión", 1);
        this.meeting.getAssistantsRol().add(assistantLeader);
        String timeTaker = this.cboBoxTimeTaker.getSelectionModel().getSelectedItem();
        AssistantRol assistantTimeTaker = new AssistantRol(0, timeTaker, "Tomador de tiempo", 2);
        this.meeting.getAssistantsRol().add(assistantTimeTaker);
        String secretary = this.cboBoxSecretary.getSelectionModel().getSelectedItem();
        AssistantRol assistantSecretary = new AssistantRol(0, secretary, "Secretario", 3);
        this.meeting.getAssistantsRol().add(assistantSecretary);
    }
    
    
    private void setIntoMeetingAgendaInformation(){
        this.meeting.getMeetingAgenda().setTotalTime("00:00:00");
        this.meeting.getMeetingAgenda().setEstimatedTotalTime("00:00:00");
        this.meeting.getMeetingAgenda().setTotaltopics(meeting.getMeetingAgenda().getTopics().size());
    }
    
    private void validateTopicInformation() throws InvalidFormException{
        ValidatorForm.isComboBoxSelected(this.cboBoxHourTopic);
        ValidatorForm.isComboBoxSelected(this.cboBoxMinuteTopic);
        ValidatorForm.chechkAlphabeticalField(this.txtFieldDescriptionTopic, 3 , 200);
        ValidatorForm.isComboBoxSelected(this.cboBoxDiscissionLeaderTopic);
    }

    @FXML
    private void addRowMeetingAgendaTable(ActionEvent event){
        try{
            validateTopicInformation();
            this.meeting.getMeetingAgenda().getTopics().add(getOutTopicInformtation());
            tvMeetingAgenda.setItems(makeTopicItems());
            clearCampsOfTopic();
        }catch(InvalidFormException ex){
            WindowControllerB.getWindowController().showErrorAlert(event, ex.getMessage());
        }
    }
    
    private Topic getOutTopicInformtation(){
        String hourTopic = this.cboBoxHourTopic.getValue();
        String minuteTopic = this.cboBoxMinuteTopic.getValue();
        String planedTimeTopic = hourTopic + ":" + minuteTopic + ":00";
        String descriptionTopic = this.txtFieldDescriptionTopic.getText();
        String discissionLeader = this.cboBoxDiscissionLeaderTopic.getValue();
        
        Topic newTopic = new Topic(
            0, "00:00:00", "00:00:00", planedTimeTopic, "00:00:00", descriptionTopic, discissionLeader, "Pendiente"
        );
        return newTopic;
    }
    
    private void clearCampsOfTopic(){
        this.cboBoxHourTopic.setValue(null);
        this.cboBoxMinuteTopic.setValue(null);
        this.txtFieldDescriptionTopic.clear();
        this.cboBoxDiscissionLeaderTopic.setValue(null);
    }
    
    private void validatePrerequisiteInformation() throws InvalidFormException{
        ValidatorForm.chechkAlphabeticalField(this.txtFieldDescriptionPrerequisite, 1 ,  200);
        ValidatorForm.isComboBoxSelected(this.cboBoxResponsiblePrerequisite);
    }

    @FXML
    private void addRowPrerequisiteTable(ActionEvent event){
        try{
            validatePrerequisiteInformation();
            meeting.getMeetingAgenda().getPrerequisites().add(getOutPrerequisiteInformation());
            tvPrerequisite.setItems(makePrerequisiteItems());
            clearCampsOfPrerequisite();
        }catch(InvalidFormException ex){
            WindowControllerB.getWindowController().showErrorAlert(event, ex.getMessage());
        }
    }
    
    private Prerequisite getOutPrerequisiteInformation(){
        String descriptionPrerequisite = this.txtFieldDescriptionPrerequisite.getText();
        String responsiblePrerequisite = this.cboBoxResponsiblePrerequisite.getValue();
        Prerequisite newPrerequisite = new Prerequisite(
            0, responsiblePrerequisite, descriptionPrerequisite
        );
        return newPrerequisite;
    }
    
    private void clearCampsOfPrerequisite(){
        this.txtFieldDescriptionPrerequisite.clear();
        this.cboBoxResponsiblePrerequisite.setValue(null);
    }

    @FXML
    private void closeMeetingFormWindow(ActionEvent event){
        Optional<ButtonType> action = WindowControllerB.getWindowController().showConfirmacionAlert(event,
        "¿Seguro que deseas salir del registro de reunión? Los cambios no se guardarán");
        if(action.get() == ButtonType.OK){
            if(this.addNewMeeting){
                FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("MeetingHistory.fxml", btnClose);
                MeetingHistoryController controller = loader.getController();
                controller.receiveToken(token);
            }else{
                FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("MeetingRequest.fxml", btnClose);
                MeetingRequestController controller = loader.getController();
                controller.reciveMeeting(meeting.getMeetingKey());
                controller.receiveToken(token);
            }
        }
    }

    @FXML
    private void removeRowMeetingAgendaTable(ActionEvent event){
        Topic topicToRemove = tvMeetingAgenda.getSelectionModel().getSelectedItem();
        meeting.getMeetingAgenda().getTopics().remove(topicToRemove);
        tvMeetingAgenda.setItems(makeTopicItems());
    }

    @FXML
    private void removeRowPrerequisiteTable(ActionEvent event){
        Prerequisite prerequisiteToRemove = tvPrerequisite.getSelectionModel().getSelectedItem();
        meeting.getMeetingAgenda().getPrerequisites().remove(prerequisiteToRemove);
        tvPrerequisite.setItems(makePrerequisiteItems());
    }
    
    @FXML
    private void updateMeeting(ActionEvent event){
        try{
            validateMeetingInformation();
            this.getOutMeetingInformation();
            this.setIntoMeetingAgendaInformation();
            if(MEETING_DAO.updateMeeting(meeting, meeting)){
                WindowControllerB.getWindowController().showInfoAlert(event, "Reunión modificada con éxito");
            }else{
                WindowControllerB.getWindowController().showErrorAlert(event, "Error en el sistema, favor de ponerse en contacto con soporte técnico");
            }
            FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("MeetingRequest.fxml", btnClose);
            MeetingRequestController controller = loader.getController();
            controller.receiveToken(token);
            controller.reciveMeeting(meeting.getMeetingKey());
        }catch(InvalidFormException ex){
            WindowControllerB.getWindowController().showErrorAlert(event, ex.getMessage());
        }
    } 
    
    private void preparePrerequisiteTable(){
        colDescriptionPrerequisite.setCellValueFactory(new PropertyValueFactory<Prerequisite, String>("descrptionPrerequisite"));
        colResponsiblePrerequisite.setCellValueFactory(new PropertyValueFactory<Prerequisite, String>("responsiblePrerequisite"));
        tvPrerequisite.setItems(makePrerequisiteItems());
    }
    
    private void prepareMeetingAgendaTable(){
        colEstimatedTime.setCellValueFactory(new PropertyValueFactory<Topic, String>("plannedTime"));
        colDescriptionTopic.setCellValueFactory(new PropertyValueFactory<Topic, String>("descriptionTopic"));
        colDiscussionLeaderTopic.setCellValueFactory(new PropertyValueFactory<Topic, String>("discissionLeader"));
        tvMeetingAgenda.setItems(makeTopicItems());
    }
    
    private ObservableList<Topic> makeTopicItems(){
        ObservableList<Topic> itemsTopic = FXCollections.observableArrayList();
        List<Topic> topicsList = meeting.getMeetingAgenda().getTopics();
        itemsTopic.addAll(topicsList);
        return itemsTopic;
    }
    
    private ObservableList<Prerequisite> makePrerequisiteItems(){
        ObservableList<Prerequisite> itemsPrerequisite = FXCollections.observableArrayList();
        List<Prerequisite> prerequisiteList = meeting.getMeetingAgenda().getPrerequisites();
        itemsPrerequisite.addAll(prerequisiteList);
        return itemsPrerequisite;
    }
    
    private ObservableList<String> makeitemsIntegrantForComboBox(){
        ObservableList<String> itemsIntegrant = FXCollections.observableArrayList();
        List<String> integrantsName = INTEGRANT_DAO.getAllIntegrantsName(token.getBodyAcademyKey());
        itemsIntegrant.addAll(integrantsName);
        return itemsIntegrant;
    }
    
    private ObservableList<String> makeitemsHoursList(){
        ObservableList<String> itemsHours = FXCollections.observableArrayList();
        int hours = 24;
        for(int i = 1; i <= hours; i++){
            if(i<10){
                itemsHours.add("0" + i);
            }else{
                itemsHours.add("" + i);
            }
        }
        return itemsHours;
    }
    
    private ObservableList<String> makeitemsHoursListForTopic(){
        ObservableList<String> itemsHours = FXCollections.observableArrayList();
        int hours = 24;
        for(int i = 0; i <= hours; i++){
            if(i<10){
                itemsHours.add("0" + i);
            }else{
                itemsHours.add("" + i);
            }
        }
        return itemsHours;
    }
    
    private ObservableList<String> makeitemsMinutesList(){
        ObservableList<String> itemsMinutes = FXCollections.observableArrayList();
        int minutes = 60;
        for(int i = 0; i < minutes; i++){
            if(i<10){
                itemsMinutes.add("0" + i);
            }else{
                itemsMinutes.add("" + i);
            }
        }
        return itemsMinutes;
    }
    
    private ObservableList<String> makeitemsMinutesListForTopic(){
        ObservableList<String> itemsMinutes = FXCollections.observableArrayList();
        int minutes = 60;
        for(int i = 1; i < minutes; i++){
            if(i<10){
                itemsMinutes.add("0" + i);
            }else{
                itemsMinutes.add("" + i);
            }
        }
        return itemsMinutes;
    }
    
    private void comproveMeetingDifferentNull(Meeting meeting){
        if(meeting == null){
            WindowControllerB.getWindowController().showErrorAlert(null, "Lo sentimos,no se pudo encontrar la información de la reunión");
            FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("MeetingHistory.fxml", null);
            MeetingHistoryController controller = loader.getController();
            controller.receiveToken(token);
        }
    }
    
}
