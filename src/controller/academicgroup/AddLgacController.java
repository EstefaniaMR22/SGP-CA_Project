package controller.academicgroup;

import controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.dao.LgacDAO;
import model.domain.LGAC;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddLgacController extends Controller {
    @FXML private TextField identificatorTextField;
    @FXML private TextArea descriptionTextArea;

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/AddLgacView.fxml"), this);
        stage.showAndWait();
    }

    @FXML
    void cancelOnAction(ActionEvent event) {
        stage.close();
    }

    @FXML
    void registerOnAction(ActionEvent event) {
        LGAC lgac = new LGAC();
        lgac.setDescription(descriptionTextArea.getText());
        lgac.setIdentification(identificatorTextField.getText());
        try {
            lgac.setId(new LgacDAO().addLgac(lgac));
        } catch (SQLException sqlException) {
            Logger.getLogger(AddLgacController.class.getName()).log(Level.SEVERE, null, sqlException);
        }
        System.out.println(lgac.getId());

    }



}
