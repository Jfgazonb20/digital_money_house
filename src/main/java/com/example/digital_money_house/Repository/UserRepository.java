package com.example.digital_money_house.Repository;

import com.example.digital_money_house.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Buscar por nombre de usuario
    Optional<User> findByUsername(String username);

    // Buscar por email
    Optional<User> findByEmail(String email);

    // Buscar por CVU
    Optional<User> findByCvu(String cvu);

    // Buscar por alias
    Optional<User> findByAlias(String alias);
}
