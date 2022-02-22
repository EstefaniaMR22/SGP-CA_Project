/*
* @author Johann
* @versión v1.0
* Last modification date: 17-06-2021
*/

package model.dataaccess;

import java.util.List;
import model.domain.Project;

public interface IProjectDAO{
    public List<Project> getProjectList();
    public void addProject(Project newProject);
    public Project getProjectbyName(String projectName);
    public boolean updateProject(Project project, String oldProjectName);
    public boolean projectRegistered(String projectName);
}