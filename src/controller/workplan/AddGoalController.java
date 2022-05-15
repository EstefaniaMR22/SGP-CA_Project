package controller.workplan;

import assets.utils.DateFormatter;
import controller.control.ValidatorController;
import controller.control.validator.Validator;
import controller.control.validator.ValidatorComboBoxBaseWithConstraints;
import controller.control.validator.ValidatorTextInputControl;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.domain.Goal;
import model.domain.GoalState;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;

public class AddGoalController extends ValidatorController implements Initializable {
    @FXML private TextArea descriptionTextArea;
    @FXML private DatePicker endDateDatePicker;
    @FXML private TextField idTextField;
    @FXML private Label systemLabel;
    private final ObservableList<Goal> goalObservableList;
    private Goal goalRegistered;
    private boolean isRegistered = false;

    public Goal showStage() {
        loadFXMLFile(getClass().getResource("/view/AddGoalView.fxml"), this);
        stage.showAndWait();
        return goalRegistered;
    }

    public AddGoalController(ObservableList<Goal> goalObservableList) {
        this.goalObservableList = goalObservableList;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initValidator();
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    @FXML
    void cancelOnAction(ActionEvent event) {
        stage.close();
    }

    @FXML
    void registerOnAction(ActionEvent event) {
        if(validateInputs()) {
            if(!validateIdentificator()) {
                goalRegistered = new Goal();
                goalRegistered.setDescription(descriptionTextArea.getText());
                goalRegistered.setEndDate(DateFormatter.getDateFromDatepickerValue(endDateDatePicker.getValue()));
                goalRegistered.setId(idTextField.getText());
                goalRegistered.setState(GoalState.ACTIVE);
                isRegistered = true;
                stage.close();
            } else {
                systemLabel.setText("¡Al parecer ya existe una Meta registrada con ese identificador!");
            }
        } else {
            systemLabel.setText("¡Algunos campos son invalidos, por favor verifiquelos");
        }
    }

    private boolean validateIdentificator() {
        boolean isIdentificatorRepeated = false;
        for (Goal goal: goalObservableList ) {
            if(goal.getId().equals(idTextField.getText())) {
                isIdentificatorRepeated = true;
            }
        }
        return isIdentificatorRepeated;
    }

    private void initValidator() {
        Function<Object, Boolean> validateRegister = a -> true;
        addComponentToValidator(new ValidatorTextInputControl(idTextField, Validator.PATTERN_NUMBERS_AND_LETTER_WITH_STRANGE_SYMBOLS, Validator.LENGTH_GENERAL, this), false);
        addComponentToValidator(new ValidatorTextInputControl(descriptionTextArea, Validator.PATTERN_NUMBERS_AND_LETTER_WITH_STRANGE_SYMBOLS, Validator.LENGTH_LONG_SMALL_TEXT, this), false);
        addComponentToValidator(new ValidatorComboBoxBaseWithConstraints(endDateDatePicker, this, validateRegister), false);
        initListenerToControls();
    }

}
