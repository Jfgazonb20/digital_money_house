package com.example.digital_money_house;

import com.example.digital_money_house.Controller.UserController;
import com.example.digital_money_house.Exception.UserAlreadyExistsException;
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

        // Simula la excepción UserAlreadyExistsException
        doThrow(new UserAlreadyExistsException("El nombre de usuario ya existe")).when(userService).registerUser(user);

        ResponseEntity<String> response = userController.registerUser(user);

        assertThat(response.getStatusCodeValue()).isEqualTo(409); // HTTP 409 Conflict
        assertThat(response.getBody()).isEqualTo("El nombre de usuario ya existe");
    }
}
