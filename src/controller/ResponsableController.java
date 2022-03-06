package controller;

import controller.general.Controller;

public class ResponsableController extends Controller {

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/ResponsableView.fxml"), this);
        stage.showAndWait();
    }

}
