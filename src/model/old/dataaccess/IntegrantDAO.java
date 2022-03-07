/*
 * @author Josué 
 * @versión v1.0
 * Last modification date: 20-06-2021
 */

package model.old.dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.old.domain.Collaborator;
import model.old.domain.Integrant;
import model.old.domain.Member;
import model.old.domain.Schooling;
import model.old.ConnectionDatabase;

public class IntegrantDAO implements IMemberDAO, IIntegrantDAO{

    private final ConnectionDatabase CONNECTION = new ConnectionDatabase();
    
    public List<String> getAllIntegrantsName(String bodyAcademyKey){
        List<String> integrantsNameList = new ArrayList();
        try{
            PreparedStatement sentenceQuery = CONNECTION.getConnectionDatabase().prepareStatement(
                "SELECT * FROM Integrant WHERE participationStatus = 'Activo' AND bodyAcademyKey = ?;"
            );
            sentenceQuery.setString(1, bodyAcademyKey);
            ResultSet queryResult = sentenceQuery.executeQuery();
            while(queryResult.next()){
                String integrantName = queryResult.getString("fullName");
                integrantsNameList.add(integrantName);
            }
        }catch(SQLException sqlException){
            Logger.getLogger(Integrant.class.getName()).log(Level.SEVERE, null, sqlException);
        }finally{
            CONNECTION.closeConnection();
            return integrantsNameList;
        }
    }
    
    @Override
    public Integrant getIntegrantTocken(Integrant usuario){
        Integrant integrantVerified = new Integrant();
        try{
            PreparedStatement sentenceQuery = CONNECTION.getConnectionDatabase().prepareStatement(
                "SELECT emailUV, fullName, bodyAcademyKey, participationType, rfc, participationStatus FROM Integrant WHERE  emailUV = ? AND password = ? AND bodyAcademyKey = ?;"
            );
            sentenceQuery.setString(1, usuario.getEmailUV());
            sentenceQuery.setString(2, usuario.getPassword());
            sentenceQuery.setString(3, usuario.getBodyAcademyKey());
            ResultSet result = sentenceQuery.executeQuery();
            if(result.next()){
                integrantVerified.setFullName(result.getString("fullName"));
                integrantVerified.setEmailUV(result.getString("emailUV"));
                integrantVerified.setBodyAcademyKey(result.getString("bodyAcademyKey"));
                integrantVerified.setParticipationType(result.getString("participationType"));
                integrantVerified.setRfc(result.getString("rfc"));
                integrantVerified.setParticipationStatus(result.getString("participationStatus"));
            }
        }catch(SQLException ex){
            Logger.getLogger(IntegrantDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            CONNECTION.closeConnection();
            return integrantVerified;
        }
    }
    
    @Override
    public Integrant getIntegrantToken(String email, String password){
        Integrant integrantVerified = new Integrant();
        try{
            PreparedStatement sentenceQuery = CONNECTION.getConnectionDatabase().prepareStatement(
                "SELECT emailUV, fullName, bodyAcademyKey, participationType, rfc FROM Integrant WHERE  emailUV = ? AND password = ?;"
            );
            sentenceQuery.setString(1, email);
            sentenceQuery.setString(2, password);
            ResultSet result = sentenceQuery.executeQuery();
            if(result.next()){
                integrantVerified.setFullName(result.getString("fullName"));
                integrantVerified.setEmailUV(result.getString("emailUV"));
                integrantVerified.setBodyAcademyKey(result.getString("bodyAcademyKey"));
                integrantVerified.setParticipationType(result.getString("participationType"));
                integrantVerified.setRfc(result.getString("rfc"));
            }
        }catch(SQLException ex){
            Logger.getLogger(IntegrantDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            CONNECTION.closeConnection();
            return integrantVerified;
        }
    }
    
    @Override
    public List<Member> getMembers(String bodyAcademyKey){
        List<Member> integrants = new ArrayList<>();
        try{
            PreparedStatement sentenceQuery = CONNECTION.getConnectionDatabase().prepareStatement(
                "SELECT rfc, fullName, participationType, emailUV, cellPhone, participationStatus FROM `Integrant` WHERE bodyAcademyKey = ?;"
            );
            sentenceQuery.setString(1, bodyAcademyKey);
            ResultSet result = sentenceQuery.executeQuery();
            while(result.next()){
                Integrant integrant = new Integrant();
                integrant.setRfc(result.getString("rfc"));
                integrant.setFullName(result.getString("fullName"));
                integrant.setParticipationType(result.getString("participationType"));
                integrant.setEmailUV(result.getString("emailUV"));
                integrant.setCellphone(result.getString("cellPhone"));
                integrant.setParticipationStatus(result.getString("participationStatus"));
                integrants.add(integrant);
            }
        }catch(SQLException sqlException){
            Logger.getLogger(Collaborator.class.getName()).log(Level.SEVERE, null, sqlException);
        }finally{
            CONNECTION.closeConnection();
            return integrants;
        }
    }
       
    @Override
    public Member getMemberByUVmail(String emailUV){
        Integrant integrant = new Integrant();
        Connection connection = CONNECTION.getConnectionDatabaseNotAutoCommit();
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "SELECT * FROM Integrant WHERE emailUV = ?;"
            );
            sentenceQuery.setString(1, emailUV);
            ResultSet queryResult = sentenceQuery.executeQuery();
            if(queryResult.next()){
                integrant = new Integrant(
                    queryResult.getString("rfc"), 
                    queryResult.getString("fullName"),
                    queryResult.getString("emailUV"),
                    queryResult.getString("participationStatus"),
                    queryResult.getString("password"),
                    queryResult.getString("curp"),
                    queryResult.getString("participationType"),
                    queryResult.getString("nacionality"),
                    queryResult.getDate("dateOfAdmission").toString(),
                    queryResult.getString("educationalProgram"),
                    queryResult.getInt("satffNumber"),
                    queryResult.getString("cellPhone"),
                    queryResult.getString("bodyAcademyKey"),
                    queryResult.getString("appointment"),
                    queryResult.getString("additionalEmail"),
                    queryResult.getString("homePhone"),
                    queryResult.getString("workPhone")
                );
            }
            integrant.setSchooling(this.getIntegrantStudies(connection, integrant.getRfc()));
        }catch(SQLException sqlException){
            Logger.getLogger(Integrant.class.getName()).log(Level.SEVERE, null, sqlException);
        }finally{
            CONNECTION.closeConnection();
            return integrant;
        }
    }

    @Override
    public boolean addMember(Member newMember){
        Connection connection = CONNECTION.getConnectionDatabaseNotAutoCommit();
        boolean correctInsertion = false;
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "INSERT INTO Integrant VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"
            );
            sentenceQuery.setString(1, newMember.getRfc());
            sentenceQuery.setString(2, newMember.getBodyAcademyKey());
            sentenceQuery.setString(3, newMember.getFullName());
            sentenceQuery.setString(4, newMember.getDateOfAdmission());
            sentenceQuery.setString(5, newMember.getEmailUV());
            sentenceQuery.setString(6, ((Integrant)newMember).getPassword());
            sentenceQuery.setString(7, newMember.getParticipationStatus());
            sentenceQuery.setString(8, ((Integrant)newMember).getAditionalMail());
            sentenceQuery.setString(9, newMember.getCurp());
            sentenceQuery.setString(10, newMember.getNationality());
            sentenceQuery.setString(11, newMember.getEducationalProgram());
            sentenceQuery.setString(12, newMember.getCellphone());
            sentenceQuery.setInt(13, newMember.getStaffNumber());
            sentenceQuery.setString(14, ((Integrant)newMember).getHomePhone());
            sentenceQuery.setString(15, ((Integrant)newMember).getWorkPhone());
            sentenceQuery.setString(16, ((Integrant)newMember).getAppointmentMember());
            sentenceQuery.setString(17, ((Integrant)newMember).getParticipationType());
            sentenceQuery.executeUpdate();
            this.addIntegrantStudies(connection, ((Integrant)newMember));
            connection.commit();
            connection.setAutoCommit(true);
            correctInsertion = true;
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                Logger.getLogger(Integrant.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(IntegrantDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }finally{
            CONNECTION.closeConnection();
            return correctInsertion;
        }
    }

    @Override
    public boolean updateMember(Member member, String oldEmailUv){
        Connection connection = CONNECTION.getConnectionDatabaseNotAutoCommit();
        boolean correctUpdate = false;
        try{
            this.deleteIntegrantStudies(connection, oldEmailUv);
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "UPDATE Integrant SET rfc = ?, bodyAcademyKey = ?, fullName = ?, dateOfAdmission = ?, emailUV = ?, password = ?, "
                + "participationStatus = ?, additionalEmail = ?, curp = ?, nacionality = ?, educationalProgram = ?, cellPhone = ?, satffNumber = ?,"
                + " homePhone = ?, workPhone = ?, appointment = ?, participationType = ? WHERE emailUV = ?;"
            );
            sentenceQuery.setString(1, member.getRfc());
            sentenceQuery.setString(2, member.getBodyAcademyKey());
            sentenceQuery.setString(3, member.getFullName());
            sentenceQuery.setString(4, member.getDateOfAdmission());
            sentenceQuery.setString(5, member.getEmailUV());
            sentenceQuery.setString(6, ((Integrant)member).getPassword());
            sentenceQuery.setString(7, member.getParticipationStatus());
            sentenceQuery.setString(8, ((Integrant)member).getAditionalMail());
            sentenceQuery.setString(9, member.getCurp());
            sentenceQuery.setString(10, member.getNationality());
            sentenceQuery.setString(11, member.getEducationalProgram());
            sentenceQuery.setString(12, member.getCellphone());
            sentenceQuery.setInt(13, member.getStaffNumber());
            sentenceQuery.setString(14, ((Integrant)member).getHomePhone());
            sentenceQuery.setString(15, ((Integrant)member).getWorkPhone());
            sentenceQuery.setString(16, ((Integrant)member).getAppointmentMember());
            sentenceQuery.setString(17, ((Integrant)member).getParticipationType());
            sentenceQuery.setString(18, oldEmailUv);
            sentenceQuery.executeUpdate();
            this.addIntegrantStudies(connection, ((Integrant)member));
            connection.commit();
            connection.setAutoCommit(true);
            correctUpdate = true;
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                Logger.getLogger(Integrant.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(IntegrantDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }finally{
            CONNECTION.closeConnection();
            return correctUpdate;
        }
    }
    
    @Override
    public boolean unsubscribeMemberByEmailUV(String emailUV){
        boolean correctUnsubscribe = false;
        try{
            PreparedStatement sentenceQuery = CONNECTION.getConnectionDatabase().prepareStatement(
                "UPDATE Integrant SET participationStatus = ? WHERE emailUV = ?;"
            );
            sentenceQuery.setString(1, "Dado de baja");
            sentenceQuery.setString(2,emailUV);
            sentenceQuery.executeUpdate();
            correctUnsubscribe = true;
        }catch(SQLException ex){
            Logger.getLogger(CollaboratorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            CONNECTION.closeConnection();
            return correctUnsubscribe;
        }
    }
    
    @Override
    public boolean subscribeMemberByEmailUV(String emailUV){
        boolean correctSubscribe = false; 
        try{
            PreparedStatement sentenceQuery = CONNECTION.getConnectionDatabase().prepareStatement(
                "UPDATE Integrant SET participationStatus = ? WHERE emailUV = ?;"
            );
            sentenceQuery.setString(1, "Activo");
            sentenceQuery.setString(2,emailUV);
            sentenceQuery.executeUpdate();
            correctSubscribe = true;
        }catch(SQLException ex){
            Logger.getLogger(CollaboratorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            CONNECTION.closeConnection();
            return correctSubscribe;
        }
    }
    
    private List<Schooling> getIntegrantStudies(Connection connection, String integrantRFC){
        List<Schooling> schooling = new ArrayList<>();
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "SELECT * FROM Schooling WHERE rfc = ?;"
            );
            sentenceQuery.setString(1, integrantRFC);
            ResultSet queryResult = sentenceQuery.executeQuery();
            while(queryResult.next()){
                schooling.add(new Schooling(
                    queryResult.getString("levelOfStudy"),
                    queryResult.getString("studyName"), 
                    queryResult.getDate("dateOfObtainingStudies").toString(),
                    queryResult.getString("studiesInsitution"),
                    queryResult.getString("studiesState"),
                    queryResult.getString("professionalId"),
                    queryResult.getString("studyArea"),
                    queryResult.getString("studyDiscipline")
                ));
            }
        }catch(SQLException sqlException){
            connection.rollback();
            connection.close();
            Logger.getLogger(Integrant.class.getName()).log(Level.SEVERE, null, sqlException);
        }finally{
            return schooling;
        }
    }
    
    @Override
    public Member searchMemberByEmailUv(String emailUv){
        Member integrant = new Integrant();
        try{
            PreparedStatement sentenceQuery = CONNECTION.getConnectionDatabase().prepareStatement(
                "SELECT rfc, fullName, participationType, emailUV, cellPhone, participationStatus FROM `Integrant` WHERE emailUV = ?;"
            );
            sentenceQuery.setString(1, emailUv);
            ResultSet result = sentenceQuery.executeQuery();
            if(result.next()){
                integrant = new Integrant();
                integrant.setRfc(result.getString("rfc"));
                integrant.setFullName(result.getString("fullName"));
                integrant.setParticipationType(result.getString("participationType"));
                integrant.setEmailUV(result.getString("emailUV"));
                integrant.setCellphone(result.getString("cellPhone"));
                integrant.setParticipationStatus(result.getString("participationStatus"));
            }
        }catch(SQLException sqlException){
            integrant = null;
            Logger.getLogger(Collaborator.class.getName()).log(Level.SEVERE, null, sqlException);
        }finally{
            CONNECTION.closeConnection();
            return integrant;
        }
    }
    
    private void addIntegrantStudies(Connection connection, Integrant integrant){
        integrant.getSchooling().forEach(schooling -> {
            try{
                PreparedStatement sentenceQuery = connection.prepareStatement(
                    "INSERT INTO Schooling VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);"
                );
                sentenceQuery.setString(1, schooling.getIdProfessionalStudies());
                sentenceQuery.setString(2, integrant.getRfc());
                sentenceQuery.setString(3, schooling.getStudiesInstitution());
                sentenceQuery.setString(4, schooling.getLevelOfStudy());
                sentenceQuery.setString(5, schooling.getDateOfObteiningStudies());
                sentenceQuery.setString(6, schooling.getStudiesSatate());
                sentenceQuery.setString(7, schooling.getStudiesDiscipline());
                sentenceQuery.setString(8, schooling.getStudyArea());
                sentenceQuery.setString(9, schooling.getStudyName());
                sentenceQuery.executeUpdate();
            }catch(SQLException sqlException){
                try{
                    connection.rollback();
                    connection.close();
                    Logger.getLogger(Integrant.class.getName()).log(Level.SEVERE, null, sqlException);
                }catch(SQLException ex){
                    Logger.getLogger(IntegrantDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    private void deleteIntegrantStudies(Connection connection, String emailUv){
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "DELETE FROM Schooling WHERE rfc = (SELECT rfc FROM Integrant WHERE emailUV = ?);"
            );
            sentenceQuery.setString(1, emailUv);
            sentenceQuery.executeUpdate();
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                connection.close();
                Logger.getLogger(Integrant.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(IntegrantDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
