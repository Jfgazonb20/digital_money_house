package com.example.digital_money_house.Service;

import com.example.digital_money_house.Model.User;
import com.example.digital_money_house.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.digital_money_house.Exception.UserAlreadyExistsException;

import java.util.Random;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("El nombre de usuario ya existe");
        }

        user.setCvu(generateCvu());
        user.setAlias(generateAlias());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    private String generateCvu() {
        Random random = new Random();
        StringBuilder cvu = new StringBuilder();
        for (int i = 0; i < 22; i++) {
            cvu.append(random.nextInt(10));  // Genera un dígito aleatorio (0-9)
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
