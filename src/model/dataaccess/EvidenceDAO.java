/*
 * @author Josué 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package model.dataaccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.domain.EvidenceFactory;
import model.domain.Evidence;
import utils.ConnectionDatabase;

public abstract class EvidenceDAO{
    
    private final ConnectionDatabase CONNECTION = new ConnectionDatabase();
    
    public List<Evidence> getAllEvidences(String bodyAcademyKey){
        List<Evidence> evidences = new ArrayList<>();
        try{
            PreparedStatement sentenceQuery = CONNECTION.getConnectionDatabase().prepareStatement(
                "SELECT * FROM Evidences WHERE bodyAcademyKey = ? GROUP BY urlFile ORDER BY registrationDate DESC;"
            );
            sentenceQuery.setString(1, bodyAcademyKey);
            ResultSet resultQuery = sentenceQuery.executeQuery();
            while(resultQuery.next()){
                Evidence evidence = EvidenceFactory.getEvidence(resultQuery.getString("evidenceType"));
                evidence.setUrlFile(resultQuery.getString("urlFile"));
                evidence.setEvidenceType(resultQuery.getString("evidenceType"));
                evidence.setEvidenceTitle(resultQuery.getString("evidenceTitle"));
                evidence.setImpactAB(resultQuery.getBoolean("impactBA"));
                evidence.setRegistrationResponsible(resultQuery.getString("registrationResponsible"));
                evidence.setRegistrationDate(resultQuery.getString("registrationDate"));
                evidence.setProjectName(resultQuery.getString("projectName"));
                evidences.add(evidence);
            }
        }catch(SQLException ex){
            evidences = null;
            Logger.getLogger(EvidenceDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            CONNECTION.closeConnection();
            return evidences;
        }
    }
    
    public List<Evidence> getAllEvidencesByIntegrantMailUv(String mailUv){
        List<Evidence> evidences = new ArrayList<>();
        try{
            PreparedStatement sentenceQuery = CONNECTION.getConnectionDatabase().prepareStatement(
                "SELECT * FROM Evidences WHERE emailUv = ? GROUP BY urlFile ORDER BY registrationDate DESC;"
            );
            sentenceQuery.setString(1, mailUv);
            ResultSet resultQuery = sentenceQuery.executeQuery();
            while(resultQuery.next()){
                Evidence evidence = EvidenceFactory.getEvidence(resultQuery.getString("evidenceType"));
                evidence.setUrlFile(resultQuery.getString("urlFile"));
                evidence.setEvidenceType(resultQuery.getString("evidenceType"));
                evidence.setEvidenceTitle(resultQuery.getString("evidenceTitle"));
                evidence.setImpactAB(resultQuery.getBoolean("impactBA"));
                evidence.setRegistrationResponsible(resultQuery.getString("registrationResponsible"));
                evidence.setRegistrationDate(resultQuery.getString("registrationDate"));
                evidence.setProjectName(resultQuery.getString("projectName"));
                evidences.add(evidence);
            }
        }catch(SQLException ex){
            Logger.getLogger(EvidenceDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            CONNECTION.closeConnection();
            return evidences;
        }
    }
    
    public List<Evidence> getEvidencesByProjectName(String projectName){
        List<Evidence> evidences = new ArrayList<>();
        try{
            PreparedStatement sentenceQuery = CONNECTION.getConnectionDatabase().prepareStatement(
                "SELECT * FROM Evidences WHERE projectName = ? GROUP BY urlFile ORDER BY registrationDate DESC;"
            );
            sentenceQuery.setString(1, projectName);
            ResultSet resultQuery = sentenceQuery.executeQuery();
            while(resultQuery.next()){
                Evidence evidence = EvidenceFactory.getEvidence(resultQuery.getString("evidenceType"));
                evidence.setUrlFile(resultQuery.getString("urlFile"));
                evidence.setEvidenceType(resultQuery.getString("evidenceType"));
                evidence.setEvidenceTitle(resultQuery.getString("evidenceTitle"));
                evidence.setImpactAB(resultQuery.getBoolean("impactBA"));
                evidence.setRegistrationResponsible(resultQuery.getString("registrationResponsible"));
                evidence.setRegistrationDate(resultQuery.getString("registrationDate"));
                evidence.setProjectName(resultQuery.getString("projectName"));
                evidences.add(evidence);
            }
        }catch(SQLException ex){
            Logger.getLogger(ArticleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            CONNECTION.closeConnection();
            return evidences;
        }
    }
    
    public List<String> getStudentsNameByEvidenceUrl(String url){
        List<String> students = new ArrayList<>();
        try{
            PreparedStatement sentenceQuery = CONNECTION.getConnectionDatabase().prepareStatement(
                "SELECT student FROM `Students` WHERE urlFile = ?;"
            );
            sentenceQuery.setString(1, url);
            ResultSet resultQuery = sentenceQuery.executeQuery();
            while(resultQuery.next()){
                students.add(resultQuery.getString("student"));
            }
        }catch(SQLException ex){
            students = null;
            Logger.getLogger(ArticleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            CONNECTION.closeConnection();
            return students;
        }
    }
    
    @Override
    public abstract String toString();
    public abstract Evidence getEvidenceByUrl(String urlEvidenceFile);
    public abstract boolean addNewEvidence(Evidence evidence);
    public abstract boolean updateEvidence(Evidence evidence, String oldUrlFile);
    public abstract boolean deleteEvidenceByUrl(String urlEvidenceFile);
    public abstract EvidenceDAO getEvidenceDaoInstance(String evidenceType);
    
}
