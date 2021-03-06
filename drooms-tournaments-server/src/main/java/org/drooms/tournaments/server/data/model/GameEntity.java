package org.drooms.tournaments.server.data.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.drooms.tournaments.domain.Game;
import org.drooms.tournaments.server.util.Converter;
import org.drooms.tournaments.server.util.Convertible;
import org.drooms.tournaments.server.util.NullForbiddingSet;

/**
 * Entity implementation class for Entity: Game
 * 
 */
@Entity
@Table(name = "GAME")
public class GameEntity implements Serializable, Convertible<Game> {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private GameStatus status = GameStatus.NEW;
    private Calendar lastModified;
    private String artifactPath;
    @ManyToOne(optional = false)
    @JoinColumn(name = "PLAYGROUND_NAME")
    private PlaygroundEntity playground;
    @ManyToOne(optional = false)
    @JoinColumn(name = "TOURNAMENT_NAME")
    private TournamentEntity tournament;
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<GameResultEntity> gameResults = new NullForbiddingSet<>();

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
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

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        if (this.status == GameStatus.FINISHED) {
            throw new IllegalStateException("Game is already finished");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status must not be null");
        }
        if (this.status.compareTo(status) > 0) {
            throw new IllegalArgumentException("Status can't go back.");
        }
        this.status = status;
        this.lastModified = Calendar.getInstance();
    }

    public Calendar getLastModified() {
        return (Calendar) lastModified.clone();
    }

    public void setLastModified(Calendar lastModified) {
        if (this.lastModified != null) {
            throw new IllegalStateException("Last modification date is already set");
        }
        if (lastModified == null) {
            throw new IllegalArgumentException("Last modification date must not be null");
        }
        this.lastModified = (Calendar) lastModified.clone();
    }

    public String getArtifactPath() {
        return artifactPath;
    }

    public void setArtifactPath(String artifactPath) {
        this.artifactPath = artifactPath;
    }

    public TournamentEntity getTournament() {
        return tournament;
    }

    public void setTournament(TournamentEntity tournament) {
        if (this.tournament != null) {
            throw new IllegalStateException("Tournament is already set");
        }
        if (tournament == null) {
            throw new IllegalArgumentException("Tournament must not be null");
        }
        this.tournament = tournament;
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
        if (!equals(gameResult.getGame())) {
            throw new IllegalArgumentException("GameResult must not be assigned to another Game");
        }
        gameResults.add(gameResult);
    }

    @Override
    public Game convert(int depth) {
        Game result = new Game();
        result.setId(getId());
        result.setFinished(getStatus() == GameStatus.FINISHED);

        if (getPlayground() != null) {
            result.setPlayground(getPlayground().convert(depth - 1));
        }
        if (getTournament() != null) {
            result.setTournament(getTournament().convert(depth - 1));
        }

        if (depth > 0) {
            result.setResults(Converter.forClass(GameResultEntity.class).setRecurseDepth(depth - 1).convert(getGameResults()));
        }

        return result;
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
