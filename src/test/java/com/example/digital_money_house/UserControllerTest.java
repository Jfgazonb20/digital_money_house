package com.example.digital_money_house;

import com.example.digital_money_house.Controller.UserController;
import com.example.digital_money_house.Exception.UserAlreadyExistsException;
import com.example.digital_money_house.Exception.ResourceNotFoundException;
import com.example.digital_money_house.Model.User;
import com.example.digital_money_house.Service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private final UserService userService = Mockito.mock(UserService.class);
    private final UserController userController = new UserController(userService);

    @Test
    void registerUser_Success() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("password");

        doNothing().when(userService).registerUser(user);

        ResponseEntity<String> response = userController.registerUser(user);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("Usuario registrado exitosamente");
    }

    @Test
    void registerUser_Failure_UserExists() {
        User user = new User();
        user.setUsername("testuser");

        doThrow(new UserAlreadyExistsException("El nombre de usuario ya existe"))
                .when(userService).registerUser(user);

        ResponseEntity<String> response = userController.registerUser(user);

        assertThat(response.getStatusCodeValue()).isEqualTo(409);
        assertThat(response.getBody()).isEqualTo("El nombre de usuario ya existe");
    }

    @Test
    void getUserById_Success() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        when(userService.getUserById(1L)).thenReturn(user);

        ResponseEntity<?> response = userController.getUserById(1L);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(((User) response.getBody()).getUsername()).isEqualTo("testuser");
    }

    @Test
    void getUserById_NotFound() {
        when(userService.getUserById(1L)).thenThrow(new ResourceNotFoundException("Usuario no encontrado con ID: 1"));

        ResponseEntity<?> response = userController.getUserById(1L);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getBody()).isEqualTo("Usuario no encontrado con ID: 1");
    }

    @Test
    void updateUser_Success() {
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setEmail("newemail@example.com");

        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(updatedUser);

        ResponseEntity<?> response = userController.updateUser(1L, updatedUser);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(((User) response.getBody()).getEmail()).isEqualTo("newemail@example.com");
    }

    @Test
    void updateUser_NotFound() {
        when(userService.updateUser(eq(1L), any(User.class)))
                .thenThrow(new ResourceNotFoundException("Usuario no encontrado con ID: 1"));

        ResponseEntity<?> response = userController.updateUser(1L, new User());

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getBody()).isEqualTo("Usuario no encontrado con ID: 1");
    }
}
