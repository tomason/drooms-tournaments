package org.drooms.tournaments.client.model.report;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

class Turn implements Comparable<Turn> {
    private final int number;
    private final List<Snake> snakes = new LinkedList<Snake>();
    private final List<Score> scores = new LinkedList<Score>();
    private final List<Collectible> collectibles = new LinkedList<Collectible>();

    public Turn(int number) {
        this.number = number;
    }

    void addSnake(Snake snake) {
        snakes.add(snake);
    }

    public List<Snake> getSnakes() {
        return snakes;
    }

    public List<Score> getScores() {
        return scores;
    }

    public void setScore(Player player, int score) {
        scores.add(new Score(player, score));
        Collections.sort(scores, Collections.reverseOrder());
    }

    public int getNumber() {
        return number;
    }

    public List<Collectible> getCollectibles() {
        return collectibles;
    }

    @Override
    public int compareTo(Turn o) {
        return number - o.number;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + number;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Turn other = (Turn) obj;
        if (number != other.number)
            return false;
        return true;
    }

}
