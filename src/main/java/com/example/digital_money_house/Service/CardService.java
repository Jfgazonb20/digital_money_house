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
        return cardRepository.findByAccountId(accountId);
    }

    public Card getCardById(Long cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with ID: " + cardId));
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

    public void deleteCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with ID: " + cardId));
        cardRepository.delete(card);
    }
}
