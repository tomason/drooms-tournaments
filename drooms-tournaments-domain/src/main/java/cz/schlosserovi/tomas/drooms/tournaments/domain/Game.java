package cz.schlosserovi.tomas.drooms.tournaments.domain;

import java.util.Collection;

public class Game implements Comparable<Game> {
    private String id;
    private Tournament tournament;
    private Playground playground;
    private Collection<GameResult> results;
    private boolean finished;
    private String gameLog;
    private String gameReport;

    public Game() {
    }

    public Game(String id, Tournament tournament, Playground playground, Collection<GameResult> results, boolean finished) {
        this.id = id;
        this.tournament = tournament;
        this.playground = playground;
        this.results = results;
        this.finished = finished;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Tournament getTournament() {
        return this.tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public Playground getPlayground() {
        return this.playground;
    }

    public void setPlayground(Playground playground) {
        this.playground = playground;
    }

    public Collection<GameResult> getResults() {
        return this.results;
    }

    public void setResults(Collection<GameResult> results) {
        this.results = results;
    }

    public boolean isFinished() {
        return this.finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String getGameLog() {
        return gameLog;
    }

    public void setGameLog(String gameLog) {
        this.gameLog = gameLog;
    }

    public String getGameReport() {
        return gameReport;
    }

    public void setGameReport(String gameReport) {
        this.gameReport = gameReport;
    }

    @Override
    public int compareTo(Game o) {
        if (id == null || o.id == null) {
            throw new NullPointerException("Game id is not set");
        }
        return id.compareTo(o.id);
    }

    @Override
    public String toString() {
        return String.format("Game[id='%s', tournament='%s', playground='%s', finished='%s', results='%s']", id, tournament,
                playground, finished, results);
    }
}
