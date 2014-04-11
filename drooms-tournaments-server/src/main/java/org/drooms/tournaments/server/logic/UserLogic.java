package org.drooms.tournaments.server.logic;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.drooms.tournaments.domain.User;
import org.drooms.tournaments.server.data.UserDAO;
import org.drooms.tournaments.server.data.model.UserEntity;

public class UserLogic {
    private UserDAO users;

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

    private String encryptPassword(String password) {
        return new String(Base64.encodeBase64(DigestUtils.sha256(password)));
    }

}
