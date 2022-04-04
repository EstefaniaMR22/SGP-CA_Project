import controller.LoginController;
import controller.ResponsableController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
//        LoginController loginController = new LoginController();
//        loginController.showStage();
        new ResponsableController().showStage();
    }

    public static void main(String[] args){
        launch(args);
    }

}