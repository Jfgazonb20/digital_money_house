package com.example.digital_money_house.Service;

import com.example.digital_money_house.Exception.ResourceNotFoundException;
import com.example.digital_money_house.Exception.UserAlreadyExistsException;
import com.example.digital_money_house.Model.Account;
import com.example.digital_money_house.Model.Card;
import com.example.digital_money_house.Repository.AccountRepository;
import com.example.digital_money_house.Repository.CardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;

    public CardService(CardRepository cardRepository, AccountRepository accountRepository) {
        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
    }

    public List<Card> getCardsByAccountId(Long accountId) {
        // Verificar si la cuenta existe
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con ID: " + accountId));
        return cardRepository.findByAccountId(account.getId());
    }

    public Card getCardByIdAndAccountId(Long accountId, Long cardId) {
        // Verificar si la cuenta existe
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con ID: " + accountId));

        // Verificar si la tarjeta pertenece a la cuenta
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Tarjeta no encontrada con ID: " + cardId));

        if (!card.getAccount().getId().equals(account.getId())) {
            throw new IllegalArgumentException("La tarjeta no pertenece a la cuenta con ID: " + accountId);
        }

        return card;
    }

    public Card addCard(Long accountId, Card card) {
        // Verificar si la tarjeta ya está registrada en otra cuenta
        if (cardRepository.findByCardNumber(card.getCardNumber()).isPresent()) {
            throw new UserAlreadyExistsException("La tarjeta ya está registrada en otra cuenta.");
        }

        // Verificar si la cuenta existe
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con ID: " + accountId));

        // Asociar la tarjeta con la cuenta
        card.setAccount(account);
        return cardRepository.save(card);
    }

    public void deleteCard(Long accountId, Long cardId) {
        // Verificar si la tarjeta existe
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Tarjeta no encontrada con ID: " + cardId));

        // Validar que la tarjeta pertenece a la cuenta del usuario
        if (!card.getAccount().getId().equals(accountId)) {
            throw new IllegalArgumentException("La tarjeta no pertenece a la cuenta con ID: " + accountId);
        }

        // Si pasa la validación, se elimina
        cardRepository.delete(card);
    }
}
