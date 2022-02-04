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
import sgp.ca.domain.Book;
import sgp.ca.domain.ChapterBook;
import sgp.ca.domain.Collaborator;
import sgp.ca.domain.Integrant;

public class ChapterBookDAO implements IChapterBookDAO{
    private final ConnectionDatabase CONNECTION = new ConnectionDatabase();
    
    @Override
    public List<ChapterBook> getChapterBooksListByBook(String urlFileBook) {
        List<ChapterBook> chapterBooksList = new ArrayList();
        try{
            PreparedStatement sentenceQuery = CONNECTION.getConnectionDatabase().prepareStatement(
                "SELECT * FROM ChapterBook WHERE urlFileBook = ?;"
            );
            sentenceQuery.setString(1, urlFileBook);
            ResultSet resultQuery = sentenceQuery.executeQuery();
            while(resultQuery.next()){
                ChapterBook chapterBook = new ChapterBook();
                chapterBook.setUrlFile(resultQuery.getString("urlFile"));
                chapterBook.setChapterBookTitle(resultQuery.getString("chapterBookTitle"));
                chapterBook.setPageNumberRange(resultQuery.getString("pagesNumber"));
                chapterBook.setUrlFileBook(resultQuery.getString("urlFileBook"));
                chapterBooksList.add(chapterBook);
            }
        }catch(SQLException sqlException){
            chapterBooksList = null;
            Logger.getLogger(ChapterBookDAO.class.getName()).log(Level.SEVERE, null, sqlException);
        }finally{
            return chapterBooksList;
        }
    }
    
    @Override
    public ChapterBook getChapterBookByURLFile(String urlFileChapterBook){
        ChapterBook chapterBook = new ChapterBook();
        Connection connection = CONNECTION.getConnectionDatabaseNotAutoCommit();
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "SELECT * FROM ChapterBook WHERE urlFile = ?;"
            );
            sentenceQuery.setString(1, urlFileChapterBook);
            ResultSet resultQuery = sentenceQuery.executeQuery();
            if(resultQuery.next()){
                chapterBook = new ChapterBook(
                    resultQuery.getString("urlFile"),
                    resultQuery.getString("chapterBookTitle"),
                    resultQuery.getString("registrationDate"),
                    resultQuery.getString("registrationResponsible"),
                    resultQuery.getString("pagesNumber"),
                    resultQuery.getString("urlFileBook")
                );
                chapterBook.setStudents(this.getStudentNamesChapterBookParticipation(connection, urlFileChapterBook));
                chapterBook.setIntegrants(this.getIntegrantsChapterBookParticipation(connection, urlFileChapterBook));
                chapterBook.setCollaborators(this.getCollaboratorsChapterBookParticipation(connection, urlFileChapterBook));
                
            }
            connection.commit();
            connection.setAutoCommit(true);
        }catch(SQLException sqlException){
            chapterBook = null;
            Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, sqlException);
        }finally{
            CONNECTION.closeConnection();
            return chapterBook;
        }
    }
    
    @Override
    public boolean addChapterBook(ChapterBook chapterBook, Book book){
        Connection connection = CONNECTION.getConnectionDatabaseNotAutoCommit();
        boolean correctInsertion = false;
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "INSERT INTO ChapterBook VALUES (?,?,?,?,?,?);"
            );
            sentenceQuery.setString(1, chapterBook.getUrlFile());
            sentenceQuery.setString(2, chapterBook.getChapterBookTitle());
            sentenceQuery.setString(3, chapterBook.getRegistrationDate());
            sentenceQuery.setString(4, chapterBook.getRegistrationResponsible());
            sentenceQuery.setString(5, chapterBook.getPageNumberRange());
            sentenceQuery.setString(6, book.getUrlFile());
            sentenceQuery.executeUpdate();
            this.insertIntoChapterbookStudent(connection, chapterBook);
            this.insertIntoIntegrantChapterbook(connection, chapterBook);
            this.insertIntoCollaborateChapterbook(connection, chapterBook);
            connection.commit();
            connection.setAutoCommit(true);
            correctInsertion = true;
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                Logger.getLogger(ChapterBookDAO.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(ChapterBookDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }finally{
            CONNECTION.closeConnection();
            return correctInsertion;
        }
    }

    @Override
    public boolean updateChepterBook(ChapterBook newChapterBook, String oldUrlFile){
         Connection connection = CONNECTION.getConnectionDatabaseNotAutoCommit();
        boolean correctUpdate = false;
        try{
            this.deleteStudentsFromChapterBook(connection, oldUrlFile);
            this.deleteIntegrantsFromChapterBook(connection, oldUrlFile);
            this.deleteCollaboratorsFromChapterBook(connection, oldUrlFile);
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "UPDATE ChapterBook SET urlFile = ?, chapterBookTitle = ?, registrationDate = ?, "
                + "registrationResponsible = ?, pagesNumber = ?, urlFileBook = ?"
                + "WHERE urlFile = ?;"
            );
            sentenceQuery.setString(1, newChapterBook.getUrlFile());
            sentenceQuery.setString(2, newChapterBook.getChapterBookTitle());
            sentenceQuery.setString(3, newChapterBook.getRegistrationDate());
            sentenceQuery.setString(4, newChapterBook.getRegistrationResponsible());
            sentenceQuery.setString(5, newChapterBook.getPageNumberRange());
            sentenceQuery.setString(6, newChapterBook.getUrlFileBook());
            sentenceQuery.setString(7, oldUrlFile);
            sentenceQuery.executeUpdate();
            this.insertIntoChapterbookStudent(connection, newChapterBook);
            this.insertIntoIntegrantChapterbook(connection, newChapterBook);
            this.insertIntoCollaborateChapterbook(connection, newChapterBook);
            connection.commit();
            connection.setAutoCommit(true);
            correctUpdate = true;
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                Logger.getLogger(ChapterBookDAO.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(ChapterBookDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }finally{
            CONNECTION.closeConnection();
            return correctUpdate;
        }
    }

    @Override
    public boolean deleteChapterBook(String urlFileChapterBook){
        Connection connection = CONNECTION.getConnectionDatabaseNotAutoCommit();
        boolean correctDelete = false;
        try{
            this.deleteStudentsFromChapterBook(connection, urlFileChapterBook);
            this.deleteIntegrantsFromChapterBook(connection, urlFileChapterBook);
            this.deleteCollaboratorsFromChapterBook(connection, urlFileChapterBook);
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "DELETE FROM ChapterBook WHERE urlFile = ?;"
            );
            sentenceQuery.setString(1, urlFileChapterBook);
            sentenceQuery.executeQuery();
            connection.commit();
            connection.setAutoCommit(true);
            correctDelete = true;
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                connection.close();
                Logger.getLogger(ChapterBookDAO.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(ChapterBookDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }finally{
            CONNECTION.closeConnection();
            return correctDelete;
        }
    }

    private void deleteStudentsFromChapterBook(Connection connection, String urlFileChapterBook){
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "DELETE FROM ChapterbookStudent WHERE urlFile = ?;"
            );
            sentenceQuery.setString(1, urlFileChapterBook);
            sentenceQuery.executeUpdate();
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                connection.close();
                Logger.getLogger(ChapterBookDAO.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(ChapterBookDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void deleteIntegrantsFromChapterBook(Connection connection, String urlFileChapterBook){
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "DELETE FROM IntegrantChapterbook WHERE urlFile = ?;"
            );
            sentenceQuery.setString(1, urlFileChapterBook);
            sentenceQuery.executeUpdate();
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                connection.close();
                Logger.getLogger(ChapterBookDAO.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(ChapterBookDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void deleteCollaboratorsFromChapterBook(Connection connection, String urlFileChapterBook){
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "DELETE FROM CollaborateChapterbook WHERE urlFile = ?;"
            );
            sentenceQuery.setString(1, urlFileChapterBook);
            sentenceQuery.executeUpdate();
        }catch(SQLException sqlException){
            try{
                connection.rollback();
                connection.close();
                Logger.getLogger(ChapterBookDAO.class.getName()).log(Level.SEVERE, null, sqlException);
            }catch(SQLException ex){
                Logger.getLogger(ChapterBookDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void insertIntoChapterbookStudent(Connection connection, ChapterBook chapterBook){
        chapterBook.getStudents().forEach( student -> {
            try{
                PreparedStatement sentenceQuery = connection.prepareStatement(
                    "INSERT INTO ChapterbookStudent VALUES(?,?);"
                );
                sentenceQuery.setString(1, chapterBook.getUrlFile());
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
    
    private void insertIntoIntegrantChapterbook(Connection connection, ChapterBook chapterBook){
        chapterBook.getIntegrants().forEach( integrant -> {
            try{
                PreparedStatement sentenceQuery = connection.prepareStatement(
                    "INSERT INTO IntegrantChapterbook VALUES(?,?);"
                );
                sentenceQuery.setString(1, integrant.getRfc());
                sentenceQuery.setString(2, chapterBook.getUrlFile());
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
    
    private void insertIntoCollaborateChapterbook(Connection connection, ChapterBook chapterBook){
        chapterBook.getCollaborators().forEach( collaborator -> {
            try{
                PreparedStatement sentenceQuery = connection.prepareStatement(
                    "INSERT INTO CollaborateChapterbook VALUES(?,?);"
                );
                sentenceQuery.setString(1, collaborator.getRfc());
                sentenceQuery.setString(2, chapterBook.getUrlFile());
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
    
    private List<String> getStudentNamesChapterBookParticipation(Connection connection, String urlFileChapterBook){
        List<String> students = new ArrayList<>();
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "SELECT * FROM ChapterbookStudent WHERE urlFile = ?;"
            );
            sentenceQuery.setString(1, urlFileChapterBook);
            ResultSet resultQuery = sentenceQuery.executeQuery();
            while(resultQuery.next()){
                students.add(resultQuery.getString("student"));
            }
        }catch(SQLException ex){
            Logger.getLogger(ChapterBookDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            return students;
        }
    }
    
    private List<Integrant> getIntegrantsChapterBookParticipation(Connection connection, String urlFileChapterBook){
        List<Integrant> integrants = new ArrayList<>();
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "SELECT i.fullName, i.rfc FROM Integrant i, IntegrantChapterbook ia WHERE ia.rfc = i.rfc AND urlFile = ?;"
            );
            sentenceQuery.setString(1, urlFileChapterBook);
            ResultSet resultQuery = sentenceQuery.executeQuery();
            while(resultQuery.next()){
                Integrant integrant = new Integrant();
                integrant.setFullName(resultQuery.getString("fullName"));
                integrant.setRfc(resultQuery.getString("rfc"));
                integrants.add(integrant);
            }
        }catch(SQLException ex){
            Logger.getLogger(ChapterBookDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            return integrants;
        }
    }
    
    private List<Collaborator> getCollaboratorsChapterBookParticipation(Connection connection, String urlFileChapterBook){
        List<Collaborator> collaborators = new ArrayList<>();
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "SELECT c.fullName, c.rfc FROM CollaborateChapterbook ca, Collaborator c WHERE ca.rfc = c.rfc AND urlFile = ?;"
            );
            sentenceQuery.setString(1, urlFileChapterBook);
            ResultSet resultQuery = sentenceQuery.executeQuery();
            while(resultQuery.next()){
                Collaborator collaborator = new Collaborator();
                collaborator.setFullName(resultQuery.getString("fullName"));
                collaborator.setRfc(resultQuery.getString("rfc"));
                collaborators.add(collaborator);
            }
        }catch(SQLException ex){
            Logger.getLogger(ChapterBookDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            return collaborators;
        }
    }
}