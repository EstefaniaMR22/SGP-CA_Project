package model.domain;

public enum ParticipationType {
    COLABORATOR("COLABORADOR"),
    INTEGRANT("INTEGRANTE"),
    RESPONSABLE("RESPONSABLE"),
    OTHER("OTROS");

    private final String participationType;

    ParticipationType(String participationType) {
        this.participationType = participationType;
    }

    public String getParticipationType() {
        return participationType;
    }

    @Override
    public String toString() {
        return participationType.replaceAll("_", " ");
    }
}
