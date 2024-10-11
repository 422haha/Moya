package com.e22e.moya.user.service.user;

import com.e22e.moya.common.entity.Users;
import com.e22e.moya.user.dto.UserNameResponseDto;
import com.e22e.moya.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Optional;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Users findOrCreateUser(String email, String name, String profileImageUrl) {
        Optional<Users> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            log.info("user가 존재합니다. {}", user.get().getName());
            return user.get();
        } else {
            Users newUser = new Users();
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setProfileImageUrl(profileImageUrl);
            log.info("새로운 user 가입, 이름:{}", newUser.getName());
            log.info("새로운 user 가입, email:{}", newUser.getEmail());
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