package com.example.digital_money_house.Controller;

import com.example.digital_money_house.Model.Transaction;
import com.example.digital_money_house.Service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // Endpoint para ingresar dinero
    @PostMapping("/{id}/transferences")
    public ResponseEntity<Transaction> depositMoney(
            @PathVariable Long id,
            @RequestParam Double amount,
            @RequestParam String description) {
        Transaction transaction = accountService.depositMoney(id, amount, description);
        return ResponseEntity.ok(transaction);
    }
}
