package cz.schlosserovi.tomas.drooms.tournaments.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String name;
    @OneToMany(mappedBy = "author")
    private Collection<Playground> playgrounds = new LinkedList<>();
    @OneToMany(mappedBy = "author")
    private Collection<Strategy> strategies = new LinkedList<>();

    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Playground> getPlaygrounds() {
        return playgrounds;
    }

    public void setPlaygrounds(Collection<Playground> playgrounds) {
        this.playgrounds = playgrounds;
    }

    public void addPlayground(Playground playground) {
        playgrounds.add(playground);
        playground.setAuthor(this);
    }

    public void removePlayground(Playground playground) {
        playgrounds.remove(playground);
        playground.setAuthor(null);
    }

    public Collection<Strategy> getStrategies() {
        return strategies;
    }

    public void setStrategies(Collection<Strategy> strategies) {
        this.strategies = strategies;
    }

    public void addStrategy(Strategy strategy) {
        strategies.add(strategy);
        strategy.setAuthor(this);
    }

    public void removeStrategy(Strategy strategy) {
        strategies.remove(strategy);
        strategy.setAuthor(null);
    }

}
