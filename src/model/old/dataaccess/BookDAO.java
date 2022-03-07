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

import model.old.domain.Book;
import model.old.domain.Collaborator;
import model.old.domain.Evidence;
import model.old.domain.Integrant;
import model.old.ConnectionDatabase;

public class BookDAO extends EvidenceDAO {
    private final ConnectionDatabase CONNECTION = new ConnectionDatabase();

    @Override
    public Evidence getEvidenceByUrl(String urlEvidenceFile){
        Evidence book = new Book();
        Connection connection = CONNECTION.getConnectionDatabaseNotAutoCommit();
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "SELECT * FROM Book WHERE urlFile = ?;"
            );
            sentenceQuery.setString(1, urlEvidenceFile);
            ResultSet resultQuery = sentenceQuery.executeQuery();
            if(resultQuery.next()){
                book = this.getoutBookDataFromQuery(resultQuery);
                book.setIntegrants(this.getIntegrantsBookParticipation(connection, urlEvidenceFile));
                book.setCollaborators(this.getCollaboratorsBookParticipation(connection, urlEvidenceFile));
                book.setStudents(this.getStudentNamesBookParticipation(connection, urlEvidenceFile));
            }
            connection.commit();
            connection.setAutoCommit(true);
        }catch(SQLException sqlException){
            Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, sqlException);
        }finally{
            CONNECTION.closeConnection();
            return book;
        }
    }
    
    public List<Book> getBooksByIntegrant(String rfc){
        List<Book> booksList = new ArrayList<>();
        try{
            PreparedStatement sentenceQuery = CONNECTION.getConnectionDatabase().prepareStatement(
                "SELECT urlFile, evidenceTitle, editionsNumber FROM `Book` WHERE registrationResponsible = ?;"
            );
            sentenceQuery.setString(1, rfc);
            ResultSet result = sentenceQuery.executeQuery();
            while(result.next()){
                Book book = new Book();
                book.setUrlFile(result.getString("urlFile"));
                book.setEvidenceTitle(result.getString("evidenceTitle"));
                book.setEditionsNumber(result.getInt("editionsNumber"));
                booksList.add(book);
            }
        }catch(SQLException sqlException){
            booksList = null;
            Logger.getLogger(Integrant.class.getName()).log(Level.SEVERE, null, sqlException);
        }finally{
            CONNECTION.closeConnection();
            return booksList;
        }
    }

    @Override
    public boolean addNewEvidence(Evidence evidence){
        Connection connection = CONNECTION.getConnectionDatabaseNotAutoCommit();
        boolean correctInsertion = false;
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "INSERT INTO Book VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);"
            );
            sentenceQuery.setString(1, evidence.getUrlFile());
            sentenceQuery.setString(2, evidence.getProjectName());
            sentenceQuery.setBoolean(3, evidence.getImpactAB());
            sentenceQuery.setString(4, "Libro");
            sentenceQuery.setString(5, evidence.getEvidenceTitle());
            sentenceQuery.setString(6, evidence.getRegistrationResponsible());
            sentenceQuery.setString(7, evidence.getRegistrationDate());
            sentenceQuery.setString(8, evidence.getStudyDegree());
            sentenceQuery.setString(9, evidence.getPublicationDate());
            sentenceQuery.setString(10, evidence.getCountry());
            sentenceQuery.setString(11, ((Book)evidence).getPublisher());
            sentenceQuery.setInt(12, ((Book)evidence).getEditionsNumber());
            sentenceQuery.setString(13, ((Book)evidence).getIsbn());
            sentenceQuery.executeUpdate();
            this.insertIntoStudentBook(connection, (Book)evidence);
            this.insertIntoIntegrantBook(connection, (Book)evidence);
            this.insertIntoCollaboratorBook(connection, (Book)evidence);
            connection.commit();
            connection.setAutoCommit(true);
            correctInsertion = true;
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex);
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
            this.deleteStudentsFromURLFileBook(connection, oldUrlFile);
            this.deleteIntegrantsFromURLFileBook(connection, oldUrlFile);
            this.deleteCollaboratorsFromURLFileBook(connection, oldUrlFile);
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "UPDATE Book SET urlFile = ?, projectName = ?, impactBA = ?, "
                + "evidenceType = ?, evidenceTitle = ?, registrationResponsible = ?,"
                + " registrationDate = ?, studyDegree = ?, publicationDate = ?, "
                + "country = ?, publisher = ?, editionsNumber = ?, isbn = ? "
                + "WHERE urlFile = ?;"
            );
            sentenceQuery.setString(1, evidence.getUrlFile());
            sentenceQuery.setString(2, evidence.getProjectName());
            sentenceQuery.setBoolean(3, evidence.getImpactAB());
            sentenceQuery.setString(4, "Libro");
            sentenceQuery.setString(5, evidence.getEvidenceTitle());
            sentenceQuery.setString(6, evidence.getRegistrationResponsible());
            sentenceQuery.setString(7, evidence.getRegistrationDate());
            sentenceQuery.setString(8, evidence.getStudyDegree());
            sentenceQuery.setString(9, evidence.getPublicationDate());
            sentenceQuery.setString(10, evidence.getCountry());
            sentenceQuery.setString(11, ((Book)evidence).getPublisher());
            sentenceQuery.setInt(12, ((Book)evidence).getEditionsNumber());
            sentenceQuery.setString(13, ((Book)evidence).getIsbn());
            sentenceQuery.setString(14, oldUrlFile);
            sentenceQuery.executeUpdate();
            this.insertIntoStudentBook(connection, (Book)evidence);
            this.insertIntoIntegrantBook(connection, (Book)evidence);
            this.insertIntoCollaboratorBook(connection, (Book)evidence);
            connection.commit();
            connection.setAutoCommit(true);
            correctUpdate = true;
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex);
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
            this.deleteStudentsFromURLFileBook(connection, urlEvidenceFile);
            this.deleteIntegrantsFromURLFileBook(connection, urlEvidenceFile);
            this.deleteCollaboratorsFromURLFileBook(connection, urlEvidenceFile);
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "DELETE FROM Book WHERE urlFile = ?;"
            );
            sentenceQuery.setString(1, urlEvidenceFile);
            sentenceQuery.executeQuery();
            connection.commit();
            connection.setAutoCommit(true);
            correctDelete = true;
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                connection.close();
                Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }finally{
            CONNECTION.closeConnection();
            return correctDelete;
        }
    }
    
    private void deleteStudentsFromURLFileBook(Connection connection, String urlFileBook){
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "DELETE FROM BookStudent WHERE urlFile = ?;"
            );
            sentenceQuery.setString(1, urlFileBook);
            sentenceQuery.executeUpdate();
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                connection.close();
                Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void deleteIntegrantsFromURLFileBook(Connection connection, String urlFileBook){
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "DELETE FROM IntegrantBook WHERE urlFile = ?;"
            );
            sentenceQuery.setString(1, urlFileBook);
            sentenceQuery.executeUpdate();
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                connection.close();
                Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void deleteCollaboratorsFromURLFileBook(Connection connection, String urlFileBook){
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "DELETE FROM CollaborateBook WHERE urlFile = ?;"
            );
            sentenceQuery.setString(1, urlFileBook);
            sentenceQuery.executeUpdate();
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                connection.close();
                Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void insertIntoStudentBook(Connection connection, Book book){
        book.getStudents().forEach( student -> {
            try{
                PreparedStatement sentenceQuery = connection.prepareStatement(
                    "INSERT INTO BookStudent VALUES(?,?);"
                );
                sentenceQuery.setString(1, book.getUrlFile());
                sentenceQuery.setString(2, student);
                sentenceQuery.executeUpdate();
            }catch(SQLException sqlException){
                try{
                    connection.rollback();
                    connection.close();
                    Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, sqlException);
                }catch(SQLException ex){
                    Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    
    
    private void insertIntoIntegrantBook(Connection connection, Book book){
        book.getIntegrants().forEach( integrant -> {
            try{
                PreparedStatement sentenceQuery = connection.prepareStatement(
                    "INSERT INTO IntegrantBook VALUES(?,?);"
                );
                sentenceQuery.setString(1, integrant.getRfc());
                sentenceQuery.setString(2, book.getUrlFile());
                sentenceQuery.executeUpdate();
            }catch(SQLException sqlException){
                try{
                    connection.rollback();
                    connection.close();
                    Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, sqlException);
                }catch(SQLException ex){
                    Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    private void insertIntoCollaboratorBook(Connection connection, Book book){
        book.getCollaborators().forEach( collaborator -> {
            try{
                PreparedStatement sentenceQuery = connection.prepareStatement(
                    "INSERT INTO CollaborateBook VALUES(?,?);"
                );
                sentenceQuery.setString(1, collaborator.getRfc());
                sentenceQuery.setString(2, book.getUrlFile());
                sentenceQuery.executeUpdate();
            }catch(SQLException sqlException){
                try{
                    connection.rollback();
                    connection.close();
                    Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, sqlException);
                }catch(SQLException ex){
                    Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    private Book getoutBookDataFromQuery(ResultSet resultBookQuery){
        Book book = new Book();
        try{
            book = new Book(
                resultBookQuery.getString("urlFile"),
                resultBookQuery.getString("projectName"),
                resultBookQuery.getBoolean("impactBA"),
                resultBookQuery.getString("evidenceType"),
                resultBookQuery.getString("evidenceTitle"),
                resultBookQuery.getString("registrationResponsible"),
                resultBookQuery.getDate("registrationDate").toString(),
                resultBookQuery.getString("studyDegree"),
                resultBookQuery.getDate("publicationDate").toString(),
                resultBookQuery.getString("country"),
                resultBookQuery.getString("publisher"),
                resultBookQuery.getInt("editionsNumber"),
                resultBookQuery.getString("isbn")
            );
        }catch(SQLException sqlException){
            Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, sqlException);
        }finally{
            return book;
        }
    }
    
    private List<Integrant> getIntegrantsBookParticipation(Connection connection, String urlFileBook){
        List<Integrant> integrants = new ArrayList<>();
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "SELECT i.fullName, i.rfc FROM Integrant i, IntegrantBook ia WHERE ia.rfc = i.rfc AND urlFile = ?;"
            );
            sentenceQuery.setString(1, urlFileBook);
            ResultSet resultQuery = sentenceQuery.executeQuery();
            while(resultQuery.next()){
                Integrant integrant = new Integrant();
                integrant.setFullName(resultQuery.getString("fullName"));
                integrant.setRfc(resultQuery.getString("rfc"));
                integrants.add(integrant);
            }
        }catch(SQLException ex){
            Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            return integrants;
        }
    }
    
    private List<Collaborator> getCollaboratorsBookParticipation(Connection connection, String urlFileBook){
        List<Collaborator> collaborators = new ArrayList<>();
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "SELECT c.fullName, c.rfc FROM CollaborateBook ca, Collaborator c WHERE ca.rfc = c.rfc AND urlFile = ?;"
            );
            sentenceQuery.setString(1, urlFileBook);
            ResultSet resultQuery = sentenceQuery.executeQuery();
            while(resultQuery.next()){
                Collaborator collaborator = new Collaborator();
                collaborator.setFullName(resultQuery.getString("fullName"));
                collaborator.setRfc(resultQuery.getString("rfc"));
                collaborators.add(collaborator);
            }
        }catch(SQLException ex){
            Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            return collaborators;
        }
    }
    
    private List<String> getStudentNamesBookParticipation(Connection connection, String urlFileBook){
        List<String> students = new ArrayList<>();
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "SELECT * FROM BookStudent WHERE urlFile = ?;"
            );
            sentenceQuery.setString(1, urlFileBook);
            ResultSet resultQuery = sentenceQuery.executeQuery();
            while(resultQuery.next()){
                students.add(resultQuery.getString("student"));
            }
        }catch(SQLException ex){
            Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            return students;
        }
    }

    @Override
    public String toString() {
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