package cz.schlosserovi.tomas.drooms.tournaments.domain;

public class GameResult {
    private User player;
    private Strategy strategy;
    private int points;
    private int position;

    public GameResult() {
    }

    public GameResult(User player, Strategy strategy, int points, int position) {
        this.strategy = strategy;
        this.player = player;
        this.points = points;
        this.position = position;
    }

    public User getPlayer() {
        return player;
    }

    public void setPlayer(User player) {
        this.player = player;
    }

    public Strategy getStrategy() {
        return this.strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public int getPoints() {
        return this.points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
