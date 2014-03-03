package cz.schlosserovi.tomas.drooms.tournaments.data;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.slf4j.LoggerFactory;

import cz.schlosserovi.tomas.drooms.tournaments.model.UserEntity;

@Stateless
public class UserDAO extends AbstractDAO {
    private static final int PASSWORD_SIZE = 128;

    public UserEntity insertUser(String name, byte[] password) {
        byte[] salt = getSalt(password);
        byte[] dbPassword = encryptPassword(password, salt);
        UserEntity entity = new UserEntity();
        entity.setName(name);
        entity.setSalt(salt);
        entity.setPassword(dbPassword);

        em.persist(entity);
        em.flush();

        return entity;
    }

    public boolean loginUser(String name, byte[] password) {
        UserEntity entity = getUser(name);
        byte[] dbPassword = encryptPassword(password, entity.getSalt());

        return Arrays.equals(entity.getPassword(), dbPassword);
    }

    public UserEntity getUser(String name) {
        return em.find(UserEntity.class, name);
    }

    public List<UserEntity> getUsers() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<UserEntity> query = builder.createQuery(UserEntity.class);

        query.select(query.from(UserEntity.class));

        return em.createQuery(query).getResultList();
    }

    private byte[] getSalt(byte[] password) {
        int saltLength = PASSWORD_SIZE - password.length;
        byte[] salt = new byte[saltLength];

        Random random = new SecureRandom();
        random.nextBytes(salt);

        return salt;
    }

    private byte[] encryptPassword(byte[] password, byte[] salt) {
        byte[] base = new byte[PASSWORD_SIZE];
        int index = 0;
        for (int i = 0; i < password.length; i++) {
            base[index++] = password[i];
        }
        for (int i = 0; i < salt.length; i++) {
            base[index++] = salt[i];
        }
        
        try {
            return MessageDigest.getInstance("MD5").digest(base);
        } catch (NoSuchAlgorithmException ex) {
            LoggerFactory.getLogger(UserDAO.class).error("Unable to find MD5 algorithm", ex);
            return null;
        }
    }
}
