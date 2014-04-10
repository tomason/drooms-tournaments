package cz.schlosserovi.tomas.drooms.tournaments.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Strategy implements Comparable<Strategy> {
    // Keep these two in sync
    private static final Pattern TO_STRING_PATTERN = Pattern.compile("Strategy\\[gav='(.+)', active='(.+)'\\]");
    private static final String TO_STRING_FORMAT = "Strategy[gav='%s', active='%s']";

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

    public static Strategy fromString(String strategy) {
        Strategy result = new Strategy();

        Matcher m = TO_STRING_PATTERN.matcher(strategy);
        m.matches();

        result.gav = GAV.fromString(m.group(1));
        result.active = Boolean.valueOf(m.group(2));

        return result;
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
        return String.format(TO_STRING_FORMAT, gav, active);
    }
}
