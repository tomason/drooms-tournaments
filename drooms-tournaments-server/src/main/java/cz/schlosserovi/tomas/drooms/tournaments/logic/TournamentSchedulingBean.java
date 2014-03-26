package cz.schlosserovi.tomas.drooms.tournaments.logic;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.schlosserovi.tomas.drooms.tournaments.events.NewTournamentEvent;
import cz.schlosserovi.tomas.drooms.tournaments.model.TournamentEntity;

@Singleton
@Startup
public class TournamentSchedulingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(TournamentSchedulingBean.class);
    private static final Timer TIMER = new Timer("Tournament scheduling timer");

    private TournamentLogic logic;

    public TournamentSchedulingBean() {
    }

    @Inject
    public TournamentSchedulingBean(TournamentLogic logic) {
        this.logic = logic;
    }

    @PostConstruct
    public void scheduleAlreadyExistingTournaments() {
        LOGGER.info("Tournament Scheduling started");
        for (TournamentEntity tournament : logic.getUnfinishedTournaments()) {
            scheduleGameInsertion(tournament);
        }
    }

    @PreDestroy
    public void stopThread() {
        LOGGER.info("Shutting down scheduler service");
        TIMER.cancel();
    }

    public void scheduleNewlyInsertedTournament(@Observes(during = TransactionPhase.AFTER_SUCCESS) NewTournamentEvent event) {
        scheduleGameInsertion(event.getTournament());
    }

    private void scheduleGameInsertion(TournamentEntity tournament) {
        Calendar when = (Calendar) tournament.getStart().clone();
        while (when.compareTo(Calendar.getInstance()) < 0) {
            when.add(Calendar.HOUR_OF_DAY, tournament.getPeriod());
        }

        scheduleGameInsertion(tournament, when);
    }

    private void scheduleGameInsertion(TournamentEntity tournament, Calendar when) {
        if (when.compareTo(tournament.getEnd()) > 0) {
            // TODO finished tournaments
        } else {
            TIMER.schedule(new GameInsertionRunnable(tournament, when), when.getTime());
            LOGGER.info("Scheduled '{}' run to {}", tournament.getName(), when.getTime());
        }
    }

    private class GameInsertionRunnable extends TimerTask {
        private final TournamentEntity tournament;
        private final Calendar when;

        public GameInsertionRunnable(TournamentEntity tournament, Calendar when) {
            this.tournament = tournament;
            this.when = when;
        }

        @Override
        public void run() {
            logic.createGameRun(tournament);

            when.add(Calendar.HOUR_OF_DAY, tournament.getPeriod());
            scheduleGameInsertion(tournament, when);
        }
    }

}
