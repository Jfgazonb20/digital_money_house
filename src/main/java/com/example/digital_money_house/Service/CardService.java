package com.example.digital_money_house.Service;

import com.example.digital_money_house.Exception.ResourceNotFoundException;
import com.example.digital_money_house.Model.Account;
import com.example.digital_money_house.Model.Card;
import com.example.digital_money_house.Repository.CardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {

    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public List<Card> getCardsByAccountId(Long accountId) {
        return cardRepository.findByAccountId(accountId);
    }

    public Card getCardById(Long cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with ID: " + cardId));
    }

    public Card addCard(Long accountId, Card card) {
        card.setAccount(new Account());
        card.getAccount().setId(accountId);
        return cardRepository.save(card);
    }

    public void deleteCard(Long cardId) {
        if (!cardRepository.existsById(cardId)) {
            throw new ResourceNotFoundException("Card not found with ID: " + cardId);
        }
        cardRepository.deleteById(cardId);
    }
}
