package org.drooms.tournaments.server.logic;

import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;

import org.drooms.tournaments.domain.Playground;
import org.drooms.tournaments.domain.Tournament;
import org.drooms.tournaments.server.data.GameDAO;
import org.drooms.tournaments.server.data.PlaygroundDAO;
import org.drooms.tournaments.server.data.StrategyDAO;
import org.drooms.tournaments.server.data.TournamentDAO;
import org.drooms.tournaments.server.data.TournamentResultDAO;
import org.drooms.tournaments.server.data.UserDAO;
import org.drooms.tournaments.server.data.model.GameEntity;
import org.drooms.tournaments.server.data.model.GameResultEntity;
import org.drooms.tournaments.server.data.model.PlaygroundEntity;
import org.drooms.tournaments.server.data.model.StrategyEntity;
import org.drooms.tournaments.server.data.model.TournamentEntity;
import org.drooms.tournaments.server.data.model.TournamentResultEntity;
import org.drooms.tournaments.server.data.model.UserEntity;
import org.drooms.tournaments.server.events.GameFinishedEvent;
import org.drooms.tournaments.server.events.UpdatedTournamentEvent;
import org.drooms.tournaments.server.logic.task.GameResultUpdateTask;
import org.drooms.tournaments.server.logic.task.TournamentResultUpdateTask;
import org.drooms.tournaments.server.util.Converter;

public class TournamentLogic {
    private TournamentDAO tournaments;
    private PlaygroundDAO playgrounds;
    private StrategyDAO strategies;
    private GameDAO games;
    private UserDAO users;

    public TournamentLogic() {
    }

    @Inject
    public TournamentLogic(UserDAO users, GameDAO games, StrategyDAO strategies, PlaygroundDAO playgrounds, TournamentDAO tournaments) {
        this.users = users;
        this.games = games;
        this.strategies = strategies;
        this.playgrounds = playgrounds;
        this.tournaments = tournaments;
    }

    public void newTournament(Tournament tournament) {
        TournamentEntity entity = new TournamentEntity();
        entity.setName(tournament.getName());
        entity.setPeriod(tournament.getPeriod());

        Calendar start = Calendar.getInstance();
        start.setTime(tournament.getStart());
        entity.setStart(start);

        Calendar end = Calendar.getInstance();
        end.setTime(tournament.getEnd());
        entity.setEnd(end);

        for (Playground playground : tournament.getPlaygrounds()) {
            entity.addPlayground(playgrounds.getPlaygroundWithTournaments(playground.getName()));
        }

        tournaments.insertTournament(entity);
    }

    public Collection<Tournament> getAllTournaments() {
        return getConverter().convert(tournaments.getTournaments());
    }

    public Tournament getTournamentDetail(String tournamentName) {
        return getConverter(1).convert(tournaments.getTournamentDetail(tournamentName));
    }

    public Collection<Tournament> getUserTournaments(String userName) {
        UserEntity user = users.getUser(userName);

        Collection<TournamentEntity> unfinished = tournaments.getUnfinishedTournaments();
        Collection<TournamentEntity> enrolled = tournaments.getTournaments(user);
        unfinished.removeAll(enrolled);

        Collection<Tournament> results = new LinkedList<>();
        for (TournamentEntity te : enrolled) {
            Tournament result = te.convert(0);
            result.setEnrolled(true);
            results.add(result);
        }
        for (TournamentEntity te : unfinished) {
            Tournament result = te.convert(0);
            result.setEnrolled(false);
            results.add(result);
        }

        return results;
    }

    public void joinTournament(String userName, Tournament tournament) {
        UserEntity user = users.getUserWithTournamentResults(userName);
        TournamentEntity entity = tournaments.getTournamentWithResults(tournament.getName());

        TournamentResultEntity result = new TournamentResultEntity();
        result.setPlayer(user);
        result.setTournament(entity);

        tournaments.updateTournament(entity);
    }

    List<TournamentEntity> getUnfinishedTournaments() {
        return tournaments.getUnfinishedTournaments();
    }

    void createGameRun(TournamentEntity entity) {
        List<StrategyEntity> strategyEntities = strategies.getActiveStrategies(entity);

        for (PlaygroundEntity playgroundEntity : entity.getPlaygrounds()) {
            GameEntity gameEntity = new GameEntity();
            gameEntity.setPlayground(playgroundEntity);
            gameEntity.setTournament(entity);
            games.insertGame(gameEntity);

            for (StrategyEntity strategyEntity : strategyEntities) {
                GameResultEntity resultEntity = new GameResultEntity();
                resultEntity.setGame(gameEntity);
                resultEntity.setStrategy(strategyEntity);
            }
        }

        tournaments.updateTournament(entity);
    }

    public static void updateGameResults(@Observes(during = TransactionPhase.AFTER_SUCCESS) GameFinishedEvent event,
            ExecutionBean executor, GameDAO games, TournamentResultDAO tournamentResults) {
        executor.submit(new GameResultUpdateTask(games, tournamentResults, event));
    }

    public static void updateTournamentResults(@Observes(during = TransactionPhase.AFTER_SUCCESS) UpdatedTournamentEvent event,
            ExecutionBean executor, TournamentResultDAO tournamentResults) {
        executor.submit(new TournamentResultUpdateTask(tournamentResults, event.getTournament()));
    }

    private Converter<TournamentEntity, Tournament> getConverter() {
        return getConverter(0);
    }

    private Converter<TournamentEntity, Tournament> getConverter(int depth) {
        return Converter.forClass(TournamentEntity.class).setRecurseDepth(depth);
    }
}
