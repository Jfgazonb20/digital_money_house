package com.example.digital_money_house.Service;

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

    // Obtener destinatarios recientes
    public List<Transaction> getRecentTransfers(Long accountId) {
        return transactionRepository.findTop5ByAccountIdOrderByDateDesc(accountId);
    }

    // Realizar transferencia
    @Transactional
    public Transaction transferMoney(Long sourceAccountId, String destinationCvuOrAlias, Double amount, String description) {
        if (amount <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }

        if (destinationCvuOrAlias == null || destinationCvuOrAlias.isBlank()) {
            throw new IllegalArgumentException("El CVU o Alias del destinatario no puede estar vacío");
        }

        // Obtener cuenta origen
        Account sourceAccount = accountRepository.findById(sourceAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta origen no encontrada con ID: " + sourceAccountId));

        // Obtener cuenta destino
        Account destinationAccount = accountRepository.findByCvuOrAlias(destinationCvuOrAlias, destinationCvuOrAlias)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta destino no encontrada con CVU/Alias: " + destinationCvuOrAlias));

        if (sourceAccount.getBalance() < amount) {
            throw new IllegalArgumentException("Fondos insuficientes para realizar la transferencia");
        }

        // Actualizar saldos
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);
        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        // Registrar transacción en cuenta origen
        Transaction sourceTransaction = new Transaction();
        sourceTransaction.setAccount(sourceAccount);
        sourceTransaction.setAmount(-amount);
        sourceTransaction.setDate(LocalDateTime.now());
        sourceTransaction.setDescription("Transferencia a -> " + destinationAccount.getAlias());
        transactionRepository.save(sourceTransaction);

        // Registrar transacción en cuenta destino
        Transaction destinationTransaction = new Transaction();
        destinationTransaction.setAccount(destinationAccount);
        destinationTransaction.setAmount(amount);
        destinationTransaction.setDate(LocalDateTime.now());
        destinationTransaction.setDescription("Transferencia recibida de -> " + sourceAccount.getAlias());
        transactionRepository.save(destinationTransaction);

        return sourceTransaction;
    }
}
