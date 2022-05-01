package controller.control.validator;

import controller.control.ValidatorController;
import javafx.scene.control.Control;
import javafx.scene.control.TextInputControl;

/***
 * Only for TextArea, TextField
 */
public class ValidatorTextInputControl extends Control implements IValidatorControlFX {
    private final TextInputControl component;
    private final String regexConstraint;
    private final int lengthConstraint;
    private final ValidatorController validatorController;

    public ValidatorTextInputControl(TextInputControl component, String regexConstraint, int lengthConstraint, ValidatorController validatorController) {
        this.component = component;
        this.regexConstraint = regexConstraint;
        this.lengthConstraint = lengthConstraint;
        this.validatorController = validatorController;
    }

    @Override
    public void initListener() {
        component.textProperty().addListener( ( (observable, oldValue, newValue) -> {
            if( !Validator.doesStringMatchPattern(newValue,regexConstraint) || Validator.isStringLargerThanLimitOrEmpty(newValue,lengthConstraint) ){
                validatorController.addComponentToValidator(this, false);
                component.getStyleClass().removeAll("wrongInput");
                component.getStyleClass().add("wrongInput");
            } else {
                validatorController.addComponentToValidator(this, true);
                component.getStyleClass().removeAll("wrongInput");
            }
        }));
    }

    @Override
    public void setInvalidStyleClass() {
        component.getStyleClass().add("wrongInput");
    }

    @Override
    public void clearStyleClass() {
        component.getStyleClass().remove("wrongTextInput");
    }
}
