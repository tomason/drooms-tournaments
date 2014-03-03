package cz.schlosserovi.tomas.drooms.tournaments.domain;


import java.util.Properties;
import java.util.UUID;

public class ExecutionSetup {
    private UUID gameId;
    private String className;
    private String playground;
    private Properties players;
    private Properties gameConfig;

    public ExecutionSetup() {
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GameSetup[gameId='").append(gameId).append("', ");
        sb.append("className='").append(className).append("', players={");
        boolean first = true;
        for (String key : players.stringPropertyNames()) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(key).append("=").append(players.getProperty(key));
        }
        sb.append("}, gameConfig={");
        first = true;
        for (String key : gameConfig.stringPropertyNames()) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(key).append("=").append(gameConfig.getProperty(key));
        }
        sb.append("}, playground='").append(playground).append("']");

        return sb.toString();
    }
}
