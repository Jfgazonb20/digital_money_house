package com.example.digital_money_house.Controller;

import com.example.digital_money_house.Model.Transaction;
import com.example.digital_money_house.Service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
public class DashboardController {

    private final AccountService accountService;

    public DashboardController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getAccountBalance(@PathVariable("id") Long id, HttpServletRequest request) {
        try {
            accountService.validateUserAccessToAccount(id, request);
            var account = accountService.getAccountById(id);
            Map<String, Object> response = new HashMap<>();
            response.put("accountNumber", account.getAccountNumber());
            response.put("balance", account.getBalance());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<Transaction>> getLastFiveTransactions(@PathVariable Long id, HttpServletRequest request) {
        try {
            accountService.validateUserAccessToAccount(id, request);
            List<Transaction> transactions = accountService.getAccountTransactions(id);
            return ResponseEntity.ok(transactions);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(403).body(null);
        }
    }
}
