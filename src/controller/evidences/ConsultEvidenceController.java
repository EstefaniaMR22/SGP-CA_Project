package controller.evidences;

import assets.utils.DateFormatter;
import assets.utils.SQLStates;
import controller.academicgroup.AddMemberController;
import controller.control.AlertController;
import controller.control.ValidatorController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.dao.EvidenceDAO;
import model.domain.Evidence;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsultEvidenceController extends ValidatorController implements Initializable {

    @FXML TextField projectTextField;
    @FXML TextField typeEvidenceTextField;
    @FXML TextField titleBookTextField;
    @FXML TextField titleTextField;
    @FXML TextField referTextField;
    @FXML TextField countryTextField;
    @FXML TextField editionNumberTextField;
    @FXML TextField isbnTextField;
    @FXML TextField editorialTextField;
    @FXML TextField pagesTextField;
    @FXML TextField articleISNNTextField;
    @FXML TextField articleIndiceTextField;
    @FXML TextField articleNameMagazineTextField;
    @FXML TextField characteristicsPrototipeTextField;
    @FXML TextField publicationDateTextField;
    @FXML VBox bookVbox;
    @FXML HBox chapterVbox;
    @FXML VBox prototipeVbox;
    @FXML VBox articleVbox;


    private  int idEvidence;
    private Evidence consultEvidence;

    public ConsultEvidenceController(int idEvidence) {
        this.idEvidence = idEvidence;

    }

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/ConsultEvidencesView.fxml"), this);
        stage.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        try{
        consultEvidence = evidenceDAO.getEvidence(idEvidence);
        } catch(SQLException chargeLGACException) {
            deterMinateSQLState(chargeLGACException);
        }
        chargeDetailsEvidence();
        disableTextFields();
        chargeEvidence();
    }

    private void chargeDetailsEvidence(){
        projectTextField.setText(consultEvidence.getProjectName());
        typeEvidenceTextField.setText(consultEvidence.getEvidenceType());
        titleBookTextField.setText(consultEvidence.getTitleBook());
        titleTextField.setText(consultEvidence.getEvidenceTitle());
        referTextField.setText(consultEvidence.getReference());
        countryTextField.setText(consultEvidence.getCountry());
        editionNumberTextField.setText(consultEvidence.getEditionNumberBook());
        isbnTextField.setText(consultEvidence.getIsbnBook());
        editorialTextField.setText(consultEvidence.getEditorialBook());
        pagesTextField.setText(consultEvidence.getPagesBook());
        articleISNNTextField.setText(consultEvidence.getIsnnMagazine());
        articleIndiceTextField.setText(consultEvidence.getIndiceMagazine());
        articleNameMagazineTextField.setText(consultEvidence.getNameMagazine());
        characteristicsPrototipeTextField.setText(consultEvidence.getDescriptionEvidence());
        publicationDateTextField.setText(DateFormatter.getParseDate(consultEvidence.getPublicationDate()));
    }

    private void chargeEvidence(){

        switch (consultEvidence.getEvidenceType()){
            case "Libro":

                bookVbox.setVisible(true);
                break;
            case "Capitulo de libro":

                    chapterVbox.setVisible(true);

                break;
            case "Articulo":

                articleVbox.setVisible(true);

                break;
            case "Prototipo":

                prototipeVbox.setVisible(true);
                break;
        }

    }

    private void deterMinateSQLState(SQLException sqlException) {
        Logger.getLogger(AddMemberController.class.getName()).log(Level.SEVERE, null, sqlException);
        if(sqlException.getSQLState().equals(SQLStates.SQL_NO_CONNECTION.getSqlState())) {
            AlertController.getInstance().showConnectionErrorAlert();
        }
        AlertController.getInstance().showActionFailedAlert(sqlException.getLocalizedMessage());
    }

    private void disableTextFields(){
        projectTextField.setDisable(true);
        typeEvidenceTextField.setDisable(true);
        titleTextField.setDisable(true);
        referTextField.setDisable(true);
        countryTextField.setDisable(true);
        publicationDateTextField.setDisable(true);

        bookVbox.setDisable(true);
        bookVbox.setVisible(false);

        chapterVbox.setDisable(true);
        chapterVbox.setVisible(false);

        prototipeVbox.setDisable(true);
        prototipeVbox.setVisible(false);

        articleVbox.setDisable(true);
        articleVbox.setVisible(false);
    }



    @FXML
    void returnViewOnAction(ActionEvent actionEvent) {
        try{
            stage.close();
        }catch(Exception returnViewOnActionExeception){
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" No se pudo volver a la ventana anterior." +
                    "Causa: " + returnViewOnActionExeception);

        }
    }

}
