package cz.schlosserovi.tomas.drooms.tournaments.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import cz.schlosserovi.tomas.drooms.tournaments.domain.GAV;
import cz.schlosserovi.tomas.drooms.tournaments.util.NullForbiddingSet;

@Entity
@Table(name = "STRATEGY")
public class StrategyEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private GAV gav;
    private boolean active = false;
    @ManyToOne(optional = false, cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
    private UserEntity author;
    @OneToMany(mappedBy = "strategy", cascade = CascadeType.ALL)
    private Collection<GameResultEntity> gameResults = new NullForbiddingSet<>();

    public StrategyEntity() {
    }

    public StrategyEntity(UserEntity author, GAV gav) {
        setAuthor(author);
        setGav(gav);
    }

    public GAV getGav() {
        return gav;
    }

    public void setGav(GAV gav) {
        if (this.gav != null) {
            throw new IllegalStateException("GAV is already set");
        }
        if (gav == null) {
            throw new IllegalArgumentException("GAV must not be null");
        }
        this.gav = gav;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserEntity author) {
        if (this.author != null) {
            throw new IllegalStateException("Author is already set");
        }
        if (author == null) {
            throw new IllegalArgumentException("Author must not be null");
        }
        this.author = author;
        author.addStrategy(this);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Collection<GameResultEntity> getGameResults() {
        return Collections.unmodifiableCollection(gameResults);
    }

    public void setGameResults(Collection<GameResultEntity> gameResults) {
        if (gameResults == null) {
            throw new IllegalArgumentException("GameResults must not be null");
        }
        this.gameResults.clear();
        this.gameResults.addAll(gameResults);
    }

    public void addGameResult(GameResultEntity gameResult) {
        if (gameResult == null) {
            throw new IllegalArgumentException("GameResult must not be null");
        }
        gameResults.add(gameResult);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((gav == null) ? 0 : gav.hashCode());
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
        StrategyEntity other = (StrategyEntity) obj;
        if (gav == null) {
            if (other.gav != null)
                return false;
        } else if (!gav.equals(other.gav))
            return false;
        return true;
    }

}
