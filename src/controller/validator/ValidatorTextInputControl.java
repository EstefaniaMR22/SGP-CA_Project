package controller.validator;

import controller.ValidatorController;
import javafx.scene.control.Control;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

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
                component.getStyleClass().removeAll("wrongTextInput", "textfield", "textarea");
                component.getStyleClass().add("wrongTextInput");
            } else {
                validatorController.addComponentToValidator(this, true);
                component.getStyleClass().removeAll("wrongTextInput", "textfield", "textarea");
                if(component instanceof TextField) {
                    component.getStyleClass().add("textfield");
                } else if(component instanceof TextArea) {
                    component.getStyleClass().add("textarea");
                }
            }
        }));
    }

    @Override
    public void setInvalidStyleClass() {
        component.getStyleClass().add("wrongTextInput");
    }
}
