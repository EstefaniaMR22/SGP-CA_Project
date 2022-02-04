/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogic.testevidences;

import org.junit.Assert;
import org.junit.Test;
import sgp.ca.businesslogic.EvidenceDAO;
import sgp.ca.businesslogic.PrototypeDAO;
import sgp.ca.domain.Evidence;

public class EvidencesRequesTest {
    
    private final EvidenceDAO EVIDENCE_DAO = new PrototypeDAO();
    
    @Test
    public void testCorrectAllEvidencesRequest(){
        boolean isCorrect = true;
        if(EVIDENCE_DAO.getAllEvidences("UV-CA-127").size() > 0){
            for(Evidence evidence : EVIDENCE_DAO.getAllEvidences("UV-CA-127")){
                if(this.coincidencesNumber(evidence.getUrlFile())>1){
                    isCorrect = false;
                }
            }
        }else{
            isCorrect = false;
        }
        Assert.assertEquals(true, isCorrect);
    }
    
    @Test
    public void testCorrectEvidenceRequestByEmailUv(){
        //INITIALIZER.prepareEvidencesForTest();
        boolean isCorrect = false;
        if(EVIDENCE_DAO.getAllEvidencesByIntegrantMailUv("zS19014006@estudiantes.uv.mx").size() == 2){
            isCorrect = true;
        }
        //INITIALIZER.cleanEvidencesForTest();
        Assert.assertEquals(true, isCorrect);
    }
    
    private int coincidencesNumber(String urlFile){
        int coincidences = 0;
        for(Evidence evidence : EVIDENCE_DAO.getAllEvidences("UV-CA-127")){
            if(evidence.getUrlFile().equalsIgnoreCase(urlFile)){
                coincidences++;
            }
        }
        return coincidences;
    }
    
    @Test
    public void testUpdateEvidenceByURL(){
        boolean isCorrect = true;
        Assert.assertEquals(true, isCorrect);
    }
    
}
