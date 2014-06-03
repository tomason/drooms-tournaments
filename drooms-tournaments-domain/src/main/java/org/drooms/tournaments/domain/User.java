package org.drooms.tournaments.domain;

public class User implements Comparable<User> {
    private String name;
    private String password;

    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int compareTo(User o) {
        if (name == null || o.name == null) {
            throw new NullPointerException("User name must not be null");
        }

        return name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return new StringBuilder("User[name='").append(name).append("']").toString();
    }
}
