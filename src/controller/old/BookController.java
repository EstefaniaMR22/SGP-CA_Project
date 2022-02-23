/*
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package controller.old;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import controller.old.pattern.EvidenceWindow;
import model.domain.Book;
import model.domain.ChapterBook;
import model.domain.Evidence;
import model.domain.Integrant;
import model.dataaccess.BookDAO;
import model.dataaccess.ChapterBookDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import controller.old.pattern.DialogBox;

public class BookController implements Initializable, EvidenceWindow {
    
    @FXML
    private HBox hbOptions;
    @FXML
    private Label lbUsername;
    @FXML
    private Button btnUpdateEvidence;
    @FXML
    private Button btnAddChapterBook;
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
    private TextField txtFieldPublisher;
    @FXML
    private TextField txtFieldISBN;
    @FXML
    private TextField txtFieldNumberEdition;
    @FXML
    private Button btnDownloadDocument;
    @FXML
    private Label lbDocumentName;
    @FXML
    private VBox vbChapterBook;
    @FXML
    private TableView<ChapterBook> tvChapterBook;
    @FXML
    private TableColumn<ChapterBook, String> colTitleChapterBook;
    @FXML
    private TableColumn<ChapterBook, String> colRangePagesChapterBook;
    @FXML
    private ListView<String> lvIntegrants;
    @FXML
    private ListView<String> lvCollaborators;
    @FXML
    private ListView<String> lvStudent;
    
    private Integrant token;
    private final BookDAO BOOK_DAO= new BookDAO();
    private final ChapterBookDAO CHAPTERBOOK_DAO = new ChapterBookDAO();
    private Book book;
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        hbOptions.getChildren().removeAll(btnRemoveEvidence, btnUpdateEvidence, btnAddChapterBook);
        book = new Book();
        this.prepareChapterBookTable();
    }
    
    public void showBook(String url, Integrant token){
        this.token = token;
        this.lbUsername.setText(token.getFullName());
        this.book = (Book) BOOK_DAO.getEvidenceByUrl(url);
        if(book == null){
            WindowControllerB.getWindowController().showErrorAlert(new ActionEvent(), "Lo sentimos, el sistema no está funcionando correctamente");
        }else{
            this.setDataIntoBookInterface();
            this.prepareChapterBookTable();
            this.grantPermissions();
        }
    }

    @FXML
    private void updateEvidence(ActionEvent event){
        FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("EvidenceEdit.fxml", btnCloseWindowEvidenceRequest);
        EvidenceEditController controller = loader.getController();
        controller.receiveBookAndToken(this.book,token);
    }

    @FXML
    private void addChapterBook(ActionEvent event){
        FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("ChapterBookEdit.fxml", btnCloseWindowEvidenceRequest);
        ChapterBookEditController controller = loader.getController();
        controller.receiveToken(token);
        controller.receiveBook(book);
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
        if(dialogBox.openDialogDirectorySelector(this.book.getUrlFile())){
            WindowControllerB.getWindowController().showConfirmationAlert(event, "Archivo descargado correctamente");
        }else{
            WindowControllerB.getWindowController().showErrorAlert(event, "El libro no fue descargado");
        }
    }

    @FXML
    private void observeChapterBookInformation(MouseEvent event){
        if(this.tvChapterBook.getSelectionModel().getSelectedItem() != null){
            FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("ChapterBookRequest.fxml", btnCloseWindowEvidenceRequest);
            ChapterBookRequestController controller = loader.getController();
            Book book = new Book();
            book.setUrlFile(this.book.getUrlFile());
            book.setEvidenceTitle(this.book.getEvidenceTitle());
            book.setEditionsNumber(this.book.getEditionsNumber());
            controller.receiveBook(book);
            controller.receiveChapterBookURLFile(
                this.tvChapterBook.getSelectionModel().getSelectedItem().getUrlFile()
            );
            controller.receiveToken(token);
        }
    }
    
    private void setDataIntoBookInterface(){
        if(this.book.getImpactAB()){
            this.chBoxImpactAB.setSelected(true);
        }
        lvStudent.getItems().addAll(this.book.getStudents());
        this.book.getIntegrants().forEach(integrant -> this.lvIntegrants.getItems().add(integrant.getFullName()));
        this.book.getCollaborators().forEach(collaborator -> this.lvCollaborators.getItems().add(collaborator.getFullName()));
        this.txtFieldPublisher.setText(this.book.getPublisher());
        this.txtFieldNumberEdition.setText(this.book.getEditionsNumber() + "");
        this.txtFieldISBN.setText(this.book.getIsbn() + "");
        this.lbTypeEvidence.setText(this.book.getEvidenceType());
        this.txtFieldEvidenceTittle.setText(this.book.getEvidenceTitle());
        this.txtFieldPublisherDate.setText(this.book.getPublicationDate());
        this.txtFieldPublicationCountry.setText(this.book.getCountry());
        this.txtFieldInvestigationProject.setText(this.book.getProjectName());
        this.txtFieldStudyDegree.setText(this.book.getStudyDegree());
        this.lbDocumentName.setText(this.book.getUrlFile());
    }
    
    private void grantPermissions(){
        if(this.token.getRfc().equalsIgnoreCase(this.book.getRegistrationResponsible())){
            hbOptions.getChildren().addAll(btnUpdateEvidence, btnAddChapterBook);
        }
    }

    @Override
    public String toString(){
        return "Libro";
    }

    @Override
    public void createWindowAccordingEvidenceType(Evidence evidence, Node graphicElement, Integrant token){
        if(this.toString().equalsIgnoreCase(evidence.getEvidenceType())){
            FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("Book.fxml", graphicElement);
            BookController controller = loader.getController();
            controller.showBook(evidence.getUrlFile(), token);
        }
    }
    
    private void prepareChapterBookTable(){
        this.colTitleChapterBook.setCellValueFactory(new PropertyValueFactory<ChapterBook, String>("chapterBookTitle"));
        this.colRangePagesChapterBook.setCellValueFactory(new PropertyValueFactory<ChapterBook, String>("pageNumberRange"));
        this.tvChapterBook.setItems(makeitemsChapterBooksForTable());
    }
    
    private ObservableList<ChapterBook> makeitemsChapterBooksForTable(){
        ObservableList<ChapterBook> itemsChapterBooks = FXCollections.observableArrayList();
        List<ChapterBook> chapterBooksList = CHAPTERBOOK_DAO.getChapterBooksListByBook(book.getUrlFile());
        itemsChapterBooks.addAll(chapterBooksList);
        return itemsChapterBooks;
    }
    
}
