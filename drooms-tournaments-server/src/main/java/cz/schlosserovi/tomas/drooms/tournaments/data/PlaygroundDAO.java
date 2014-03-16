package cz.schlosserovi.tomas.drooms.tournaments.data;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import cz.schlosserovi.tomas.drooms.tournaments.model.PlaygroundConfigEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.PlaygroundEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.TournamentEntity;
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
        PlaygroundEntity playground = getPlayground(playgroundName);
        for (String key : playgroundConfig.stringPropertyNames()) {
            PlaygroundConfigEntity config = new PlaygroundConfigEntity(key, playgroundConfig.getProperty(key));
            playground.addConfiguration(config);
            em.persist(config);
        }

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

    public PlaygroundEntity getPlaygroundWithTournaments(String name) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<PlaygroundEntity> query = builder.createQuery(PlaygroundEntity.class);

        Root<PlaygroundEntity> playground = query.from(PlaygroundEntity.class);
        playground.fetch("tournaments", JoinType.LEFT);
        query.select(playground).where(builder.equal(playground.get("name"), name));

        return em.createQuery(query).getSingleResult();
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

        Root<PlaygroundEntity> playground = query.from(PlaygroundEntity.class);
        query.select(playground).where(builder.equal(playground.get("author"), author));

        return em.createQuery(query).getResultList();
    }

    public List<PlaygroundEntity> getPlaygrounds(TournamentEntity tournament) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<PlaygroundEntity> query = builder.createQuery(PlaygroundEntity.class);

        Root<PlaygroundEntity> playground = query.from(PlaygroundEntity.class);
        query.select(playground).where(builder.isMember(tournament, playground.<Collection<TournamentEntity>>get("tournaments")));

        return em.createQuery(query).getResultList();
    }
}
