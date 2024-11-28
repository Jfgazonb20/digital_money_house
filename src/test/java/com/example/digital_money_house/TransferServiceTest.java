package com.example.digital_money_house;

import com.example.digital_money_house.Exception.ResourceNotFoundException;
import com.example.digital_money_house.Model.Account;
import com.example.digital_money_house.Model.Transaction;
import com.example.digital_money_house.Repository.AccountRepository;
import com.example.digital_money_house.Repository.TransactionRepository;
import com.example.digital_money_house.Service.TransferService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TransferServiceTest {

    private final AccountRepository accountRepository = Mockito.mock(AccountRepository.class);
    private final TransactionRepository transactionRepository = Mockito.mock(TransactionRepository.class);
    private final TransferService transferService = new TransferService(accountRepository, transactionRepository);

    @Test
    void transferMoney_Success() {
        Account sourceAccount = new Account();
        sourceAccount.setId(1L);
        sourceAccount.setBalance(1000.00);

        Account destinationAccount = new Account();
        destinationAccount.setId(2L);
        destinationAccount.setAlias("alias.destino");
        destinationAccount.setBalance(500.00);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByCvuOrAlias("alias.destino", "alias.destino")).thenReturn(Optional.of(destinationAccount));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transaction transaction = transferService.transferMoney(1L, "alias.destino", 200.00, "Pago Servicio");

        assertThat(sourceAccount.getBalance()).isEqualTo(800.00);
        assertThat(destinationAccount.getBalance()).isEqualTo(700.00);
        assertThat(transaction.getAmount()).isEqualTo(-200.00);
        assertThat(transaction.getDescription()).isEqualTo("Pago Servicio -> alias.destino");

        verify(accountRepository, times(1)).save(sourceAccount);
        verify(accountRepository, times(1)).save(destinationAccount);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void transferMoney_Failure_SourceAccountNotFound() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            transferService.transferMoney(1L, "alias.destino", 200.00, "Pago Servicio");
        });

        verify(accountRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void transferMoney_Failure_DestinationAccountNotFound() {
        Account sourceAccount = new Account();
        sourceAccount.setId(1L);
        sourceAccount.setBalance(1000.00);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByCvuOrAlias("alias.invalido", "alias.invalido")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            transferService.transferMoney(1L, "alias.invalido", 200.00, "Pago Servicio");
        });

        verify(accountRepository, times(1)).findById(1L);
        verify(accountRepository, times(1)).findByCvuOrAlias("alias.invalido", "alias.invalido");
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void transferMoney_Failure_InsufficientFunds() {
        Account sourceAccount = new Account();
        sourceAccount.setId(1L);
        sourceAccount.setBalance(100.00);

        Account destinationAccount = new Account();
        destinationAccount.setId(2L);
        destinationAccount.setAlias("alias.destino");

        when(accountRepository.findById(1L)).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByCvuOrAlias("alias.destino", "alias.destino")).thenReturn(Optional.of(destinationAccount));

        assertThrows(IllegalArgumentException.class, () -> {
            transferService.transferMoney(1L, "alias.destino", 200.00, "Pago Servicio");
        });

        verify(accountRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }
}
