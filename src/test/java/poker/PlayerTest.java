package poker;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junitpioneer.jupiter.params.IntRangeSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlayerTest {

  @ParameterizedTest
  @IntRangeSource(from = 0, to = Deck.NEW_DECK_SIZE + 1)
  void testPlayerThrowsWhenDealtDifferentThan7Cards(final int howManyCards) {
    Assumptions.assumeFalse(Player.DEAL_SIZE == howManyCards);
    final var deck = new Deck();

    final List<Card> cards = deck.deal(howManyCards);
    assertThrows(IllegalArgumentException.class, () -> new Player(1, cards));
  }

  @Test
  void testPlayerRequires7Cards() {
    final var deck = new Deck();

    final List<Card> cards = deck.deal(Player.DEAL_SIZE);

    assertDoesNotThrow(() -> new Player(1, cards));
  }

  @Test
  void testPlayerExpectsAllDistinctCards() {
    final var deck = new Deck();

    final List<Card> cards = new ArrayList<>(deck.deal(Player.DEAL_SIZE - 1));
    cards.add(cards.get(0));

    assertThrows(IllegalArgumentException.class, () -> new Player(1, cards));
  }

  @ParameterizedTest
  @MethodSource
  void testBestPokerHand(final List<Card> cards, final PokerHand expectedHand) {
    final var player = new Player(1, cards);
    final var bestHand = player.getBestPokerHand();

    assertEquals(expectedHand, bestHand);
  }

  public static Stream<Arguments> testBestPokerHand() {
    return Stream.of(
        Arguments.arguments(
            Card.parseMany("♠9,♦6,♥8,♦4,♦J,♠7,♥10"),
            new PokerHand(Card.parseMany("♠9,♥8,♦J,♠7,♥10"))),
        Arguments.arguments(
            Card.parseMany("♣K,♥4,♥10,♣Q,♠9,♦J,♣10"),
            new PokerHand(Card.parseMany("♣K,♥10,♣Q,♠9,♦J"))));
  }
}
