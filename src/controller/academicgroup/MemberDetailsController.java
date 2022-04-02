package controller.academicgroup;

import controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import model.domain.Member;

import java.net.URL;
import java.util.ResourceBundle;

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
    @FXML private Label personalNumberLabel;
    @FXML private Label rfcLabel;
    @FXML private Label stateLabel;
    @FXML private Label telephoneLabel;
    @FXML private Label themesNumberLabel;
    @FXML private Label uvEmailNumberLabel;
    @FXML private Label workTelephoneNumberLabel;
    @FXML private Label admissionDateLabel;
    @FXML private Label birtdateLabel;
    @FXML private Label studyAreaLabel;
    @FXML private Label studyGradeLabel;
    @FXML private Label maternalLastName;
    @FXML private Label paternalLastName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setMemberDataToTextField();
    }

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/MemberDetailsView.fxml"), this);
        stage.showAndWait();
    }

    public MemberDetailsController(Member member) {
        this.memberSelected = member;
    }

    public Member getMemberSelected() {
        return memberSelected;
    }

    @FXML
    void cancelOnAction(ActionEvent event) {
        stage.close();
    }

    @FXML
    void updateMemberOnAction(ActionEvent event) {
        ModifyMemberController modifyMemberController = new ModifyMemberController(memberSelected);
        modifyMemberController.showStage();
        if(!modifyMemberController.getMemberSelected().equals(memberSelected)) {
            memberSelected = modifyMemberController.getMemberSelected();
            setMemberDataToTextField();
        }
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
        nationalityLabel.setText(memberSelected.getNationality());
        admissionDateLabel.setText(memberSelected.getAdmissionDate().toString());
        birtdateLabel.setText(memberSelected.getBirthDate().toString());
        appointmentLabel.setText(memberSelected.getAppointment());
        homeTelephoneLabel.setText(memberSelected.getHomeTelephone());
        workTelephoneNumberLabel.setText(memberSelected.getWorkTelephone());
        aditionalEmailLabel.setText(memberSelected.getAditionalEmail());
        studyGradeLabel.setText(memberSelected.getMaxStudyGrade().toString());
        studyAreaLabel.setText(memberSelected.getStudyArea());
        maternalLastName.setText(memberSelected.getMaternalLastname());
        paternalLastName.setText(memberSelected.getPaternalLastname());
    }
}
