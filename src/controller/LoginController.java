/*
 * @author Josué 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package controller;

import java.net.URL;
import java.util.ResourceBundle;

import controller.pattern.GenericWindowDriver;
import model.domain.Integrant;
import model.dataaccess.IntegrantDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class LoginController implements Initializable{

    @FXML
    private Button btnSignIn;
    @FXML
    private Label lbSignUpNewBdyAcademy;
    @FXML
    private Label lbSignUpNewUser;
    @FXML
    private TextField txtFieldEmailUv;
    @FXML
    private PasswordField passFieldPasswordUvmail;
    @FXML
    private TextField txtFieldBodyAcademyKey;
    
    private final IntegrantDAO INTEGRANT_DAO = new IntegrantDAO();
    private Integrant integrantLogger;
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        integrantLogger = new Integrant();
    }    

    @FXML
    private void signIn(ActionEvent event){
        if(checkUserLoginWithBodyKey()){
            FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("Start.fxml", btnSignIn);
            StartController controller = loader.getController();
            controller.receiveIntegrantToken(integrantLogger);
        }else{
            GenericWindowDriver.getGenericWindowDriver().showErrorAlert(event, "Hay campos inválidos en el formulario");
        }
    }
    
    @FXML
    private void signUpNewBdyAcademy(MouseEvent event){
        if(checkUserLogin() && this.integrantLogger.getBodyAcademyKey()== null){
            FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("GeneralResumeEditable.fxml", btnSignIn);
            GeneralResumeEditableController controller = loader.getController();
            controller.showGeneralResumeInsertForm(integrantLogger);
        }else{
            txtFieldEmailUv.setStyle("-fx-border-color: red;");
            passFieldPasswordUvmail.setStyle("-fx-border-color: red;");
            GenericWindowDriver.getGenericWindowDriver().showErrorAlert(
                event, "El email y contraseña deben estar llenos correctamente y No debes pertenecer a ningún cuerpo académico"
            );
        }
    }

    @FXML
    private void signUpNewUser(MouseEvent event){
        FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("IntegrantEditable.fxml", btnSignIn);
        IntegrantEditableController controller = loader.getController();
        controller.showResponsibleInscriptionForm();
    }
    
    private boolean checkUserLogin(){
        integrantLogger.setEmailUV(this.txtFieldEmailUv.getText());
        integrantLogger.setPassword(this.passFieldPasswordUvmail.getText());
        integrantLogger = INTEGRANT_DAO.getIntegrantToken(this.integrantLogger.getEmailUV(), this.integrantLogger.getPassword());
        boolean isVerify = false;
        if(integrantLogger.getFullName() != null){
            isVerify = true;
        }
        return isVerify;
    }
    
    private boolean checkUserLoginWithBodyKey(){
        integrantLogger.setEmailUV(this.txtFieldEmailUv.getText());
        integrantLogger.setPassword(this.passFieldPasswordUvmail.getText());
        integrantLogger.setBodyAcademyKey(this.txtFieldBodyAcademyKey.getText());
        boolean isVerify = false;
        integrantLogger = INTEGRANT_DAO.getIntegrantTocken(integrantLogger);
        if(integrantLogger.getFullName() != null && integrantLogger.getParticipationStatus().equalsIgnoreCase("Activo")){
            isVerify = true;
        }
        return isVerify;
    }
    
}
