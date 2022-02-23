/*
 * @author Todos
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

import controller.LoginController;
import controller.old.IntegrantEditableController;
import controller.old.WindowControllerB;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
//        LoginController loginController = new LoginController();
//        loginController.showStage();
//        Parent root = FXMLLoader.load(getClass().getResource("view/Login.fxml"));
//        Scene scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();

        FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("IntegrantEditable.fxml", btnSignIn);
        IntegrantEditableController controller = loader.getController();
        controller.showResponsibleInscriptionForm();
    }

    public static void main(String[] args){
        launch(args);
    }

}