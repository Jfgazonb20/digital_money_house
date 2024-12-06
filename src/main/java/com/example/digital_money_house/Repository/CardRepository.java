package com.example.digital_money_house.Repository;

import com.example.digital_money_house.Model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByAccountId(Long accountId);
    Optional<Card> findByCardNumber(String cardNumber);

}
