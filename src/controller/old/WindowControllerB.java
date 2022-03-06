/*
 * @author Josué
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package controller.old;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import controller.general.LoginController;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class WindowControllerB {

    private static WindowControllerB windowControllerB;
    private Stage stage;

    private WindowControllerB() {
    }

    public static WindowControllerB getWindowController() {
        if (windowControllerB == null) {
            windowControllerB = new WindowControllerB();
        }
        return windowControllerB;
    }

    public void showErrorAlert(Event event, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showInfoAlert(Event event, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Info");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showWarningAlert(Event event, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setTitle("Info");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showConfirmationAlert(Event event, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Confirmacion");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showHeaderAlert(Event event, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Cabecera");
        alert.setTitle("Info");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Optional<ButtonType> showConfirmacionAlert(Event event, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Confirmación");
        alert.setContentText(message);
        Optional<ButtonType> action = alert.showAndWait();
        return action;
    }

    public FXMLLoader changeWindow(String window, Node graphicElement) {
        Stage stage = new Stage();
        FXMLLoader loader = null;
        try {
            loader = new FXMLLoader(getClass().getResource(window));
            stage.setScene(new Scene((Pane) loader.load()));
            stage.show();
            Stage currentStage = (Stage) graphicElement.getScene().getWindow();
            currentStage.close();
        } catch (IOException io) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, io);
        } finally {
            return loader;
        }
    }

}
