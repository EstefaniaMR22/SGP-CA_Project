package controller.control;

import assets.utils.SQLStates;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import java.sql.SQLException;
import java.util.Optional;

public class AlertController {
    private final static ButtonType ACCEPT_BUTTON_TYPE = new ButtonType("¡Si, deseo continuar!", ButtonBar.ButtonData.APPLY);
    private final static ButtonType CANCEL_BUTTON_TYPE = new ButtonType("¡No, deseo cancelar!", ButtonBar.ButtonData.CANCEL_CLOSE);
    private final static ButtonType AWARE_BUTTON_TYPE = new ButtonType("Enterado", ButtonBar.ButtonData.YES);
    private final static ButtonType CANCEL_CONFIRMATION_BUTTON_TYPE = new ButtonType("Sí, deseo cancelar", ButtonBar.ButtonData.YES);
    private final static ButtonType NO_CANCEL_CONFIRMATION_BUTTON_TYPE = new ButtonType("No,no deseo cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
    private static Alert alert;
    private static AlertController instance;

    public static AlertController getInstance() {
        if (instance == null) {
            instance = new AlertController();
        }
        return instance;
    }

    public boolean showConfirmationAlert() {
        boolean isAccepted = false;
        String contextText = "¿Estas seguro que deseas realizar esta acción?";
        alert = new Alert(Alert.AlertType.CONFIRMATION, contextText, ACCEPT_BUTTON_TYPE, CANCEL_BUTTON_TYPE);
        setStyleClass();
        setContentMessage(alert, contextText);
        alert.getDialogPane().lookupButton(ACCEPT_BUTTON_TYPE).getStyleClass().add("main-button");
        alert.getDialogPane().lookupButton(CANCEL_BUTTON_TYPE).getStyleClass().add("secondary-button");
        alert.setHeaderText("¡Confirmación!");
        alert.setResizable(false);
        alert.setTitle("Confirmacion");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ACCEPT_BUTTON_TYPE) {
            isAccepted = true;
        }
        return isAccepted;
    }


    public boolean showActionFailedAlert(String errorMessage) {
        boolean isAccepted = false;
        String contextText = "Al parecer hubo un error en el sistema y no se ha podido realizar la acción. \n\nError: " + errorMessage;
        alert = new Alert(Alert.AlertType.ERROR, "", AWARE_BUTTON_TYPE);
        setStyleClass();
        setContentMessage(alert, contextText);
        alert.getDialogPane().lookupButton(AWARE_BUTTON_TYPE).getStyleClass().add("main-button");
        alert.setHeaderText("¡No se ha podido realizar la acción!");
        alert.setResizable(false);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == AWARE_BUTTON_TYPE) {
            isAccepted = true;
        }
        return isAccepted;
    }

    public boolean showCancelationConfirmationAlert() {
        boolean isAccepted = false;
        String contextText = "Esta acción eliminará los datos ingresados en el formulario y no se podrán recuperar. \n\n¿Está seguro que desea cancelar?";
        alert = new Alert(Alert.AlertType.CONFIRMATION, contextText, CANCEL_CONFIRMATION_BUTTON_TYPE, NO_CANCEL_CONFIRMATION_BUTTON_TYPE);
        setStyleClass();
        setContentMessage(alert, contextText);
        alert.getDialogPane().lookupButton(CANCEL_CONFIRMATION_BUTTON_TYPE).getStyleClass().add("main-button");
        alert.getDialogPane().lookupButton(NO_CANCEL_CONFIRMATION_BUTTON_TYPE).getStyleClass().add("secondary-button");
        alert.setHeaderText("¡Confirmación de cancelación!");
        alert.setTitle("Confirmación cancelación");
        alert.setResizable(false);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == CANCEL_CONFIRMATION_BUTTON_TYPE) {
            isAccepted = true;
        }
        return isAccepted;
    }

    public boolean showSuccessfullRemoveAlert() {
        boolean isAccepted = false;
        String contextText = "Se ha eliminado con éxito el registro dentro del sistema";
        alert = new Alert(Alert.AlertType.INFORMATION, contextText, AWARE_BUTTON_TYPE);
        setStyleClass();
        setContentMessage(alert, contextText);
        alert.getDialogPane().lookupButton(AWARE_BUTTON_TYPE).getStyleClass().add("main-button");
        alert.setHeaderText("¡Eliminación exitosa!");
        alert.setResizable(false);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == AWARE_BUTTON_TYPE) {
            isAccepted = true;
        }
        return isAccepted;
    }

    public boolean showSuccessfullRegisterAlert() {
        boolean isAccepted = false;
        String contextText = "Se ha realizado el registro de forma exitosa dentro del sistema";
        alert = new Alert(Alert.AlertType.INFORMATION, contextText, AWARE_BUTTON_TYPE);
        setStyleClass();
        setContentMessage(alert, contextText);
        alert.getDialogPane().lookupButton(AWARE_BUTTON_TYPE).getStyleClass().add("main-button");
        alert.setHeaderText("Registro exitoso!");
        alert.setResizable(false);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == AWARE_BUTTON_TYPE) {
            isAccepted = true;
        }
        return isAccepted;
    }

    public boolean showSuccessfullUpdateAlert() {
        boolean isAccepted = false;
        String contextText = "Se ha realizado la actualización de forma exitosa dentro del sistema";
        alert = new Alert(Alert.AlertType.INFORMATION, contextText, AWARE_BUTTON_TYPE);
        setStyleClass();
        setContentMessage(alert, contextText);
        alert.getDialogPane().lookupButton(AWARE_BUTTON_TYPE).getStyleClass().add("main-button");
        alert.setHeaderText("¡Actualización exitosa!");
        alert.setResizable(false);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == AWARE_BUTTON_TYPE) {
            isAccepted = true;
        }
        return isAccepted;
    }

    public boolean showConnectionErrorAlert() {
        boolean isAccepted = false;
        String contextText = "Al parecer no existe una conexión a internet. Revisa tu conexión a internet e intentalo de nuevo";
        alert = new Alert(Alert.AlertType.ERROR, contextText, AWARE_BUTTON_TYPE);
        setStyleClass();
        setContentMessage(alert, contextText);
        alert.getDialogPane().lookupButton(AWARE_BUTTON_TYPE).getStyleClass().add("main-button");
        alert.setHeaderText("¡Error con la conexión a internet!");
        alert.setTitle("Error de conexión");
        alert.setResizable(false);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == AWARE_BUTTON_TYPE) {
            isAccepted = true;
        }
        return isAccepted;
    }

    public void determinateAlertBySQLException(Exception exception) {
        System.out.println("*********************");
        System.out.println(exception.getLocalizedMessage());
        System.out.println(exception.getClass());
        System.out.println("*********************");
        if(exception instanceof SQLException) {
        if (((SQLException)  exception).getSQLState().equals(SQLStates.SQL_NO_CONNECTION.getSqlState())) {
            AlertController.getInstance().showConnectionErrorAlert();
        } else {
            AlertController.getInstance().showActionFailedAlert(exception.getLocalizedMessage());
        }
        }
    }

    private AlertController() {

    }

    private void setStyleClass() {
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/assets/style/standard.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog-alert-pane");
    }

    private void setContentMessage(Alert alert, String message) {
        final int MAX_WIDTH = 620;
        final int MIN_WIDTH = 500;
        final int MAX_HEIGHT = 420;
        Label label = new Label(message);
        label.setWrapText(true);
        label.setMaxWidth(MAX_WIDTH);
        alert.setTitle(null);
        alert.getDialogPane().setContent(label);
        alert.getDialogPane().setMinWidth(MIN_WIDTH);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.getDialogPane().setMaxWidth(MAX_WIDTH);
    }
}