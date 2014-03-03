package cz.schlosserovi.tomas.drooms.tournaments.domain;

public class Strategy {
    private GAV gav;
    private boolean active = false;

    public Strategy() {
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

    @Override
    public String toString() {
        return String.format("Strategy[groupId='%s', artifactId='%s', version='%s', active='%s']", gav.getGroupId(),
                gav.getArtifactId(), gav.getVersion(), active);
    }
}
