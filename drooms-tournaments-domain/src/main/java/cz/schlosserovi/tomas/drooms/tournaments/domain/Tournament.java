package cz.schlosserovi.tomas.drooms.tournaments.domain;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tournament {
    // Keep these two in sync
    private static final Pattern TO_STRING_PATTERN = Pattern
            .compile("Tournament\\[name='(.+)', start='(\\d+)', end='(\\d+)', period='(\\d+)'\\]");
    private static final String TO_STRING_FORMAT = "Tournament[name='%s', start='%s', end='%s', period='%s']";

    private String name;
    private Calendar start;
    private Calendar end;
    private int period;
    private boolean enrolled;

    private Collection<Playground> playgrounds;
    private Collection<TournamentResult> results;

    public Tournament() {
    }

    public Tournament(String name, Calendar start, Calendar end, int period, List<Playground> playgrounds) {
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

    public Calendar getStart() {
        return start;
    }

    public void setStart(Calendar start) {
        this.start = start;
    }

    public Calendar getEnd() {
        return end;
    }

    public void setEnd(Calendar end) {
        this.end = end;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public Collection<Playground> getPlaygrounds() {
        return playgrounds;
    }

    public void setPlaygrounds(Collection<Playground> playgrounds) {
        this.playgrounds = playgrounds;
    }

    public Collection<TournamentResult> getResults() {
        return results;
    }

    public void setResults(Collection<TournamentResult> results) {
        this.results = results;
    }

    public boolean isEnrolled() {
        return enrolled;
    }

    public void setEnrolled(boolean enrolled) {
        this.enrolled = enrolled;
    }

    public static Tournament fromString(String tournament) {
        Matcher m = TO_STRING_PATTERN.matcher(tournament);
        m.matches();

        Tournament result = new Tournament();
        result.name = m.group(1);

        result.start = Calendar.getInstance();
        result.start.setTimeInMillis(Long.valueOf(m.group(2)));

        result.end = Calendar.getInstance();
        result.end.setTimeInMillis(Long.valueOf(m.group(3)));

        result.period = Integer.valueOf(m.group(4));

        return result;
    }

    @Override
    public String toString() {
        return String.format(TO_STRING_FORMAT, name, start.getTimeInMillis(), end.getTimeInMillis(), period);
    }
}
