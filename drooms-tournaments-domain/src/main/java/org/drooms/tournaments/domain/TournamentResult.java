package org.drooms.tournaments.domain;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class TournamentResult implements Comparable<TournamentResult> {
    private User player;
    private int position;

    public User getPlayer() {
        return player;
    }

    public void setPlayer(User player) {
        this.player = player;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public int compareTo(TournamentResult o) {
        return position - o.position;
    }

    @Override
    public String toString() {
        return new StringBuilder("TournamentResult[player='").append(player).append("', position='").append(position)
                .append("']").toString();
    }
}
