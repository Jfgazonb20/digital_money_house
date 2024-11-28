package com.example.digital_money_house.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String accountNumber; // Número de cuenta único

    @Column(nullable = false)
    private Double balance; // Saldo de la cuenta

    @Column(unique = true, nullable = false)
    private String alias; // Alias único de la cuenta

    @Column(unique = true, nullable = false, length = 22)
    private String cvu; // CVU único de 22 dígitos

    // Relación con el usuario
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Getters y Setters (si no usas Lombok)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getCvu() {
        return cvu;
    }

    public void setCvu(String cvu) {
        this.cvu = cvu;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
