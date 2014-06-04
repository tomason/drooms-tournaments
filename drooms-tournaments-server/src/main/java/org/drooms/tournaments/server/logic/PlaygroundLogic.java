package org.drooms.tournaments.server.logic;

import java.util.Collection;
import java.util.Map.Entry;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.drooms.tournaments.domain.Playground;
import org.drooms.tournaments.server.data.PlaygroundDAO;
import org.drooms.tournaments.server.data.UserDAO;
import org.drooms.tournaments.server.data.model.PlaygroundConfigEntity;
import org.drooms.tournaments.server.data.model.PlaygroundEntity;
import org.drooms.tournaments.server.util.Converter;

@ApplicationScoped
public class PlaygroundLogic {
    private UserDAO users;
    private PlaygroundDAO playgrounds;

    public PlaygroundLogic() {
    }

    @Inject
    public PlaygroundLogic(UserDAO users, PlaygroundDAO playgrounds) {
        this.users = users;
        this.playgrounds = playgrounds;
    }

    public Collection<Playground> getAllPlaygrounds() {
        return getConverter().convert(playgrounds.getPlaygrounds());
    }

    public Collection<Playground> getUserPlaygrounds(String userName) {
        return getConverter().convert(playgrounds.getPlaygrounds(users.getUser(userName)));
    }

    public void insertPlayground(String userName, Playground playground) {
        PlaygroundEntity entity = new PlaygroundEntity();
        entity.setAuthor(users.getUser(userName));
        entity.setName(playground.getName());
        entity.setSource(playground.getSource());
        entity.recountMaxPlayers();

        for (Entry<String, String> entry : playground.getConfiguration().entrySet()) {
            PlaygroundConfigEntity config = new PlaygroundConfigEntity();
            config.setKey(entry.getKey());
            config.setValue(entry.getValue());
            entity.addConfiguration(config);
        }

        playgrounds.insertPlayground(entity);
    }

    private Converter<PlaygroundEntity, Playground> getConverter() {
        return getConverter(0);
    }

    private Converter<PlaygroundEntity, Playground> getConverter(int depth) {
        return Converter.forClass(PlaygroundEntity.class).setRecurseDepth(depth);
    }
}
