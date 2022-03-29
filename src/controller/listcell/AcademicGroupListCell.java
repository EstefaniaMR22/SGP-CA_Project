package controller.listcell;

import controller.ResponsableController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import model.domain.AcademicGroup;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AcademicGroupListCell extends ListCell<AcademicGroup> {
    @FXML private Label academicNameLabel;
    @FXML private AnchorPane mainPane;
    @FXML private Label academicGroupID;
    private FXMLLoader fxmlLoader;

    @Override
    protected void updateItem(AcademicGroup item, boolean empty) {
        super.updateItem(item, empty);
        if(empty || item == null ) {
            setText(null);
            setGraphic(null);

        } else {
            if(fxmlLoader == null ) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/view/AcademicGroupListCell.fxml"));
                fxmlLoader.setController(this);
                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    Logger.getLogger(AcademicGroupListCell.class.getName()).log(Level.SEVERE, null, e);
                }
            }
            academicNameLabel.setText(item.getName());
            academicGroupID.setText(item.getId());
            setText(null);
            setGraphic(mainPane);
        }
    }
}
