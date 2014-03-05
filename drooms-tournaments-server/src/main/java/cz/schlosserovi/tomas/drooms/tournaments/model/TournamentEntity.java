package cz.schlosserovi.tomas.drooms.tournaments.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import cz.schlosserovi.tomas.drooms.tournaments.util.NullForbiddingSet;

@Entity
@Table(name = "TOURNAMENT")
public class TournamentEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String name;
    private Date start;
    private Date end;
    private int period = 24;
    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private Collection<GameEntity> games = new NullForbiddingSet<GameEntity>();
    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private Collection<TournamentResultEntity> results = new NullForbiddingSet<>();
    @ManyToMany
    private Collection<PlaygroundEntity> playgrounds = new NullForbiddingSet<>();

    public TournamentEntity() {
    }

    public TournamentEntity(String name, Date start, Date end, int period) {
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

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        if (this.start != null) {
            throw new IllegalStateException("Start date was already set");
        }
        if (start == null) {
            throw new IllegalArgumentException("Start date must not be null");
        }
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        if (end == null) {
            throw new IllegalArgumentException("End date must not be null");
        }
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
