package model.domain;

public class Colaborator extends Member{
    private String studyArea;
    private StudyGrade maxStudyGrade;

    public Colaborator() {
    }

    public String getStudyArea() {
        return studyArea;
    }

    public void setStudyArea(String studyArea) {
        this.studyArea = studyArea;
    }

    public StudyGrade getMaxStudyGrade() {
        return maxStudyGrade;
    }

    public void setMaxStudyGrade(StudyGrade maxStudyGrade) {
        this.maxStudyGrade = maxStudyGrade;
    }

    @Override
    public String toString() {
        return "Colaborator{" +
                "studyArea='" + studyArea + '\'' +
                ", maxStudyGrade=" + maxStudyGrade +
                '}';
    }
}
