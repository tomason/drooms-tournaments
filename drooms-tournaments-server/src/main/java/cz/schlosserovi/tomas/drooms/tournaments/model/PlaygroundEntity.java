package cz.schlosserovi.tomas.drooms.tournaments.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import cz.schlosserovi.tomas.drooms.tournaments.domain.Playground;
import cz.schlosserovi.tomas.drooms.tournaments.util.Convertible;
import cz.schlosserovi.tomas.drooms.tournaments.util.NullForbiddingSet;

@Entity
@Table(name = "PLAYGROUND")
public class PlaygroundEntity implements Serializable, Convertible<Playground> {
    private static final long serialVersionUID = 1L;

    @Id
    private String name;
    private int maxPlayers;
    @Column(length = 100_000)
    private String source;
    @ManyToOne(optional = false, cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
    private UserEntity author;
    @OneToMany(mappedBy = "playground")
    private Collection<GameEntity> games = new NullForbiddingSet<>();
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Collection<PlaygroundConfigEntity> configurations = new NullForbiddingSet<>();
    @ManyToMany(mappedBy = "playgrounds")
    private Collection<TournamentEntity> tournaments = new NullForbiddingSet<>();

    public PlaygroundEntity() {
    }

    public PlaygroundEntity(String name) {
        setName(name);
    }

    public PlaygroundEntity(UserEntity author, String name, String source) {
        setAuthor(author);
        setName(name);
        setSource(source);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (this.name != null) {
            throw new IllegalStateException("Name is already set");
        }
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("Name must not be null nor empty");
        }
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        if (source == null || source.length() == 0) {
            throw new IllegalArgumentException("Source must not be null nor empty");
        }
        this.source = source;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public void recountMaxPlayers() {
        this.maxPlayers = source.length() - source.replace("@", "").length();
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
        author.addPlayground(this);
    }

    public Collection<GameEntity> getGames() {
        return Collections.unmodifiableCollection(games);
    }

    public void setGames(Collection<GameEntity> games) {
        if (games == null) {
            throw new IllegalArgumentException("Games must not be null");
        }
        this.games.clear();
        this.games.addAll(games);
    }

    public void addGame(GameEntity game) {
        if (game == null) {
            throw new IllegalArgumentException("Game must not be null");
        }
        if (!equals(game.getPlayground())) {
            throw new IllegalArgumentException("Game must not be assigned to another Playground");
        }
        games.add(game);
    }

    public Collection<PlaygroundConfigEntity> getConfigurations() {
        return Collections.unmodifiableCollection(configurations);
    }

    public void setConfigurations(Collection<PlaygroundConfigEntity> configurations) {
        if (configurations == null) {
            throw new IllegalArgumentException("Configurations must not be null");
        }
        this.configurations.clear();
        this.configurations.addAll(configurations);
    }

    public void addConfiguration(PlaygroundConfigEntity configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException("Configuration must not be null");
        }
        configurations.add(configuration);
    }

    public void removeConfiguration(PlaygroundConfigEntity configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException("Configuration must not be null");
        }
        configurations.remove(configuration);
    }

    public Collection<TournamentEntity> getTournaments() {
        return Collections.unmodifiableCollection(tournaments);
    }

    public void setTournaments(Collection<TournamentEntity> tournaments) {
        if (tournaments == null) {
            throw new IllegalArgumentException("Tournaments must not be null");
        }
        this.tournaments.clear();
        this.tournaments.addAll(tournaments);
    }

    public void addTournament(TournamentEntity tournament) {
        if (tournament == null) {
            throw new IllegalArgumentException("Tournament must not be null");
        }
        if (!tournament.getPlaygrounds().contains(this)) {
            throw new IllegalArgumentException("This playground is not registered in Tournament");
        }
        tournaments.add(tournament);
    }

    @Override
    public Playground convert(int depth) {
        Playground result = new Playground();
        result.setName(getName());
        result.setSource(getSource());
        result.setMaxPlayers(getMaxPlayers());

        if (depth > 0) {
            Properties config = new Properties();
            for (PlaygroundConfigEntity entity : getConfigurations()) {
                config.setProperty(entity.getKey(), entity.getValue());
            }
            result.setConfiguration(config);
        }

        return result;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        PlaygroundEntity other = (PlaygroundEntity) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
