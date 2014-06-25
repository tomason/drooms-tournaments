package org.drooms.tournaments.domain;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class Strategy implements Comparable<Strategy> {
    private GAV gav;
    private boolean active = false;
    private User player;

    public Strategy() {
    }

    public Strategy(String groupId, String artifactId, String version) {
        gav = new GAV(groupId, artifactId, version);
    }

    public Strategy(GAV gav, boolean active) {
        this.gav = gav;
        this.active = active;
    }

    public GAV getGav() {
        return gav;
    }

    public void setGav(GAV gav) {
        this.gav = gav;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public User getPlayer() {
        return player;
    }

    public void setPlayer(User player) {
        this.player = player;
    }

    @Override
    public int compareTo(Strategy o) {
        if (gav == null || o.gav == null) {
            throw new NullPointerException("Strategy GAV is not set");
        }
        return gav.compareTo(o.gav);
    }

    @Override
    public String toString() {
        return new StringBuilder("Strategy[gav='").append(gav).append("', active='").append(active).append("']").toString();
    }
}
