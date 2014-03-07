package cz.schlosserovi.tomas.drooms.tournaments.domain;

import java.util.Properties;

public class Playground {
    private String name;
    private String source;
    private int maxPlayers;
    private Properties configuration;

    public Playground() {
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

    @Override
    public String toString() {
        return String.format("Playground[name='%s', maxPlayers='%s']", name);
    }
}
