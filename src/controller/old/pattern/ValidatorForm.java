/*
 * @author Josué 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package controller.old.pattern;

import java.util.List;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ValidatorForm{
    
    public static void checkAlaphabeticalFields(List<TextField> fields, int characterSizeMinimun, int characterSizeLimit) throws InvalidFormException{
        for(TextField field : fields){
            chechkAlphabeticalField(field, characterSizeMinimun, characterSizeLimit);
        }
    }
    
    public static void checkAlaphabeticalTextAreas(List<TextArea> textAreas, int characterSizeMinimun, int characterSizeLimit) throws InvalidFormException{
        for(TextArea area : textAreas){
            chechkAlphabeticalArea(area, characterSizeMinimun, characterSizeLimit);
        }
    }
    
    public static void chechkPasswordField(PasswordField field, int characterSizeMinimun, int characterSizeLimit) throws InvalidFormException{
        boolean correctField = false;
        if(!field.getText().isEmpty() && field.getText().length() >= characterSizeMinimun && field.getText().length() <= characterSizeLimit){
            field.setStyle("-fx-border-color: green;");
            correctField = true;
        }else{
            field.setStyle("-fx-border-color: red;");
        }
        if(!correctField){
            throw new InvalidFormException("El campo: " + field.getAccessibleHelp() + ", es inválido");
        }
    }
    
    
    public static void chechkAlphabeticalField(TextField field, int characterSizeMinimun, int characterSizeLimit) throws InvalidFormException{
        boolean correctField = false;
        if(!field.getText().isEmpty() && field.getText().length() >= characterSizeMinimun && field.getText().length() <= characterSizeLimit){
            field.setStyle("-fx-border-color: green;");
            correctField = true;
        }else{
            field.setStyle("-fx-border-color: red;");
        }
        if(!correctField){
            throw new InvalidFormException("El campo: " + field.getAccessibleHelp() + ", es inválido");
        }
    }
    
    public static void chechkAlphabeticalArea(TextArea txtArea, int characterSizeMinimun, int characterSizeLimit) throws InvalidFormException{
        boolean correctField = false;
        if(!txtArea.getText().isEmpty() && txtArea.getText().length() >= characterSizeMinimun && txtArea.getText().length() < characterSizeLimit){
            txtArea.setStyle("-fx-border-color: green;");
            correctField = true;
        }else{
            txtArea.setStyle("-fx-border-color: red;");
        }
        if(!correctField){
            throw new InvalidFormException("El campo: " + txtArea.getAccessibleHelp()+ ", es inválido");
        }
    }
    
    public static void isNumberData(TextField textForCheck, int characterLimit) throws InvalidFormException{
        boolean isNumber = false;
        try{
            chechkAlphabeticalField(textForCheck, 1, characterLimit);
            double number = Double.parseDouble(textForCheck.getText());
            isNumber = true;
            textForCheck.setStyle("-fx-border-color: green;");
        }finally{
            if(!isNumber){
                textForCheck.setStyle("-fx-border-color: red;");
                throw new InvalidFormException("Campo no númerico");
            }
        }
    }
    
    public static void isIntegerNumberData(TextField textForCheck, int characterLimit) throws InvalidFormException{
        boolean isIntegerNumber = false;
        try{
            int number = Integer.parseInt(textForCheck.getText());
            isIntegerNumber = true;
            textForCheck.setStyle("-fx-border-color: green;");
        }finally{
            if(!isIntegerNumber){
                textForCheck.setStyle("-fx-border-color: red;");
                throw new InvalidFormException("Campo no númerico");
            }
        }
    }
    
    public static void checkNotEmptyDateField(DatePicker date) throws InvalidFormException{
        if(date.getValue() == null){
            date.setStyle("-fx-border-color: red;");
            throw new InvalidFormException("El campo: " + date.getAccessibleHelp() + ", está vacío");
        }else{
            date.setStyle("-fx-border-color: green;");
        }
    }
    
    public static void isComboBoxSelected(ComboBox<?> comboBox) throws InvalidFormException{
        if(comboBox.getValue() == null){
            comboBox.setStyle("-fx-border-color: red;");
            throw new InvalidFormException("Campos sin seleccionar");
        }else{
            comboBox.setStyle("-fx-border-color: green;");
        }
    }
    
    public static String convertJavaDateToSQlDate(DatePicker date){
        String newDate = date.getValue().toString().replace("/", "-");
        return newDate;
    }
    
    public static String convertSQLDateToJavaDate(String date){
        String newDate = date.replace("-", "/");
        return newDate;
    }
}
