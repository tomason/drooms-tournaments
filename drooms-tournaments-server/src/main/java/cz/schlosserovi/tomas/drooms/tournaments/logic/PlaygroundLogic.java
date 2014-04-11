package cz.schlosserovi.tomas.drooms.tournaments.logic;

import java.util.Collection;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import cz.schlosserovi.tomas.drooms.tournaments.data.PlaygroundDAO;
import cz.schlosserovi.tomas.drooms.tournaments.data.UserDAO;
import cz.schlosserovi.tomas.drooms.tournaments.data.model.PlaygroundConfigEntity;
import cz.schlosserovi.tomas.drooms.tournaments.data.model.PlaygroundEntity;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Playground;
import cz.schlosserovi.tomas.drooms.tournaments.util.Converter;

@RequestScoped
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

        for (String key : playground.getConfiguration().stringPropertyNames()) {
            PlaygroundConfigEntity config = new PlaygroundConfigEntity();
            config.setKey(key);
            config.setValue(playground.getConfiguration().getProperty(key));
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
