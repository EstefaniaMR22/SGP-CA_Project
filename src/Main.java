import controller.LoginController;
import controller.academicgroup.AddAcademicGroupController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
//        LoginController loginController = new LoginController();
//        loginController.showStage();
        new AddAcademicGroupController().showStage();
    }

    public static void main(String[] args){
        launch(args);
    }

}