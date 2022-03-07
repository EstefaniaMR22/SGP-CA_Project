/*
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package controller.old;

import java.net.URL;
import java.util.ResourceBundle;

import controller.old.pattern.DialogBox;
import controller.old.pattern.EvidenceWindow;
import model.old.domain.Article;
import model.old.domain.Evidence;
import model.old.domain.Integrant;
import model.old.dataaccess.ArticleDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ArticleController implements Initializable, EvidenceWindow {

    @FXML
    private Label lbUsername;
    @FXML
    private Button btnUpdateEvidence;
    @FXML
    private Button btnRemoveEvidence;
    @FXML
    private Button btnCloseWindowEvidenceRequest;
    @FXML
    private CheckBox chBoxImpactAB;
    @FXML
    private Label lbTypeEvidence;
    @FXML
    private TextField txtFieldEvidenceTittle;
    @FXML
    private TextField txtFieldPublicationCountry;
    @FXML
    private TextField txtFieldPublisherDate;
    @FXML
    private TextField txtFieldInvestigationProject;
    @FXML
    private TextField txtFieldStudyDegree;
    @FXML
    private ListView<String> lvIntegrants;
    @FXML
    private ListView<String> lvCollaborators;
    @FXML
    private ListView<String> lvStudents;
    @FXML
    private TextField txtFieldMagazineName;
    @FXML
    private TextField txtFieldMagazineEditorial;
    @FXML
    private TextField txtFieldISnn;
    @FXML
    private TextArea txtAreaIndex;
    @FXML
    private Button btnDownloadDocument;
    @FXML
    private Label lbDocumentName;
    @FXML
    private HBox hbOptions;

    private Integrant token;
    private final ArticleDAO ARTICLE_DAO = new ArticleDAO();
    private Article article = new Article();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        hbOptions.getChildren().removeAll(btnRemoveEvidence, btnUpdateEvidence);
    } 
    
    public void showArticleByUrl(String url, Integrant token){
        this.token = token;
        this.lbUsername.setText(token.getFullName());
        this.article = (Article) ARTICLE_DAO.getEvidenceByUrl(url);
        if(this.article != null){
            this.setArticleDataIntoInterface();
            this.grantPermissions();
        }
    }

    @FXML
    private void updateEvidence(ActionEvent event){
        FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("EvidenceEdit.fxml", btnCloseWindowEvidenceRequest);
        EvidenceEditController controller = loader.getController();
        controller.receiveArticleAndToken(this.article,token);
    }

    @FXML
    private void removeEvidence(ActionEvent event){
    }

    @FXML
    private void closeWindowEvidenceRequest(ActionEvent event){
        FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("EvidenceList.fxml", btnCloseWindowEvidenceRequest);
        EvidenceListController controller = loader.getController();
        controller.showGeneralResumeEvidences(token);
    }

    @FXML
    private void downloadDocument(ActionEvent event){
        DialogBox dialogBox = new DialogBox((Stage)((Node)event.getSource()).getScene().getWindow());
        if(dialogBox.openDialogDirectorySelector(this.article.getUrlFile())){
            WindowControllerB.getWindowController().showConfirmationAlert(event, "Archivo descargado correctamente");
        }else{
            WindowControllerB.getWindowController().showErrorAlert(event, "El artículo no fue descargado");
        }
    }

    
    private void setArticleDataIntoInterface(){
        if(this.article.getImpactAB()){
            this.chBoxImpactAB.setSelected(true);
        }
        lvStudents.getItems().addAll(this.article.getStudents());
        this.article.getIntegrants().forEach(integrant -> this.lvIntegrants.getItems().add(integrant.getFullName()));
        this.article.getCollaborators().forEach(collaborator -> this.lvCollaborators.getItems().add(collaborator.getFullName()));
        this.txtFieldMagazineName.setText(this.article.getMagazineName());
        this.txtFieldMagazineEditorial.setText(this.article.getMagazineEditorial());
        this.txtFieldISnn.setText(this.article.getIsnn() + "");
        this.lbTypeEvidence.setText(this.article.getEvidenceType());
        this.txtFieldEvidenceTittle.setText(this.article.getEvidenceTitle());
        this.txtFieldPublisherDate.setText(this.article.getPublicationDate());
        this.txtFieldPublicationCountry.setText(this.article.getCountry());
        this.txtFieldInvestigationProject.setText(this.article.getProjectName());
        this.txtFieldStudyDegree.setText(this.article.getStudyDegree());
        this.lbDocumentName.setText(this.article.getUrlFile());
    }
    
    private void grantPermissions(){
        if(this.token.getRfc().equalsIgnoreCase(this.article.getRegistrationResponsible())){
            hbOptions.getChildren().addAll(btnUpdateEvidence);
        }
    }
    
    @Override
    public String toString(){
        return "Artículo";
    }

    @Override
    public void createWindowAccordingEvidenceType(Evidence evidence, Node graphicElement, Integrant token){
        if(this.toString().equalsIgnoreCase(evidence.getEvidenceType())){
            FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("Article.fxml", graphicElement);
            ArticleController controller = loader.getController();
            controller.showArticleByUrl(evidence.getUrlFile(), token);
        }
    }
    
}
