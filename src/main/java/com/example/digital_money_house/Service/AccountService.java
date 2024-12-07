package com.example.digital_money_house.Service;

import com.example.digital_money_house.Exception.ResourceNotFoundException;
import com.example.digital_money_house.Model.Account;
import com.example.digital_money_house.Model.Transaction;
import com.example.digital_money_house.Repository.AccountRepository;
import com.example.digital_money_house.Repository.TransactionRepository;
import com.example.digital_money_house.Security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final JwtService jwtService;

    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository, JwtService jwtService) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.jwtService = jwtService;
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con ID: " + id));
    }

    public void validateUserAccessToAccount(Long accountId, HttpServletRequest request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con ID: " + accountId));

        String token = request.getHeader("Authorization").replace("Bearer ", "");
        String username = jwtService.extractUsername(token);

        if (!account.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("No tiene permisos para acceder a esta cuenta"); // Cambiado para coincidir con el test
        }
    }


    public List<Transaction> getAccountTransactions(Long accountId) {
        Account account = getAccountById(accountId);
        return transactionRepository.findByAccountIdOrderByDateDesc(account.getId());
    }

    @Transactional
    public Transaction depositMoney(Long accountId, Double amount, String description) {
        if (amount <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }

        Account account = getAccountById(accountId);

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setDate(LocalDateTime.now());
        transaction.setDescription(description);

        return transactionRepository.save(transaction);
    }
}
