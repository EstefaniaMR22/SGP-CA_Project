package controller;

import controller.general.Controller;

public class AdministratorController extends Controller {

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/AdministratorView.fxml"), this);
        stage.showAndWait();
    }

}
