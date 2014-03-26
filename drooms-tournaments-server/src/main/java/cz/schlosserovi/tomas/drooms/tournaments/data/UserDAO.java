package cz.schlosserovi.tomas.drooms.tournaments.data;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.schlosserovi.tomas.drooms.tournaments.model.UserEntity;

@Stateless
public class UserDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDAO.class);
    private EntityManager em;

    public UserDAO() {
    }

    @Inject
    public UserDAO(EntityManager em) {
        this.em = em;
    }

    public UserEntity insertUser(String name, String password) {
        String dbPassword = encryptPassword(password);
        UserEntity entity = new UserEntity(name, dbPassword);

        em.persist(entity);
        em.flush();

        return entity;
    }

    public boolean loginUser(String name, String password) throws IllegalArgumentException {
        UserEntity entity = getUser(name);
        if (entity == null) {
            throw new IllegalArgumentException("Given user is not registered");
        }
        try {
            String encryptedPwd = encryptPassword(password);

            return encryptedPwd.equals(entity.getPassword());
        } catch (Exception ex) {
            LOGGER.error("Unable to encrypt password", ex);
            return false;
        }
    }

    public UserEntity getUser(String name) {
        return em.find(UserEntity.class, name);
    }

    public UserEntity getUserWithTournamentResults(String name) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<UserEntity> query = builder.createQuery(UserEntity.class);

        Root<UserEntity> user = query.from(UserEntity.class);
        user.fetch("tournamentResults", JoinType.LEFT);
        query.select(user).where(builder.equal(user.get("name"), name));

        return em.createQuery(query).getSingleResult();
    }

    public List<UserEntity> getUsers() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<UserEntity> query = builder.createQuery(UserEntity.class);

        query.select(query.from(UserEntity.class));

        return em.createQuery(query).getResultList();
    }

    private String encryptPassword(String password) {
        return new String(Base64.encodeBase64(DigestUtils.sha256(password)));
    }
}
