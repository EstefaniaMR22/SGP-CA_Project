/*
 * @author Josué 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package model.old.dataaccess;

import java.util.List;
import model.old.domain.GeneralResume;

public interface IGeneralResumeDAO{
    
    public boolean isBodyAcademyRegistered(String bodyAcademyKey);
    public List<String> getGeneralResumeKeys();
    public GeneralResume getGeneralResumeByKey(String bodyAcademyKey);
    public boolean addGeneralResume(GeneralResume newGeneralResume);
    public boolean updateGeneralResume(GeneralResume generalResume, String oldBodyAcademyKey);
    
}
