package cz.schlosserovi.tomas.drooms.tournaments.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
@IdClass(GAV.class)
public class Strategy implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String groupId;
    @Id
    private String artifactId;
    @Id
    private String version;
    @ManyToOne(optional = false, cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
    private User author;
    @OneToMany(mappedBy = "strategy", cascade = CascadeType.ALL)
    private Collection<GameResult> gameResults = new LinkedList<>();

    public Strategy() {
    }

    public Strategy(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Collection<GameResult> getGameResults() {
        return gameResults;
    }

    public void setGameResults(Collection<GameResult> gameResults) {
        this.gameResults = gameResults;
    }

    public void addGameResult(GameResult gameResult) {
        gameResults.add(gameResult);
        gameResult.setStrategy(this);
    }

    public void removeGameResult(GameResult gameResult) {
        gameResults.remove(gameResult);
        gameResult.setStrategy(null);
    }
}
