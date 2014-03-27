package cz.schlosserovi.tomas.drooms.tournaments.logic;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import cz.schlosserovi.tomas.drooms.tournaments.data.GameDAO;
import cz.schlosserovi.tomas.drooms.tournaments.data.PlaygroundDAO;
import cz.schlosserovi.tomas.drooms.tournaments.data.StrategyDAO;
import cz.schlosserovi.tomas.drooms.tournaments.data.TournamentDAO;
import cz.schlosserovi.tomas.drooms.tournaments.data.UserDAO;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Playground;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Tournament;
import cz.schlosserovi.tomas.drooms.tournaments.model.GameEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.GameResultEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.PlaygroundEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.StrategyEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.TournamentEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.TournamentResultEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.UserEntity;
import cz.schlosserovi.tomas.drooms.tournaments.util.Converter;

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
        entity.setStart(tournament.getStart());
        entity.setEnd(tournament.getEnd());
        entity.setPeriod(tournament.getPeriod());

        for (Playground playground : tournament.getPlaygrounds()) {
            entity.addPlayground(playgrounds.getPlayground(playground.getName()));
        }

        tournaments.insertTournament(entity);
    }

    public Collection<Tournament> getAllTournaments() {
        return getConverter().convert(tournaments.getTournaments());
    }

    public Collection<Tournament> getUserTournaments(String userName) {
        UserEntity user = users.getUser(userName);

        Collection<TournamentEntity> unfinished = tournaments.getUnfinishedTournaments();
        Collection<TournamentEntity> enrolled = tournaments.getTournaments(user);
        unfinished.removeAll(enrolled);

        Collection<Tournament> results = new LinkedList<>();
        for (TournamentEntity te : enrolled) {
            Tournament result = te.convert(1);
            result.setEnrolled(true);
            results.add(result);
        }
        for (TournamentEntity te : unfinished) {
            Tournament result = te.convert(1);
            result.setEnrolled(false);
            results.add(result);
        }

        return results;
    }

    public void joinTournament(String userName, Tournament tournament) {
        UserEntity user = users.getUser(userName);
        TournamentEntity entity = tournaments.getTournament(tournament.getName());

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

    private Converter<TournamentEntity, Tournament> getConverter() {
        return getConverter(0);
    }

    private Converter<TournamentEntity, Tournament> getConverter(int depth) {
        return Converter.forClass(TournamentEntity.class).setRecurseDepth(depth);
    }
}
