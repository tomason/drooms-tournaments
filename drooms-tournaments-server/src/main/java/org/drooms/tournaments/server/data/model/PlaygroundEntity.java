package org.drooms.tournaments.server.data.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.drooms.tournaments.domain.Playground;
import org.drooms.tournaments.server.util.Convertible;
import org.drooms.tournaments.server.util.NullForbiddingSet;

@Entity
@Table(name = "PLAYGROUND")
public class PlaygroundEntity implements Serializable, Convertible<Playground> {
    private static final long serialVersionUID = 1L;

    @Id
    private String name;
    private int maxPlayers;
    @Column(length = 100_000)
    private String source;
    @ManyToOne(optional = false)
    @JoinColumn(name = "AUTHOR_NAME")
    private UserEntity author;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "PLAYGROUND_NAME", nullable = false)
    private Set<PlaygroundConfigEntity> configurations = new NullForbiddingSet<>();

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

    @Override
    public Playground convert(int depth) {
        Playground result = new Playground();
        result.setName(getName());
        result.setSource(getSource());
        result.setMaxPlayers(getMaxPlayers());

        // properties are eagerly fetched, no need to use depth
        Map<String, String> config = new TreeMap<>();
        for (PlaygroundConfigEntity entity : getConfigurations()) {
            config.put(entity.getKey(), entity.getValue());
        }
        result.setConfiguration(config);

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
