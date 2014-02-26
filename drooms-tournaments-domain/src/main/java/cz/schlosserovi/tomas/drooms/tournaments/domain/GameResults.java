package cz.schlosserovi.tomas.drooms.tournaments.domain;

import java.util.Properties;
import java.util.UUID;

public class GameResults {
    private UUID gameId;
    private Properties results;

    public GameResults() {
    }

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public Properties getResults() {
        return results;
    }

    public void setResults(Properties results) {
        this.results = results;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("GameResults[gameId=").append(gameId).append(", results={");
        boolean first = true;
        for (String player : results.stringPropertyNames()) {
            if (!first) {
                builder.append(",");
            }
            builder.append(player).append("=").append(results.getProperty(player));
            first = false;
        }
        builder.append("}]");

        return builder.toString();
    }
}
