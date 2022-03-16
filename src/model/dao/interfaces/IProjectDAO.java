/*
 * @author V Manuel Arrys
 * @versión v1.0
 * Last modification date: 15-03-2022
 */

package model.dao.interfaces;

import model.domain.Project;

import java.util.List;

public interface IProjectDAO{

    public List<Project> getProjectList();

    public int addProject(Project newProject);
    public Project getProjectDetails(int idProject);
    public int updateProject(int idProject, Project project);
}