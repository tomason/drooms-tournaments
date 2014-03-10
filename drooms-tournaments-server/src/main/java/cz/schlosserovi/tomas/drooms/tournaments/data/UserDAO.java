package cz.schlosserovi.tomas.drooms.tournaments.data;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.schlosserovi.tomas.drooms.tournaments.model.UserEntity;

@Stateless
public class UserDAO extends AbstractDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDAO.class);

    public UserEntity insertUser(String name, String password) {
        String salt = generateSaltString();
        String dbPassword = encryptPassword(password, salt);
        UserEntity entity = new UserEntity(name, salt, dbPassword);

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

    private String generateSaltString() {
        Random r = new SecureRandom();
        byte[] salt = new byte[32];
        r.nextBytes(salt);
        return Base64.encodeBase64String(salt);
    }
    
    private String encryptPassword(String password, String salt) {
        byte[] pwd = Base64.decodeBase64(password);
        byte[] slt = Base64.decodeBase64(salt);
        byte[] base = new byte[pwd.length + slt.length];
        int index = 0;
        for (byte b : pwd) {
            base[index++] = b;
        }
        for (byte b : slt) {
            base[index++] = b;
        }
        return Base64.encodeBase64String(DigestUtils.md5(base));
    }
}
