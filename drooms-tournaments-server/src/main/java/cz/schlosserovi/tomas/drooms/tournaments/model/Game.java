package cz.schlosserovi.tomas.drooms.tournaments.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * Entity implementation class for Entity: Game
 * 
 */
@Entity
public class Game implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;
    private boolean finished = false;
    @ManyToOne(optional = false, cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
    private Playground playground;
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private Collection<GameResult> gameResults = new LinkedList<>();

    public Game() {
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Playground getPlayground() {
        return playground;
    }

    public void setPlayground(Playground playground) {
        this.playground = playground;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Collection<GameResult> getGameResults() {
        return gameResults;
    }

    public void setGameResults(Collection<GameResult> gameResults) {
        this.gameResults = gameResults;
    }

    public void addGameResult(GameResult gameResult) {
        gameResults.add(gameResult);
        gameResult.setGame(this);
    }

    public void removeGameResult(GameResult gameResult) {
        gameResults.remove(gameResult);
        gameResult.setGame(null);
    }

}
