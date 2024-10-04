package com.e22e.moya.user.service.user;

import com.e22e.moya.common.entity.Users;
import com.e22e.moya.user.dto.UserNameResponseDto;
import com.e22e.moya.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Users findOrCreateUser(String email, String name,String profileImageUrl) {
        Optional<Users> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            return user.get();
        } else {
            Users newUser = new Users();
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setProfileImageUrl(profileImageUrl);
            return userRepository.save(newUser);
        }
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public UserNameResponseDto getUserName(Long userId) {
        Users user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return new UserNameResponseDto(user.getName(), user.getProfileImageUrl());
    }

    @Override
    public String getEmailById(Long userId) {
        Users user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return user.getEmail();
    }
}