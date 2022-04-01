import controller.AdministratorController;
import controller.LoginController;
import controller.academicgroup.AddMemberController;
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