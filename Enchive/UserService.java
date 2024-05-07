package net.skhu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import net.skhu.entity.UserEntity;
import net.skhu.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void createUser(UserEntity userEntity) {
        userRepository.save(userEntity);
    }

    public boolean authenticate(String userid, String password) {
        UserEntity user = userRepository.findByUserid(userid);
        if (user != null && user.getPw().equals(password)) {
            return true;
        }
        return false;
    }

    public void updateNickname(String userid, String newNickname) {
        UserEntity userEntity = userRepository.findByUserid(userid);
        if (userEntity != null) {
            userEntity.setNickname(newNickname);
            userRepository.save(userEntity);
        }


    }
    public UserEntity findByUserid(String userid) {
        return userRepository.findByUserid(userid);
    }

    public void changePassword(String userid, String newPassword) {
        UserEntity userEntity = userRepository.findByUserid(userid);
        if (userEntity != null) {
            userEntity.setPw(newPassword);
            userRepository.save(userEntity);
        }
    }

}
