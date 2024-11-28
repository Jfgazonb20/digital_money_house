package com.example.digital_money_house.Repository;

import com.example.digital_money_house.Model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    // Buscar cuenta por ID
    Optional<Account> findById(Long id);

    // Buscar cuenta por CVU o Alias
    Optional<Account> findByCvuOrAlias(String cvu, String alias);

    // Nuevo: Buscar cuenta por Usuario
    Optional<Account> findByUserId(Long userId);
}
