package controller.meets;

import assets.utils.DateFormatter;
import assets.utils.SQLStates;
import controller.academicgroup.AddMemberController;
import controller.control.AlertController;
import controller.control.ValidatorController;
import controller.control.validator.Validator;
import controller.control.validator.ValidatorComboBoxBaseWithConstraints;
import controller.control.validator.ValidatorTextInputControl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.dao.AgreementDAO;
import model.dao.MeetDAO;
import model.domain.Agreement;

import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddTimeMeetController extends ValidatorController implements Initializable {

    @FXML
    private TableView<Agreement> agreementTableView;
    @FXML
    private TableColumn<Agreement, String> descriptionAgreementColumn;
    @FXML
    private TableColumn<Agreement, String> authorColumn;
    @FXML
    private TableColumn<Agreement, String> dateColumn;
    @FXML
    private DatePicker dateDealDataPicker;
    @FXML
    private TextField dealTextField;
    @FXML
    private TextField hourFinalizationMeetTextField;
    @FXML
    private Label bussinesLabel;
    @FXML
    private Label hourLabel;
    @FXML
    private Label systemLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Button registerTimeButton;
    @FXML
    private Button registerDealButton;
    @FXML
    private Button exitButton;

    private int idUser;
    private int idMeet;
    private String user;

    private final int SECONDS_IN_ONE_DAY = 86_400;
    private final int SECONDS_IN_ONE_HOUR = 3_600;
    private final int SECONDS_IN_ONE_MINUTE = 60;

    private ObservableList<Agreement> agreementObservableList;

    public AddTimeMeetController(int idUser, int idMeet, String user) {
        this.idUser = idUser;
        this.idMeet = idMeet;
        this.user = user;
    }

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/AddTimeMeetView.fxml"), this);
        setTableComponents();
        stage.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setTableComponents();
        MeetDAO meetDAO = new MeetDAO();
        try {
            if(meetDAO.timeMeetIsNull(idMeet)){
                registerDealButton.setVisible(false);
                registerDealButton.setDisable(true);
            }
        } catch (SQLException addAgreementException) {

            deterMinateSQLState(addAgreementException);

        }

        initListenerHour();
        initValidatorToTextInput();
    }
    private void setTableComponents() {
        agreementObservableList = FXCollections.observableArrayList();
        descriptionAgreementColumn.setCellValueFactory(new PropertyValueFactory<>("descriptionAgreement"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("authorAgreement"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("registerAgreement"));
        chargeAgreements();
    }


    private void chargeAgreements(){
        AgreementDAO agreementDAO = new AgreementDAO();
        try {

            agreementObservableList = agreementDAO.getAgreementList(idMeet);
            System.out.println("Solicito los acuerdos" + agreementObservableList);
            agreementTableView.setItems(agreementObservableList);

        }catch(SQLException chargeMembersExeception){
            deterMinateSQLState(chargeMembersExeception);
        }

    }

    private void deterMinateSQLState(SQLException sqlException) {
        Logger.getLogger(AddMemberController.class.getName()).log(Level.SEVERE, null, sqlException);
        if(sqlException.getSQLState().equals(SQLStates.SQL_NO_CONNECTION.getSqlState())) {
            AlertController.getInstance().showConnectionErrorAlert();
        }
        AlertController.getInstance().showActionFailedAlert(sqlException.getLocalizedMessage());
    }

    @FXML
    void addTimeOnAction(ActionEvent actionEvent) {

        try {
            if((hourFinalizationMeetTextField.getText().length() <= 5) && (hourFinalizationMeetTextField.getText().length() > 0)) {

                addTimeMmeet();
            }else {
                AlertController alertView = AlertController.getInstance();
                alertView.showActionFailedAlert("Algunos datos ingresados son inválidos, por favor verifíquelos");
            }
        } catch (Exception e) {
            systemLabel.setText(e.getLocalizedMessage());
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert("Error: " + e);

        }

    }

    private String  calculateHours(String hora1, String hora2) {
        String resultHours = "";
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            Date startDate = simpleDateFormat.parse(hora1);
            Date endDate = simpleDateFormat.parse(hora2);

            long start = startDate.getTime() / 1000;
            long end = endDate.getTime() / 1000;

            if(end < start)
                end += SECONDS_IN_ONE_DAY;

            int difference = (int) (end - start);

            int days = difference / SECONDS_IN_ONE_DAY;
            difference %= SECONDS_IN_ONE_DAY;
            int hours = difference / SECONDS_IN_ONE_HOUR;
            difference %= SECONDS_IN_ONE_HOUR;
            int minutes = difference / SECONDS_IN_ONE_MINUTE;

            System.out.println(hora1);
            System.out.println(hora2);

            System.out.println(hours + ":" + minutes);
            resultHours = hours + ":" + minutes;
            return resultHours;
        } catch (Exception exeception){
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert("Este campo solo acepta valores tipo Hora");


        }
        return resultHours;
    }

    private void addTimeMmeet(){
        MeetDAO meetDAO = new MeetDAO();
        String hourStart;
        String hoursMeet = "";
        try {
            hourStart = meetDAO.getMeetDetails(idMeet).getHour();

            hoursMeet = calculateHours(hourStart, hourFinalizationMeetTextField.getText());
        } catch (SQLException sqlException) {
            deterMinateSQLState(sqlException);
        }

        try {
            boolean correctAddAgreement = false;

            correctAddAgreement = meetDAO.addTimeMeet(idMeet,hoursMeet);
            if (correctAddAgreement == true) {
                AlertController.getInstance().showSuccessfullRegisterAlert();
                stage.close();
            }else {
                AlertController alertView = AlertController.getInstance();
                alertView.showActionFailedAlert("Error al guardar el tiempo de la reunión");
            }

        } catch (SQLException addAgreementException) {

            deterMinateSQLState(addAgreementException);

        }

    }

    @FXML
    void addDealOnAction(ActionEvent actionEvent) {

        try {
            if(validateInputs()) {
                if(!validateAgreement()) {
                        addAgreement();
                        chargeAgreements();

                } else {
                    systemLabel.setText("¡Al parecer ya existe acuerdo igual\n"+
                            " ,de favor ingrese uno distinto!");
                }

            }else {
                AlertController alertView = AlertController.getInstance();
                alertView.showActionFailedAlert("Algunos datos ingresados son inválidos, por favor verifíquelos");
            }
        } catch (SQLException e) {
            systemLabel.setText(e.getLocalizedMessage());
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert("Error: " + e);

        }

    }


    private boolean validateAgreement() throws SQLException {
        return new AgreementDAO().checkAgreement(dealTextField.getText(), idMeet);
    }

    private void addAgreement(){
        AgreementDAO agreementDAO = new AgreementDAO();
        Agreement agreementNew = new Agreement();

        agreementNew.setIdMeet(idMeet);
        agreementNew.setAuthorAgreement(user);
        agreementNew.setAuthor(idUser);
        agreementNew.setDescriptionAgreement(dealTextField.getText());
        agreementNew.setRegisterAgreement(DateFormatter.getDateFromDatepickerValue(dateDealDataPicker.getValue()));

        try {
            boolean correctAddAgreement = false;

            correctAddAgreement = agreementDAO.addMeet(agreementNew);
            if (correctAddAgreement == true) {
                AlertController.getInstance().showSuccessfullRegisterAlert();
                stage.close();
            }else {
                AlertController alertView = AlertController.getInstance();
                alertView.showActionFailedAlert("Error al guardar acuerdo");
            }

        } catch (SQLException addAgreementException) {

            deterMinateSQLState(addAgreementException);

        }
    }

    @FXML
    void returnViewOnAction(ActionEvent actionEvent) {
        try{
            stage.close();
        }catch(Exception returnViewOnActionExeception){
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" No se pudo volver a la ventana anterior." +
                    "Causa: " + returnViewOnActionExeception);

        }
    }

    private void initValidatorToTextInput() {
        Function<Object, Boolean> validateDate = a -> {
            return DateFormatter.compareActualDateToSelectedDate((LocalDate) a) == 1;
        };

        addComponentToValidator(new ValidatorTextInputControl(dealTextField, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);

        addComponentToValidator(new ValidatorComboBoxBaseWithConstraints(dateDealDataPicker, this, validateDate), false);

        initListenerToControls();
    }

    public void initListenerHour() {
        hourFinalizationMeetTextField.textProperty().addListener((observable, oldValue, newValue) -> {
             addComponentToValidator(new ValidatorTextInputControl(hourFinalizationMeetTextField, Validator.PATTERN_HOURS, Validator.LENGTH_HOUR, this), false);
             initListenerToControls();
        });

    }

}
