package com.example.digital_money_house.Controller;

import com.example.digital_money_house.Model.Transaction;
import com.example.digital_money_house.Service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    @PostMapping("/{id}/deposit")
    public ResponseEntity<Transaction> depositMoney(
            @PathVariable Long id,
            @RequestParam Double amount,
            @RequestParam String description) {
        Transaction transaction = accountService.depositMoney(id, amount, description);

        // Construye la URI del recurso creado
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest() // Toma la URL actual
                .path("/{transactionId}") // Agrega un nuevo segmento de path
                .buildAndExpand(transaction.getId()) // Reemplaza {transactionId} con el ID de la transacción
                .toUri();

        // Devuelve 201 Created con la ubicación del recurso creado
        return ResponseEntity.created(location).body(transaction);
    }

}
