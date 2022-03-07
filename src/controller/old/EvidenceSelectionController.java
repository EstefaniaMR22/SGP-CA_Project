/*
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package controller.old;

import java.net.URL;
import java.util.ResourceBundle;

import model.old.domain.Article;
import model.old.domain.Book;
import model.old.domain.Integrant;
import model.old.domain.Prototype;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

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
        FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("EvidenceList.fxml", btnCancel);
        EvidenceListController controller = loader.getController();
        controller.showGeneralResumeEvidences(token);
    }

    @FXML
    private void selectReceptionWork(ActionEvent event){
        FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("ReceptionWorkForm.fxml", btnCancel);
        ReceptionWorkFormController controller = loader.getController();
        controller.receiveReceptionWorkSaveToken(token);
    }

    @FXML
    private void selectArticle(ActionEvent event){
        FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("EvidenceEdit.fxml", btnCancel);
        EvidenceEditController controller = loader.getController();
        Article article = new Article();
        controller.receiveArticleAndToken(article,token);
    }

    @FXML
    private void selectPrototype(ActionEvent event){
        FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("EvidenceEdit.fxml", btnCancel);
        EvidenceEditController controller = loader.getController();
        Prototype prototype = new Prototype();
        controller.receivePrototypeAndToken(prototype,token);
    }

    @FXML
    private void selectBook(ActionEvent event){
        FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("EvidenceEdit.fxml", btnCancel);
        EvidenceEditController controller = loader.getController();
        Book book = new Book();
        controller.receiveBookAndToken(book,token);
    }
    
}
