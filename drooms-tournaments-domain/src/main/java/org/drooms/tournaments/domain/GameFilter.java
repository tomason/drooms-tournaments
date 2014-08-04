package org.drooms.tournaments.domain;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class GameFilter {
    private User player;
    private Strategy strategy;
    private Playground playground;
    private Tournament tournament;
    private Boolean finished;

    public GameFilter() {
        this(null, null, null, null, null);
    }

    public GameFilter(User player, Strategy strategy, Playground playground, Tournament tournament, Boolean finished) {
        this.player = player;
        this.strategy = strategy;
        this.playground = playground;
        this.tournament = tournament;
        this.finished = finished;
    }

    public GameFilter(User player) {
        this(player, null, null, null, null);
    }

    public GameFilter(Strategy strategy) {
        this(null, strategy, null, null, null);
    }

    public GameFilter(Playground playground) {
        this(null, null, playground, null, null);
    }

    public GameFilter(Tournament tournament) {
        this(null, null, null, tournament, null);
    }

    public GameFilter(Boolean finished) {
        this(null, null, null, null, finished);
    }

    public User getPlayer() {
        return player;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public Playground getPlayground() {
        return playground;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setPlayer(User player) {
        this.player = player;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public void setPlayground(Playground playground) {
        this.playground = playground;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

}
