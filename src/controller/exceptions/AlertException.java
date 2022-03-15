package controller.exceptions;

import javafx.scene.control.Alert;

/**
 *
 * @author V Manuel Arrys
 */
public class AlertException {

    public static Alert builderAlert(String Titulo, String Mensaje, Alert.AlertType Tipo){
        Alert alert = new Alert(Tipo);
        alert.setTitle(Titulo);
        alert.setContentText(Mensaje);
        return alert;
    }


}