package me.lozm.domain.user.service;

import lombok.RequiredArgsConstructor;
import me.lozm.domain.user.entity.User;
import me.lozm.domain.user.repository.UserRepository;
import me.lozm.global.code.UseYn;
import me.lozm.global.code.UsersType;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserHelperService {

    private final UserRepository userRepository;


    public Optional<User> findUser(String identifier, UseYn useYn) {
        return userRepository.findByIdentifierAndUse(identifier, useYn);
    }

    public User getUser(String identifier, UseYn useYn) {
        return findUser(identifier, useYn)
                .orElseThrow(() -> new IllegalArgumentException(String.format("존재하지 않는 사용자입니다. 사용자 계정: [%s]", identifier)));
    }

    public Optional<User> findUser(Long userId, UseYn useYn) {
        return userRepository.findByIdAndUse(userId, useYn);
    }

    public User getUser(Long userId, UseYn useYn) {
        if (userId.equals(UsersType.SYSTEM.getCode())) {
            return User.from(UsersType.SYSTEM);
        } else {
            return findUser(userId, useYn)
                    .orElseThrow(() -> new IllegalArgumentException(String.format("존재하지 않는 사용자입니다. 사용자 ID: [%d]", userId)));
        }
    }

}
