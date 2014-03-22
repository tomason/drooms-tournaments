package cz.schlosserovi.tomas.drooms.tournaments.domain;

public class GameResult {
    private Strategy strategy;
    private int points;
    private int position;

    public GameResult() {
    }

    public GameResult(Strategy strategy, int points, int position) {
        this.strategy = strategy;
        this.points = points;
        this.position = position;
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

    @Override
    public String toString() {
        return String.format("GameResult[strategy='%s', points='%s', position='%s']", strategy, points, position);
    }
}
