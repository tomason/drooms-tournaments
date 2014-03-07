package cz.schlosserovi.tomas.drooms.tournaments.data;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.schlosserovi.tomas.drooms.tournaments.model.UserEntity;

@Stateless
public class UserDAO extends AbstractDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDAO.class);
    private static final char[] SALT_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890=%?,.:-_§!".toCharArray();
    private static final int PASSWORD_SIZE = 128;

    public UserEntity insertUser(String name, String password) {
        String salt = getSalt(password);
        String dbPassword = encryptPassword(password, salt);
        UserEntity entity = new UserEntity();
        entity.setName(name);
        entity.setSalt(salt);
        entity.setPassword(dbPassword);

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
            String encryptedPwd = encryptPassword(password, entity.getSalt());

            return encryptedPwd.equals(entity.getPassword());
        } catch (Exception ex) {
            LOGGER.error("Unable to encrypt password", ex);
            return false;
        }
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

    private String getSalt(String password) {
        StringBuilder sb = new StringBuilder();

        Random random = new SecureRandom();
        while (sb.length() - password.length() < PASSWORD_SIZE) {
            sb.append(SALT_CHARS[random.nextInt(SALT_CHARS.length)]);
        }
        
        return sb.toString();
    }

    private String encryptPassword(String password, String salt) {
        String base = password + salt;
        byte[] bytes = base.getBytes(StandardCharsets.US_ASCII);
        try {
            return new String(MessageDigest.getInstance("MD5").digest(bytes));
        } catch (NoSuchAlgorithmException ex) {
            LoggerFactory.getLogger(UserDAO.class).error("Unable to find MD5 algorithm", ex);
            return null;
        }
    }
}
