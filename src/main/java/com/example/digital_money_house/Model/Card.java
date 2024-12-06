package com.example.digital_money_house.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Data
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El número de tarjeta es obligatorio.")
    @Pattern(regexp = "\\d{16}", message = "El número de tarjeta debe tener 16 dígitos.")
    @Column(unique = true)
    private String cardNumber;

    @NotBlank(message = "El nombre del titular es obligatorio.")
    private String cardHolderName;

    @NotBlank(message = "El tipo de tarjeta (Crédito o Débito) es obligatorio.")
    private String cardType; // "Crédito" o "Débito"

    @NotBlank(message = "La fecha de expiración es obligatoria.")
    @Pattern(regexp = "(0[1-9]|1[0-2])/(\\d{2})", message = "La fecha de expiración debe estar en formato MM/YY.")
    private String expirationDate;

    @NotBlank(message = "El código de seguridad (CVV) es obligatorio.")
    @Pattern(regexp = "\\d{3}", message = "El CVV debe tener 3 dígitos.")
    private String securityCode;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
}
