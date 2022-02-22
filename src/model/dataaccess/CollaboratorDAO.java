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

import model.domain.Collaborator;
import model.domain.Member;
import utils.ConnectionDatabase;

public class CollaboratorDAO implements IMemberDAO{
    
    private final ConnectionDatabase CONNECTION = new ConnectionDatabase();
    
    @Override
    public List<Member> getMembers(String bodyAcademyKey){
        List<Member> collaborators = new ArrayList<>();
        try{
            PreparedStatement sentenceQuery = CONNECTION.getConnectionDatabase().prepareStatement(
                "SELECT rfc, fullName, participationType, emailUV, cellPhone, participationStatus FROM `Collaborator` WHERE bodyAcademyKey = ?;"
            );
            sentenceQuery.setString(1, bodyAcademyKey);
            ResultSet result = sentenceQuery.executeQuery();
            while(result.next()){
                Collaborator collaborator = new Collaborator();
                collaborator.setRfc(result.getString("rfc"));
                collaborator.setFullName(result.getString("fullName"));
                collaborator.setParticipationType(result.getString("participationType"));
                collaborator.setEmailUV(result.getString("emailUV"));
                collaborator.setCellphone(result.getString("cellPhone"));
                collaborator.setParticipationStatus(result.getString("participationStatus"));
                collaborators.add(collaborator);
            }
        }catch(SQLException sqlException){
            Logger.getLogger(Collaborator.class.getName()).log(Level.SEVERE, null, sqlException);
        }finally{
            CONNECTION.closeConnection();
            return collaborators;
        }
    }
    
    @Override
    public Member getMemberByUVmail(String emailUV){
        Collaborator collaborator = new Collaborator();
        try{
            PreparedStatement sentenceQuery = CONNECTION.getConnectionDatabase().prepareStatement(
                "select * from Collaborator where emailUV = ?;"
            );
            sentenceQuery.setString(1, emailUV);
            ResultSet queryResult = sentenceQuery.executeQuery();
            if(queryResult.next()){collaborator = new Collaborator(
                queryResult.getString("rfc"), 
                queryResult.getString("fullName"),
                queryResult.getString("emailUV"),
                queryResult.getString("participationStatus"),
                queryResult.getString("curp"),
                queryResult.getString("participationType"),
                queryResult.getString("nacionality"),
                queryResult.getDate("dateOfAdmission").toString(),
                queryResult.getString("educationalProgram"),
                queryResult.getInt("satffNumber"),
                queryResult.getString("cellPhone"),
                queryResult.getString("bodyAcademyKey"),
                queryResult.getString("studyArea"),
                queryResult.getString("nameBACollaborator"),
                queryResult.getString("highestDegreeStudies")
            );}
        }catch(SQLException sqlException){
            collaborator = null;
            Logger.getLogger(Collaborator.class.getName()).log(Level.SEVERE, null, sqlException);
        }finally{
            CONNECTION.closeConnection();
            return collaborator;
        }
    }

    @Override
    public boolean addMember(Member newMember){
        boolean correctRegistrer = false;
        try{
            PreparedStatement sentenceQuery = CONNECTION.getConnectionDatabase().prepareStatement(
                "INSERT INTO Collaborator VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"
            );
            sentenceQuery.setString(1, newMember.getRfc());
            sentenceQuery.setString(2, newMember.getBodyAcademyKey());
            sentenceQuery.setString(3, newMember.getFullName());
            sentenceQuery.setString(4, newMember.getDateOfAdmission());
            sentenceQuery.setString(5, newMember.getEmailUV());
            sentenceQuery.setString(6, newMember.getParticipationStatus());
            sentenceQuery.setString(7, newMember.getCurp());
            sentenceQuery.setString(8, newMember.getNationality());
            sentenceQuery.setString(9, newMember.getEducationalProgram());
            sentenceQuery.setString(10, newMember.getCellphone());
            sentenceQuery.setInt(11, newMember.getStaffNumber());
            sentenceQuery.setString(12, ((Collaborator)newMember).getStudyArea());
            sentenceQuery.setString(13, ((Collaborator)newMember).getNameBACollaborator());
            sentenceQuery.setString(14, ((Collaborator)newMember).getHighestDegreeStudies());
            sentenceQuery.setString(15, newMember.getParticipationType());
            sentenceQuery.executeUpdate();
            correctRegistrer = true;
        }catch(SQLException sqlException){
            Logger.getLogger(Collaborator.class.getName()).log(Level.SEVERE, null, sqlException);
        }finally{
            CONNECTION.closeConnection();
            return correctRegistrer;
        }
    }

    @Override
    public boolean updateMember(Member member, String oldRFC){
        boolean correctUpdate = false;
        try{
            PreparedStatement sentenceQuery = CONNECTION.getConnectionDatabase().prepareStatement(
                "UPDATE Collaborator SET rfc = ?, bodyAcademyKey = ?, fullName = ?, dateOfAdmission = ?, emailUV = ?, "
                + "participationStatus = ?, curp = ?, nacionality = ?, educationalProgram = ?, cellPhone = ?, satffNumber = ?,"
                + " studyArea = ?, nameBACollaborator = ?, highestDegreeStudies = ?, participationType = ? WHERE rfc = ?;"
            );
            sentenceQuery.setString(1, member.getRfc());
            sentenceQuery.setString(2, member.getBodyAcademyKey());
            sentenceQuery.setString(3, member.getFullName());
            sentenceQuery.setString(4, member.getDateOfAdmission());
            sentenceQuery.setString(5, member.getEmailUV());
            sentenceQuery.setString(6, member.getParticipationStatus());
            sentenceQuery.setString(7, member.getCurp());
            sentenceQuery.setString(8, member.getNationality());
            sentenceQuery.setString(9, member.getEducationalProgram());
            sentenceQuery.setString(10, member.getCellphone());
            sentenceQuery.setInt(11, member.getStaffNumber());
            sentenceQuery.setString(12, ((Collaborator)member).getStudyArea());
            sentenceQuery.setString(13, ((Collaborator)member).getNameBACollaborator());
            sentenceQuery.setString(14, ((Collaborator)member).getHighestDegreeStudies());
            sentenceQuery.setString(15, member.getParticipationType());
            sentenceQuery.setString(16, oldRFC);
            sentenceQuery.executeUpdate();
            correctUpdate = true;
        }catch(SQLException sqlException){
            Logger.getLogger(Collaborator.class.getName()).log(Level.SEVERE, null, sqlException);
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
                "UPDATE Collaborator SET participationStatus = ? WHERE emailUV = ?;"
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
    
    public boolean searchCollaboratorByRfc(String collaboratorRfc){
        boolean collaboratorRegistered = false;
        try{
            PreparedStatement sentenceQuery = CONNECTION.getConnectionDatabase().prepareStatement(
                "select * from Collaborator where rfc = ?;"
            );
            sentenceQuery.setString(1, collaboratorRfc);
            ResultSet result = sentenceQuery.executeQuery();
            if(result.next()){
                collaboratorRegistered = true;
            }
        }catch(SQLException ex){
            Logger.getLogger(CollaboratorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            CONNECTION.closeConnection();
            return collaboratorRegistered;
        }
    }
    
    @Override
    public boolean subscribeMemberByEmailUV(String emailUV){
        boolean correctSubscribe = false;
        try{
            PreparedStatement sentenceQuery = CONNECTION.getConnectionDatabase().prepareStatement(
                "UPDATE Collaborator SET participationStatus = ? WHERE emailUV = ?;"
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
    
}
