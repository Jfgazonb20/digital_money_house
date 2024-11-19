package com.example.digital_money_house.Controller;

import com.example.digital_money_house.Exception.UserAlreadyExistsException;
import com.example.digital_money_house.Model.User;
import com.example.digital_money_house.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            userService.registerUser(user);
            return ResponseEntity.ok("Usuario registrado exitosamente");
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(409).body(e.getMessage()); // HTTP 409 Conflict
        }
    }

    @GetMapping("/protected")
    public ResponseEntity<String> getProtectedData() {
        return ResponseEntity.ok("Esta es información protegida.");
    }
}
