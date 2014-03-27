package cz.schlosserovi.tomas.drooms.tournaments.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import cz.schlosserovi.tomas.drooms.tournaments.domain.Tournament;
import cz.schlosserovi.tomas.drooms.tournaments.util.Converter;
import cz.schlosserovi.tomas.drooms.tournaments.util.Convertible;
import cz.schlosserovi.tomas.drooms.tournaments.util.NullForbiddingSet;

@Entity
@Table(name = "TOURNAMENT")
public class TournamentEntity implements Serializable, Convertible<Tournament> {
    private static final long serialVersionUID = 1L;

    @Id
    private String name;
    @Column(name = "TOURNAMENT_START")
    private Calendar start;
    @Column(name = "TOURNAMENT_END")
    private Calendar end;
    private int period = 24;
    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private Set<GameEntity> games = new NullForbiddingSet<GameEntity>();
    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private Set<TournamentResultEntity> results = new NullForbiddingSet<>();
    @ManyToMany
    private Set<PlaygroundEntity> playgrounds = new NullForbiddingSet<>();

    public TournamentEntity() {
    }

    public TournamentEntity(String name) {
        setName(name);
    }

    public TournamentEntity(String name, Calendar start, Calendar end, int period) {
        setName(name);
        setStart(start);
        setEnd(end);
        setPeriod(period);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (this.name != null) {
            throw new IllegalStateException("Name is already set");
        }
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("Name must not be null nor empty");
        }
        this.name = name;
    }

    public Calendar getStart() {
        return start;
    }

    public void setStart(Calendar start) {
        if (this.start != null) {
            throw new IllegalStateException("Start date was already set");
        }
        if (start == null) {
            throw new IllegalArgumentException("Start date must not be null");
        }
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        this.start = start;
    }

    public Calendar getEnd() {
        return end;
    }

    public void setEnd(Calendar end) {
        if (end == null) {
            throw new IllegalArgumentException("End date must not be null");
        }
        end.set(Calendar.HOUR_OF_DAY, 0);
        end.set(Calendar.MINUTE, 0);
        end.set(Calendar.SECOND, 0);
        end.set(Calendar.MILLISECOND, 0);
        this.end = end;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        if (period < 1) {
            throw new IllegalArgumentException("Period must be greater than zero");
        }
        this.period = period;
    }

    public Collection<GameEntity> getGames() {
        return Collections.unmodifiableCollection(games);
    }

    public void setGames(Collection<GameEntity> games) {
        if (games == null) {
            throw new IllegalArgumentException("Games must not be null");
        }
        this.games.clear();
        this.games.addAll(games);
    }

    public void addGame(GameEntity game) {
        if (game == null) {
            throw new IllegalArgumentException("Game must not be null");
        }
        if (!equals(game.getTournament())) {
            throw new IllegalArgumentException("Game is already assigned to another tournament");
        }
        games.add(game);
    }

    public Collection<TournamentResultEntity> getResults() {
        return Collections.unmodifiableCollection(results);
    }

    public void setResults(Collection<TournamentResultEntity> results) {
        if (results == null) {
            throw new IllegalArgumentException("Results must not be null");
        }
        this.results.clear();
        this.results.addAll(results);
    }

    public void addResult(TournamentResultEntity result) {
        if (result == null) {
            throw new IllegalArgumentException("Result must not be null");
        }
        if (!equals(result.getTournament())) {
            throw new IllegalArgumentException("Result is already assigned to another tournament");
        }
        results.add(result);
    }

    public Collection<PlaygroundEntity> getPlaygrounds() {
        return Collections.unmodifiableCollection(playgrounds);
    }

    public void setPlaygrounds(Collection<PlaygroundEntity> playgrounds) {
        if (playgrounds == null) {
            throw new IllegalArgumentException("Playgrounds must not be null");
        }
        this.playgrounds.clear();
        this.playgrounds.addAll(playgrounds);
    }

    public void addPlayground(PlaygroundEntity playground) {
        if (playground == null) {
            throw new IllegalArgumentException("Playground must not be null");
        }
        playgrounds.add(playground);
        playground.addTournament(this);
    }

    @Override
    public Tournament convert(int depth) {
        Tournament result = new Tournament();
        result.setName(getName());
        result.setStart(getStart());
        result.setEnd(getEnd());
        result.setPeriod(getPeriod());

        if (depth > 0) {
            result.setPlaygrounds(Converter.forClass(PlaygroundEntity.class).setRecurseDepth(depth - 1)
                    .convert(getPlaygrounds()));
        }

        return result;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        TournamentEntity other = (TournamentEntity) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
