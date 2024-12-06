package com.example.digital_money_house.Service;

import com.example.digital_money_house.Exception.ResourceNotFoundException;
import com.example.digital_money_house.Exception.UserAlreadyExistsException;
import com.example.digital_money_house.Model.Account;
import com.example.digital_money_house.Model.Role;
import com.example.digital_money_house.Model.User;
import com.example.digital_money_house.Repository.AccountRepository;
import com.example.digital_money_house.Repository.RoleRepository;
import com.example.digital_money_house.Repository.UserRepository;
import com.example.digital_money_house.Util.AliasGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Random;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("El nombre de usuario ya existe");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("El email ya está registrado");
        }

        String alias = AliasGenerator.generateAlias();
        String cvu = AliasGenerator.generateCvu();
        while (accountRepository.findByCvuOrAlias(cvu, alias).isPresent()) {
            alias = AliasGenerator.generateAlias();
            cvu = AliasGenerator.generateCvu();
        }

        // Asignar alias y CVU al usuario
        user.setAlias(alias);
        user.setCvu(cvu);

        // Encriptar contraseña
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Asignar rol por defecto
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Rol USER no encontrado"));

        user.setRoles(Collections.singleton(userRole));

        // Guardar el usuario
        User savedUser = userRepository.save(user);

        // Crear automáticamente una cuenta vinculada
        createAccountForUser(savedUser, cvu, alias);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
    }

    public User updateUser(Long id, User updatedUserData) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        // Validar y actualizar los campos que se quieren modificar
        if (updatedUserData.getEmail() != null && !updatedUserData.getEmail().equals(user.getEmail())) {
            if (userRepository.findByEmail(updatedUserData.getEmail()).isPresent()) {
                throw new UserAlreadyExistsException("El email ya está registrado.");
            }
            user.setEmail(updatedUserData.getEmail());
        }

        if (updatedUserData.getUsername() != null && !updatedUserData.getUsername().equals(user.getUsername())) {
            if (userRepository.findByUsername(updatedUserData.getUsername()).isPresent()) {
                throw new UserAlreadyExistsException("El nombre de usuario ya existe.");
            }
            user.setUsername(updatedUserData.getUsername());
        }

        // Actualizar contraseña (si aplica)
        if (updatedUserData.getPassword() != null && !updatedUserData.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(updatedUserData.getPassword()));
        }

        // Guardar los cambios
        return userRepository.save(user);
    }


    private void createAccountForUser(User user, String cvu, String alias) {
        Account account = new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setBalance(0.0); // Saldo inicial
        account.setCvu(cvu); // Usar el CVU proporcionado
        account.setAlias(alias); // Usar el alias proporcionado
        account.setUser(user); // Asociar la cuenta al usuario

        accountRepository.save(account);
    }

    private String generateAccountNumber() {
        Random random = new Random();
        StringBuilder accountNumber = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            accountNumber.append(random.nextInt(10));
        }
        return accountNumber.toString();
    }
}
