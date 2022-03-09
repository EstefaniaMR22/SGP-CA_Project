package controller;

public class IntegrantController extends Controller {

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/IntegrantView.fxml"), this);
        stage.showAndWait();
    }

}
