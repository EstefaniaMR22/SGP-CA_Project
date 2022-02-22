/*
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package model.dataaccess;

import java.util.Arrays;
import java.util.List;

public class EvidenceDAOFactory{
    
    private static final List<EvidenceDAO> evidencesDao = Arrays.asList(
        new ArticleDAO(),
        new BookDAO(),
        new PrototypeDAO(),
        new ReceptionWorkDAO()
    );
    
    public static EvidenceDAO getSpecificEvidenceDaoInstance(String evidenceType){
        EvidenceDAO evidenceDao = null;
        for(EvidenceDAO genericObjectDao : evidencesDao){
            if(genericObjectDao.toString().equalsIgnoreCase(evidenceType)){
                evidenceDao = genericObjectDao.getEvidenceDaoInstance(evidenceType);
                break;
            }
        }
        return evidenceDao;
    }
    
}
