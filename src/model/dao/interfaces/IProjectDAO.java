/*
 * @author V Manuel Arrys
 * @versión v1.0
 * Last modification date: 15-03-2022
 */

package model.dao.interfaces;

import javafx.collections.ObservableList;
import model.domain.Project;

import java.sql.SQLException;

public interface IProjectDAO{

    ObservableList<Project> getProjectList(String academicGroupID) throws SQLException;

    boolean addProject(Project newProject) throws SQLException;
    int addProjectLGAC(int idProject, int idLGAC) throws SQLException;
    int getIdProject(String nameProject) throws SQLException;
    Project getProjectDetails(int idProject) throws SQLException;

    boolean checkProject(String projectName) throws SQLException;
    boolean checkProjectUpdated(String projectName, int idProject) throws SQLException;

    int getIdLGAC(int idProject) throws SQLException;
    boolean updateProject(Project project) throws SQLException;
}