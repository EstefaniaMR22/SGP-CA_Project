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
import model.domain.Agreement;
import model.domain.Integrant;
import model.domain.Meeting;
import model.dataaccess.IntegrantDAO;
import model.dataaccess.MeetingDAO;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import controller.old.pattern.ValidatorForm;

public class MeetingRealizedEditController implements Initializable{
    
    @FXML
    private Label lbUserName;
    @FXML
    private Button btnUpdateMeeting;
    @FXML
    private Button btnCloseUpdateMeetingWindow;
    @FXML
    private Button btnAddAgreementFile;
    @FXML
    private Button btnUpdateAgreementFile;
    @FXML
    private Button btnDeleteAgreementFile;
    @FXML
    private TextField txtFieldDescriptionAgreement;
    @FXML
    private DatePicker dtpDeliveryDate;
    @FXML
    private ComboBox<String> cboBoxResponsibleAgreement;
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
    
    private final IntegrantDAO INTEGRANT_DAO = new IntegrantDAO();
    private final MeetingDAO MEETING_DAO = new MeetingDAO();
    private Meeting meeting = new Meeting();
    Calendar date = new GregorianCalendar();
    private Integrant token;
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
    }
    
    public void reciveMeeting (Meeting meeting){
        this.meeting = meeting;
        setMeetingInformation();
        prepareAgreementTable();
    }
    
    public void receiveToken(Integrant integrantToken){
        this.token = integrantToken;
        this.lbUserName.setText(token.getFullName());
        this.cboBoxResponsibleAgreement.setItems(makeitemsIntegrantForComboBox());
    }
    
    private void setMeetingInformation(){
        this.txtAreaNoteMeeting.setText(meeting.getMeetingNote());
        this.txtAreaPendingMeeting.setText(meeting.getMeetingPending());
    }
    
    private void validateAgreementCamps() throws InvalidFormException {
        ValidatorForm.chechkAlphabeticalField(this.txtFieldDescriptionAgreement, 1 , 200);
        ValidatorForm.checkNotEmptyDateField(this.dtpDeliveryDate);
        validateDate();
        ValidatorForm.isComboBoxSelected(this.cboBoxResponsibleAgreement);
    }
    
    private void validateDate() throws InvalidFormException{
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd");
            Date meetingDate = dateFormat.parse(this.dtpDeliveryDate.getValue().toString());
            Date actualDate = dateFormat.parse(date.get(Calendar.YEAR) + "-" + (date.get(Calendar.MONTH) + 1) + "-" + date.get(Calendar.DAY_OF_MONTH));
            if(meetingDate.before(actualDate)){
                this.dtpDeliveryDate.setStyle("-fx-border-color: red;");
                throw new InvalidFormException("La fecha de entrega del acuerdo no puede ser menor a la fecha actual");
            }
        }catch(ParseException ex){
            Logger.getLogger(MeetingEditController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    @FXML
    private void addAgreementFile(ActionEvent event){
        try{
            validateAgreementCamps();
            meeting.getAgreements().add(getOutCommentInformation());
            this.prepareAgreementTable();
            clearAgreementCamps();
        }catch(InvalidFormException ex){
            WindowControllerB.getWindowController().showErrorAlert(event, ex.getMessage());
        }

    }
    
    private Agreement getOutCommentInformation(){
        String agreementDescription = this.txtFieldDescriptionAgreement.getText();
        String agreementDeliveyDate = this.dtpDeliveryDate.getValue().toString();
        String agreementIntegrantResponsible = this.cboBoxResponsibleAgreement.getValue();
        Agreement newAgreement = new Agreement(0, agreementDescription, agreementIntegrantResponsible, agreementDeliveyDate);
        return newAgreement;
    }
    
    private void clearAgreementCamps(){
        this.txtFieldDescriptionAgreement.clear();
        this.dtpDeliveryDate.setValue(null);
        this.cboBoxResponsibleAgreement.setValue(null);
    }

    @FXML
    private void closeUpdateMeetingWindow(ActionEvent event){
        Optional<ButtonType> action = WindowControllerB.getWindowController().showConfirmacionAlert(event,
        "¿Seguro que deseas salir del registro de reunión? Los cambios no se guardarán");
        if(action.get() == ButtonType.OK){
            FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("MeetingRequest.fxml", btnCloseUpdateMeetingWindow);
            MeetingRequestController controller = loader.getController();
            controller.reciveMeeting(meeting.getMeetingKey());
            controller.receiveToken(token);
        }
    }
    
    

    @FXML
    private void deleteAgreementFile(ActionEvent event){
        Agreement agreementForDelete = this.tvAgreement.getSelectionModel().getSelectedItem();
        meeting.getAgreements().remove(agreementForDelete);
        this.tvAgreement.setItems(makeItemsAgreementTable());
    }

    @FXML
    private void updateAgreementFile(ActionEvent event){
        Agreement agreementForUpdate = this.tvAgreement.getSelectionModel().getSelectedItem();
        setAgreementInformationsCamps(agreementForUpdate);
        meeting.getAgreements().remove(agreementForUpdate);
    }
    
    private void setAgreementInformationsCamps(Agreement agreement){
        this.txtFieldDescriptionAgreement.setText(agreement.getDescriptionAgreement());
        LocalDate deliveryDate = LocalDate.parse(agreement.getDeliveryDate());
        this.dtpDeliveryDate.setValue(deliveryDate);
        this.cboBoxResponsibleAgreement.setValue(agreement.getResponsibleAgreement());
    }

    @FXML
    private void updateMeeting(ActionEvent event){
        try{
            validateMeetingInformation();
            meeting.setMeetingNote(this.txtAreaNoteMeeting.getText());
            meeting.setMeetingPending(this.txtAreaPendingMeeting.getText());
            if(MEETING_DAO.updateMeeting(meeting, meeting)){
                WindowControllerB.getWindowController().showInfoAlert(event, "Reunión modificada con éxito");
            }else{
                WindowControllerB.getWindowController().showErrorAlert(event, "Error en el sistema, favor de ponerse en contacto con sopoerte técnico");
            }
            FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("MeetingRequest.fxml", btnCloseUpdateMeetingWindow);
            MeetingRequestController controller = loader.getController();
            controller.receiveToken(token);
            controller.reciveMeeting(meeting.getMeetingKey());
        }catch(InvalidFormException ex){
            WindowControllerB.getWindowController().showErrorAlert(event, ex.getMessage());
        }
    }
    
    private void validateMeetingInformation() throws InvalidFormException{
        ValidatorForm.chechkAlphabeticalArea(this.txtAreaNoteMeeting, 0, 450);
        ValidatorForm.chechkAlphabeticalArea(this.txtAreaPendingMeeting, 0, 450);
    }
    
    private void prepareAgreementTable(){
        this.colDescriptionAgreement.setCellValueFactory(new PropertyValueFactory<Agreement, String>("descriptionAgreement"));
        this.colDeliveryDate.setCellValueFactory(new PropertyValueFactory<Agreement, String>("deliveryDate"));
        this.colResponsibleAgreement.setCellValueFactory(new PropertyValueFactory<Agreement, String>("responsibleAgreement"));
        this.tvAgreement.setItems(makeItemsAgreementTable());
    }
    
    private ObservableList<Agreement> makeItemsAgreementTable(){
        ObservableList<Agreement> itemsAgreement = FXCollections.observableArrayList();
        List agreementsList = meeting.getAgreements();
        itemsAgreement.addAll(agreementsList);
        return itemsAgreement;
    }
    
    private ObservableList<String> makeitemsIntegrantForComboBox(){
        ObservableList<String> itemsIntegrant = FXCollections.observableArrayList();
        List<String> integrantsName = INTEGRANT_DAO.getAllIntegrantsName(token.getBodyAcademyKey());
        itemsIntegrant.addAll(integrantsName);
        return itemsIntegrant;
    }    
}
