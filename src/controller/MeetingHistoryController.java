/**
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import controller.pattern.GenericWindowDriver;
import model.domain.Integrant;
import model.domain.Meeting;
import model.dataaccess.MeetingDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class MeetingHistoryController implements Initializable{
    
    @FXML
    private Label lbUserName;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnSearchMeeting;
    @FXML
    private TextField txtFieldMeetingIssue;
    @FXML
    private Button btnAddMeting;
    @FXML
    private DatePicker dtpMeetingDate;
    @FXML
    private TableView<Meeting> tvMeetingHistory;
    @FXML
    private TableColumn<Meeting, String> colIssueMeeting;
    @FXML
    private TableColumn<Meeting, String> colMeetingDate;
    @FXML
    private TableColumn<Meeting, String> colMeetingTime;
    @FXML
    private TableColumn<Meeting, String> colIntegrantResponsibleMeeting;
    @FXML
    private Button btnRefreshTable;
    
    private final MeetingDAO MEETING_DAO = new MeetingDAO();
    private List<Meeting> meetingsList = new ArrayList<>();
    private Integrant token;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        preparedMeetingTable();
    }   
    
    public void receiveToken(Integrant integrantToken){
        this.token = integrantToken;
        this.lbUserName.setText(token.getFullName());
        preparedMeetingTable();
    }
    
    @FXML
    private void addMeeting(ActionEvent event){
        FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("MeetingEdit.fxml", btnClose);
        MeetingEditController controller = loader.getController();
        controller.receiveToken(token);
        controller.addNewMeeting(true);
    }

    @FXML
    private void closeMeetingHistoryWindow(ActionEvent event){
        FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("Start.fxml", btnClose);
        StartController controller = loader.getController();
        controller.receiveIntegrantToken(token);
    }

    @FXML
    private void refreshMeetingHistoryMeeting(ActionEvent event){
        tvMeetingHistory.getItems().clear();
        setAllMeetingInformationIntoTable();
    }
    
    @FXML
    private void observeMeetingInformation(MouseEvent event){
        if(tvMeetingHistory.getSelectionModel().getSelectedItem() != null){
            FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("MeetingRequest.fxml", btnClose);
            MeetingRequestController controller = loader.getController();
            controller.receiveToken(token);
            controller.reciveMeeting(
                tvMeetingHistory.getSelectionModel().getSelectedItem().getMeetingKey()
            );
        }
    }

    @FXML
    private void searchMeeting(ActionEvent event){
        System.out.print(this.txtFieldMeetingIssue.getText());
        if((txtFieldMeetingIssue.getText()).equals(null)){
            if((this.dtpMeetingDate.getValue())!=null){
                tvMeetingHistory.getItems().clear();
                setMeetingInformationByDate();
            }    
        }else {
            if((this.dtpMeetingDate.getValue())!=null){
                tvMeetingHistory.getItems().clear();
                setMeetingInformationByDateAndIssueIntoTable();
            }else{
                tvMeetingHistory.getItems().clear();
                setMeetingInformationByIssue();
            }  
        }
        this.txtFieldMeetingIssue.clear();
        this.dtpMeetingDate.setValue(null);
    }
    
    private ObservableList<Meeting> makeitemsAllMeetings(){
        ObservableList<Meeting> itemsMeeting = FXCollections.observableArrayList();
        meetingsList.addAll(MEETING_DAO.getAllMeetings());
        itemsMeeting.addAll(meetingsList);
        return itemsMeeting;
    }
    
    private ObservableList<Meeting> makeitemsMeetings(List meetingList){
        ObservableList<Meeting> itemsMeeting = FXCollections.observableArrayList();
        itemsMeeting.addAll(meetingList);
        return itemsMeeting;
    }
    
    private void preparedMeetingTable(){
        colIssueMeeting.setCellValueFactory(new PropertyValueFactory<Meeting, String>("issueMeeting"));
        colMeetingDate.setCellValueFactory(new PropertyValueFactory<Meeting, String>("meetingDate"));
        colMeetingTime.setCellValueFactory(new PropertyValueFactory<Meeting, String>("meetingTime"));
        colIntegrantResponsibleMeeting.setCellValueFactory(new PropertyValueFactory<Meeting,String>("integrantResponsible"));
        tvMeetingHistory.setItems(makeitemsAllMeetings());
    }
    
    private void setAllMeetingInformationIntoTable(){
        List meetingslist = this.meetingsList;
        tvMeetingHistory.setItems(makeitemsMeetings(meetingslist));
    }
    
    private void setMeetingInformationByDateAndIssueIntoTable(){
        String dateMeeting = this.dtpMeetingDate.getValue().toString();
        String meetingIssue = (this.txtFieldMeetingIssue.getText());
        List meetingslist = new ArrayList<>();
        for(int i=0; i< this.meetingsList.size(); i++){
            if((this.meetingsList.get(i).getMeetingDate().equals(dateMeeting)) && 
            (this.meetingsList.get(i).getIssueMeeting().contains(meetingIssue))){
                meetingslist.add(this.meetingsList.get(i));
            }
        }
       tvMeetingHistory.setItems(makeitemsMeetings(meetingslist));
    }
    
    private void setMeetingInformationByIssue(){ 
        String meetingIssue = (this.txtFieldMeetingIssue.getText());
        List meetingslist = new ArrayList<>();
        for(int i=0; i< this.meetingsList.size(); i++){
            if(this.meetingsList.get(i).getIssueMeeting().contains(meetingIssue)){
                meetingslist.add(this.meetingsList.get(i));
            }
        }
        tvMeetingHistory.setItems(makeitemsMeetings(meetingslist));
    }
    
    private void setMeetingInformationByDate(){
        String meetingDate = this.dtpMeetingDate.getValue().toString();
        System.out.print(meetingDate);
        List meetingslist = new ArrayList<>();
        for(int i=0; i<this.meetingsList.size(); i++){
            if(this.meetingsList.get(i).getMeetingDate().equals(meetingDate)){
                meetingslist.add(this.meetingsList.get(i));
            }
        }
        tvMeetingHistory.setItems(makeitemsMeetings(meetingslist));   
    }
    
}
