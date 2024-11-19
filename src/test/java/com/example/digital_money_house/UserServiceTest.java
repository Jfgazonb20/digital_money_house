package com.example.digital_money_house;

import com.example.digital_money_house.Exception.UserAlreadyExistsException;
import com.example.digital_money_house.Model.User;
import com.example.digital_money_house.Repository.UserRepository;
import com.example.digital_money_house.Service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
    private final UserService userService = new UserService(userRepository, passwordEncoder);

    @Test
    void registerUser_Success() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("password");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.registerUser(user);

        verify(userRepository, times(1)).save(user);
        verify(passwordEncoder, times(1)).encode("password");
        assertThat(user.getPassword()).isEqualTo("encodedPassword");
    }

    @Test
    void registerUser_Failure_UserExists() {
        User user = new User();
        user.setUsername("testuser");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> {
            userService.registerUser(user);
        });

        assertThat(exception.getMessage()).isEqualTo("El nombre de usuario ya existe");
        verify(userRepository, never()).save(any(User.class));
    }
}
