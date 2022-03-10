package controller.academicgroup;

import controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import model.dao.MiembroDAO;
import model.domain.Integrant;
import model.domain.Member;
import model.domain.ParticipationType;
import model.domain.Responsable;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MemberDetailsController extends Controller implements Initializable {
    private Member memberSelected;
    @FXML private Label aditionalEmailLabel;
    @FXML private Label appointmentLabel;
    @FXML private Label civilStatusLabel;
    @FXML private Label curpLabel;
    @FXML private Label directedEvidencesLabel;
    @FXML private Label educationalProgramLabel;
    @FXML private Label homeTelephoneLabel;
    @FXML private Label nameLabel;
    @FXML private Label nationalityLabel;
    @FXML private Label numberAsistanceRoleLabel;
    @FXML private Label numberParticipationLAbel;
    @FXML private Label personalNumberLabel;
    @FXML private Label rfcLabel;
    @FXML private Label stateLabel;
    @FXML private Label telephoneLabel;
    @FXML private Label themesNumberLabel;
    @FXML private Label totalActionsLabel;
    @FXML private Label typeMemberLabel;
    @FXML private Label uvEmailNumberLabel;
    @FXML private Label workTelephoneNumberLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getMemberDetails();
        setMemberDataToTextField();
    }

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/MemberDetailsView.fxml"), this);
        stage.showAndWait();
    }

    public MemberDetailsController(Member member) {
        this.memberSelected = member;
    }

    @FXML
    void cancelOnAction(ActionEvent event) {
        stage.close();
    }

    @FXML
    void updateMemberOnAction(ActionEvent event) {
        stage.hide();
        ModifyMemberController modifyMemberController = new ModifyMemberController(memberSelected);
        modifyMemberController.showStage();
        stage.show();
    }

    private void setMemberDataToTextField() {
        nameLabel.setText(memberSelected.getName() + " " + memberSelected.getPaternalLastname() + " " + memberSelected.getMaternalLastname());
        rfcLabel.setText(memberSelected.getRfc());
        curpLabel.setText(memberSelected.getCurp());
        telephoneLabel.setText(memberSelected.getTelephone());
        civilStatusLabel.setText(memberSelected.getCivilStatus().getCivilStatus().replaceAll("_", " "));
        personalNumberLabel.setText(memberSelected.getPersonalNumber());
        uvEmailNumberLabel.setText(memberSelected.getUvEmail());
        educationalProgramLabel.setText(memberSelected.getEducationalProgram());
        stateLabel.setText(memberSelected.getState());
        typeMemberLabel.setText(memberSelected.getParticipationType().getParticipationType());
        nationalityLabel.setText(memberSelected.getNationality());
    }

    private void getMemberDetails() {
        if(memberSelected.getParticipationType() == ParticipationType.INTEGRANT) {
            try {
                memberSelected = new MiembroDAO().getIntegrantDetails(memberSelected.getId());
                appointmentLabel.setText(((Integrant) memberSelected).getAppointment());
                homeTelephoneLabel.setText(((Integrant) memberSelected).getHomeTelephone());
                workTelephoneNumberLabel.setText(((Integrant) memberSelected).getWorkTelephone());
                aditionalEmailLabel.setText(((Integrant) memberSelected).getAditionalEmail());
            } catch (SQLException sqlException) {
                Logger.getLogger(MemberDetailsController.class.getName()).log(Level.SEVERE, null, sqlException);
            }
        } else if (memberSelected.getParticipationType() == ParticipationType.RESPONSABLE) {
            try {
                memberSelected = new MiembroDAO().getResponsableDetails(memberSelected.getId());
                appointmentLabel.setText(((Responsable) memberSelected).getAppointment());
                homeTelephoneLabel.setText(((Responsable) memberSelected).getHomeTelephone());
                workTelephoneNumberLabel.setText(((Responsable) memberSelected).getWorkTelephone());
                aditionalEmailLabel.setText(((Responsable) memberSelected).getAditionalEmail());
            } catch (SQLException sqlException) {
                Logger.getLogger(MemberDetailsController.class.getName()).log(Level.SEVERE, null, sqlException);
            }
        }
    }

}
