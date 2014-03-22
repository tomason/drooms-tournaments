package cz.schlosserovi.tomas.drooms.tournaments.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import cz.schlosserovi.tomas.drooms.tournaments.domain.GAV;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Strategy;
import cz.schlosserovi.tomas.drooms.tournaments.util.Converter;
import cz.schlosserovi.tomas.drooms.tournaments.util.Convertible;
import cz.schlosserovi.tomas.drooms.tournaments.util.NullForbiddingSet;

@Entity
@IdClass(GAV.class)
@Table(name = "STRATEGY")
public class StrategyEntity implements Serializable, Convertible<Strategy> {
    private static final long serialVersionUID = 1L;

    @Id
    private String groupId;
    @Id
    private String artifactId;
    @Id
    private String version;
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
        return new GAV(groupId, artifactId, version);
    }

    public void setGav(GAV gav) {
        if (this.groupId != null && this.artifactId != null && this.version != null) {
            throw new IllegalStateException("GAV is already set");
        }
        if (gav == null || gav.getGroupId() == null || gav.getArtifactId() == null || gav.getVersion() == null) {
            throw new IllegalArgumentException("GAV must not be null");
        }
        this.groupId = gav.getGroupId();
        this.artifactId = gav.getArtifactId();
        this.version = gav.getVersion();
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
    public Strategy convert(int depth) {
        Strategy result = new Strategy();
        result.setActive(isActive());
        result.setGav(getGav());
        result.setPlayer(Converter.forClass(UserEntity.class).setRecurseDepth(depth - 1).convert(getAuthor()));

        return result;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((artifactId == null) ? 0 : artifactId.hashCode());
        result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
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
        if (artifactId == null) {
            if (other.artifactId != null)
                return false;
        } else if (!artifactId.equals(other.artifactId))
            return false;
        if (groupId == null) {
            if (other.groupId != null)
                return false;
        } else if (!groupId.equals(other.groupId))
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        return true;
    }

}
