/*
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package sgp.ca.demodao;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import sgp.ca.domain.Article;
import sgp.ca.domain.Book;
import sgp.ca.domain.Integrant;
import sgp.ca.domain.Prototype;

public class EvidenceSelectionController implements Initializable{

    @FXML
    private Button btnCancel;
    @FXML
    private Button btnSelectReceptionWork;
    @FXML
    private Button btnSelectArticle;
    @FXML
    private Button btnSelectPrototype;
    @FXML
    private Button btnSelectBook;
    
    private Integrant token;

    @Override
    public void initialize(URL url, ResourceBundle rb){
    }    
    
    public void receiveToken(Integrant token){
        this.token = token;
    }

    @FXML
    private void selectCancel(ActionEvent event){
        FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("EvidenceList.fxml", btnCancel);
        EvidenceListController controller = loader.getController();
        controller.showGeneralResumeEvidences(token);
    }

    @FXML
    private void selectReceptionWork(ActionEvent event){
        FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("ReceptionWorkForm.fxml", btnCancel);
        ReceptionWorkFormController controller = loader.getController();
        controller.receiveReceptionWorkSaveToken(token);
    }

    @FXML
    private void selectArticle(ActionEvent event){
        FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("EvidenceEdit.fxml", btnCancel);
        EvidenceEditController controller = loader.getController();
        Article article = new Article();
        controller.receiveArticleAndToken(article,token);
    }

    @FXML
    private void selectPrototype(ActionEvent event){
        FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("EvidenceEdit.fxml", btnCancel);
        EvidenceEditController controller = loader.getController();
        Prototype prototype = new Prototype();
        controller.receivePrototypeAndToken(prototype,token);
    }

    @FXML
    private void selectBook(ActionEvent event){
        FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("EvidenceEdit.fxml", btnCancel);
        EvidenceEditController controller = loader.getController();
        Book book = new Book();
        controller.receiveBookAndToken(book,token);
    }
    
}
