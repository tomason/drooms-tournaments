package cz.schlosserovi.tomas.drooms.tournaments.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "USER")
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String name;
    @NotNull
    private byte[] password;
    @NotNull
    private byte[] salt;
    @OneToMany(mappedBy = "author")
    private Set<PlaygroundEntity> playgrounds = new HashSet<>();
    @OneToMany(mappedBy = "author")
    private Set<StrategyEntity> strategies = new HashSet<>();

    public UserEntity() {
    }

    public UserEntity(String name, byte[] salt, byte[] password) {
        if (name == null || salt == null || password == null) {
            throw new IllegalArgumentException("Name, Salt and Password must not be null");
        }
        this.name = name;
        this.salt = salt;
        this.password = password;
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

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        if (password == null) {
            throw new IllegalArgumentException("Password must not be null");
        }
        this.password = password;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        if (salt == null) {
            throw new IllegalArgumentException("Salt must not be null");
        }
        this.salt = salt;
    }

    public Set<PlaygroundEntity> getPlaygrounds() {
        return Collections.unmodifiableSet(playgrounds);
    }

    public void setPlaygrounds(Set<PlaygroundEntity> playgrounds) {
        if (playgrounds == null) {
            throw new IllegalArgumentException("Playgrounds must not be null");
        }
        this.playgrounds = playgrounds;
    }

    public void addPlayground(PlaygroundEntity playground) {
        if (playground == null) {
            throw new IllegalArgumentException("Playground must not be null");
        }
        playgrounds.add(playground);
    }

    public Set<StrategyEntity> getStrategies() {
        return Collections.unmodifiableSet(strategies);
    }

    public void setStrategies(Set<StrategyEntity> strategies) {
        if (strategies == null) {
            throw new IllegalArgumentException("Strategies must not be null");
        }
        this.strategies = strategies;
    }

    public void addStrategy(StrategyEntity strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Strategy must not be null");
        }
        strategies.add(strategy);
    }

}
