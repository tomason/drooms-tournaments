package cz.schlosserovi.tomas.drooms.tournaments.domain;


import java.util.Properties;
import java.util.UUID;

public class GameSetup {
    private UUID gameId;
    private String className;
    private String playground;
    private Properties players;
    private Properties gameConfig;

    public GameSetup() {
    }

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPlayground() {
        return playground;
    }

    public void setPlayground(String playground) {
        this.playground = playground;
    }

    public Properties getPlayers() {
        return players;
    }

    public void setPlayers(Properties players) {
        this.players = players;
    }

    public Properties getGameConfig() {
        return gameConfig;
    }

    public void setGameConfig(Properties gameConfig) {
        this.gameConfig = gameConfig;
    }

}
