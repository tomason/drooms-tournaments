package org.drooms.tournaments.domain;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.databinding.client.api.Bindable;

@Bindable
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
        int result = position - o.position;
        if (result == 0) {
            result = player.getName().compareTo(o.player.getName());
        }

        return result;
    }

    @Override
    public String toString() {
        return new StringBuilder("TournamentResult[player='").append(player).append("', position='").append(position)
                .append("']").toString();
    }
}
