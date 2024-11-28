package com.example.digital_money_house;

import com.example.digital_money_house.Exception.ResourceNotFoundException;
import com.example.digital_money_house.Exception.UserAlreadyExistsException;
import com.example.digital_money_house.Model.Account;
import com.example.digital_money_house.Model.Role;
import com.example.digital_money_house.Model.User;
import com.example.digital_money_house.Repository.AccountRepository;
import com.example.digital_money_house.Repository.RoleRepository;
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
    private final RoleRepository roleRepository = Mockito.mock(RoleRepository.class);
    private final AccountRepository accountRepository = Mockito.mock(AccountRepository.class);
    private final PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);

    private final UserService userService = new UserService(userRepository, roleRepository, accountRepository, passwordEncoder);

    @Test
    void registerUser_Success() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("password");
        user.setCvu("1234567890123456789012");
        user.setAlias("alias.test");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("testuser@example.com")).thenReturn(Optional.empty());
        when(accountRepository.findByCvuOrAlias("1234567890123456789012", "alias.test")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(new Role("USER")));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.registerUser(user);

        verify(userRepository, times(1)).save(user);
        verify(accountRepository, times(1)).save(any(Account.class));
        assertThat(user.getPassword()).isEqualTo("encodedPassword");
    }

    @Test
    void registerUser_Failure_UserExists() {
        User user = new User();
        user.setUsername("testuser");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.registerUser(user);
        });
    }
}
