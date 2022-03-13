package model.domain;

public enum StudyGrade {
    PRESCHOOL_EDUCATION("EDUCACION_PREESCOLAR"),
    PRIMARY_EDUCATION("EDUCACION_PRIMARIA"),
    SECONDARY_EDUCATION("EDUCACION_SECUNDARIA"),
    MID_SUPERIOR_EDUCATION("EDUCACION_MEDIA_SUPERIOR"),
    SUPERIOR_EDUCATION("EDUCACION_SUPERIOR"),
    ;

    private final String studyGrade;

    StudyGrade(String studyGrade) {
        this.studyGrade = studyGrade;
    }

    public String getStudyGrade() {
        return studyGrade;
    }
}
