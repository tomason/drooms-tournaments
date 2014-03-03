package cz.schlosserovi.tomas.drooms.tournaments.data;

import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import cz.schlosserovi.tomas.drooms.tournaments.model.PlaygroundConfigEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.PlaygroundEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.UserEntity;

@Stateless
public class PlaygroundDAO extends AbstractDAO {
    @Inject
    private UserDAO users;

    public PlaygroundEntity insertPlayground(String userName, String name, String source) {
        PlaygroundEntity result = new PlaygroundEntity();
        result.setName(name);
        result.setSource(source);
        result.recountMaxPlayers();
        result.setAuthor(users.getUser(userName));

        em.persist(result);
        em.flush();

        return result;
    }

    public void setPlaygroundConfiguration(String playgroundName, Properties playgroundConfig) {
        Set<PlaygroundConfigEntity> config = new HashSet<>();
        for (String key : playgroundConfig.stringPropertyNames()) {
            config.add(new PlaygroundConfigEntity(key, playgroundConfig.getProperty(key)));
        }
        PlaygroundEntity playground = getPlayground(playgroundName);
        playground.setConfigurations(config);

        em.merge(playground);
        em.flush();
    }

    public void setPlaygroundSource(String name, String source) {
        PlaygroundEntity entity = getPlayground(name);
        entity.setSource(source);
        entity.recountMaxPlayers();

        em.merge(entity);
        em.flush();
    }

    public PlaygroundEntity getPlayground(String name) {
        return em.find(PlaygroundEntity.class, name);
    }

    public List<PlaygroundEntity> getPlaygrounds() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<PlaygroundEntity> query = builder.createQuery(PlaygroundEntity.class);

        query.select(query.from(PlaygroundEntity.class));

        return em.createQuery(query).getResultList();
    }

    public List<PlaygroundEntity> getPlaygrounds(UserEntity author) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<PlaygroundEntity> query = builder.createQuery(PlaygroundEntity.class);

        Root<PlaygroundEntity> gameResult = query.from(PlaygroundEntity.class);
        query.select(gameResult).where(builder.equal(gameResult.get("author"), author));

        return em.createQuery(query).getResultList();
    }
}
