/**
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package sgp.ca.businesslogic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sgp.ca.dataaccess.ConnectionDatabase;
import sgp.ca.domain.Collaborator;
import sgp.ca.domain.Evidence;
import sgp.ca.domain.Integrant;
import sgp.ca.domain.Prototype;

public class PrototypeDAO extends EvidenceDAO{
    
    private final ConnectionDatabase CONNECTION = new ConnectionDatabase();
    
    @Override
    public Evidence getEvidenceByUrl(String urlEvidenceFile){
        Evidence prototype = new Prototype();
        Connection connection = CONNECTION.getConnectionDatabaseNotAutoCommit();
        try{
            PreparedStatement senenceQuery = connection.prepareStatement(
                "SELECT * FROM Prototype WHERE urlFile = ?;"
            );
            senenceQuery.setString(1, urlEvidenceFile);
            ResultSet resultQuery = senenceQuery.executeQuery();
            if(resultQuery.next()){
                prototype = this.getOutPrototypeDataFromQuery(resultQuery);
                prototype.setIntegrants(this.getIntegrantsPrototypeParticipation(connection, urlEvidenceFile));
                prototype.setCollaborators(this.getCollaboratorsPrototypeParticipation(connection, urlEvidenceFile));
                prototype.setStudents(this.getStudentNamesPrototypeParticipation(connection, urlEvidenceFile));
            }
            connection.commit();
            connection.setAutoCommit(true);
        }catch(SQLException ex){
            Logger.getLogger(PrototypeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            CONNECTION.closeConnection();
            return prototype;
        }
    }
    
    @Override
    public boolean addNewEvidence(Evidence evidence){
        Connection connection = CONNECTION.getConnectionDatabaseNotAutoCommit();
        boolean correctInsert = false;
        try{
            PreparedStatement sentence = connection.prepareStatement(
                "INSERT INTO Prototype VALUES (?,?,?,?,?,?,?,?,?,?,?);"
            );
            sentence.setString(1, evidence.getUrlFile());
            sentence.setString(2, evidence.getProjectName());
            sentence.setBoolean(3, evidence.getImpactAB());
            sentence.setString(4, "Prototipo");
            sentence.setString(5, evidence.getEvidenceTitle());
            sentence.setString(6, evidence.getRegistrationResponsible());
            sentence.setString(7, evidence.getRegistrationDate());
            sentence.setString(8, evidence.getStudyDegree());
            sentence.setString(9, evidence.getPublicationDate());
            sentence.setString(10, evidence.getCountry());
            sentence.setString(11, ((Prototype)evidence).getFeatures());
            sentence.executeUpdate();
            this.insertIntoStudentPrototype(connection, (Prototype)evidence);
            this.insertIntoIntegrantPrototype(connection, (Prototype)evidence);
            this.insertIntoCollaboratorPrototype(connection, (Prototype)evidence);
            connection.commit();
            connection.setAutoCommit(true);
            correctInsert = true;
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                Logger.getLogger(PrototypeDAO.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(PrototypeDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }finally{
            CONNECTION.closeConnection();
            return correctInsert;
        }
    }

    @Override
    public boolean updateEvidence(Evidence evidence, String oldUrlFile){
        Connection connection = CONNECTION.getConnectionDatabaseNotAutoCommit();
        boolean correctUpdate = false;
        try{
            this.deleteStudensFromURLFilePrototype(connection, oldUrlFile);
            this.deleteIntegrantsFromURLFilePrototype(connection, oldUrlFile);
            this.deleteCollaboratorsFromURLFilePrototype(connection, oldUrlFile);
            PreparedStatement sentence = connection.prepareStatement(
                "UPDATE Prototype SET urlFile = ?, projectName = ?, impactBA = ?, evidenceType = ?,"
                + " evidenceTitle = ?, registrationResponsible = ?, registrationDate = ?, "
                + "studyDegree = ?, publicationDate = ?, country = ?, feautures = ?"
                + " WHERE urlFile = ?;"
            );
            sentence.setString(1, evidence.getUrlFile());
            sentence.setString(2, evidence.getProjectName());
            sentence.setBoolean(3, evidence.getImpactAB());
            sentence.setString(4, "Prototipo");
            sentence.setString(5, evidence.getEvidenceTitle());
            sentence.setString(6, evidence.getRegistrationResponsible());
            sentence.setString(7, evidence.getRegistrationDate());
            sentence.setString(8, evidence.getStudyDegree());
            sentence.setString(9, evidence.getPublicationDate());
            sentence.setString(10, evidence.getCountry());
            sentence.setString(11, ((Prototype)evidence).getFeatures());
            sentence.setString(12, oldUrlFile);
            sentence.executeUpdate();
            this.insertIntoStudentPrototype(connection, (Prototype)evidence);
            this.insertIntoIntegrantPrototype(connection, (Prototype)evidence);
            this.insertIntoCollaboratorPrototype(connection, (Prototype)evidence);
            connection.commit();
            connection.setAutoCommit(true);
            correctUpdate = true;
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                Logger.getLogger(PrototypeDAO.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(PrototypeDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }finally{
            CONNECTION.closeConnection();
            return correctUpdate;
        }
    }

    @Override
    public boolean deleteEvidenceByUrl(String urlEvidenceFile){
        Connection connection = CONNECTION.getConnectionDatabaseNotAutoCommit();
        boolean correctDelete = false;
        try{
            this.deleteStudensFromURLFilePrototype(connection, urlEvidenceFile);
            this.deleteIntegrantsFromURLFilePrototype(connection, urlEvidenceFile);
            this.deleteCollaboratorsFromURLFilePrototype(connection, urlEvidenceFile);
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "DELETE FROM Prototype WHERE urlFile = ?;"
            );
            sentenceQuery.setString(1, urlEvidenceFile);
            sentenceQuery.executeUpdate();
            connection.commit();
            connection.setAutoCommit(true);
            correctDelete = true;
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                connection.close();
                Logger.getLogger(PrototypeDAO.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(PrototypeDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }finally{
            CONNECTION.closeConnection();
            return correctDelete;
        }
    }
    
    private void deleteStudensFromURLFilePrototype(Connection connection, String urlFilePrototype){
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "DELETE FROM PrototypeStudent WHERE urlFile = ?;"
            );
            sentenceQuery.setString(1, urlFilePrototype);
            sentenceQuery.executeUpdate();
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                connection.close();
                Logger.getLogger(PrototypeDAO.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(PrototypeDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void deleteIntegrantsFromURLFilePrototype(Connection connection, String urlFilePrototype){
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "DELETE FROM IntegrantPrototype WHERE urlFile = ?;"
            );
            sentenceQuery.setString(1, urlFilePrototype);
            sentenceQuery.executeUpdate();
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                connection.close();
                Logger.getLogger(PrototypeDAO.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(PrototypeDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void deleteCollaboratorsFromURLFilePrototype(Connection connection, String urlFilePrototype){
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "DELETE FROM CollaboratePrototype WHERE urlFile = ?;"
            );
            sentenceQuery.setString(1, urlFilePrototype);
            sentenceQuery.executeUpdate();
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                connection.close();
                Logger.getLogger(PrototypeDAO.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(PrototypeDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void insertIntoStudentPrototype(Connection connection, Prototype prototype){
        prototype.getStudents().forEach( student -> {
            try{
                PreparedStatement sentenceQuery = connection.prepareStatement(
                    "INSERT INTO PrototypeStudent VALUES(?,?);"
                );
                sentenceQuery.setString(1, prototype.getUrlFile());
                sentenceQuery.setString(2, student);
                sentenceQuery.executeUpdate();
            }catch(SQLException sqlException){
                try{
                    connection.rollback();
                    connection.close();
                    Logger.getLogger(PrototypeDAO.class.getName()).log(Level.SEVERE, null, sqlException);
                }catch(SQLException ex){
                    Logger.getLogger(PrototypeDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    private void insertIntoIntegrantPrototype(Connection connection, Prototype prototype){
        prototype.getIntegrants().forEach( integrant -> {
            try{
                PreparedStatement sentenceQuery = connection.prepareStatement(
                    "INSERT INTO IntegrantPrototype VALUES(?,?);"
                );
                sentenceQuery.setString(1, integrant.getRfc());
                sentenceQuery.setString(2, prototype.getUrlFile());
                sentenceQuery.executeUpdate();
            }catch(SQLException sqlException){
                try{
                    connection.rollback();
                    connection.close();
                    Logger.getLogger(PrototypeDAO.class.getName()).log(Level.SEVERE, null, sqlException);
                }catch(SQLException ex){
                    Logger.getLogger(PrototypeDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    private void insertIntoCollaboratorPrototype(Connection connection, Prototype prototype){
        prototype.getCollaborators().forEach( collaborator -> {
            try{
                PreparedStatement sentenceQuery = connection.prepareStatement(
                    "INSERT INTO CollaboratePrototype VALUES(?,?);"
                );
                sentenceQuery.setString(1, collaborator.getRfc());
                sentenceQuery.setString(2, prototype.getUrlFile());
                sentenceQuery.executeUpdate();
            }catch(SQLException sqlException){
                try{
                    connection.rollback();
                    connection.close();
                    Logger.getLogger(PrototypeDAO.class.getName()).log(Level.SEVERE, null, sqlException);
                }catch(SQLException ex){
                    Logger.getLogger(PrototypeDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    private Prototype getOutPrototypeDataFromQuery(ResultSet resultPrototypeQuery){
        Prototype prototype = new Prototype();
        try{
            prototype = new Prototype(
                resultPrototypeQuery.getString("urlFile"),
                resultPrototypeQuery.getString("projectName"),
                resultPrototypeQuery.getString("evidenceTitle"),
                resultPrototypeQuery.getString("country"),
                resultPrototypeQuery.getString("publicationDate"),
                resultPrototypeQuery.getBoolean("impactBA"),
                resultPrototypeQuery.getString("registrationDate"),
                resultPrototypeQuery.getString("registrationResponsible"),
                resultPrototypeQuery.getString("studyDegree"),
                resultPrototypeQuery.getString("evidenceType"),
                resultPrototypeQuery.getString("feautures")
            );
        }catch(SQLException ex){
            Logger.getLogger(PrototypeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            return prototype;
        }
    }
    
    private List<Integrant> getIntegrantsPrototypeParticipation(Connection connection, String urlFilePrototype){
        List<Integrant> integrants = new ArrayList<>();
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "SELECT i.fullName, i.rfc FROM Integrant i, IntegrantPrototype ia WHERE ia.rfc = i.rfc AND urlFile = ?;"
            );
            sentenceQuery.setString(1, urlFilePrototype);
            ResultSet resultQuery = sentenceQuery.executeQuery();
            while(resultQuery.next()){
                Integrant integrant = new Integrant();
                integrant.setFullName(resultQuery.getString("fullName"));
                integrant.setRfc(resultQuery.getString("rfc"));
                integrants.add(integrant);
            }
        }catch(SQLException ex){
            Logger.getLogger(PrototypeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            return integrants;
        }
    }
    
    private List<Collaborator> getCollaboratorsPrototypeParticipation(Connection connection, String urlFilePrototype){
        List<Collaborator> collaborators = new ArrayList<>();
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "SELECT c.fullName, c.rfc FROM CollaboratePrototype ca, Collaborator c WHERE ca.rfc = c.rfc AND urlFile = ?;"
            );
            sentenceQuery.setString(1, urlFilePrototype);
            ResultSet resultQuery = sentenceQuery.executeQuery();
            while(resultQuery.next()){
                Collaborator collaborator = new Collaborator();
                collaborator.setFullName(resultQuery.getString("fullName"));
                collaborator.setRfc(resultQuery.getString("rfc"));
                collaborators.add(collaborator);
            }
        }catch(SQLException ex){
            Logger.getLogger(PrototypeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            return collaborators;
        }
    }
    
    private List<String> getStudentNamesPrototypeParticipation(Connection connection, String urlFilePrototype){
        List<String> students = new ArrayList<>();
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "SELECT * FROM PrototypeStudent WHERE urlFile = ?;"
            );
            sentenceQuery.setString(1, urlFilePrototype);
            ResultSet resultQuery = sentenceQuery.executeQuery();
            while(resultQuery.next()){
                students.add(resultQuery.getString("student"));
            }
        }catch(SQLException ex){
            Logger.getLogger(PrototypeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            return students;
        }
    }
    
    @Override
    public String toString(){
        return "Libro";
    }

    @Override
    public EvidenceDAO getEvidenceDaoInstance(String evidenceType){
        EvidenceDAO evidenceDao = null;
        if(this.toString().equalsIgnoreCase(evidenceType)){
            evidenceDao = new BookDAO();
        }
        return evidenceDao;
    }
    
}
