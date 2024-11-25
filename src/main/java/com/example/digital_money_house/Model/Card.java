package com.example.digital_money_house.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cardNumber;

    private String cardHolderName;

    private String cardType; // Crédito o Débito

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
