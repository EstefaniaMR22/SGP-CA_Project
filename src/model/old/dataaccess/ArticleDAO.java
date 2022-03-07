/**
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
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

import model.old.domain.Article;
import model.old.domain.Collaborator;
import model.old.domain.Evidence;
import model.old.domain.Integrant;
import model.old.ConnectionDatabase;

public class ArticleDAO extends EvidenceDAO {
    
    private final ConnectionDatabase CONNECTION = new ConnectionDatabase();
    
    @Override
    public Evidence getEvidenceByUrl(String urlEvidenceFile){
        Article article = new Article();
        Connection connection = CONNECTION.getConnectionDatabaseNotAutoCommit();
        try{
            PreparedStatement senenceQuery = connection.prepareStatement(
                "SELECT * from Article WHERE urlFile = ?;"
            );
            senenceQuery.setString(1, urlEvidenceFile);
            ResultSet resultQuery = senenceQuery.executeQuery();
            if(resultQuery.next()){
                article = this.getOutArticleDataFromQuery(resultQuery);
                article.setIntegrants(this.getIntegrantArticleParticipation(connection, urlEvidenceFile));
                article.setCollaborators(this.getCollaboratorArticleParticipation(connection, urlEvidenceFile));
                article.setStudents(this.getStudentsArticleParticipation(connection, urlEvidenceFile));
            }
            connection.commit();
            connection.setAutoCommit(true);
        }catch(SQLException ex){
            article = null;
            Logger.getLogger(PrototypeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            CONNECTION.closeConnection();
            return article;
        }
    }

    @Override
    public boolean addNewEvidence(Evidence evidence){
        Connection connection = CONNECTION.getConnectionDatabaseNotAutoCommit();
        boolean correctInsertion = false;
        try{
            PreparedStatement sentence = connection.prepareStatement(
                "INSERT INTO Article VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);"
            );
            sentence.setString(1, evidence.getUrlFile());
            sentence.setString(2, evidence.getProjectName());
            sentence.setBoolean(3, evidence.getImpactAB());
            sentence.setString(4, "Artículo");
            sentence.setString(5, evidence.getEvidenceTitle());
            sentence.setString(6, evidence.getRegistrationResponsible());
            sentence.setString(7, evidence.getRegistrationDate());
            sentence.setString(8, evidence.getStudyDegree());
            sentence.setString(9, evidence.getPublicationDate());
            sentence.setString(10, evidence.getCountry());
            sentence.setString(11, ((Article)evidence).getIsnn());
            sentence.setString(12, ((Article)evidence).getMagazineEditorial());
            sentence.setString(13, ((Article)evidence).getMagazineName());
            sentence.executeUpdate();
            this.insertIntoStudentArticle(connection, (Article)evidence);
            this.insertIntoIntegrantArticle(connection, (Article)evidence);
            this.insertIntoCollaboratorArticle(connection, (Article)evidence);
            connection.commit();
            connection.setAutoCommit(true);
            correctInsertion = true;
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                Logger.getLogger(Article.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(ArticleDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }finally{
            CONNECTION.closeConnection();
            return correctInsertion;
        }
    }

    @Override
    public boolean updateEvidence(Evidence evidence, String oldUrlFile){
        Connection connection = CONNECTION.getConnectionDatabaseNotAutoCommit();
        boolean correctUpdate = false;
        try{
            this.deleteCollaboratorsFromURLFileArticle(connection, oldUrlFile);
            this.deleteIntegrantsFromURLFileArticle(connection, oldUrlFile);
            this.deleteStudensFromURLFileArticle(connection, oldUrlFile);
            PreparedStatement sentence = connection.prepareStatement(
                "UPDATE Article SET urlFile = ?, projectName = ?, impactBA = ?, evidenceType = ?,"
                + " evidenceTitle = ?, registrationResponsible = ?, registrationDate = ?, "
                + "studyDegree = ?, publicationDate = ?, country = ?, isnn = ?, editorialName = ?, magazineName = ?"
                + " WHERE urlFile = ?;"
            );
            sentence.setString(1, evidence.getEvidenceTitle() + ".pdf");
            sentence.setString(2, evidence.getProjectName());
            sentence.setBoolean(3, evidence.getImpactAB());
            sentence.setString(4, "Artículo");
            sentence.setString(5, evidence.getEvidenceTitle());
            sentence.setString(6, evidence.getRegistrationResponsible());
            sentence.setString(7, evidence.getRegistrationDate());
            sentence.setString(8, evidence.getStudyDegree());
            sentence.setString(9, evidence.getPublicationDate());
            sentence.setString(10, evidence.getCountry());
            sentence.setString(11, ((Article)evidence).getIsnn());
            sentence.setString(12, ((Article)evidence).getMagazineEditorial());
            sentence.setString(13, ((Article)evidence).getMagazineName());
            sentence.setString(14, oldUrlFile);
            sentence.executeUpdate();
            this.insertIntoCollaboratorArticle(connection, (Article)evidence);
            this.insertIntoIntegrantArticle(connection, (Article)evidence);
            this.insertIntoStudentArticle(connection, (Article)evidence);
            connection.commit();
            connection.setAutoCommit(true);
            correctUpdate = true;
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                Logger.getLogger(Article.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(Article.class.getName()).log(Level.SEVERE, null, ex);
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
            this.deleteStudensFromURLFileArticle(connection, urlEvidenceFile);
            this.deleteIntegrantsFromURLFileArticle(connection, urlEvidenceFile);
            this.deleteCollaboratorsFromURLFileArticle(connection, urlEvidenceFile);
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "DELETE FROM Article WHERE urlFile = ?;"
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
                Logger.getLogger(Article.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(Article.class.getName()).log(Level.SEVERE, null, ex);
            }
        }finally{
            CONNECTION.closeConnection();
            return correctDelete;
        }
    }
    
    private void deleteStudensFromURLFileArticle(Connection connection, String urlFileArticle){
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "DELETE FROM ArticleStudent WHERE urlFile = ?;"
            );
            sentenceQuery.setString(1, urlFileArticle);
            sentenceQuery.executeUpdate();
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                connection.close();
                Logger.getLogger(Article.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(GoalDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void deleteIntegrantsFromURLFileArticle(Connection connection, String urlFileArticle){
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "DELETE FROM IntegrantArticle WHERE urlFile = ?;"
            );
            sentenceQuery.setString(1, urlFileArticle);
            sentenceQuery.executeUpdate();
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                connection.close();
                Logger.getLogger(Article.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(GoalDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void deleteCollaboratorsFromURLFileArticle(Connection connection, String urlFileArticle){
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "DELETE FROM CollaborateArticle WHERE urlFile = ?;"
            );
            sentenceQuery.setString(1, urlFileArticle);
            sentenceQuery.executeUpdate();
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                connection.close();
                Logger.getLogger(Article.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(GoalDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void insertIntoStudentArticle(Connection connection, Article article){
        article.getStudents().forEach( student -> {
            try{
                PreparedStatement sentenceQuery = connection.prepareStatement(
                    "INSERT INTO ArticleStudent VALUES(?,?);"
                );
                sentenceQuery.setString(1, article.getUrlFile());
                sentenceQuery.setString(2, student);
                sentenceQuery.executeUpdate();
            }catch(SQLException sqlException){
                try{
                    connection.rollback();
                    connection.close();
                    Logger.getLogger(Article.class.getName()).log(Level.SEVERE, null, sqlException);
                }catch(SQLException ex){
                    Logger.getLogger(GoalDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    private void insertIntoIntegrantArticle(Connection connection, Article article){
        article.getIntegrants().forEach( integrant -> {
            try{
                PreparedStatement sentenceQuery = connection.prepareStatement(
                    "INSERT INTO IntegrantArticle VALUES(?,?);"
                );
                sentenceQuery.setString(1, integrant.getRfc());
                sentenceQuery.setString(2, article.getUrlFile());
                sentenceQuery.executeUpdate();
            }catch(SQLException sqlException){
                try{
                    connection.rollback();
                    connection.close();
                    Logger.getLogger(Article.class.getName()).log(Level.SEVERE, null, sqlException);
                }catch(SQLException ex){
                    Logger.getLogger(GoalDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    private void insertIntoCollaboratorArticle(Connection connection, Article article){
        article.getCollaborators().forEach( collaborator -> {
            try{
                PreparedStatement sentenceQuery = connection.prepareStatement(
                    "INSERT INTO CollaborateArticle VALUES(?,?);"
                );
                sentenceQuery.setString(1, collaborator.getRfc());
                sentenceQuery.setString(2, article.getUrlFile());
                sentenceQuery.executeUpdate();
            }catch(SQLException sqlException){
                try{
                    connection.rollback();
                    connection.close();
                    Logger.getLogger(Article.class.getName()).log(Level.SEVERE, null, sqlException);
                }catch(SQLException ex){
                    Logger.getLogger(GoalDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    private Article getOutArticleDataFromQuery(ResultSet resultArticleQuery){
        Article article = new Article();
        try{
            article = new Article(
                resultArticleQuery.getString("urlFile"),
                resultArticleQuery.getString("projectName"),
                resultArticleQuery.getBoolean("impactBA"),
                resultArticleQuery.getString("evidenceType"),
                resultArticleQuery.getString("evidenceTitle"),
                resultArticleQuery.getString("registrationResponsible"),
                resultArticleQuery.getString("registrationDate"),
                resultArticleQuery.getString("studyDegree"),
                resultArticleQuery.getString("publicationDate"),
                resultArticleQuery.getString("country"),
                resultArticleQuery.getString("isnn"),
                resultArticleQuery.getString("editorialName"),
                resultArticleQuery.getString("magazineName")
            );
        }catch(SQLException ex){
            Logger.getLogger(ArticleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            return article;
        }
    }
    
    private List<Integrant> getIntegrantArticleParticipation(Connection connection, String urlFileArticle){
        List<Integrant> integrants = new ArrayList<>();
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "SELECT i.fullName, i.rfc FROM Integrant i, IntegrantArticle ia WHERE ia.rfc = i.rfc AND urlFile = ?;"
            );
            sentenceQuery.setString(1, urlFileArticle);
            ResultSet resultQuery = sentenceQuery.executeQuery();
            while(resultQuery.next()){
                Integrant integrant = new Integrant();
                integrant.setFullName(resultQuery.getString("fullName"));
                integrant.setRfc(resultQuery.getString("rfc"));
                integrants.add(integrant);
            }
        }catch(SQLException ex){
            Logger.getLogger(ArticleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            return integrants;
        }
    }
    
    private List<Collaborator> getCollaboratorArticleParticipation(Connection connection, String urlFileArticle){
        List<Collaborator> collaborators = new ArrayList<>();
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "SELECT c.fullName, c.rfc FROM CollaborateArticle ca, Collaborator c WHERE ca.rfc = c.rfc AND urlFile = ?;"
            );
            sentenceQuery.setString(1, urlFileArticle);
            ResultSet resultQuery = sentenceQuery.executeQuery();
            while(resultQuery.next()){
                Collaborator collaborator = new Collaborator();
                collaborator.setFullName(resultQuery.getString("fullName"));
                collaborator.setRfc(resultQuery.getString("rfc"));
                collaborators.add(collaborator);
            }
        }catch(SQLException ex){
            Logger.getLogger(ArticleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            return collaborators;
        }
    }
    
    private List<String> getStudentsArticleParticipation(Connection connection, String urlFileArticle){
        List<String> students = new ArrayList<>();
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "SELECT * FROM ArticleStudent WHERE urlFile = ?;"
            );
            sentenceQuery.setString(1, urlFileArticle);
            ResultSet resultQuery = sentenceQuery.executeQuery();
            while(resultQuery.next()){
                students.add(resultQuery.getString("student"));
            }
        }catch(SQLException ex){
            Logger.getLogger(ArticleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            return students;
        }
    }

    @Override
    public String toString(){
        return "Artículo";
    }

    @Override
    public EvidenceDAO getEvidenceDaoInstance(String evidenceType){
        EvidenceDAO evidenceDao = null;
        if(this.toString().equalsIgnoreCase(evidenceType)){
            evidenceDao = new ArticleDAO();
        }
        return evidenceDao;
    }
    
}
