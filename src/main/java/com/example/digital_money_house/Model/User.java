package com.example.digital_money_house.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El campo 'username' es obligatorio.")
    @NotBlank(message = "El campo 'username' no puede estar vacío.")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "El campo 'email' es obligatorio.")
    @Email(message = "El email debe tener un formato válido.")
    @Column(unique = true)
    private String email;

    @NotNull(message = "El campo 'password' es obligatorio.")
    @NotBlank(message = "El campo 'password' no puede estar vacío.")
    private String password;

    @Column(unique = true, length = 22)
    private String cvu; // Este campo será generado automáticamente.

    @Column(unique = true)
    private String alias; // Este campo será generado automáticamente.

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    // Constructor adicional para inicializar campos específicos
    public User(String username) {
        this.username = username;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
