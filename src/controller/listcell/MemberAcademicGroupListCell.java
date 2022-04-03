package controller.listcell;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import model.domain.Member;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MemberAcademicGroupListCell extends ListCell<Member> {
    private FXMLLoader fxmlLoader;
    @FXML private Label fullnameLabel;
    @FXML private AnchorPane mainPane;
    @FXML private Label personalNumberLabel;

    @Override
    protected void updateItem(Member item, boolean empty) {
        super.updateItem(item, empty);
        super.updateItem(item, empty);
        if(empty || item == null ) {
            setText(null);
            setGraphic(null);

        } else {
            if(fxmlLoader == null ) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/view/MemberAcademicGroupListCellView.fxml"));
                fxmlLoader.setController(this);
                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    Logger.getLogger(AdministratorMemberListCell.class.getName()).log(Level.SEVERE, null, e);
                }
            }
            if(getListView().getItems().size() == 1 ) {
                this.getStyleClass().add("single-cell");
            } else {
                this.getStyleClass().remove("single-cell");
                this.getStyleClass().remove("first-cell");
                this.getStyleClass().remove("last-cell");
                this.getStyleClass().remove("middle-cell");
                if (getIndex() == 0) {
                    this.getStyleClass().add("first-cell");
                } else if (getIndex() == (getListView().getItems().size() - 1)) {
                    this.getStyleClass().add("last-cell");
                } else {
                    this.getStyleClass().add("middle-cell");
                }
            }
            fullnameLabel.setText(item.getFullName());
            personalNumberLabel.setText(item.getPersonalNumber());
            setText(null);
            setGraphic(mainPane);
        }
    }
}
