/*
 * @author Todos
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package sgp.ca.demodao;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SGPCA_Project extends Application {

    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);        
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
    
}