package cz.schlosserovi.tomas.drooms.tournaments.domain;

public class GameResult {
    private String gameId;
    private String playgroundName;
    private int points;
    private boolean finished;

    public GameResult() {
    }

    public GameResult(String gameId, String playgroundName, int points, boolean finished) {
        super();
        this.gameId = gameId;
        this.playgroundName = playgroundName;
        this.points = points;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getPlaygroundName() {
        return playgroundName;
    }

    public void setPlaygroundName(String playgroundName) {
        this.playgroundName = playgroundName;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    @Override
    public String toString() {
        return String.format("GameResult[gameId='%s', playground='%s', points='%s', finished='%s']", gameId, playgroundName, points, finished);
    }
}
