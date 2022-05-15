package controller.workplan;

import controller.academicgroup.AddAcademicGroupController;
import controller.control.AlertController;
import controller.control.ValidatorController;
import controller.control.validator.Validator;
import controller.control.validator.ValidatorTextInputControl;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import model.dao.MemberDAO;
import model.domain.Action;
import model.domain.ActionState;
import model.domain.Member;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddActionController extends ValidatorController implements Initializable {
    private FilteredList<Member> filteredData;
    @FXML private TextArea descriptionTextArea;
    @FXML private ListView<Member> membersAvailableListView;
    @FXML private TextField resourceTextField;
    @FXML private ListView<String> resourcesListView;
    @FXML private TextField searchMemberTextField;
    @FXML private Label systemLabel;
    @FXML private Label memberSeletectedLabel;
    @FXML private Label resourceSystemLabel;
    private Member memberSelected;
    private ObservableList<Action> actionObservableList;
    private boolean isRegistered;
    private Action actionRegistered;

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/AddActionView.fxml"), this);
        stage.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initValidator();
        getAllMembersFromDatabase();
        initializeFilterSearchInput();
    }

    @FXML
    void registerOnAction(ActionEvent event) {
        if(validateInputs()) {
            if(!validateDescriptionAndAssigned()) {
                if(memberSelected != null ) {
                    actionRegistered = new Action();
                    actionRegistered.setDescription(descriptionTextArea.getText());
                    actionRegistered.setResponsable(memberSelected);
                    actionRegistered.setRecursos(new ArrayList<>(resourcesListView.getItems()));
                    actionRegistered.setState(ActionState.ACTIVE);
                    isRegistered = true;
                    stage.close();
                } else {
                    systemLabel.setText("¡Debes asignar un miembro!");
                }
            } else {
                systemLabel.setText("¡Al parecer ya existe una accion con esa descripcion asignada al mismo miembro!");
            }
        } else {
            systemLabel.setText("¡Algunos campos son invalidos, por favor verifiquelos!");
        }
    }

    @FXML
    void cancelOnAction(ActionEvent event) {
        stage.close();
    }

    @FXML
    void deleteMemberOnAction(ActionEvent event) {
        memberSelected = null;
        memberSeletectedLabel.setText("");
    }

    @FXML
    void selectMemberOnAction(ActionEvent event) {
        Member selected = membersAvailableListView.getSelectionModel().getSelectedItem();
        if(selected != null ) {
            memberSelected = selected;
            memberSeletectedLabel.setText(memberSelected.getFullName() + " " + memberSelected.getId());
        }
    }

    @FXML
    void deleteResourceOnAction(ActionEvent event) {
        if(resourcesListView.getSelectionModel().getSelectedItem() != null ) {
            resourcesListView.getItems().remove(resourcesListView.getSelectionModel().getSelectedIndex());
        }
    }

    @FXML
    void addResourceOnAction(ActionEvent event) {
        if(!resourceTextField.getText().equals("")) {
            if(resourcesListView.getItems().stream().noneMatch(x -> x.equals(resourceTextField.getText()))) {
                resourcesListView.getItems().add(resourceTextField.getText());
                resourceTextField.setText("");
            } else {
                resourceSystemLabel.setText("No puedes agregar el mismo recurso.");
                pauseAndRemoveResourceLabel(3);
            }
        }
    }

    private void pauseAndRemoveResourceLabel(int seconds) {
        PauseTransition pause = new PauseTransition(Duration.seconds(seconds));
        pause.setOnFinished(event -> resourceSystemLabel.setText(""));
        pause.play();
    }

    private boolean validateDescriptionAndAssigned() {
        boolean isDescriptionRepeated = false;
        for (Action action: actionObservableList ) {
            if(action.getDescription().equals(descriptionTextArea.getText()) && action.getResponsable().getPersonalNumber().equals(memberSelected.getPersonalNumber())) {
                isDescriptionRepeated = true;
            }
        }
        return isDescriptionRepeated;
    }

    private void getAllMembersFromDatabase() {
        List<Member> members = null;
        try {
            members = new MemberDAO().getAllMembers();
            members.removeIf(member -> member.getId() == 1);
            ObservableList<Member> memberObservableList = FXCollections.observableArrayList(members);
            membersAvailableListView.setItems(memberObservableList);
        } catch (SQLException sqlException) {
            Logger.getLogger(AddAcademicGroupController.class.getName()).log(Level.SEVERE, null, sqlException);
            AlertController.getInstance().determinateAlertBySQLException(sqlException);
        }
    }

    private void initializeFilterSearchInput() {
        filteredData = new FilteredList<>(membersAvailableListView.getItems(), p -> true);
        searchMemberTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate( object -> {
                if(newValue == null | newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if(String.valueOf(object.getFullName()).toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if(String.valueOf(object.getPersonalNumber()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
            SortedList<Member> sortedList = new SortedList<>(filteredData);
            membersAvailableListView.setItems(sortedList);
        });
    }

    private void initValidator() {
        addComponentToValidator(new ValidatorTextInputControl(descriptionTextArea, Validator.PATTERN_NUMBERS_AND_LETTERS, Validator.LENGTH_LONG_LONG_TEXT, this), false);
        initListenerToControls();
    }

}

