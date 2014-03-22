package cz.schlosserovi.tomas.drooms.tournaments.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cz.schlosserovi.tomas.drooms.tournaments.domain.GameResult;
import cz.schlosserovi.tomas.drooms.tournaments.util.Converter;
import cz.schlosserovi.tomas.drooms.tournaments.util.Convertible;

@Entity
@Table(name = "GAME_RESULT")
public class GameResultEntity implements Serializable, Comparable<GameResultEntity>, Convertible<GameResult> {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;
    private Integer points;
    private Integer position;
    @ManyToOne(optional = false, cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
    private StrategyEntity strategy;
    @ManyToOne(optional = false, cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
    private GameEntity game;

    public GameResultEntity() {
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

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        if (this.position != null) {
            throw new IllegalStateException("Position is already set");
        }
        this.position = position;
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
        strategy.addGameResult(this);
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
        result.setPoints(getPoints() == null ? -1 : getPoints());
        result.setPosition(getPosition() == null ? -1 : getPosition());
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
