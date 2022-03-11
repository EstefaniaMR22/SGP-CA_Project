import controller.AdministratorController;
import controller.LoginController;
import controller.ResponsableController;
import controller.academicgroup.AddAcademicGroupProgramController;
import controller.academicgroup.AddMemberController;
import controller.academicgroup.ModifyMemberController;
import javafx.application.Application;
import javafx.stage.Stage;
import model.dao.MiembroDAO;
import model.dao.interfaces.AcademicGroupProgramDAO;
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
//        ModifyMemberController modifyMemberController = new ModifyMemberController();
//        modifyMemberController.showStage();
//        AdministratorController administratorController = new AdministratorController();
//        administratorController.showStage();
//        AddAcademicGroupProgramController addAcademicGroupProgramController = new AddAcademicGroupProgramController();
//        addAcademicGroupProgramController.showStage();
        ResponsableController responsableController = new ResponsableController();
        responsableController.showStage();
        //new AcademicGroupProgramDAO().getAllAcademicGroupPrograms().forEach(System.out::println);
        //System.out.println("");
//        System.out.println(new AcademicGroupProgramDAO().getAcademicGroupProgramDetails("hola"));
    }


    public static void main(String[] args){
        launch(args);
    }

}