package cz.schlosserovi.tomas.drooms.tournaments.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
    // Keep these two in sync
    private static final String TO_STRING_FORMAT = "User[name='%s']";
    private static final Pattern TO_STRING_PATTERN = Pattern.compile("User\\[name='(.+)'\\]");

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

    public static User fromString(String user) {
        User result = new User();

        Matcher m = TO_STRING_PATTERN.matcher(user);
        m.matches();

        result.name = m.group(1);

        return result;
    }

    @Override
    public String toString() {
        return String.format(TO_STRING_FORMAT, name);
    }
}
