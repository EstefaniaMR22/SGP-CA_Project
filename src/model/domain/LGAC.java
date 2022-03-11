package model.domain;

public class LGAC {
    private int id;
    private String identification;
    private String description;

    public LGAC() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "LGAC{" +
                "id=" + id +
                ", identification='" + identification + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
