/*
 * @author Josué 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package controller.old;

import java.net.URL;
import java.util.ResourceBundle;

import model.domain.Integrant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class StartController implements Initializable{

    @FXML
    private Button btnGeneralResume;
    @FXML
    private Button btnPersonalResume;
    @FXML
    private Button btnWorkPlan;
    @FXML
    private Button btnExit;
    @FXML
    private Label lblUserName;
    @FXML
    private Button btnAcademyProduction;
    @FXML
    private Button btnMeetingsRequest;
    @FXML
    private Button btnProject;
    
    private Integrant token;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        token = new Integrant();
    }  
    
    public void receiveIntegrantToken(Integrant integrant){
        this.token = integrant;
        this.lblUserName.setText(this.token.getFullName());
    }

    @FXML
    private void consultGeneralResume(ActionEvent event){
        FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("GeneralResumeRequest.fxml", btnExit);
        GeneralResumeRequestController controller = loader.getController();
        controller.showGeneralResume(token);
    }

    @FXML
    private void consultPersonalResume(ActionEvent event){
        FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("PersonalResumeRequest.fxml", btnExit);
        PersonalResumeRequestController controller = loader.getController();
        controller.receiveIntegrantToken(token);
    }

    @FXML
    private void consultWorkPlan(ActionEvent event){
        FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("WorkPlanRequest.fxml", btnExit);
        WorkPlanRequestController controller = loader.getController();
        controller.receiveToken(token);
    }
    
    @FXML
    private void consultProject(ActionEvent event){
        FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("ProjectList.fxml", btnExit);
        ProjectListController controller = loader.getController();
        controller.receiveToken(token);
    }
    
    @FXML
    private void consultEvidencesList(ActionEvent event){
        FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("EvidenceList.fxml", btnExit);
        EvidenceListController controller = loader.getController();
        controller.showGeneralResumeEvidences(token);
    }

    @FXML
    private void consultMeetings(ActionEvent event){
        FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("MeetingHistory.fxml", btnExit);
        MeetingHistoryController controller = loader.getController();
        controller.receiveToken(token);
    }

    @FXML
    private void exit(ActionEvent event){
        WindowControllerB.getWindowController().changeWindow("Login.fxml", btnExit);
    }
    
}
