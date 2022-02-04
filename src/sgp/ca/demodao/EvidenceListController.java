/*
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package sgp.ca.demodao;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import sgp.ca.businesslogic.ReceptionWorkDAO;
import sgp.ca.domain.Evidence;
import sgp.ca.domain.Integrant;

public class EvidenceListController implements Initializable{

    @FXML
    private Label lbUserName;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnSearch;
    @FXML
    private TextField txtFieldEvicendeSearch;
    @FXML
    private Button btnAddNewEvidence;
    @FXML
    private TableView<Evidence> tvEvidences;
    @FXML
    private TableColumn<Evidence, String> colEvidenceType;
    @FXML
    private TableColumn<Evidence, String> colEvidenceTittle;
    @FXML
    private TableColumn<Evidence, String> colImpactAB;
    @FXML
    private TableColumn<Evidence, String> colRegistrationResponsible;
    @FXML
    private TableColumn<Evidence, String> colRegistrationDate;
    @FXML
    private TableColumn<Evidence, String> colUrl;

    private final ReceptionWorkDAO RECEPTION_WORK_DAO = new ReceptionWorkDAO();
    private Integrant token;
    List<Evidence> listEvidences;
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        preprareSchoolingTable();
    }
    
    public void showGeneralResumeEvidences(Integrant token){
        this.token = token;
        lbUserName.setText(this.token.getFullName());
        listEvidences = RECEPTION_WORK_DAO.getAllEvidences(this.token.getBodyAcademyKey());
        tvEvidences.getItems().addAll(listEvidences);
    }

    @FXML
    private void searchEvidence(ActionEvent event){
        List<Evidence> listEvidenceFiltered = new ArrayList<>();
        this.tvEvidences.getItems().clear();
        for(Evidence evidence : this.listEvidences){
            if(evidence.getEvidenceTitle().contains(this.txtFieldEvicendeSearch.getText())){
                listEvidenceFiltered.add(evidence);
            }
        }
        if(listEvidenceFiltered.isEmpty()){
            tvEvidences.getItems().addAll(this.listEvidences);
        }else{
            tvEvidences.getItems().addAll(listEvidenceFiltered);
        }
    }

    @FXML
    private void addEvidence(ActionEvent event){
        FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("EvidenceSelection.fxml", btnClose);
        EvidenceSelectionController controller = loader.getController();
        controller.receiveToken(this.token);
    }

    @FXML
    private void showEvidence(MouseEvent event){
        if(tvEvidences.getSelectionModel().getSelectedItem() != null){
            Evidence evidence = tvEvidences.getSelectionModel().getSelectedItem();
            EvidenceWindowFactory.showSpecificEvidenceWindow(evidence, btnClose, token);
        }
    }
    
    private void preprareSchoolingTable(){
        colEvidenceTittle.setCellValueFactory(new PropertyValueFactory<>("evidenceTitle"));
        colEvidenceType.setCellValueFactory(new PropertyValueFactory<>("evidenceType"));
        colImpactAB.setCellValueFactory(new PropertyValueFactory<>("impactAB"));
        colRegistrationDate.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
        colRegistrationResponsible.setCellValueFactory(new PropertyValueFactory<>("registrationResponsible"));
        colUrl.setCellValueFactory(new PropertyValueFactory<>("urlFile"));
    }

    @FXML
    private void closeWindow(ActionEvent event){
        FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("Start.fxml", btnClose);
        StartController controller = loader.getController();
        controller.receiveIntegrantToken(this.token);
    }
    
}
