package controller.control.validator;

import controller.control.ValidatorController;
import javafx.scene.control.Control;
import javafx.scene.control.ToggleGroup;

/**
 * Only for ToggleGroup control.
 */
public class ValidatorToggleGroup extends Control implements IValidatorControlFX{
    private final ToggleGroup component;
    private final ValidatorController validatorController;

    public ValidatorToggleGroup(ToggleGroup component, ValidatorController validatorController) {
        this.component = component;
        this.validatorController = validatorController;
    }

    @Override
    public void initListener() {
        component.selectedToggleProperty().addListener((observable, oldValue, newValue) -> validatorController.addComponentToValidator(this, newValue != null));
    }

    // RESOLVE INTERFACE SEGREGATION
    @Override
    public void setInvalidStyleClass() {
    }

    // RESOLVE INTERFACE SEGREGATION
    @Override
    public void clearStyleClass() {

    }
}
