package org.drooms.tournaments.client.model.report;

public class Score implements Comparable<Score> {
    private final Player player;
    private final int score;
    
    Score(Player player, int score) {
        this.player = player;
        this.score = score;
    }

    public String getPlayerName() {
        return player.getName();
    }

    public String getPlayerColor() {
        return player.getColor();
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(Score o) {
        if (score - o.score == 0) {
            return player.getName().compareToIgnoreCase(o.player.getName());
        } else {
            return score - o.score;
        }
    }
}
