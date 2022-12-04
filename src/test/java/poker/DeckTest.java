package poker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junitpioneer.jupiter.RetryingTest;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DeckTest {

  private final Deck deck = new Deck();

  @Test
  void testNewDeckDealsInNewDeckOrder() {
    for (final var suit : Suit.values()) {
      for (int v = Values.MIN_VALUE; Values.MAX_VALUE >= v; v++) {
        assertEquals(new Card(suit, v), this.deck.deal());
      }
    }
  }

  @Test
  void testDealReduceCardsByOne() {
    assertNotNull(this.deck.deal());

    assertEquals(51, this.deck.size());
  }

  @Test
  void testDealThrowsExceptionWhenEmpty() {
    IntStream.range(0, 52).forEach(__ -> this.deck.deal());

    assertThrows(IllegalStateException.class, this.deck::deal);
  }

  @ParameterizedTest
  @ValueSource(ints = {0, 1, 4, 15, Deck.NEW_DECK_SIZE})
  void testDealReduceCardsByOne(final int howMany) {
    assertEquals(howMany, this.deck.deal(howMany).size());

    assertEquals(Deck.NEW_DECK_SIZE - howMany, this.deck.size());
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, Deck.NEW_DECK_SIZE + 1})
  void testDealThrowsExceptionOnInvalidHowMany(final int howMany) {
    assertThrows(IllegalArgumentException.class, () -> this.deck.deal(howMany));
  }

  @RetryingTest(3)
  void testShuffle() {
    final var newDeck = new Deck();
    final var otherDeck = new Deck();

    // no shuffle for new deck
      this.deck.shuffle();
    otherDeck.shuffle();

    final var newDeckCards = newDeck.deal(Deck.NEW_DECK_SIZE);
    final var deckCards = this.deck.deal(Deck.NEW_DECK_SIZE);
    final var otherDeckCards = otherDeck.deal(Deck.NEW_DECK_SIZE);

    assertNotEquals(newDeckCards, deckCards);
    assertNotEquals(otherDeckCards, deckCards);
  }
}
