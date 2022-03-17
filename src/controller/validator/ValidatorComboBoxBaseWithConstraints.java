package controller.validator;

import controller.ValidatorController;
import javafx.scene.control.ComboBoxBase;

import java.util.function.Function;

/**
 * Only for ComboBox, DatePicker or ColorPicker
 * but only if you need an specific (own) validation.
 * e.g. validate dates or color hex.
 * This class need a Function passed as a parameter.
 */
public class ValidatorComboBoxBaseWithConstraints extends ValidatorComboBoxBase {
    private final Function<Object, Boolean> parameter;

    public ValidatorComboBoxBaseWithConstraints(ComboBoxBase<?> component, ValidatorController validatorController, Function<Object, Boolean> parameter ) {
        super(component, validatorController);
        this.parameter = parameter;
    }

    @Override
    public void initListener() {
        component.valueProperty().addListener( (observable, oldValue, newValue) -> {
            if( parameter.apply(newValue) ) {
                validatorController.addComponentToValidator(this, true);
                component.getStyleClass().add("wrongInput");
            } else {
                validatorController.addComponentToValidator(this, false);
                component.getStyleClass().remove("wrongInput");
            }
        });
    }

    @Override
    public void setInvalidStyleClass() {
        component.getStyleClass().add("wrongInput");
    }
}
