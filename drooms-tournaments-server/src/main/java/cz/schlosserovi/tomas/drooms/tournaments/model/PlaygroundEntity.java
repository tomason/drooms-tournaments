package cz.schlosserovi.tomas.drooms.tournaments.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "PLAYGROUND")
public class PlaygroundEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String name;
    private int maxPlayers;
    @Column(length = Integer.MAX_VALUE)
    private String source;
    @ManyToOne(optional = false, cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
    private UserEntity author;
    @OneToMany(mappedBy = "playground")
    private Set<GameEntity> games = new HashSet<>();
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<PlaygroundConfigEntity> configurations = new HashSet<>();

    public PlaygroundEntity() {
    }

    public PlaygroundEntity(UserEntity author, String name, String source) {
        if (author == null) {
            throw new IllegalArgumentException("Author must not be null");
        }
        if (name == null || source == null || name.length() == 0 || source.length() == 0) {
            throw new IllegalArgumentException("Name and Source must not be null nor emty");
        }
        this.name = name;
        this.source = source;
        this.author = author;
        author.addPlayground(this);
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

    public Set<GameEntity> getGames() {
        return Collections.unmodifiableSet(games);
    }

    public void setGames(Set<GameEntity> games) {
        if (games == null) {
            throw new IllegalArgumentException("Games must not be null");
        }
        this.games = games;
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

    public Set<PlaygroundConfigEntity> getConfigurations() {
        return Collections.unmodifiableSet(configurations);
    }

    public void setConfigurations(Set<PlaygroundConfigEntity> configurations) {
        if (configurations == null) {
            throw new IllegalArgumentException("Configurations must not be null");
        }
        this.configurations = configurations;
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
