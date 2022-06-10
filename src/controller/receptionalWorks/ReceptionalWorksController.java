package controller.receptionalWorks;

import assets.utils.SQLStates;
import controller.control.Controller;
import controller.IntegrantController;
import controller.academicgroup.AddMemberController;
import controller.control.AlertController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.dao.ProjectDAO;
import model.dao.ReceptionalWorkDAO;
import model.domain.Project;
import model.domain.ReceptionalWork;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceptionalWorksController extends Controller implements Initializable {

    @FXML private TableView<ReceptionalWork> receptionalWorksTableView;
    @FXML private TableColumn<ReceptionalWork, String> nameReceptionalWorkColumn;
    @FXML private TableColumn<ReceptionalWork, String> nameProjectColumn;
    @FXML private TableColumn<ReceptionalWork, String> modalityReceptionalWorkColumn;
    @FXML private TableColumn<ReceptionalWork, String> directorColumn;
    @FXML private TableColumn<ReceptionalWork, String> codirectorColumn;
    @FXML private TextField searchTextField;
    @FXML private Button newReceptionalWorkButton;
    @FXML private Button updateReceptionalWorkButton;
    @FXML private Button consultReceptionalWorkButton;
    @FXML private Button exitButton;

    private ObservableList<ReceptionalWork> receptionalWorksObservableList;
    private String idAcademicGroup;

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/ReceptionalWorksView.fxml"), this);
        stage.show();
    }

    public ReceptionalWorksController(String idAcademicGroup) {
        this.idAcademicGroup = idAcademicGroup;

    }

    private void setTableComponents() {
        receptionalWorksObservableList = FXCollections.observableArrayList();
        nameReceptionalWorkColumn.setCellValueFactory(new PropertyValueFactory<>("nameReceptionalWork"));
        nameProjectColumn.setCellValueFactory(new PropertyValueFactory<>("nameProject"));
        modalityReceptionalWorkColumn.setCellValueFactory(new PropertyValueFactory<>("modality"));
        directorColumn.setCellValueFactory(new PropertyValueFactory<>("director"));
        codirectorColumn.setCellValueFactory(new PropertyValueFactory<>("codirector"));
        chargeReceptionalWorks();
        searchReceptionalWorks();
    }

    private void searchReceptionalWorks() {

        if (receptionalWorksObservableList.size() > 0) {
            FilteredList<ReceptionalWork> receptionalWorkSearch = new FilteredList<ReceptionalWork>(receptionalWorksObservableList, p -> true);
            searchTextField.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    receptionalWorkSearch.setPredicate(search -> {
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }

                        String lowerCaseFilter = newValue.toLowerCase();

                        if (search.getNameProject().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (search.getNameReceptionalWork().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else {
                            return false;
                        }
                    });
                }
            });

            SortedList<ReceptionalWork> sortedData = new SortedList<>(receptionalWorkSearch);
            sortedData.comparatorProperty().bind(receptionalWorksTableView.comparatorProperty());
            receptionalWorksTableView.setItems(sortedData);
        }
    }

    private void chargeReceptionalWorks() {
        ReceptionalWorkDAO receptionalWorkDAO = new ReceptionalWorkDAO();
        try {
            receptionalWorksObservableList = receptionalWorkDAO.getReceptionalWorksList(idAcademicGroup);
            receptionalWorksTableView.setItems(receptionalWorksObservableList);
        } catch (SQLException chargeProjectsExeception) {
            deterMinateSQLState(chargeProjectsExeception);
        }

    }

    private void deterMinateSQLState(SQLException sqlException) {
        Logger.getLogger(AddMemberController.class.getName()).log(Level.SEVERE, null, sqlException);
        if (sqlException.getSQLState().equals(SQLStates.SQL_NO_CONNECTION.getSqlState())) {
            AlertController.getInstance().showConnectionErrorAlert();
        }
        AlertController.getInstance().showActionFailedAlert(sqlException.getLocalizedMessage());
    }

    @FXML
    void addReceptionalWorksOnAction(ActionEvent actionEvent) throws SQLException {
        if (!verifyProjects().isEmpty()) {
            try {
                AddReceptionalWorkController addReceptionalWorkController = new AddReceptionalWorkController(idAcademicGroup);
                addReceptionalWorkController.showStage();

            } catch (Exception addReceptionalWorkException) {
                AlertController alertView = AlertController.getInstance();
                alertView.showActionFailedAlert(" No se pudo abrir la ventana " +
                        "AddReceptionalWork. Causa: " + addReceptionalWorkException);

            }
        } else {
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" Sin 'proyectos' registrados, no puede agregar un trabajo recepcional");
        }
        chargeReceptionalWorks();
    }

    private List<Project> verifyProjects() {
        ProjectDAO projectDAO = new ProjectDAO();
        List<Project> projectList = null;
        try {
            projectList = projectDAO.getProjectList(idAcademicGroup);
        } catch (SQLException getAllLgacsException) {
            Logger.getLogger(AddReceptionalWorkController.class.getName()).log(Level.SEVERE, null, getAllLgacsException);
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" No se pudo cargar los proyectos de investigación. Causa: " + getAllLgacsException);

        }
        return projectList;
    }

    @FXML
    void updateReceptionalWorksOnAction(ActionEvent actionEvent) {

        ReceptionalWork selectedReceptionalWork = receptionalWorksTableView.getSelectionModel().getSelectedItem();
        if (selectedReceptionalWork != null) {
            System.out.println(selectedReceptionalWork.getIdReceptionalWork() + " | " + idAcademicGroup);
            try {
                UpdateReceptionalWorkController modifyReceptionalWorkController = new UpdateReceptionalWorkController(selectedReceptionalWork, idAcademicGroup);
                modifyReceptionalWorkController.showStage();

            } catch (Exception updateReceptionalWorkException) {
                Logger.getLogger(ReceptionalWorksController.class.getName()).log(Level.SEVERE, null, updateReceptionalWorkException);

                AlertController alertView = AlertController.getInstance();
                alertView.showActionFailedAlert(" No se pudo abrir la ventana " +
                        "ModifyReceptionalWork. Causa: " + updateReceptionalWorkException.toString());
            }

        } else {
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" Antes de presionar modificar debes seleccionar un " +
                    "trabajo recepcional de la tabla");
        }

        chargeReceptionalWorks();

    }

    @FXML
    void consultReceptionalWorksOnAction(ActionEvent actionEvent) {

        ReceptionalWork selectedReceptionalWork = receptionalWorksTableView.getSelectionModel().getSelectedItem();
        if (selectedReceptionalWork != null) {
            try {
                ConsultReceptionalWorkController consultReceptionalWorkController = new ConsultReceptionalWorkController(selectedReceptionalWork);
                consultReceptionalWorkController.showStage();

            } catch (Exception consultReceptionalWorkException) {
                Logger.getLogger(ReceptionalWorksController.class.getName()).log(Level.SEVERE, null, consultReceptionalWorkException);

                AlertController alertView = AlertController.getInstance();
                alertView.showActionFailedAlert(" No se pudo abrir la ventana " +
                        "ConsultReceptionalWork. Causa: " + consultReceptionalWorkException);

            }
        } else {
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" Antes de presionar consultar debes seleccionar un " +
                    "trabajo recepcional de la tabla");
        }

        chargeReceptionalWorks();

    }

    @FXML
    void returnViewOnAction(ActionEvent actionEvent) {
        try {
            stage.close();

        } catch (Exception returnViewOnActionExeception) {
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" Error en el metodo returnViewOnActionExeception:  " + returnViewOnActionExeception);

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setTableComponents();
    }
}
