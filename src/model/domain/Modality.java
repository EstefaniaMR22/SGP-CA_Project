package model.domain;

public enum Modality {
    TESIS("TESIS"),
    TESINA("TESINA"),
    MONOGRAFIA("MONOGRAFIA"),
    MEMORIA("MEMORIA"),
    ESTUDIO_DE_CASO("ESTUDIO_DE_CASO"),
    ESTUDIO_ETNOMUSICOLOGICO("ESTUDIO_ETNOMUSICOLOGICO"),
    PROYECTO_DE_INTERVENCION_PEDAGOGICA("PROYECTO_DE_INTERVENCION_PEDAGOGICA"),
    PROYECTO_DE_INNOVACION_PEDAGOGICA("PROYECTO_DE_INNOVACION_PEDAGOGICA"),
    PROYECTO_DE_INNOVACION_DE_LA_GESTION_DE_LA_DOCENCIA("PROYECTO_DE_INNOVACION_DE_LA_GESTION_DE_LA_DOCENCIA"),
    TRABAJO_ARTISTICO_EDUCATIVO("TRABAJO_ARTISTICO_EDUCATIVO");

    private final String modality;

    Modality(String modality) {
        this.modality = modality;
    }

    public String getModality() {
        return modality;
    }

    @Override
    public String toString() {
        return "| " + modality + " |";
    }

}
