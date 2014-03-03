package cz.schlosserovi.tomas.drooms.tournaments.domain;

import java.util.List;

public class Game {
    private Playground playground;
    private List<Strategy> players;

    public Game() {
    }

    public Game(Playground playground, List<Strategy> players) {
        super();
        this.playground = playground;
        this.players = players;
    }

    public Playground getPlayground() {
        return playground;
    }

    public void setPlayground(Playground playground) {
        this.playground = playground;
    }

    public List<Strategy> getPlayers() {
        return players;
    }

    public void setPlayers(List<Strategy> players) {
        this.players = players;
    }

}
