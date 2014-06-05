package org.drooms.tournaments.domain;

import java.util.Map;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.databinding.client.api.Bindable;

@Portable
@Bindable
public class Playground implements Comparable<Playground> {
    private String name;
    private String source;
    private int maxPlayers;
    private Map<String, String> configuration;

    public Playground() {
    }

    public Playground(String name, String source) {
        this.name = name;
        this.source = source;
    }

    public Playground(String name, String source, int maxPlayers, Map<String, String> configuration) {
        this.name = name;
        this.source = source;
        this.maxPlayers = maxPlayers;
        this.configuration = configuration;
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

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public Map<String, String> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Map<String, String> configuration) {
        this.configuration = configuration;
    }

    @Override
    public int compareTo(Playground o) {
        if (name == null || o.name == null) {
            throw new NullPointerException("Playground name is not set");
        }
        return name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return new StringBuilder("Playground[name='").append(name).append("', maxPlayers='").append(maxPlayers).append("']")
                .toString();
    }
}
