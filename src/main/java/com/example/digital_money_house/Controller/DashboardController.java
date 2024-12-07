package com.example.digital_money_house.Controller;

import com.example.digital_money_house.Model.Account;
import com.example.digital_money_house.Model.Transaction;
import com.example.digital_money_house.Service.AccountService;
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
    public ResponseEntity<Map<String, Object>> getAccountBalance(@PathVariable("id") Long id) {
        Account account = accountService.getAccountById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("accountNumber", account.getAccountNumber());
        response.put("balance", account.getBalance());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable Long id, @RequestBody Account updatedAccountData) {
        Account updatedAccount = accountService.updateAccount(id, updatedAccountData);
        return ResponseEntity.ok(updatedAccount);
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<Transaction>> getLastFiveTransactions(@PathVariable Long id) {
        List<Transaction> transactions = accountService.getAccountTransactions(id);
        return ResponseEntity.ok(transactions);
    }
}
