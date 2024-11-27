package com.example.digital_money_house.Service;

import com.example.digital_money_house.Exception.ResourceNotFoundException;
import com.example.digital_money_house.Model.Account;
import com.example.digital_money_house.Model.Transaction;
import com.example.digital_money_house.Repository.AccountRepository;
import com.example.digital_money_house.Repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    // Obtener cuenta por ID
    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + id));
    }

    // Actualizar cuenta
    public Account updateAccount(Long id, Account updatedAccountData) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + id));

        if (updatedAccountData.getAccountNumber() != null) {
            account.setAccountNumber(updatedAccountData.getAccountNumber());
        }

        if (updatedAccountData.getBalance() != null) {
            account.setBalance(updatedAccountData.getBalance());
        }

        accountRepository.save(account);
        return account;
    }

    // Ingresar dinero a la cuenta
    @Transactional
    public Transaction depositMoney(Long accountId, Double amount, String description) {
        if (amount <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con ID: " + accountId));

        // Actualiza el balance de la cuenta
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        // Registra la transacción
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setDate(LocalDateTime.now());
        transaction.setDescription(description);

        return transactionRepository.save(transaction);
    }
}
