package controller.evidences;

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
import model.domain.Meet;
import model.domain.Member;
import model.domain.Project;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddEvidenceController extends ValidatorController implements Initializable {

    @FXML ComboBox<Project> projectsCombobox;
    @FXML ComboBox<Member> responseEvidenceCombobox;
    @FXML ComboBox titleBooksCombobox;

    private String idAcademicGroup;



    public AddEvidenceController(String idAcademicGroup) {
        this.idAcademicGroup = idAcademicGroup;
    }

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/AddEvidencesView.fxml"), this);
        stage.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initValidatorToTextInput();
    }


    @FXML
    void addEvidenceOnAction(ActionEvent actionEvent) {

    }

    //private boolean validateEvidence() throws SQLException {
        //return new MeetDAO().checkMeet(DateFormatter.getDateFromDatepickerValue(meetDateDataPicker.getValue()), hourTextField.getText());
    //}

    private void addEvidence(){

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


        initListenerToControls();
    }

}
