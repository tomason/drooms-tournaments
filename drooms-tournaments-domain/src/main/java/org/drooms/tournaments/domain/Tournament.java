package org.drooms.tournaments.domain;

import java.util.Date;
import java.util.List;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.databinding.client.api.Bindable;

@Bindable
@Portable
public class Tournament implements Comparable<Tournament> {
    private String name;
    private Date start;
    private Date end;
    private int period;
    private boolean enrolled;

    private List<Playground> playgrounds;
    private List<TournamentResult> results;

    public Tournament() {
    }

    public Tournament(String name, Date start, Date end, int period, List<Playground> playgrounds) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.period = period;
        this.playgrounds = playgrounds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public List<Playground> getPlaygrounds() {
        return playgrounds;
    }

    public void setPlaygrounds(List<Playground> playgrounds) {
        this.playgrounds = playgrounds;
    }

    public List<TournamentResult> getResults() {
        return results;
    }

    public void setResults(List<TournamentResult> results) {
        this.results = results;
    }

    public boolean isEnrolled() {
        return enrolled;
    }

    public void setEnrolled(boolean enrolled) {
        this.enrolled = enrolled;
    }

    @Override
    public int compareTo(Tournament o) {
        if (name == null || o.name == null) {
            throw new NullPointerException("Tournament name is not set");
        }

        return name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return new StringBuilder("Tournament[name='").append(name).append("', start='").append(start).append("', end='")
                .append(end).append("', period='").append(period).append("']").toString();
    }
}
