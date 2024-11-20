package com.example.digital_money_house.Service;

import com.example.digital_money_house.Exception.ResourceNotFoundException;
import com.example.digital_money_house.Exception.UserAlreadyExistsException;
import com.example.digital_money_house.Model.Role;
import com.example.digital_money_house.Model.User;
import com.example.digital_money_house.Repository.RoleRepository;
import com.example.digital_money_house.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("El nombre de usuario ya existe");
        }

        user.setCvu(generateCvu());
        user.setAlias(generateAlias());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Rol USER no encontrado"));

        user.setRoles(Collections.singleton(userRole));
        userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
    }

    public User updateUser(Long id, User updatedUserData) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        if (updatedUserData.getEmail() != null) {
            user.setEmail(updatedUserData.getEmail());
        }

        if (updatedUserData.getAlias() != null) {
            user.setAlias(updatedUserData.getAlias());
        }

        userRepository.save(user);
        return user;
    }

    private String generateCvu() {
        Random random = new Random();
        StringBuilder cvu = new StringBuilder();
        for (int i = 0; i < 22; i++) {
            cvu.append(random.nextInt(10));
        }
        return cvu.toString();
    }

    private String generateAlias() {
        String[] words = {"digital", "money", "house", "wallet", "secure", "bank", "transaction"};
        Random random = new Random();
        return words[random.nextInt(words.length)] + "." +
                words[random.nextInt(words.length)] + "." +
                words[random.nextInt(words.length)];
    }
}
