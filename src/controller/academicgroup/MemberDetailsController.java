package controller.academicgroup;

import controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MemberDetailsController extends Controller {


    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/MemberDetailsView.fxml"), this);
        stage.showAndWait();
    }


    @FXML
    private Label aditionalEmailLabel;

    @FXML
    private Label appointmentLabel;

    @FXML
    private Label civilStatusLabel;

    @FXML
    private Label curpLabel;

    @FXML
    private Label directedEvidencesLabel;

    @FXML
    private Label educationalProgramLabel;

    @FXML
    private Label homeTelephoneLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label nationalityLabel;

    @FXML
    private Label numberAsistanceRoleLabel;

    @FXML
    private Label numberParticipationLAbel;

    @FXML
    private Label personalNumberLabel;

    @FXML
    private Label rfcLabel;

    @FXML
    private Label stateLabel;

    @FXML
    private Label telefonoLabel;

    @FXML
    private Label themesNumberLabel;

    @FXML
    private Label totalActionsLabel;

    @FXML
    private Label typeMemberLabel;

    @FXML
    private Label uvEmailNumber;

    @FXML
    private Label workTelephoneNumber;

    @FXML
    void cancelOnAction(ActionEvent event) {
        stage.close();
    }

    @FXML
    void updateMemberOnAction(ActionEvent event) {

    }


}
