package org.drooms.tournaments.domain;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Playground implements Comparable<Playground> {
    // Keep these two in sync
    private static final Pattern TO_STRING_PATTERN = Pattern.compile("Playground\\[name='(.+)', maxPlayers='(\\d+)']");
    private static final String TO_STRING_FORMAT = "Playground[name='%s', maxPlayers='%s']";

    private String name;
    private String source;
    private int maxPlayers;
    private Properties configuration;

    public Playground() {
    }

    public Playground(String name, String source) {
        this.name = name;
        this.source = source;
    }

    public Playground(String name, String source, int maxPlayers, Properties configuration) {
        this.name = name;
        this.source = source;
        this.maxPlayers = maxPlayers;
        this.configuration = configuration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public Properties getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Properties configuration) {
        this.configuration = configuration;
    }

    public static Playground fromString(String playground) {
        Playground result = new Playground();

        Matcher m = TO_STRING_PATTERN.matcher(playground);
        m.matches();

        result.name = m.group(1);
        result.maxPlayers = Integer.valueOf(m.group(2));

        return result;
    }

    @Override
    public int compareTo(Playground o) {
        if (name == null || o.name == null) {
            throw new NullPointerException("Playground name is not set");
        }
        return name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return String.format(TO_STRING_FORMAT, name, maxPlayers);
    }
}
