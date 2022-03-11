package model.domain;

public enum ConsolidationGrade {
    CONSOLIDATED("CONSOLIDADO"),
    IN_CONSOLIDATION("EN_CONSOLIDACION"),
    IN_FORMATION("EN_FORMACION");

    private final String consolidationGrade;

    ConsolidationGrade(String consolidationGrade) {
        this.consolidationGrade = consolidationGrade;
    }

    public String getConsolidationGrade() {
        return consolidationGrade;
    }

    @Override
    public String toString() {
        return consolidationGrade.replaceAll("_", " ");
    }
}
