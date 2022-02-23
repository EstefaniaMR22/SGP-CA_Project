/*
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package controller.old.pattern;

import java.util.Arrays;
import java.util.List;

import controller.old.ArticleController;
import controller.old.ReceptionWorkController;
import model.domain.Evidence;
import model.domain.Integrant;
import javafx.scene.Node;
import controller.old.BookController;
import controller.old.PrototypeController;

public class EvidenceWindowFactory{
    
    private static List<EvidenceWindow> listEvidenceView = Arrays.asList(
        new BookController(),
        new ReceptionWorkController(),
        new ArticleController(),
        new PrototypeController()
    );
    
    public static void showSpecificEvidenceWindow(Evidence evidence, Node graphic, Integrant token){
        for(EvidenceWindow evidenceView : listEvidenceView){
            evidenceView.createWindowAccordingEvidenceType(evidence, graphic, token);
        }
    }
    
}
