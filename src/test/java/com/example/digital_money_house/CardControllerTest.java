package com.example.digital_money_house;

import com.example.digital_money_house.Controller.CardController;
import com.example.digital_money_house.Model.Card;
import com.example.digital_money_house.Service.CardService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CardControllerTest {

    private final CardService cardService = Mockito.mock(CardService.class);
    private final CardController cardController = new CardController(cardService);

    @Test
    void getCards_Success() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card());

        when(cardService.getCardsByAccountId(1L)).thenReturn(cards);

        ResponseEntity<List<Card>> response = cardController.getCards(1L);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().size()).isEqualTo(1);
    }

    @Test
    void addCard_Success() {
        Card card = new Card();
        when(cardService.addCard(1L, card)).thenReturn(card);

        ResponseEntity<Card> response = cardController.addCard(1L, card);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }
}
