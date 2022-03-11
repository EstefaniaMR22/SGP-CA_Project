package model.domain;

public enum CivilStatus {
    SINGLE("SOLTERO"),
    MARRIED("CASADO"),
    DIVORCED("DIVORCIADO"),
    SEPARATION_IN_PROGRESS("SEPARACION_EN_PROCESO"),
    WIDOWER("VIUDO"),
    CONCUBINAGE("CONCUBINATO");

    private final String civilStatus;

    CivilStatus(String civilStatus) {
        this.civilStatus = civilStatus;
    }

    public String getCivilStatus() {
        return civilStatus;
    }

    @Override
    public String toString() {
        return civilStatus.replaceAll("_", " ");
    }
}
