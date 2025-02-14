package com.example.digital_money_house.Service;

import com.example.digital_money_house.Exception.CustomGoneException;
import com.example.digital_money_house.Exception.ResourceNotFoundException;
import com.example.digital_money_house.Model.Account;
import com.example.digital_money_house.Model.Transaction;
import com.example.digital_money_house.Repository.AccountRepository;
import com.example.digital_money_house.Repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransferService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Transaction transferMoney(Long sourceAccountId, String destinationCvuOrAlias, Double amount, String description) {
        if (amount <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }

        Account sourceAccount = accountRepository.findById(sourceAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta origen no encontrada con ID: " + sourceAccountId));

        if (sourceAccount.getBalance() < amount) {
            throw new CustomGoneException("Fondos insuficientes");
        }

        Account destinationAccount = accountRepository.findByCvuOrAlias(destinationCvuOrAlias, destinationCvuOrAlias)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta destino no encontrada con CVU/Alias: " + destinationCvuOrAlias));

        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);

        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        Transaction transaction = new Transaction();
        transaction.setAccount(sourceAccount);
        transaction.setAmount(-amount);
        transaction.setDate(LocalDateTime.now());
        transaction.setDescription(description + " -> " + destinationAccount.getAlias());

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getRecentTransfers(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con ID: " + accountId));

        return transactionRepository.findTop5ByAccountIdOrderByDateDesc(account.getId());
    }

    public List<Transaction> getAllTransfers(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con ID: " + accountId));

        return transactionRepository.findByAccountIdOrderByDateDesc(account.getId());
    }

    public void validateUserAccessToAccount(Long accountId, String username) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con ID: " + accountId));

        if (!account.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("No tiene permisos para acceder a esta cuenta");
        }
    }
}
