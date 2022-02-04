/*
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package sgp.ca.demodao;

import java.util.Arrays;
import java.util.List;
import javafx.scene.Node;
import sgp.ca.domain.Evidence;
import sgp.ca.domain.Integrant;

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
