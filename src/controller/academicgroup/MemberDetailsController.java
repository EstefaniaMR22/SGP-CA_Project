package controller.academicgroup;

import controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import model.dao.MiembroDAO;
import model.domain.*;

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
    @FXML private Label admissionDateLabel;
    @FXML private Label birtdateLabel;
    @FXML private Label studyAreaLabel;
    @FXML private Label studyGradeLabel;
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
        ModifyMemberController modifyMemberController = new ModifyMemberController(memberSelected);
        modifyMemberController.showStage();
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
        stateLabel.setText(memberSelected.getBirthState());
        typeMemberLabel.setText(memberSelected.getParticipationType().getParticipationType());
        nationalityLabel.setText(memberSelected.getNationality());
        // Ternary operator here!
        admissionDateLabel.setText(memberSelected.getAdmissionDate() != null ?  memberSelected.getAdmissionDate().toString() : null );
        birtdateLabel.setText(memberSelected.getBirthDate() != null ? memberSelected.getBirthState().toString() : null);
    }

    private void getMemberDetails() {
//        if(memberSelected.getParticipationType() == ParticipationType.INTEGRANT) {
//        } else if (memberSelected.getParticipationType() == ParticipationType.RESPONSABLE) {
//            try {
//                memberSelected = new MiembroDAO().getResponsableDetails(memberSelected.getId());
//                appointmentLabel.setText(((Responsable) memberSelected).getAppointment());
//                homeTelephoneLabel.setText(((Responsable) memberSelected).getHomeTelephone());
//                workTelephoneNumberLabel.setText(((Responsable) memberSelected).getWorkTelephone());
//                aditionalEmailLabel.setText(((Responsable) memberSelected).getAditionalEmail());
//            } catch (SQLException sqlException) {
//                Logger.getLogger(MemberDetailsController.class.getName()).log(Level.SEVERE, null, sqlException);
//            }
//        } else if(memberSelected.getParticipationType() == ParticipationType.COLABORATOR ) {
//            try {
//                memberSelected = new MiembroDAO().getColaboratorDetails(memberSelected.getId());
//                studyAreaLabel.setText( ((Colaborator) memberSelected).getStudyArea());
//                studyGradeLabel.setText( ((Colaborator) memberSelected).getMaxStudyGrade().toString());
//            } catch (SQLException sqlException) {
//                Logger.getLogger(MemberDetailsController.class.getName()).log(Level.SEVERE, null, sqlException);
//            }
//        }
    }

}
