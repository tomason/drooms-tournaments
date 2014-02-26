package cz.schlosserovi.tomas.drooms.tournaments.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Playground implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String name;
    @Column(length = Integer.MAX_VALUE)
    private String source;
    @ManyToOne(optional = false, cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
    private User author;
    @OneToMany(mappedBy = "playground")
    private Collection<Game> games = new LinkedList<>();
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Collection<PlaygroundConfig> configurations = new LinkedList<>();

    public Playground() {
    }

    public Playground(String name, String source) {
        this.name = name;
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Collection<Game> getGames() {
        return games;
    }

    public void setGames(Collection<Game> games) {
        this.games = games;
    }

    public void addGame(Game game) {
        games.add(game);
        game.setPlayground(this);
    }

    public void removeGame(Game game) {
        games.remove(game);
        game.setPlayground(null);
    }

    public Collection<PlaygroundConfig> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(Collection<PlaygroundConfig> configurations) {
        this.configurations = configurations;
    }

    public void addConfiguration(PlaygroundConfig configuration) {
        configurations.add(configuration);
    }

}
