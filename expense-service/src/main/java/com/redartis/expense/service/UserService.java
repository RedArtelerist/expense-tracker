package com.redartis.expense.service;

import com.redartis.dto.account.AccountDataDto;
import com.redartis.expense.exception.UserNotFoundException;
import com.redartis.expense.mapper.UserMapper;
import com.redartis.expense.model.User;
import com.redartis.expense.repository.UserRepository;
import com.redartis.expense.security.dto.TelegramAuthRequest;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public long getUsersCount() {
        return userRepository.count();
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void saveUser(AccountDataDto accountData) {
        try {
            getUserById(accountData.getUserId());
        } catch (UserNotFoundException e) {
            User user = new User();
            user.setId(accountData.getUserId());
            user.setUsername("Anonymous");
            userRepository.save(user);
        }
    }

    @Transactional
    public void saveUser(TelegramAuthRequest telegramAuthRequest) {
        try {
            User user = getUserById(telegramAuthRequest.id());
            if (!(Objects.equals(user.getUsername(), telegramAuthRequest.username())
                    && Objects.equals(user.getFirstName(), telegramAuthRequest.firstName())
                    && Objects.equals(user.getLastName(), telegramAuthRequest.lastName())
                    && Objects.equals(user.getPhotoUrl(), telegramAuthRequest.photoUrl()))) {
                userRepository.updateUserDetailsByUserId(telegramAuthRequest.id(),
                        telegramAuthRequest.username(),
                        telegramAuthRequest.firstName(),
                        telegramAuthRequest.lastName(),
                        telegramAuthRequest.photoUrl(),
                        telegramAuthRequest.authDate());
            }
        } catch (UserNotFoundException e) {
            userRepository.save(userMapper.mapTelegramAuthToUser(telegramAuthRequest));
        }
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(
                        "User with id " + id + " does not exist")
                );
    }

    public User getUserByIdWithAccountCategories(Long id) {
        return userRepository.findByIdWithAccount(id)
                .orElseThrow(() -> new UserNotFoundException(
                        "User with id " + id + " does not exist")
                );
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public List<User> getUsersByIds(List<Long> userIds) {
        return userRepository.findAllUsersByIds(userIds);
    }
}
