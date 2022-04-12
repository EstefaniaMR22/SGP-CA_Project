package controller.academicgroup;

import controller.control.AlertController;
import controller.control.ValidatorController;
import controller.control.validator.Validator;
import controller.control.validator.ValidatorTextInputControl;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.domain.LGAC;
import assets.utils.DateFormatter;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.function.Function;

public class AddLgacController extends ValidatorController implements Initializable {
    @FXML private TextField identificatorTextField;
    @FXML private TextArea descriptionTextArea;
    @FXML private Label systemLabel;
    private LGAC lgacRegistered;
    private ObservableList<LGAC> lgacObservableList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initValidator();
    }

    public AddLgacController(ObservableList<LGAC> lgacObservableList) {
        this.lgacObservableList = lgacObservableList;
    }

    public LGAC showStage() {
        loadFXMLFile(getClass().getResource("/view/AddLgacView.fxml"), this);
        stage.setTitle("Registrar LGAC");
        stage.showAndWait();
        return lgacRegistered;
    }

    @FXML
    void cancelOnAction(ActionEvent event) {
        if (AlertController.getInstance().showCancelationConfirmationAlert()) {
            stage.close();
        }
    }

    @FXML
    void registerOnAction(ActionEvent event) {
        if(validateInputs()) {
            if(!validateIdentificator()) {
                lgacRegistered = new LGAC();
                lgacRegistered.setDescription(descriptionTextArea.getText());
                lgacRegistered.setIdentification(identificatorTextField.getText());
                stage.close();
            } else {
                systemLabel.setText("¡Al parecer ya existe un LGAC registrado con ese identificador!");
            }
        } else {
            systemLabel.setText("Algunos campos son invalidos, por favor verifiquelos");
        }
    }

    private boolean validateIdentificator() {
        boolean isIdentificatorRepeated = false;
        for (LGAC lgac: lgacObservableList ) {
            if(lgac.getIdentification().equals(identificatorTextField.getText())) {
                isIdentificatorRepeated = true;
            }
        }
        return isIdentificatorRepeated;
    }

    private void initValidator() {
        Function<Object, Boolean> validateRegister = a -> DateFormatter.compareActualDateToLocalDate((LocalDate) a) >= 0;
        addComponentToValidator(new ValidatorTextInputControl(identificatorTextField, Validator.PATTERN_NUMBERS_AND_LETTERS, Validator.LENGTH_SMALL_TEXT, this), false);
        addComponentToValidator(new ValidatorTextInputControl(descriptionTextArea, Validator.PATTERN_NUMBERS_AND_LETTERS, Validator.LENGTH_LONG_LONG_TEXT, this), false);
        initListenerToControls();
    }
}
