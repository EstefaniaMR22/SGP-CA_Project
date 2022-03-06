/*
 * @author Todos
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

import controller.general.LoginController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        LoginController loginController = new LoginController();
        loginController.showStage();
    }

    public static void main(String[] args){
        launch(args);
    }

}