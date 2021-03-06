package org.drooms.tournaments.server.data.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.drooms.tournaments.domain.User;
import org.drooms.tournaments.server.util.Convertible;
import org.drooms.tournaments.server.util.NullForbiddingSet;

@Entity
@Table(name = "PLAYER")
public class UserEntity implements Serializable, Convertible<User> {
    private static final long serialVersionUID = 1L;

    @Id
    private String name;
    private String password;
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private Set<StrategyEntity> strategies = new NullForbiddingSet<>();
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    private Set<TournamentResultEntity> tournamentResults = new NullForbiddingSet<>();

    public UserEntity() {
    }

    public UserEntity(String name) {
        setName(name);
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
    public User convert(int recurseDepth) {
        User result = new User();
        result.setName(getName());
        // we are not giving password to anyone

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
        UserEntity other = (UserEntity) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
