package org.drooms.tournaments.server.logic;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.drooms.tournaments.domain.User;
import org.drooms.tournaments.server.data.UserDAO;
import org.drooms.tournaments.server.data.model.UserEntity;
import org.drooms.tournaments.server.util.Converter;

@ApplicationScoped
public class UserLogic {
    private UserDAO users;

    public UserLogic() {
        this(null);
    }

    @Inject
    public UserLogic(UserDAO users) {
        this.users = users;
    }

    public void registerUser(User user) {
        UserEntity entity = new UserEntity();
        entity.setName(user.getName());
        entity.setPassword(encryptPassword(user.getPassword()));

        users.insertUser(entity);
    }

    public void changePassword(String userName, User user) {
        UserEntity entity = users.getUser(userName);

        if (entity.getName().equals(user.getName())) {
            entity.setPassword(encryptPassword(user.getPassword()));
            users.updateUser(entity);
        }
    }

    public User getUser(String userName) {
        return Converter.forClass(UserEntity.class).convert(users.getUser(userName));
    }

    private String encryptPassword(String password) {
        return new String(Base64.encodeBase64(DigestUtils.sha256(password)));
    }

}
