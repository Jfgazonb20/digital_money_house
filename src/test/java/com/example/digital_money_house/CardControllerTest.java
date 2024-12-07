package com.example.digital_money_house;

import com.example.digital_money_house.Controller.CardController;
import com.example.digital_money_house.Model.Card;
import com.example.digital_money_house.Service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CardControllerTest {

    private final CardService cardService = Mockito.mock(CardService.class);
    private final CardController cardController = new CardController(cardService);

    @BeforeEach
    void setUp() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    void getCards_Success() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card());

        when(cardService.getCardsByAccountId(1L)).thenReturn(cards);

        ResponseEntity<List<Card>> response = cardController.getCards(1L);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(1);
    }

    @Test
    void getCardById_Success() {
        Card card = new Card();
        card.setId(1L);

        when(cardService.getCardByIdAndAccountId(1L, 1L)).thenReturn(card);

        ResponseEntity<Card> response = cardController.getCardById(1L, 1L);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(card);
    }

    @Test
    void getCardById_NotBelongToAccount() {
        doThrow(new IllegalArgumentException("La tarjeta no pertenece a la cuenta con ID: 2"))
                .when(cardService).getCardByIdAndAccountId(2L, 1L);

        try {
            cardController.getCardById(2L, 1L);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("La tarjeta no pertenece a la cuenta con ID: 2");
        }
    }

    @Test
    void addCard_Success() {
        Card card = new Card();
        card.setId(1L);
        when(cardService.addCard(1L, card)).thenReturn(card);

        ResponseEntity<Card> response = cardController.addCard(1L, card);

        assertThat(response.getStatusCodeValue()).isEqualTo(201); // Verifica el código HTTP 201 Created
        assertThat(response.getBody()).isEqualTo(card);
    }

    @Test
    void deleteCard_Success() {
        doNothing().when(cardService).deleteCard(1L, 1L);

        ResponseEntity<String> response = cardController.deleteCard(1L, 1L);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("Card deleted successfully");
    }

    @Test
    void deleteCard_NotBelongToAccount() {
        doThrow(new IllegalArgumentException("La tarjeta no pertenece a la cuenta con ID: 2"))
                .when(cardService).deleteCard(2L, 1L);

        try {
            cardController.deleteCard(2L, 1L);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("La tarjeta no pertenece a la cuenta con ID: 2");
        }
    }

    @Test
    void deleteCard_NotFound() {
        doThrow(new RuntimeException("Card not found with ID: 99"))
                .when(cardService).deleteCard(1L, 99L);

        try {
            cardController.deleteCard(1L, 99L);
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("Card not found with ID: 99");
        }
    }
}
