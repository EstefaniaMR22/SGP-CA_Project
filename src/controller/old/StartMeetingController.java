/*
* @author Johann
* @versión v1.0
* Last modification date: 17-06-2021
*/

package controller.old;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import model.old.dataaccess.MeetingDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import controller.old.pattern.InvalidFormException;
import controller.old.pattern.ValidatorForm;
import model.old.domain.Agreement;
import model.old.domain.Integrant;
import model.old.domain.Meeting;
import model.old.domain.MeetingAgenda;
import model.old.domain.Topic;

public class StartMeetingController implements Initializable{

    @FXML
    private TableView<Topic> tvAgenda;
    @FXML
    private TableColumn<Topic, String> colDescriptionTopic;
    @FXML
    private TableColumn<Topic, String> colStartTime;
    @FXML
    private TableColumn<Topic, String> colEndTime;
    @FXML
    private TextField txtFieldDescriptionAgreement;
    @FXML
    private TextField txtFieldResponsibleAgreement;
    @FXML
    private Button btnAddAgreement;
    @FXML
    private Button btnDeleteAgreement;
    @FXML
    private TableView<Agreement> tvAgreement;
    @FXML
    private TableColumn<Agreement, String> coldescriptionAgreement;
    @FXML
    private TableColumn<Agreement, String> colResponsibleAgreement;
    @FXML
    private TableColumn<Agreement, Date> coldeliveryDate;
    @FXML
    private Button btnNextTopic;
    @FXML
    private TextField txtFieldCurrentTopic;
    @FXML
    private TextArea txtAreaNoteMeeting;
    @FXML
    private TextArea txtAreaPendingMeeting;
    @FXML
    private Button btnEndMeeting;
    @FXML
    private Button btnExitMeeting;
    @FXML
    private Label lbUserName;

    private Meeting meeting;
    private final MeetingDAO MEETING_DAO = new MeetingDAO();
    private Integrant token;
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        lbUserName.setText(token.getFullName());
        coldescriptionAgreement.setCellValueFactory(new PropertyValueFactory<>("descriptionAgreement"));
        colResponsibleAgreement.setCellValueFactory(new PropertyValueFactory<>("responsibleAgreement"));
        coldeliveryDate.setCellValueFactory(new PropertyValueFactory<>("deliveryDate"));
        
    }    

    public void receiveMeetingKey(int meetingKey, Integrant token){
        this.meeting = MEETING_DAO.getMeeting(meetingKey);
        this.token = token;
        preparedAgendaMeetingTable();
    }

    @FXML
    private void addAgreement(ActionEvent event){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        List<String> listRequirements = new ArrayList<>();
         try{
            this.isValidFormAgreement();
            Agreement agreement = new Agreement(
                txtFieldDescriptionAgreement.getText(),
                txtFieldResponsibleAgreement.getText(),
                dateFormat.format(date)
            );
            ObservableList<Agreement> itemsAgreement = FXCollections.observableArrayList();
            itemsAgreement.add(agreement);
            tvAgreement.getItems().addAll(itemsAgreement);
            txtFieldDescriptionAgreement.setText("");
            txtFieldResponsibleAgreement.setText("");
        }catch(InvalidFormException ie){
             WindowControllerB.getWindowController().showErrorAlert(new ActionEvent(), ie.getMessage());
        }
    }

    @FXML
    private void deleteAgreement(ActionEvent event){
        int indexSelection = tvAgreement.getSelectionModel().getSelectedIndex();
        if(indexSelection >= 0){
            tvAgreement.getItems().remove(indexSelection);
        }else{
            WindowControllerB.getWindowController().showErrorAlert(new ActionEvent(), "No a seleccionado un acuerdo");
        }
    }

    @FXML
    private void endMeeting(ActionEvent event){
        MeetingAgenda meetingAgenda = new MeetingAgenda();
          try{
            this.isValidForm();
            meeting.setAgreements(tvAgreement.getItems());
            meeting.setMeetingNote(txtAreaNoteMeeting.getText());
            meeting.setMeetingPending(txtAreaPendingMeeting.getText());
            meeting.setStatusMeeting("Finalizada");
            meetingAgenda.setTopics(listTopic());
            meeting.setMeetingAgenda(meetingAgenda);
            if(MEETING_DAO.updateMeeting(meeting, meeting)){
                WindowControllerB.getWindowController().showInfoAlert(event, "Se actualizó correctamente");
            }else{
                WindowControllerB.getWindowController().showErrorAlert(event, "Lo sentimos ocurrió un Error");
            }
            }catch(InvalidFormException ie){
            WindowControllerB.getWindowController().showErrorAlert(new ActionEvent(), ie.getMessage());
        }
          FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("MeetingRequest.fxml", txtFieldDescriptionAgreement);
          MeetingRequestController controller = loader.getController();
          controller.receiveToken(token);
    }

    @FXML
    private void exitMeeting(ActionEvent event){
        FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("MeetingRequest.fxml", txtFieldDescriptionAgreement);
        MeetingRequestController controller = loader.getController();
        controller.receiveToken(token);
    }
    
     private void preparedAgendaMeetingTable(){
        colDescriptionTopic.setCellValueFactory(new PropertyValueFactory<Topic, String>("descriptionTopic"));
        colStartTime.setCellValueFactory(new PropertyValueFactory<Topic, String>("startTime"));
        colEndTime.setCellValueFactory(new PropertyValueFactory<Topic, String>("endTime"));
        tvAgenda.setItems(makeItemsForMeetingAgendaTableView());
        txtFieldCurrentTopic.setText(tvAgenda.getItems().get(0).getDescriptionTopic());
    }
    
    private ObservableList<Topic> makeItemsForMeetingAgendaTableView(){
        DateTimeFormatter FormatTime = DateTimeFormatter.ofPattern("HH:mm");
        ObservableList<Topic>itemsTopic = FXCollections.observableArrayList();
        List topicsList = meeting.getMeetingAgenda().getTopics();
        itemsTopic.addAll(topicsList);
        itemsTopic.get(0).setStartTime(FormatTime.format(LocalDateTime.now()));
        return itemsTopic;
    }

    @FXML
    private void nextTopic(ActionEvent event){
        DateTimeFormatter FormatTime = DateTimeFormatter.ofPattern("HH:mm");
        for(int i = 0; i < tvAgenda.getItems().size(); i++){
            if (tvAgenda.getItems().get(i).getEndTime().equals("00:00:00")){
                tvAgenda.getItems().get(i).setEndTime(FormatTime.format(LocalDateTime.now()));
                tvAgenda.getItems().get(i).setStatusTopic("Realizada");
                if(tvAgenda.getItems().get(i+1) == null){
                    btnNextTopic.setDisable(true);
                }else{
                    tvAgenda.getItems().get(i+1).setStartTime(FormatTime.format(LocalDateTime.now()));
                    txtFieldCurrentTopic.setText(tvAgenda.getItems().get(i+1).getDescriptionTopic());
                }
                break;
            }
        }
        tvAgenda.refresh();
    }
    
    private void isValidFormAgreement() throws InvalidFormException{
        ValidatorForm.chechkAlphabeticalField(txtFieldDescriptionAgreement, 5 ,2000);
        ValidatorForm.chechkAlphabeticalField(txtFieldResponsibleAgreement, 3 ,80);
    }
    
    private void isValidForm() throws InvalidFormException{
        ValidatorForm.chechkAlphabeticalArea(txtAreaNoteMeeting, 5 ,2000);
        ValidatorForm.chechkAlphabeticalArea(txtAreaPendingMeeting, 3 ,80);
    }
    
    private List<Topic> listTopic(){
        List<Topic> listTopics = new ArrayList<>();
        for(Topic topic : tvAgenda.getItems()){
            listTopics.add(topic);
        }
        return listTopics;
    }
}