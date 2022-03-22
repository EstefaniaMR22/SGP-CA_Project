/*
 * @author V Manuel Arrys
 * @versión v1.0
 * Last modification date: 15-03-2022
 */

package model.dao.interfaces;

import javafx.collections.ObservableList;
import model.domain.Project;

import java.sql.SQLException;
import java.util.List;

public interface IProjectDAO{

    public ObservableList<Project> getProjectList() throws SQLException;

    public boolean addProject(Project newProject) throws SQLException;
    public int addProjectLGAC(int idProject, int idLGAC) throws SQLException;
    public int getIdProject(String nameProject) throws SQLException;
    public Project getProjectDetails(int idProject) throws SQLException;

    boolean checkProject(String projectName) throws SQLException;

    public int getIdLGAC(int idProject) throws SQLException;
    public boolean updateProject(Project project) throws SQLException;
}