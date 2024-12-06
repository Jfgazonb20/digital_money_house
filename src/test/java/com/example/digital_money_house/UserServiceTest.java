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

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("testuser@example.com")).thenReturn(Optional.empty());
        when(accountRepository.findByCvuOrAlias(anyString(), anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(new Role("USER")));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userService.registerUser(user);

        verify(userRepository, times(1)).save(user);
        verify(accountRepository, times(1)).save(any(Account.class));
        assertThat(user.getPassword()).isEqualTo("encodedPassword");
    }

    @Test
    void registerUser_Failure_UserExistsByUsername() {
        User user = new User();
        user.setUsername("testuser");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(user));
    }

    @Test
    void registerUser_Failure_UserExistsByEmail() {
        User user = new User();
        user.setEmail("testuser@example.com");

        when(userRepository.findByEmail("testuser@example.com")).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(user));
    }

    @Test
    void getUserById_Success() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(1L);

        assertThat(foundUser.getUsername()).isEqualTo("testuser");
    }

    @Test
    void getUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void updateUser_Success() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setEmail("oldemail@example.com");

        User updatedData = new User();
        updatedData.setEmail("newemail@example.com");
        updatedData.setPassword("newpassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(passwordEncoder.encode("newpassword")).thenReturn("encodedPassword");

        User result = userService.updateUser(1L, updatedData);

        assertThat(result.getEmail()).isEqualTo("newemail@example.com");
        assertThat(result.getPassword()).isEqualTo("encodedPassword");
    }

    @Test
    void updateUser_Failure_EmailAlreadyExists() {
        User existingUser = new User();
        existingUser.setId(1L);

        User updatedData = new User();
        updatedData.setEmail("newemail@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail("newemail@example.com")).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistsException.class, () -> userService.updateUser(1L, updatedData));
    }

    @Test
    void updateUser_Failure_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(1L, new User()));
    }
}
