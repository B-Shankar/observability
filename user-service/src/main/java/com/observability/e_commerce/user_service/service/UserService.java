package com.observability.e_commerce.user_service.service;

import com.observability.e_commerce.user_service.entity.User;
import com.observability.e_commerce.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public User save(User user) {
        log.info("Saving user: {}", user.getName());
        return userRepository.save(user);
    }

    public User getById(Long id) {
        log.info("Fetching user with id: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

}
