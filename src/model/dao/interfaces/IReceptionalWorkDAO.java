package model.dao.interfaces;

import javafx.collections.ObservableList;
import model.domain.ReceptionalWork;

import java.sql.SQLException;

public interface IReceptionalWorkDAO {

    ObservableList<ReceptionalWork> getReceptionalWorksList(String idAcademicBody) throws SQLException;

    public boolean addReceptionalWork(ReceptionalWork newReceptionalWork) throws SQLException;
    public int getIdReceptionalWork(String nameReceptionalWork) throws SQLException;
    public ReceptionalWork getReceptionalWorkDetails(int idReceptionalWork) throws SQLException;

    boolean checkReceptionalWork(String nameReceptionalWork) throws SQLException;
    boolean checkReceptionalWorkUpdated(String nameReceptionalWork, int idReceptionalWork) throws SQLException;

    public boolean updateReceptionalWork(ReceptionalWork receptionalWork) throws SQLException;
}
