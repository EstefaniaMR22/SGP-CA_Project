package controller.meets;

import assets.utils.DateFormatter;
import assets.utils.SQLStates;
import controller.academicgroup.AddMemberController;
import controller.control.AlertController;
import controller.control.ValidatorController;
import controller.control.validator.Validator;
import controller.control.validator.ValidatorComboBoxBase;
import controller.control.validator.ValidatorComboBoxBaseWithConstraints;
import controller.control.validator.ValidatorTextInputControl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.dao.MeetDAO;
import model.dao.MemberDAO;
import model.dao.ProjectDAO;
import model.dao.ReceptionalWorkDAO;
import model.domain.Meet;
import model.domain.Member;
import model.domain.Project;
import model.domain.ReceptionalWork;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddMeetController extends ValidatorController implements Initializable {

    @FXML
    private TextField bussinesTextField;
    @FXML
    private TextField hourTextField;
    @FXML
    private ComboBox<Project> projectsCombobox;
    @FXML
    private TableView<Member> integrantsTableView;
    @FXML
    private TableColumn<Member, String> integrantColumn;
    @FXML
    private TableColumn<Member, String> lastName1Column;
    @FXML
    private TableColumn<Member, String> lastName2Column;
    @FXML
    private DatePicker meetDateDataPicker;
    @FXML
    private TextField leaderTextField;
    @FXML
    private TextField secretaryTextField;
    @FXML
    private Label systemLabel;
    @FXML
    private Button exitButton;
    @FXML
    private Button addMeetButton;

    private String idAcademicGroup;

    private ObservableList<Project> projectsObservableList;
    private ObservableList<Member> memberObservableList;
    private Meet newMeet = new Meet();
    private Member leaderMeet;
    private Member secretaryMeet;
    private Member timerMeet;

    public AddMeetController(String idAcademicGroup) {
        this.idAcademicGroup = idAcademicGroup;
    }

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/AddMeetView.fxml"), this);
        stage.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        projectsObservableList = FXCollections.observableArrayList();
        setTableComponents();
        chargeProjectsComboBox();
        leaderTextField.setDisable(true);
        secretaryTextField.setDisable(true);

        initValidatorToTextInput();
    }

    private void setTableComponents() {
        memberObservableList = FXCollections.observableArrayList();
        integrantColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        lastName1Column.setCellValueFactory(new PropertyValueFactory<>("maternalLastname"));
        lastName2Column.setCellValueFactory(new PropertyValueFactory<>("paternalLastname"));

        chargeMembers();
    }

    private void chargeMembers(){
        MemberDAO memberDAO = new MemberDAO();
        try {

            memberObservableList = memberDAO.getAllMembersByIdAcademicBody(idAcademicGroup);
            integrantsTableView.setItems(memberObservableList);

        }catch(SQLException chargeMembersExeception){
            deterMinateSQLState(chargeMembersExeception);
        }

    }

    private void chargeProjectsComboBox(){
        ProjectDAO projectDAO= new ProjectDAO();

        try {

            projectsObservableList = projectDAO.getProjectList(idAcademicGroup);

            projectsCombobox.setItems(projectsObservableList);

        } catch(SQLException chargeLGACException) {
            deterMinateSQLState(chargeLGACException);
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
    void addMeetOnAction(ActionEvent actionEvent) {

        try {
            if(validateInputs()) {
                if(!validateMeet()) {
                    addMeet();

                } else {
                    systemLabel.setText("¡Al parecer ya existe una reunión con \n"+
                            " el mismo nombre,de favor ingrese un nombre distinto!");
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

    private boolean validateMeet() throws SQLException {
        return new MeetDAO().checkMeet(DateFormatter.getDateFromDatepickerValue(meetDateDataPicker.getValue()), hourTextField.getText());
    }

    private void addMeet(){
        MeetDAO meetDAO = new MeetDAO();

        newMeet.setAsunto(bussinesTextField.getText());
        int positionProject = projectsCombobox.getSelectionModel().getSelectedIndex();
        newMeet.setNameProject((projectsObservableList.get(positionProject).getProjectName()));
        newMeet.setIdProject(projectsObservableList.get(positionProject).getIdProject());
        newMeet.setIdLeader(leaderMeet.getId());
        newMeet.setIdSecretary(secretaryMeet.getId());
        newMeet.setAsistents(memberObservableList);

        newMeet.setDateMeet(DateFormatter.getDateFromDatepickerValue(meetDateDataPicker.getValue()));
        meetDateDataPicker.hide();
        meetDateDataPicker.setValue(LocalDate.from(LocalDateTime.now()));

        newMeet.setRegister(DateFormatter.getDateFromDatepickerValue(meetDateDataPicker.getValue()));
        newMeet.setHour(hourTextField.getText());

        try {
            boolean correctAddMeet = false;

            correctAddMeet = meetDAO.addMeet(newMeet);
            if (correctAddMeet == true) {
                AlertController.getInstance().showSuccessfullRegisterAlert();
                stage.close();
            }else {
                AlertController alertView = AlertController.getInstance();
                alertView.showActionFailedAlert("Error al guardar reunion");
            }

        } catch (SQLException addProjectInvestigationException) {

            deterMinateSQLState(addProjectInvestigationException);

        }
    }


    @FXML
    void addLeaderOnAction(ActionEvent actionEvent) {
        Member selectedMember = integrantsTableView.getSelectionModel().getSelectedItem();
        if(!leaderTextField.getText().equals("")) {
            memberObservableList.add(leaderMeet);
            integrantsTableView.setItems(memberObservableList);

            leaderTextField.setText("");
            leaderMeet = new Member();
        }
        if(selectedMember != null) {
            memberObservableList.remove(selectedMember);
            integrantsTableView.setItems(memberObservableList);

            leaderTextField.setText(selectedMember.getFullName());

            newMeet.setIdLeader(selectedMember.getId());
            newMeet.setLeader(selectedMember.getFullName());

            leaderMeet = selectedMember;

        }else {
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" Antes de presionar agregar lider debes seleccionar un " +
                    "integrante de la tabla");
        }
    }

    @FXML
    void addSecretaryOnAction(ActionEvent actionEvent) {
        Member selectedMember = integrantsTableView.getSelectionModel().getSelectedItem();
        if(!secretaryTextField.getText().equals("")) {
            memberObservableList.add(secretaryMeet);
            integrantsTableView.setItems(memberObservableList);

            secretaryTextField.setText("");
            secretaryMeet = new Member();
        }
        if(selectedMember != null) {
            memberObservableList.remove(selectedMember);
            integrantsTableView.setItems(memberObservableList);

            secretaryTextField.setText(selectedMember.getFullName());

            newMeet.setIdSecretary(selectedMember.getId());
            newMeet.setSecretary(selectedMember.getFullName());

            secretaryMeet = selectedMember;

        }else {
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" Antes de presionar agregar secretario debes seleccionar un " +
                    "integrante de la tabla");
        }
    }

    @FXML
    void removeLeaderOnAction(ActionEvent actionEvent) {
        if(!leaderTextField.getText().equals("")) {
            memberObservableList.add(leaderMeet);
            integrantsTableView.setItems(memberObservableList);

            leaderTextField.setText("");
            leaderMeet = new Member();
        }else {
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" Selecciona antes un miembro para quitarlo");
        }
    }

    @FXML
    void removeSecretaryOnAction(ActionEvent actionEvent) {
        if(!secretaryTextField.getText().equals("")) {
            memberObservableList.add(secretaryMeet);
            integrantsTableView.setItems(memberObservableList);

            secretaryTextField.setText("");
            secretaryMeet = new Member();
        }else {
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" Selecciona antes un miembro para quitarlo");
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

        addComponentToValidator(new ValidatorTextInputControl(bussinesTextField, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);

        addComponentToValidator(new ValidatorTextInputControl(hourTextField, Validator.PATTERN_HOURS, Validator.LENGTH_HOUR, this), false);

        addComponentToValidator(new ValidatorTextInputControl(leaderTextField, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);

        addComponentToValidator(new ValidatorTextInputControl(secretaryTextField, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);

        addComponentToValidator(new ValidatorComboBoxBase(projectsCombobox, this), false);

        addComponentToValidator(new ValidatorComboBoxBaseWithConstraints(meetDateDataPicker, this, validateDate), false);

        initListenerToControls();
    }

}
