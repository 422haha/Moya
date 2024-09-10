package com.e22e.moya.user.service;

import com.e22e.moya.common.entity.Users;
import com.e22e.moya.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Users findOrCreateUser(String email, String name, String oauthProvider, String oauthId,
        String profileImageUrl) {
        Optional<Users> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            return user.get();
        } else {
            Users newUser = new Users();
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setOauthProvider(oauthProvider);
            newUser.setOauthId(oauthId);
            newUser.setProfileImageUrl(profileImageUrl);
            return userRepository.save(newUser);
        }
    }
}