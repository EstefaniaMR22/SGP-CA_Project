package controller.validator;

import controller.ValidatorController;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Control;

/***
 * Only for ComboBox, DatePicker, ColorPicker
 * but only if you need just an input without validation.
 */
public class ValidatorComboBoxBase extends Control implements IValidatorControlFX {
    protected final ComboBoxBase<?> component;
    protected final ValidatorController validatorController;

    public ValidatorComboBoxBase(ComboBoxBase<?> component, ValidatorController validatorController) {
        this.component = component;
        this.validatorController = validatorController;
    }

    @Override
    public void initListener() {
        component.valueProperty().addListener( (observable, oldValue, newValue) -> {
            if(newValue == null ) {
                validatorController.addComponentToValidator(this, false);
                component.getStyleClass().add("wrongInput");
            } else {
                validatorController.addComponentToValidator(this, true);
                component.getStyleClass().remove("wrongInput");
            }
        });
    }

    @Override
    public void setInvalidStyleClass() {
        component.getStyleClass().add("wrongInput");
    }

    @Override
    public void clearStyleClass() {
        component.getStyleClass().remove("wrongInput");
    }
}
