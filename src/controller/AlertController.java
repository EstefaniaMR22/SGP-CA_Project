package controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AlertController {
    static ButtonType ACCEPT_BUTTON_TYPE = new ButtonType("¡Si, deseo continuar!", ButtonBar.ButtonData.YES);
    static ButtonType CANCEL_BUTTON_TYPE = new ButtonType("¡No, deseo cancelar!", ButtonBar.ButtonData.CANCEL_CLOSE);
    static ButtonType AWARE_BUTTON_TYPE = new ButtonType("Enterado", ButtonBar.ButtonData.YES);
    static ButtonType CANCEL_CONFIRMATION_BUTTON_TYPE = new ButtonType("Sí, deseo cancelar", ButtonBar.ButtonData.YES);
    static ButtonType NO_CANCEL_CONFIRMATION_BUTTON_TYPE = new ButtonType("No,no deseo cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);


    public static boolean showConfirmationAlert() {
        boolean isAccepted = false;
        String contextText = "¿Estas seguro que deseas realizar esta acción?";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, contextText, CANCEL_BUTTON_TYPE, ACCEPT_BUTTON_TYPE);
        alert.setHeaderText(null);
        alert.setTitle("¡Confirmación!");
        alert.setResizable(false);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ACCEPT_BUTTON_TYPE) {
            isAccepted = true;
        }
        return isAccepted;
    }

    public static boolean showActionFailedAlert(String errorMessage) {
        boolean isAccepted = false;
        String contextText = "Al parecer hubo un error en el sistema y no se ha podido realizar la acción. \nError: " + errorMessage;
        Alert alert = new Alert(Alert.AlertType.ERROR, contextText, AWARE_BUTTON_TYPE);
        alert.setHeaderText(null);
        alert.setTitle("¡No se ha podido realizar la acción!");
        alert.setResizable(false);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == AWARE_BUTTON_TYPE) {
            isAccepted = true;
        }
        return isAccepted;
    }

    public static boolean showCancelationConfirmationAlert() {
        boolean isAccepted = false;
        String contextText = "Esta acción eliminará los datos ingresados en el formulario y no se podrán recuperar. \n\033[0;1m¿Está seguro que desea cancelar?";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, contextText, CANCEL_CONFIRMATION_BUTTON_TYPE, NO_CANCEL_CONFIRMATION_BUTTON_TYPE);
        alert.setHeaderText(null);
        alert.setTitle("¡Confirmación de cancelación!");
        alert.setResizable(false);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == CANCEL_CONFIRMATION_BUTTON_TYPE) {
            isAccepted = true;
        }
        return isAccepted;
    }

    public static boolean showSuccessfullRemoveAlert() {
        boolean isAccepted = false;
        String contextText = "Se ha eliminado con éxito el registro dentro del sistema";
        Alert alert = new Alert(Alert.AlertType.INFORMATION, contextText, AWARE_BUTTON_TYPE);
        alert.setHeaderText(null);
        alert.setTitle("¡Eliminación exitosa!");
        alert.setResizable(false);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == AWARE_BUTTON_TYPE) {
            isAccepted = true;
        }
        return isAccepted;
    }

    public static boolean showSuccessfullRegisterAlert() {
        boolean isAccepted = false;
        String contextText = "Se ha realizado el registro de forma exitosa dentro del sistema";
        Alert alert = new Alert(Alert.AlertType.INFORMATION, contextText, AWARE_BUTTON_TYPE);
        alert.setHeaderText(null);
        alert.setTitle("¡Registro exitoso!");
        alert.setResizable(false);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == AWARE_BUTTON_TYPE) {
            isAccepted = true;
        }
        return isAccepted;
    }

    public static boolean showSuccessfullUpdateAlert() {
        boolean isAccepted = false;
        String contextText = "Se ha realizado la actualización de forma exitosa dentro del sistema";
        Alert alert = new Alert(Alert.AlertType.INFORMATION, contextText, AWARE_BUTTON_TYPE);
        alert.setHeaderText(null);
        alert.setTitle("¡Actualización exitosa!");
        alert.setResizable(false);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == AWARE_BUTTON_TYPE) {
            isAccepted = true;
        }
        return isAccepted;
    }

    public static boolean showConnectionErrorAlert() {
        boolean isAccepted = false;
        String contextText = "Al parecer no existe una conexión a internet. Revisa tu conexión a internet e intentalo de nuevo";
        Alert alert = new Alert(Alert.AlertType.ERROR, contextText, AWARE_BUTTON_TYPE);
        alert.setHeaderText(null);
        alert.setTitle("¡Error con la conexión a internet!");
        alert.setResizable(false);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == AWARE_BUTTON_TYPE) {
            isAccepted = true;
        }
        return isAccepted;
    }

}
