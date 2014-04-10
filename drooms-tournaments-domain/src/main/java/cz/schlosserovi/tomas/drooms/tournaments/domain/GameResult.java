package cz.schlosserovi.tomas.drooms.tournaments.domain;

public class GameResult {
    private Strategy strategy;
    private int points;

    public GameResult() {
    }

    public GameResult(Strategy strategy, int points) {
        this.strategy = strategy;
        this.points = points;
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

    @Override
    public String toString() {
        return String.format("GameResult[strategy='%s', points='%s']", strategy, points);
    }
}
