package cz.schlosserovi.tomas.drooms.tournaments.domain;

public class User {
    private String name;
    private byte[] password;

    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    public User(String name, byte[] password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format("User[name='%s']", name);
    }
}
