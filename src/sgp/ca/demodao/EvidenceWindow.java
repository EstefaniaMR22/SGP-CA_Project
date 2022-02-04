/*
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package sgp.ca.demodao;

import javafx.scene.Node;
import sgp.ca.domain.Evidence;
import sgp.ca.domain.Integrant;

public interface EvidenceWindow{
    
    public void createWindowAccordingEvidenceType(Evidence evidence, Node graphicElement, Integrant token);
    @Override
    public String toString();
}
