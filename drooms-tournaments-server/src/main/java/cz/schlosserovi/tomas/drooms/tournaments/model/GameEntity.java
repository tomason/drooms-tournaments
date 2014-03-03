package cz.schlosserovi.tomas.drooms.tournaments.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Entity implementation class for Entity: Game
 * 
 */
@Entity
@Table(name = "GAME")
public class GameEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;
    private boolean finished = false;
    @ManyToOne(optional = false, cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
    private PlaygroundEntity playground;
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private Set<GameResultEntity> gameResults = new HashSet<>();

    public GameEntity() {
    }

    public GameEntity(PlaygroundEntity playground) {
        if (playground == null) {
            throw new IllegalArgumentException("Playground must not be null");
        }
        this.playground = playground;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        if (this.id != null) {
            throw new IllegalStateException("Can't change Id in persisted Game");
        }
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        this.id = id;
    }

    public PlaygroundEntity getPlayground() {
        return playground;
    }

    public void setPlayground(PlaygroundEntity playground) {
        if (this.playground != null) {
            throw new IllegalStateException("Playground is already set");
        }
        if (playground == null) {
            throw new IllegalArgumentException("Playground must not be null");
        }
        this.playground = playground;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        if (this.finished) {
            throw new IllegalStateException("Game is already finished");
        }
        this.finished = finished;
    }

    public Set<GameResultEntity> getGameResults() {
        return Collections.unmodifiableSet(gameResults);
    }

    public void setGameResults(Set<GameResultEntity> gameResults) {
        if (gameResults == null) {
            throw new IllegalArgumentException("GameResults must not be null");
        }
        this.gameResults = gameResults;
    }

    public void addGameResult(GameResultEntity gameResult) {
        if (gameResult == null) {
            throw new IllegalArgumentException("GameResult must not be null");
        }
        if (!equals(gameResult.getGame())) {
            throw new IllegalArgumentException("GameResult must not be assigned to another Game");
        }
        gameResults.add(gameResult);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        GameEntity other = (GameEntity) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
