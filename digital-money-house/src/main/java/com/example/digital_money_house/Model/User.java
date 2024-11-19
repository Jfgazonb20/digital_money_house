package com.example.digital_money_house.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Data
@Table(name = "app_user")  // Evitar conflicto con palabras reservadas
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String username;

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    private String password;

    // Nuevos campos
    @NotBlank
    @Column(unique = true, length = 22)  // CVU es único y tiene longitud fija de 22
    private String cvu;

    @NotBlank
    @Column(unique = true)  // Alias es único
    private String alias;
}
