/**
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package sgp.ca.domain;

import java.util.ArrayList;
import java.util.List;

public class ChapterBook{
    private String urlFile;
    private String chapterBookTitle;
    private String registrationDate;
    private String registrationResponsible;
    private String pageNumberRange;
    private String urlFileBook;
    private List<String> students;
    private List<Integrant> integrants;
    private List<Collaborator> collaborators;

    public ChapterBook(String urlFile, String chapterBookTitle, String registrationDate, 
    String registrationResponsible, String pageNumberRange, String urlFileBook){
        this.urlFile = urlFile;
        this.chapterBookTitle = chapterBookTitle;
        this.registrationDate = registrationDate;
        this.registrationResponsible = registrationResponsible;
        this.pageNumberRange = pageNumberRange;
        this.urlFileBook = urlFileBook;
        this.students = new ArrayList<>();
        this.integrants = new ArrayList<>();
        this.collaborators = new ArrayList<>();
    }

    public ChapterBook(){
        this.students = new ArrayList<>();
        this.integrants = new ArrayList<>();
        this.collaborators = new ArrayList<>();
    }

    public String getUrlFile() {
        return urlFile;
    }

    public void setUrlFile(String urlFile) {
        this.urlFile = urlFile;
    }

    public String getChapterBookTitle() {
        return chapterBookTitle;
    }

    public void setChapterBookTitle(String chapterBookTitle) {
        this.chapterBookTitle = chapterBookTitle;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getRegistrationResponsible() {
        return registrationResponsible;
    }

    public void setRegistrationResponsible(String registrationResponsible) {
        this.registrationResponsible = registrationResponsible;
    }

    public String getPageNumberRange() {
        return pageNumberRange;
    }

    public void setPageNumberRange(String pageNumberRange) {
        this.pageNumberRange = pageNumberRange;
    }

    public String getUrlFileBook() {
        return urlFileBook;
    }

    public void setUrlFileBook(String urlFileBook) {
        this.urlFileBook = urlFileBook;
    }

    public List<String> getStudents() {
        return students;
    }

    public void setStudents(List<String> students) {
        this.students = students;
    }

    public List<Integrant> getIntegrants() {
        return integrants;
    }

    public void setIntegrants(List<Integrant> integrants) {
        this.integrants = integrants;
    }

    public List<Collaborator> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<Collaborator> collaborators) {
        this.collaborators = collaborators;
    }
}
