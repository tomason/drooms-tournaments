package cz.schlosserovi.tomas.drooms.tournaments.beans;

import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.schlosserovi.tomas.drooms.tournaments.data.GameDAO;
import cz.schlosserovi.tomas.drooms.tournaments.data.StrategyDAO;
import cz.schlosserovi.tomas.drooms.tournaments.data.TournamentDAO;
import cz.schlosserovi.tomas.drooms.tournaments.domain.GAV;
import cz.schlosserovi.tomas.drooms.tournaments.events.NewTournamentEvent;
import cz.schlosserovi.tomas.drooms.tournaments.model.PlaygroundEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.StrategyEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.TournamentEntity;

@Singleton
@Startup
public class TournamentSchedulingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(TournamentSchedulingBean.class);

    private ScheduledExecutorService scheduler;
    private TournamentDAO tournaments;
    private GameDAO games;
    private StrategyDAO strategies;

    public TournamentSchedulingBean() {
    }

    @Inject
    public TournamentSchedulingBean(ScheduledExecutorService scheduler, TournamentDAO tournaments, GameDAO games,
            StrategyDAO strategies) {
        this.scheduler = scheduler;
        this.tournaments = tournaments;
        this.games = games;
        this.strategies = strategies;
    }

    @PostConstruct
    public void scheduleAlreadyExistingTournaments() {
        LOGGER.info("Tournament Scheduling started");
        for (TournamentEntity tournament : tournaments.getUnfinishedTournaments()) {
            scheduleGameInsertion(tournament);
        }
    }

    public void scheduleNewlyInsertedTournament(@Observes(during = TransactionPhase.AFTER_SUCCESS) NewTournamentEvent event) {
        scheduleGameInsertion(event.getTournament());
    }

    private void scheduleGameInsertion(TournamentEntity tournament) {
        Calendar scheduled = (Calendar) tournament.getStart().clone();
        while (scheduled.compareTo(Calendar.getInstance()) < 0) {
            scheduled.add(Calendar.HOUR_OF_DAY, tournament.getPeriod());
        }
        long delay = scheduled.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();

        scheduler.schedule(new GameInsertionRunnable(tournament), delay, TimeUnit.MILLISECONDS);
        LOGGER.info("Scheduled {} run in {} ms", tournament.getName(), delay);
    }

    private void insertGameRun(TournamentEntity tournament) {
        Collection<StrategyEntity> players = strategies.getActiveStrategies(tournament);

        for (PlaygroundEntity map : tournament.getPlaygrounds()) {
            try {
                games.insertGame(map.getName(), tournament.getName(), toGavCollection(players));
            } catch (Exception ex) {
                LOGGER.error("Unable to schedule game", ex);
            }
        }
    }

    private Collection<GAV> toGavCollection(Collection<StrategyEntity> strategies) {
        Collection<GAV> result = new LinkedList<>();
        for (StrategyEntity strategy : strategies) {
            result.add(strategy.getGav());
        }
        return result;
    }

    private class GameInsertionRunnable implements Runnable {
        private final TournamentEntity tournament;

        public GameInsertionRunnable(TournamentEntity tournament) {
            this.tournament = tournament;
        }

        @Override
        public void run() {
            insertGameRun(tournament);
            scheduler.schedule(new GameInsertionRunnable(tournament), tournament.getPeriod(), TimeUnit.HOURS);
            LOGGER.info("Scheduled {} run in {} h", tournament.getName(), tournament.getPeriod());
        }
    }

}
