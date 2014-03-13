package cz.schlosserovi.tomas.drooms.tournaments.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import cz.schlosserovi.tomas.drooms.tournaments.util.NullForbiddingSet;

@Entity
@Table(name = "PLAYER")
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String name;
    private String password;
    @OneToMany(mappedBy = "author")
    private Collection<PlaygroundEntity> playgrounds = new NullForbiddingSet<>();
    @OneToMany(mappedBy = "author")
    private Collection<StrategyEntity> strategies = new NullForbiddingSet<>();
    @OneToMany(mappedBy = "player")
    private Collection<TournamentResultEntity> tournamentResults = new NullForbiddingSet<>();

    public UserEntity() {
    }

    public UserEntity(String name, String password) {
        setName(name);
        setPassword(password);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (this.name != null) {
            throw new IllegalStateException("Name is already set");
        }
        if (name == null) {
            throw new IllegalArgumentException("Name must not be null");
        }
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Password must not be null");
        }
        this.password = password;
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
        if (!equals(playground.getAuthor())) {
            throw new IllegalArgumentException("Playground must not be assigned to another User");
        }
        playgrounds.add(playground);
    }

    public Collection<StrategyEntity> getStrategies() {
        return Collections.unmodifiableCollection(strategies);
    }

    public void setStrategies(Collection<StrategyEntity> strategies) {
        if (strategies == null) {
            throw new IllegalArgumentException("Strategies must not be null");
        }
        this.strategies.clear();
        this.strategies.addAll(strategies);
    }

    public void addStrategy(StrategyEntity strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Strategy must not be null");
        }
        if (!equals(strategy.getAuthor())) {
            throw new IllegalArgumentException("Strategy must not be assigned to another User");
        }
        strategies.add(strategy);
    }

    public Collection<TournamentResultEntity> getTournamentResults() {
        return Collections.unmodifiableCollection(tournamentResults);
    }

    public void setTournamentResults(Collection<TournamentResultEntity> tournamentResults) {
        if (tournamentResults == null) {
            throw new IllegalArgumentException("Tournament results must not be null");
        }
        this.tournamentResults.clear();
        this.tournamentResults.addAll(tournamentResults);
    }

    public void addTournamentResult(TournamentResultEntity tournamentResult) {
        if (tournamentResult == null) {
            throw new IllegalArgumentException("Tournament result must not be null");
        }
        if (!equals(tournamentResult.getPlayer())) {
            throw new IllegalArgumentException("Tournament result is already assigned");
        }
        tournamentResults.add(tournamentResult);
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
        UserEntity other = (UserEntity) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
