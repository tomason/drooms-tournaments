package cz.schlosserovi.tomas.drooms.tournaments.domain;

public class Playground {
    private String name;

    public Playground() {
    }

    public Playground(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Playground[name='%s']", name);
    }
}
