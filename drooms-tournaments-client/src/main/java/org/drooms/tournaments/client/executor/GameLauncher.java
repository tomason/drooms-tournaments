package org.drooms.tournaments.client.executor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.drooms.api.Player;
import org.drooms.api.Playground;
import org.drooms.impl.DefaultGame;
import org.drooms.tournaments.domain.GAV;
import org.drooms.tournaments.domain.Game;
import org.drooms.tournaments.domain.GameResult;
import org.kie.api.KieServices;
import org.kie.api.builder.ReleaseId;
import org.slf4j.LoggerFactory;

class GameLauncher {
    private final org.drooms.api.Game droomsGame = new DefaultGame();

    private final org.drooms.tournaments.domain.Game domainObject;
    private final Playground playground;
    private final List<Player> players;
    private final Map<Player, GameResult> playerMapping = new HashMap<>();

    public GameLauncher(org.drooms.tournaments.domain.Game game) {
        this.domainObject = game;
        setGameContext(getGameProperties(game));
        this.playground = buildPlayground(game.getPlayground().getSource());
        this.players = assemblePlayers(game.getResults());
    }

    public Game play() {
        Map<Player, Integer> gameResults = droomsGame.play(playground, players, null);

        for (Entry<Player, Integer> entry : gameResults.entrySet()) {
            playerMapping.get(entry.getKey()).setPoints(entry.getValue());
        }

        // extract game report
        StringWriter writer = new StringWriter();
        try {
            droomsGame.getReport().write(writer);
            domainObject.setGameReport(writer.toString());
        } catch (IOException ex) {
            LoggerFactory.getLogger(GameLauncher.class).error("Unable to export game report", ex);
        }

        // extract game log
        Path log = Paths.get("drooms.log");
        try {
            StringBuilder sb = new StringBuilder();
            for (String line : Files.readAllLines(log, StandardCharsets.UTF_8)) {
                sb.append(line).append("\n");
            }
            domainObject.setGameLog(sb.toString());
            Files.deleteIfExists(log);
            Files.createFile(log);
        } catch (IOException ex) {
            LoggerFactory.getLogger(GameLauncher.class).error("Unable to read game log", ex);
        }

        return domainObject;
    }

    private Playground buildPlayground(String playgroundSource) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(playgroundSource.getBytes(Charset.defaultCharset()))) {
            return droomsGame.buildPlayground("dummy", bais);
        } catch (IOException e) {
            throw new RuntimeException("Error closing playground stream", e);
        }
    }

    private List<Player> assemblePlayers(Collection<GameResult> resultCollection) {
        List<Player> players = new LinkedList<>();

        KieServices ks = KieServices.Factory.get();
        for (GameResult result : resultCollection) {
            String name = result.getStrategy().getPlayer().getName();
            GAV gav = result.getStrategy().getGav();
            ReleaseId strategy = ks.newReleaseId(gav.getGroupId(), gav.getArtifactId(), gav.getVersion());
            Player player = new Player(name, strategy);

            players.add(player);
            playerMapping.put(player, result);
        }

        Collections.shuffle(players, new SecureRandom());
        return Collections.unmodifiableList(players);
    }

    private void setGameContext(Properties gameProperties) {
        if (gameProperties == null) {
            gameProperties = new Properties();
            gameProperties.setProperty("collectibles", "default");
            gameProperties.setProperty("collectible.probability.default", "0.1");
            gameProperties.setProperty("collectible.expiration.default", "45");
            gameProperties.setProperty("collectible.price.default", "10");
        }
        StringWriter writer = new StringWriter();
        try {
            gameProperties.store(writer, null);
            try (ByteArrayInputStream bais = new ByteArrayInputStream(writer.toString().getBytes(Charset.defaultCharset()))) {
                droomsGame.setContext(bais);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Unable to set game context", ex);
        }
    }

    private Properties getGameProperties(Game game) {
        Properties result = new Properties();
        for (Entry<String, String> entry : game.getPlayground().getConfiguration().entrySet()) {
            result.setProperty(entry.getKey(), entry.getValue());
        }

        return result;
    }
}
