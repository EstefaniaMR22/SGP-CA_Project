package model.dao.interfaces;

import javafx.collections.ObservableList;
import model.domain.Meet;
import model.domain.Member;
import model.domain.Project;

import java.util.Date;
import java.sql.SQLException;

public interface IMeetDAO {
    ObservableList<Meet> getMeetList(String academicGroupID) throws SQLException;

    ObservableList<Member> getAsistentsMeetList(int idMeet) throws SQLException;

    boolean addMeet(Meet newMeet) throws SQLException;
    boolean addAsistentsMeet(int idMeet, ObservableList<Member> asistents) throws SQLException;

    int getIdMeet(Meet meetWithoutId) throws SQLException;

    Meet getMeetDetails(int idMeet) throws SQLException;

    boolean checkMeet(Date meetDate, String hourMeet) throws SQLException;

    boolean updateMeet(Meet updateMeet) throws SQLException;
}
