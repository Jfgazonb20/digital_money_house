package com.example.digital_money_house.Controller;

import com.example.digital_money_house.Model.Card;
import com.example.digital_money_house.Service.CardService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/{id}/cards")
    public ResponseEntity<List<Card>> getCards(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.getCardsByAccountId(id));
    }

    @GetMapping("/{accountId}/cards/{cardId}")
    public ResponseEntity<Card> getCardById(@PathVariable Long cardId) {
        return ResponseEntity.ok(cardService.getCardById(cardId));
    }

    @PostMapping("/{id}/cards")
    public ResponseEntity<Card> addCard(@PathVariable Long id, @Valid @RequestBody Card card) {
        Card createdCard = cardService.addCard(id, card);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{cardId}")
                .buildAndExpand(createdCard.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdCard);
    }


    @DeleteMapping("/{accountId}/cards/{cardId}")
    public ResponseEntity<String> deleteCard(@PathVariable Long cardId) {
        cardService.deleteCard(cardId);
        return ResponseEntity.ok("Card deleted successfully");
    }
}
