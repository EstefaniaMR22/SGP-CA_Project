/*
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */
package sgp.ca.domain;

import java.util.Arrays;
import java.util.List;

public class EvidenceFactory{
    
    private final static List<Evidence> listEvidences = Arrays.asList(
        new Article(), 
        new Book(), 
        new Prototype(), 
        new ReceptionWork()
    );
    
    public static Evidence getEvidence(String evidenceType){
        Evidence evidence = null;
        for(Evidence evidenceIterator : listEvidences){
            if(evidenceType.equalsIgnoreCase(evidenceIterator.toString())){
                evidence = evidenceIterator.getSpecificEvidenceInstance(evidenceType);
                break;
            }
        }
        return evidence;
    }
    
    public static Evidence getEvidence(String evidenceType, String evidenceTitle, 
    boolean impactAB, String registrationResponsible, String registrationDate, String urlFile){
        Evidence evidence = null;
        for(Evidence evidenceIterator : listEvidences){
            if(evidenceType.equalsIgnoreCase(evidenceIterator.toString())){
                evidence = evidenceIterator.getSpecificEvidenceInstance(
                    evidenceType, 
                    evidenceTitle, 
                    impactAB, 
                    registrationResponsible, 
                    registrationDate, 
                    urlFile
                );
                break;
            }
        }
        return evidence;
    }
    
}
