package com.example.digital_money_house.Service;

import com.example.digital_money_house.Exception.ResourceNotFoundException;
import com.example.digital_money_house.Exception.UserAlreadyExistsException;
import com.example.digital_money_house.Model.Account;
import com.example.digital_money_house.Model.Role;
import com.example.digital_money_house.Model.User;
import com.example.digital_money_house.Repository.AccountRepository;
import com.example.digital_money_house.Repository.RoleRepository;
import com.example.digital_money_house.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
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
        // Validar que el nombre de usuario no exista
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("El nombre de usuario ya existe");
        }

        // Validar que el email no exista
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("El email ya está registrado");
        }

        // Validar que el CVU y alias no existan
        if (accountRepository.findByCvuOrAlias(user.getCvu(), user.getAlias()).isPresent()) {
            throw new UserAlreadyExistsException("El CVU o alias ya está registrado");
        }

        // Encriptar contraseña
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Asignar rol por defecto
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Rol USER no encontrado"));

        user.setRoles(Collections.singleton(userRole));

        // Guardar el usuario
        User savedUser = userRepository.save(user);

        // Crear automáticamente una cuenta vinculada
        Account account = new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setBalance(0.0); // Saldo inicial
        account.setCvu(user.getCvu()); // Usar el CVU proporcionado
        account.setAlias(user.getAlias()); // Usar el alias proporcionado
        account.setUser(savedUser); // Asociar la cuenta al usuario

        accountRepository.save(account);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
    }

    public User updateUser(Long id, User updatedUserData) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        // Actualizar email y validar unicidad
        if (updatedUserData.getEmail() != null && !updatedUserData.getEmail().equals(user.getEmail())) {
            if (userRepository.findByEmail(updatedUserData.getEmail()).isPresent()) {
                throw new UserAlreadyExistsException("El email ya está registrado");
            }
            user.setEmail(updatedUserData.getEmail());
        }

        // Actualizar alias (si aplica)
        if (updatedUserData.getAlias() != null) {
            user.setAlias(updatedUserData.getAlias());
        }

        userRepository.save(user);
        return user;
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
