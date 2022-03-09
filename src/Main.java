import controller.LoginController;
import controller.academicgroup.AddMemberController;
import controller.academicgroup.ModifyMemberController;
import javafx.application.Application;
import javafx.stage.Stage;
import model.dao.MiembroDAO;
import model.domain.CivilStatus;
import model.domain.Integrant;
import model.domain.Member;
import model.domain.Responsable;

import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
//        LoginController loginController = new LoginController();
//        loginController.showStage();
        ModifyMemberController modifyMemberController = new ModifyMemberController();
        modifyMemberController.showStage();
    }


    public static void main(String[] args){
        launch(args);
    }

}