package org.drooms.tournaments.server.data.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.drooms.tournaments.domain.GameResult;
import org.drooms.tournaments.server.util.Converter;
import org.drooms.tournaments.server.util.Convertible;

@Entity
@Table(name = "GAME_RESULT")
public class GameResultEntity implements Serializable, Comparable<GameResultEntity>, Convertible<GameResult> {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;
    private Integer points;
    @ManyToOne(optional = false)
    @JoinColumns({ @JoinColumn(name = "STRATEGY_GROUPID", referencedColumnName = "GROUPID"),
            @JoinColumn(name = "STRATEGY_ARTIFACTID", referencedColumnName = "ARTIFACTID"),
            @JoinColumn(name = "STRATEGY_VERSION", referencedColumnName = "VERSION") })
    private StrategyEntity strategy;
    @ManyToOne(optional = false)
    private GameEntity game;

    public GameResultEntity() {
    }

    public GameResultEntity(Long id) {
        setId(id);
    }

    public GameResultEntity(StrategyEntity strategy, GameEntity game) {
        setStrategy(strategy);
        setGame(game);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        if (this.id != null) {
            throw new IllegalStateException("Can't change id in persisted GameResult");
        }
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        this.id = id;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        if (this.points != null) {
            throw new IllegalStateException("Points are already set");
        }
        this.points = points;
    }

    public StrategyEntity getStrategy() {
        return strategy;
    }

    public void setStrategy(StrategyEntity strategy) {
        if (this.strategy != null) {
            throw new IllegalStateException("Strategy is already assigned");
        }
        if (strategy == null) {
            throw new IllegalArgumentException("Strategy must not be null");
        }
        this.strategy = strategy;
    }

    public GameEntity getGame() {
        return game;
    }

    public void setGame(GameEntity game) {
        if (this.game != null) {
            throw new IllegalStateException("Game is already assigned");
        }
        if (game == null) {
            throw new IllegalArgumentException("Game must not be null");
        }
        this.game = game;
        game.addGameResult(this);
    }

    @Override
    public GameResult convert(int depth) {
        GameResult result = new GameResult();
        result.setPoints(getPoints() == null ? 0 : getPoints());
        result.setStrategy(Converter.forClass(StrategyEntity.class).setRecurseDepth(depth - 1).convert(getStrategy()));

        return result;
    }

    @Override
    public int compareTo(GameResultEntity o) {
        return points - o.points;
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
        GameResultEntity other = (GameResultEntity) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
