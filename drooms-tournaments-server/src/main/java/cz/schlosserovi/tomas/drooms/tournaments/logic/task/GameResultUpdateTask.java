package cz.schlosserovi.tomas.drooms.tournaments.logic.task;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import cz.schlosserovi.tomas.drooms.tournaments.data.GameDAO;
import cz.schlosserovi.tomas.drooms.tournaments.data.TournamentResultDAO;
import cz.schlosserovi.tomas.drooms.tournaments.events.GameFinishedEvent;
import cz.schlosserovi.tomas.drooms.tournaments.model.GameEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.GameResultEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.TournamentResultPartialEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.PlaygroundEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.TournamentEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.TournamentResultEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.UserEntity;

public class GameResultUpdateTask implements Runnable {
    private final GameDAO games;
    private final TournamentResultDAO tournamentResults;

    private final PlaygroundEntity playground;
    private final TournamentEntity tournament;

    public GameResultUpdateTask(GameDAO games, TournamentResultDAO tournamentResults, GameFinishedEvent event) {
        this.games = games;
        this.tournamentResults = tournamentResults;

        this.playground = event.getGame().getPlayground();
        this.tournament = event.getGame().getTournament();
    }

    @Override
    public void run() {
        List<GameEntity> gameResults = games.getGamesWithResults(tournament, playground);
        Map<UserEntity, TournamentResultEntity> resultMapping = createResultsMapping();
        Map<UserEntity, Integer> positions = getPositions(countMedians(collectPoints(gameResults)));

        Collection<TournamentResultEntity> toBeUpdated = new HashSet<>();

        for (Entry<UserEntity, Integer> entry : positions.entrySet()) {
            TournamentResultEntity tournamentResult = resultMapping.get(entry.getKey());
            TournamentResultPartialEntity partial = null;
            for (TournamentResultPartialEntity ptr : tournamentResult.getPartialResults()) {
                if (ptr.getPlayground().equals(playground)) {
                    partial = ptr;
                    break;
                }
            }
            if (partial == null) {
                partial = new TournamentResultPartialEntity();
                partial.setPlayground(playground);
                partial.setTournamentResult(tournamentResult);
            }
            if (!entry.getValue().equals(partial.getPosition())) {
                partial.setPosition(entry.getValue());
                toBeUpdated.add(tournamentResult);
            }
        }

        tournamentResults.updateTournamentResults(toBeUpdated, true);
    }

    /**
     * Traverses through game results and collects the list of points for every
     * user.
     */
    private Map<UserEntity, List<Integer>> collectPoints(List<GameEntity> results) {
        Map<UserEntity, List<Integer>> pointsMap = new HashMap<>();
        // create a map
        for (GameEntity entity : results) {
            for (GameResultEntity result : entity.getGameResults()) {
                if (!pointsMap.containsKey(result.getStrategy().getAuthor())) {
                    pointsMap.put(result.getStrategy().getAuthor(), new LinkedList<Integer>());
                }
                pointsMap.get(result.getStrategy().getAuthor()).add(result.getPoints());
            }
        }

        return pointsMap;
    }

    /**
     * count median for every user.
     */
    private Map<UserEntity, BigDecimal> countMedians(Map<UserEntity, List<Integer>> points) {
        Map<UserEntity, BigDecimal> medians = new HashMap<>();

        BigDecimal median;
        for (Entry<UserEntity, List<Integer>> entry : points.entrySet()) {
            List<Integer> results = entry.getValue();
            Collections.sort(results);

            if (results.size() % 2 == 0) {
                median = new BigDecimal(results.get(results.size() / 2)).add(new BigDecimal(results.size() / 2 - 1)).divide(
                        new BigDecimal(2));
            } else {
                median = new BigDecimal(results.get(entry.getValue().size() / 2)).divide(new BigDecimal(2));
            }

            medians.put(entry.getKey(), median);
        }

        return medians;
    }

    /**
     * Assigns position to each player according to score median.
     */
    private Map<UserEntity, Integer> getPositions(Map<UserEntity, BigDecimal> medians) {
        Map<UserEntity, Integer> positions = new HashMap<>();

        SortedMap<BigDecimal, Collection<UserEntity>> groupedResults = new TreeMap<>(Collections.reverseOrder());
        for (Entry<UserEntity, BigDecimal> entry : medians.entrySet()) {
            if (!groupedResults.containsKey(entry.getValue())) {
                groupedResults.put(entry.getValue(), new HashSet<UserEntity>());
            }
            groupedResults.get(entry.getValue()).add(entry.getKey());
        }

        int position = 1;
        for (Entry<BigDecimal, Collection<UserEntity>> groupedEntry : groupedResults.entrySet()) {
            for (UserEntity player : groupedEntry.getValue()) {
                positions.put(player, position);
            }
            position += groupedEntry.getValue().size();
        }

        return positions;
    }

    private Map<UserEntity, TournamentResultEntity> createResultsMapping() {
        Map<UserEntity, TournamentResultEntity> mapping = new HashMap<>();

        for (TournamentResultEntity result : tournamentResults.getTournamentResults(tournament)) {
            mapping.put(result.getPlayer(), result);
        }

        return mapping;
    }
}
