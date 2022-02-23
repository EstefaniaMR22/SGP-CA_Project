/*
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package controller.old.pattern;

import model.domain.Evidence;
import model.domain.Integrant;
import javafx.scene.Node;

public interface EvidenceWindow{
    
    public void createWindowAccordingEvidenceType(Evidence evidence, Node graphicElement, Integrant token);
    @Override
    public String toString();
}
