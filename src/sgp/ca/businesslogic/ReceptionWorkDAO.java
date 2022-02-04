/*
* @author Johann
* @versión v1.0
* Last modification date: 17-06-2021
*/

package sgp.ca.businesslogic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sgp.ca.dataaccess.ConnectionDatabase;
import sgp.ca.domain.Collaborator;
import sgp.ca.domain.Evidence;
import sgp.ca.domain.Integrant;
import sgp.ca.domain.ReceptionWork;

public class ReceptionWorkDAO extends EvidenceDAO{
    
    private final ConnectionDatabase QUERY = new ConnectionDatabase();
    private final IntegrantDAO INTEGRANT_DAO = new IntegrantDAO();
    
    public void addReceptionWork(ReceptionWork newReceptionWork){
        Connection connection = QUERY.getConnectionDatabaseNotAutoCommit();
        try{
            PreparedStatement sentenceQuery = QUERY.getConnectionDatabase().prepareStatement(
                "insert into ReceptionWork values(? , ? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"
            );
            sentenceQuery.setString(1, newReceptionWork.getUrlFile());
            sentenceQuery.setString(2, newReceptionWork.getProjectName());
            sentenceQuery.setBoolean(3, newReceptionWork.getImpactAB());
            sentenceQuery.setString(4, newReceptionWork.getEvidenceType());
            sentenceQuery.setString(5, newReceptionWork.getEvidenceTitle());
            sentenceQuery.setString(6, newReceptionWork.getRegistrationResponsible());
            sentenceQuery.setString(7, newReceptionWork.getRegistrationDate());
            sentenceQuery.setString(8, newReceptionWork.getStudyDegree());
            sentenceQuery.setString(9, newReceptionWork.getPublicationDate());
            sentenceQuery.setString(10, newReceptionWork.getCountry());
            sentenceQuery.setString(11, newReceptionWork.getDescription());
            sentenceQuery.setString(12, newReceptionWork.getStatus());
            sentenceQuery.setInt(13, newReceptionWork.getActualDurationInMonths());
            sentenceQuery.setInt(14, newReceptionWork.getEstimatedDurationInMonths());
            sentenceQuery.setString(15, newReceptionWork.getModality());
            sentenceQuery.executeUpdate();
            this.insertIntoIntegrantReceptionWork(connection, newReceptionWork);
            this.insertIntoCollaboratorReceptionWork(connection, newReceptionWork);
            this.insertIntoStudentReceptionWork(connection, newReceptionWork);
            this.insertIntoRequirementReceptionWork(connection, newReceptionWork);
            connection.commit();
            connection.setAutoCommit(true);
        }catch(SQLException sqlEsception){
            try{
                connection.rollback();
                Logger.getLogger(ReceptionWork.class.getName()).log(Level.SEVERE, null, sqlEsception);
            }catch(SQLException ex){
                Logger.getLogger(ReceptionWorkDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }finally{
            QUERY.closeConnection();
        }
    }
     
    public List<ReceptionWork> getReceptionWorkList(){
        List<ReceptionWork> listReceptionWork = new ArrayList<>();
        try{
            Statement instructionQuery = QUERY.getConnectionDatabase().createStatement();;
            ResultSet queryResult = instructionQuery.executeQuery("Select urlFile, projectName, impactBA, evidenceType, "
                    + "evidenceTitle, registrationResponsible, registrationDate, studyDegree, publicationDate, country,  "
                    + "description, status, actualDurationInMonths, estimatedDurationInMonths, modality FROM ReceptionWork");
            while(queryResult.next()){
                listReceptionWork.add(new ReceptionWork(
                    queryResult.getString("urlFile"), 
                    queryResult.getString("projectName"),
                    queryResult.getBoolean("impactBA"),
                    queryResult.getString("evidenceType"),
                    queryResult.getString("evidenceTitle"),
                    queryResult.getString("registrationResponsible"),
                    queryResult.getString("registrationDate"),
                    queryResult.getString("studyDegree"),
                    queryResult.getString("publicationDate"),
                    queryResult.getString("country"),
                    queryResult.getString("description"),
                    queryResult.getString("status"),
                    queryResult.getInt("actualDurationInMonths"),
                    queryResult.getInt("estimatedDurationInMonths"),
                    queryResult.getString("modality")));
            }
        }catch(SQLException ex){
            Logger.getLogger(ReceptionWorkDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            QUERY.closeConnection();
            return listReceptionWork;
        }
    }

    @Override
    public ReceptionWork getEvidenceByUrl(String urlEvidenceFile){
        ReceptionWork ReceptionWork = new ReceptionWork();
        Connection connection = QUERY.getConnectionDatabaseNotAutoCommit();
        try{
            PreparedStatement senenceQuery = connection.prepareStatement(
                "SELECT * FROM ReceptionWork WHERE urlFile = ?;"
            );
            senenceQuery.setString(1, urlEvidenceFile);
            ResultSet resultQuery = senenceQuery.executeQuery();
            if(resultQuery.next()){
                ReceptionWork = this.getReceptionWorkFromQuery(resultQuery);
                ReceptionWork.setIntegrants(this.getIntegrantsReceptionWorkParticipation(connection, urlEvidenceFile));
                ReceptionWork.setCollaborators(this.getCollaboratorsReceptionWorkParticipation(connection, urlEvidenceFile));
                ReceptionWork.setStudents(this.getStudentNamesReceptionWorkParticipation(connection, urlEvidenceFile));
                ReceptionWork.setRequirements(this.getRequirementsReceptionWork(connection, urlEvidenceFile));
            }
            connection.commit();
            connection.setAutoCommit(true);
        }catch(SQLException ex){
            Logger.getLogger(ReceptionWorkDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            QUERY.closeConnection();
            return ReceptionWork;
        }
    }
    
    public ReceptionWork getReceptionWorkFromQuery(ResultSet resultReceptionWorkQuery){
        ReceptionWork receptionWork = new ReceptionWork();
            try {
                receptionWork = new ReceptionWork(
                        resultReceptionWorkQuery.getString("urlFile"),
                        resultReceptionWorkQuery.getString("projectName"),
                        resultReceptionWorkQuery.getBoolean("impactBA"),
                        resultReceptionWorkQuery.getString("evidenceType"),
                        resultReceptionWorkQuery.getString("evidenceTitle"),
                        resultReceptionWorkQuery.getString("registrationResponsible"),
                        resultReceptionWorkQuery.getString("registrationDate"),
                        resultReceptionWorkQuery.getString("studyDegree"),
                        resultReceptionWorkQuery.getString("publicationDate"),
                        resultReceptionWorkQuery.getString("country"),
                        resultReceptionWorkQuery.getString("description"),
                        resultReceptionWorkQuery.getString("status"),
                        resultReceptionWorkQuery.getInt("actualDurationInMonths"),
                        resultReceptionWorkQuery.getInt("estimatedDurationInMonths"),
                        resultReceptionWorkQuery.getString("modality")
                );
            } catch (SQLException ex) {
                Logger.getLogger(ReceptionWorkDAO.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
            return receptionWork;
        }
    }
    
    public void updateReceptionWork(ReceptionWork receptionWork, String oldUrlFile){

        Connection connection = QUERY.getConnectionDatabaseNotAutoCommit();
        try{
            PreparedStatement sentenceQuery = QUERY.getConnectionDatabase().prepareStatement(
                "UPDATE ReceptionWork SET urlFile = ?, projectName = ?, impactBA = ?, evidenceType = ?, "
                    + "evidenceTitle = ?, registrationResponsible = ?, registrationDate = ?, studyDegree = ?, publicationDate = ?, country = ?,"
                    + "description = ?, status = ?, actualDurationInMonths = ?, estimatedDurationInMonths = ?, modality = ? WHERE urlFile = ?;"
            );
            sentenceQuery.setString(1, receptionWork.getUrlFile());
            sentenceQuery.setString(2, receptionWork.getProjectName());
            sentenceQuery.setBoolean(3, receptionWork.getImpactAB());
            sentenceQuery.setString(4, receptionWork.getEvidenceType());
            sentenceQuery.setString(5, receptionWork.getEvidenceTitle());
            sentenceQuery.setString(6, receptionWork.getRegistrationResponsible());
            sentenceQuery.setString(7, receptionWork.getRegistrationDate());
            sentenceQuery.setString(8, receptionWork.getStudyDegree());
            sentenceQuery.setString(9, receptionWork.getPublicationDate());
            sentenceQuery.setString(10, receptionWork.getCountry());
            sentenceQuery.setString(11, receptionWork.getDescription());
            sentenceQuery.setString(12, receptionWork.getStatus());
            sentenceQuery.setInt(13, receptionWork.getActualDurationInMonths());
            sentenceQuery.setInt(14, receptionWork.getEstimatedDurationInMonths());
            sentenceQuery.setString(15, receptionWork.getModality());
            sentenceQuery.setString(16, oldUrlFile);
            sentenceQuery.executeUpdate();
            this.insertIntoIntegrantReceptionWork(connection, receptionWork);
            this.insertIntoCollaboratorReceptionWork(connection, receptionWork);
            this.insertIntoStudentReceptionWork(connection, receptionWork);
            this.insertIntoRequirementReceptionWork(connection, receptionWork);
            connection.commit();
            connection.setAutoCommit(true);
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                Logger.getLogger(ReceptionWork.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(ReceptionWorkDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }finally{
            QUERY.closeConnection();
        }
    }
    

    public void deleteReceptionWork(String urlFileReceptionWork){
        Connection connection = QUERY.getConnectionDatabaseNotAutoCommit();
        try{
            this.deleteIntegrantsFromURLFileReceptionWork(connection, urlFileReceptionWork);
            this.deleteCollaboratorsFromURLFileReceptionWork(connection, urlFileReceptionWork);
            this.deleteStudensFromURLFileReceptionWork(connection, urlFileReceptionWork);
            this.deleteRequirementsFromURLFileReceptionWork(connection, urlFileReceptionWork);
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "DELETE FROM ReceptionWork WHERE urlFile = ?;"
            );
            sentenceQuery.setString(1, urlFileReceptionWork);
            sentenceQuery.executeUpdate();
            connection.commit();
            connection.setAutoCommit(true);
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                connection.close();
                Logger.getLogger(ReceptionWork.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(ReceptionWorkDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }finally{
            QUERY.closeConnection();
        }
    }
    
    public void insertIntoIntegrantReceptionWork(Connection connection, ReceptionWork receptionWork){
        receptionWork.getIntegrants().forEach(integrant -> {
            try{
                PreparedStatement sentenceQuery = connection.prepareStatement(
                    "INSERT INTO IntegrantReceptionWork (rfc, urlFile) VALUES (?, ?);"
                );
                sentenceQuery.setString(1, integrant.getRfc());
                sentenceQuery.setString(2, receptionWork.getUrlFile());
                sentenceQuery.executeUpdate();
            }catch(SQLException sqlException){
                try{
                    connection.rollback();
                    Logger.getLogger(ReceptionWork.class.getName()).log(Level.SEVERE, null, sqlException);
                }catch(SQLException ex){
                    Logger.getLogger(ReceptionWorkDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    public void insertIntoCollaboratorReceptionWork(Connection connection, ReceptionWork receptionWork){
        receptionWork.getCollaborators().forEach(collaborator -> {
            try{
                PreparedStatement sentenceQuery = connection.prepareStatement(
                    "INSERT INTO CollaborateReceptionWork (rfc, urlFile) VALUES (?, ?);"
                );
                sentenceQuery.setString(1, collaborator.getRfc());
                sentenceQuery.setString(2, receptionWork.getUrlFile());
                sentenceQuery.executeUpdate();
            }catch(SQLException sqlException){
                try{
                    connection.rollback();
                    Logger.getLogger(ReceptionWork.class.getName()).log(Level.SEVERE, null, sqlException);
                }catch(SQLException ex){
                    Logger.getLogger(ReceptionWorkDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    public void insertIntoStudentReceptionWork(Connection connection, ReceptionWork receptionWork){
        receptionWork.getStudents().forEach(student -> {
            try{
                PreparedStatement sentenceQuery = connection.prepareStatement(
                    "INSERT INTO ReceptionWorkStudent (urlFile, student) VALUES (?, ?);"
                );
                sentenceQuery.setString(1, receptionWork.getUrlFile());
                sentenceQuery.setString(2, student);
                sentenceQuery.executeUpdate();
            }catch(SQLException sqlException){
                try{
                    connection.rollback();
                    connection.close();
                    Logger.getLogger(ReceptionWork.class.getName()).log(Level.SEVERE, null, sqlException);
                }catch(SQLException ex){
                    Logger.getLogger(ReceptionWorkDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    public void insertIntoRequirementReceptionWork(Connection connection, ReceptionWork receptionWork){
        receptionWork.getRequirements().forEach(requirement -> {
            try{
                PreparedStatement sentenceQuery = connection.prepareStatement(
                    "INSERT INTO ReceptionWorkRequirement (urlFile, requirement) VALUES (?, ?);"
                );
                sentenceQuery.setString(1, receptionWork.getUrlFile());
                sentenceQuery.setString(2, requirement);
                sentenceQuery.executeUpdate();
            }catch(SQLException sqlException){
                try{
                    connection.rollback();
                    connection.close();
                    Logger.getLogger(ReceptionWork.class.getName()).log(Level.SEVERE, null, sqlException);
                }catch(SQLException ex){
                    Logger.getLogger(ReceptionWorkDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    public void deleteIntegrantsFromURLFileReceptionWork(Connection connection, String urlFile){
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "DELETE FROM IntegrantReceptionWork WHERE urlFile = ?;"
            );
            sentenceQuery.setString(1, urlFile);
            sentenceQuery.executeUpdate();
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                connection.close();
                Logger.getLogger(ReceptionWork.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(ReceptionWorkDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void deleteCollaboratorsFromURLFileReceptionWork(Connection connection, String urlFile){
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "DELETE FROM CollaborateReceptionWork WHERE urlFile = ?;"
            );
            sentenceQuery.setString(1, urlFile);
            sentenceQuery.executeUpdate();
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                connection.close();
                Logger.getLogger(ReceptionWork.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(ReceptionWorkDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void deleteStudensFromURLFileReceptionWork(Connection connection, String urlFile){
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "DELETE FROM ReceptionWorkStudent WHERE urlFile = ?;"
            );
            sentenceQuery.setString(1, urlFile);
            sentenceQuery.executeUpdate();
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                connection.close();
                Logger.getLogger(ReceptionWork.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(ReceptionWorkDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void deleteRequirementsFromURLFileReceptionWork(Connection connection, String urlFile){
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "DELETE FROM ReceptionWorkRequirement WHERE urlFile = ?;"
            );
            sentenceQuery.setString(1, urlFile);
            sentenceQuery.executeUpdate();
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                connection.close();
                Logger.getLogger(ReceptionWork.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(ReceptionWorkDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public boolean addNewEvidence(Evidence evidence){
        boolean correctInsert = false;
       Connection connection = QUERY.getConnectionDatabaseNotAutoCommit();
        try{
            PreparedStatement sentenceQuery = QUERY.getConnectionDatabase().prepareStatement(
                "insert into ReceptionWork values(? , ? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"
            );
            sentenceQuery.setString(1, evidence.getUrlFile());
            sentenceQuery.setString(2, evidence.getProjectName());
            sentenceQuery.setBoolean(3, evidence.getImpactAB());
            sentenceQuery.setString(4, evidence.getEvidenceType());
            sentenceQuery.setString(5, evidence.getEvidenceTitle());
            sentenceQuery.setString(6, evidence.getRegistrationResponsible());
            sentenceQuery.setString(7, evidence.getRegistrationDate());
            sentenceQuery.setString(8, evidence.getStudyDegree());
            sentenceQuery.setString(9, evidence.getPublicationDate());
            sentenceQuery.setString(10, evidence.getCountry());
            sentenceQuery.setString(11, ((ReceptionWork)evidence).getDescription());
            sentenceQuery.setString(12, ((ReceptionWork)evidence).getStatus());
            sentenceQuery.setInt(13, ((ReceptionWork)evidence).getActualDurationInMonths());
            sentenceQuery.setInt(14, ((ReceptionWork)evidence).getEstimatedDurationInMonths());
            sentenceQuery.setString(15, ((ReceptionWork)evidence).getModality());
            sentenceQuery.executeUpdate();
            this.insertIntoIntegrantReceptionWork(connection, ((ReceptionWork)evidence));
            this.insertIntoCollaboratorReceptionWork(connection, ((ReceptionWork)evidence));
            this.insertIntoStudentReceptionWork(connection, ((ReceptionWork)evidence));
            this.insertIntoRequirementReceptionWork(connection, ((ReceptionWork)evidence));
            connection.commit();
            connection.setAutoCommit(true);
            correctInsert = true;
        }catch(SQLException sqlEsception){
            try{
                connection.rollback();
                Logger.getLogger(ReceptionWork.class.getName()).log(Level.SEVERE, null, sqlEsception);
            }catch(SQLException ex){
                Logger.getLogger(ReceptionWorkDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }finally{
            QUERY.closeConnection();
            return correctInsert;
        }
    }

    @Override
    public boolean updateEvidence(Evidence evidence, String oldUrlFile){
        boolean correctUpdate = false;
        Connection connection = QUERY.getConnectionDatabaseNotAutoCommit();
        this.deleteCollaboratorsFromURLFileReceptionWork(connection, oldUrlFile);
        this.deleteIntegrantsFromURLFileReceptionWork(connection, oldUrlFile);
        this.deleteRequirementsFromURLFileReceptionWork(connection, oldUrlFile);
        this.deleteStudensFromURLFileReceptionWork(connection, oldUrlFile);
        try{
            PreparedStatement sentenceQuery = QUERY.getConnectionDatabase().prepareStatement(
                "UPDATE ReceptionWork SET urlFile = ?, projectName = ?, impactBA = ?, evidenceType = ?, "
                    + "evidenceTitle = ?, registrationResponsible = ?, registrationDate = ?, studyDegree = ?, publicationDate = ?, country = ?,"
                    + "description = ?, status = ?, actualDurationInMonths = ?, estimatedDurationInMonths = ?, modality = ? WHERE urlFile = ?;"
            );
            sentenceQuery.setString(1, evidence.getUrlFile());
            sentenceQuery.setString(2, evidence.getProjectName());
            sentenceQuery.setBoolean(3, evidence.getImpactAB());
            sentenceQuery.setString(4, evidence.getEvidenceType());
            sentenceQuery.setString(5, evidence.getEvidenceTitle());
            sentenceQuery.setString(6, evidence.getRegistrationResponsible());
            sentenceQuery.setString(7, evidence.getRegistrationDate());
            sentenceQuery.setString(8, evidence.getStudyDegree());
            sentenceQuery.setString(9, evidence.getPublicationDate());
            sentenceQuery.setString(10, evidence.getCountry());
            sentenceQuery.setString(11, ((ReceptionWork)evidence).getDescription());
            sentenceQuery.setString(12, ((ReceptionWork)evidence).getStatus());
            sentenceQuery.setInt(13, ((ReceptionWork)evidence).getActualDurationInMonths());
            sentenceQuery.setInt(14, ((ReceptionWork)evidence).getEstimatedDurationInMonths());
            sentenceQuery.setString(15, ((ReceptionWork)evidence).getModality());
            sentenceQuery.setString(16, oldUrlFile);
            sentenceQuery.executeUpdate();
            this.insertIntoIntegrantReceptionWork(connection, ((ReceptionWork)evidence));
            this.insertIntoCollaboratorReceptionWork(connection, ((ReceptionWork)evidence));
            this.insertIntoStudentReceptionWork(connection, ((ReceptionWork)evidence));
            this.insertIntoRequirementReceptionWork(connection, ((ReceptionWork)evidence));
            connection.commit();
            connection.setAutoCommit(true);
            correctUpdate = true;
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                Logger.getLogger(ReceptionWork.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(ReceptionWorkDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }finally{
            QUERY.closeConnection();
            return correctUpdate;
        }
    }

    @Override
    public boolean deleteEvidenceByUrl(String urlEvidenceFile){
        boolean correctDelete = false;
        Connection connection = QUERY.getConnectionDatabaseNotAutoCommit();
        try{
            this.deleteIntegrantsFromURLFileReceptionWork(connection, urlEvidenceFile);
            this.deleteCollaboratorsFromURLFileReceptionWork(connection, urlEvidenceFile);
            this.deleteStudensFromURLFileReceptionWork(connection, urlEvidenceFile);
            this.deleteRequirementsFromURLFileReceptionWork(connection, urlEvidenceFile);
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "DELETE FROM ReceptionWork WHERE urlFile = ?;"
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
                Logger.getLogger(ReceptionWork.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(ReceptionWorkDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }finally{
            QUERY.closeConnection();
            return correctDelete;
        }
    }
    
    public List<ReceptionWork> getReceptionWorkListForEvidence(){
        List<ReceptionWork> listReceptionWork = new ArrayList<>();
        try{
            Statement instructionQuery = QUERY.getConnectionDatabase().createStatement();;
            ResultSet queryResult = instructionQuery.executeQuery("Select urlFile, projectName, impactBA, evidenceType, "
                    + "evidenceTitle, registrationResponsible, registrationDate, studyDegree, publicationDate, country FROM ReceptionWork");
            while(queryResult.next()){
                listReceptionWork.add(new ReceptionWork(
                    queryResult.getString("urlFile"), 
                    queryResult.getString("projectName"),
                    queryResult.getBoolean("impactBA"),
                    queryResult.getString("evidenceType"),
                    queryResult.getString("evidenceTitle"),
                    queryResult.getString("registrationResponsible"),
                    queryResult.getString("registrationDate"),
                    queryResult.getString("studyDegree"),
                    queryResult.getString("publicationDate"),
                    queryResult.getString("country")));
            }
        }catch(SQLException ex){
            Logger.getLogger(ReceptionWorkDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            QUERY.closeConnection();
            return listReceptionWork;
        }
    }
    
    private List<Integrant> getIntegrantsReceptionWorkParticipation(Connection connection, String urlFileReceptionWork){
        List<Integrant> integrants = new ArrayList<>();
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "SELECT i.fullName FROM Integrant i, IntegrantReceptionWork ia WHERE ia.rfc = i.rfc AND urlFile = ?;"
            );
            sentenceQuery.setString(1, urlFileReceptionWork);
            ResultSet resultQuery = sentenceQuery.executeQuery();
            while(resultQuery.next()){
                Integrant integrant = new Integrant();
                integrant.setFullName(resultQuery.getString("fullName"));
                integrants.add(integrant);
            }
        }catch(SQLException ex){
            Logger.getLogger(ReceptionWorkDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            return integrants;
        }
    }
    
    private List<Collaborator> getCollaboratorsReceptionWorkParticipation(Connection connection, String urlFileReceptionWork){
        List<Collaborator> collaborators = new ArrayList<>();
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "SELECT c.fullName FROM CollaborateReceptionWork ca, Collaborator c WHERE ca.rfc = c.rfc AND urlFile = ?;"
            );
            sentenceQuery.setString(1, urlFileReceptionWork);
            ResultSet resultQuery = sentenceQuery.executeQuery();
            while(resultQuery.next()){
                Collaborator collaborator = new Collaborator();
                collaborator.setFullName(resultQuery.getString("fullName"));
                collaborators.add(collaborator);
            }
        }catch(SQLException ex){
            Logger.getLogger(ReceptionWorkDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            return collaborators;
        }
    }
    
    private List<String> getStudentNamesReceptionWorkParticipation(Connection connection, String urlFileReceptionWork){
        List<String> students = new ArrayList<>();
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "SELECT * FROM ReceptionWorkStudent WHERE urlFile = ?;"
            );
            sentenceQuery.setString(1, urlFileReceptionWork);
            ResultSet resultQuery = sentenceQuery.executeQuery();
            while(resultQuery.next()){
                students.add(resultQuery.getString("student"));
            }
        }catch(SQLException ex){
            Logger.getLogger(ReceptionWorkDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            return students;
        }
    }
    
    private List<String> getRequirementsReceptionWork(Connection connection, String urlFileReceptionWork){
        List<String> students = new ArrayList<>();
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "SELECT * FROM ReceptionWorkRequirement WHERE urlFile = ?;"
            );
            sentenceQuery.setString(1, urlFileReceptionWork);
            ResultSet resultQuery = sentenceQuery.executeQuery();
            while(resultQuery.next()){
                students.add(resultQuery.getString("requirement"));
            }
        }catch(SQLException ex){
            Logger.getLogger(ReceptionWorkDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            return students;
        }
    }

    @Override
    public String toString(){
        return "Trabajo Recepcional";
    }

    @Override
    public EvidenceDAO getEvidenceDaoInstance(String evidenceType){
        EvidenceDAO evidenceDao = null;
        if(this.toString().equalsIgnoreCase(evidenceType)){
            evidenceDao = new ReceptionWorkDAO();
        }
        return evidenceDao;
    }
}
