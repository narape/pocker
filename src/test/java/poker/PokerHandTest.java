package poker;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junitpioneer.jupiter.params.IntRangeSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static poker.Values.ACE;
import static poker.Values.J;
import static poker.Values.K;
import static poker.Values.Q;

class PokerHandTest {

  @ParameterizedTest
  @IntRangeSource(from = 0, to = PokerHand.SIZE + 1)
  void testPockerHandThrowsWhenDealtDifferentThan5Cards(final int howManyCards) {
    Assumptions.assumeFalse(PokerHand.SIZE == howManyCards);
    final var deck = new Deck();

    final List<Card> cards = deck.deal(howManyCards);
    assertThrows(IllegalArgumentException.class, () -> new PokerHand(cards));
  }

  @ParameterizedTest
  @MethodSource("testHandEval")
  void testHandEval(final List<Card> cards, final Evaluation expectedEval) {
    final var hand = new PokerHand(cards);

    final var eval = hand.eval();

    assertEquals(expectedEval, eval);
  }

  public static Stream<Arguments> testHandEval() {
    return Stream.of(
        arguments(Card.parseMany("♥10,♥J,♥Q,♥K,♥A"), Evaluation.royalFlush()),
        arguments(
            Card.parseMany("♣4,♣5,♣6,♣7,♣8"),
            Evaluation.of(PokerHand.Name.STRAIGHT_FLUSH, List.of(8), List.of(7, 6, 5, 4))),
        arguments(
            Card.parseMany("♣7,♥7,♦7,♠7,♠10"),
            Evaluation.of(PokerHand.Name.FOUR_OF_A_KIND, List.of(7), List.of(10))),
        arguments(
            Card.parseMany("♣K,♥K,♦K,♠J,♥J"),
            Evaluation.of(PokerHand.Name.FULL_HOUSE, List.of(K, J))),
        arguments(
            Card.parseMany("♠2,♠K,♠5,♠J,♠4"),
            Evaluation.of(PokerHand.Name.FLUSH, List.of(K), List.of(J, 5, 4, 2))),
        arguments(
            Card.parseMany("♠4,♠3,♦5,♠6,♦2"),
            Evaluation.of(PokerHand.Name.STRAIGHT, List.of(6), List.of(5, 4, 3, 2))),
        arguments(
            Card.parseMany("♠A,♠3,♦A,♥A,♦2"),
            Evaluation.of(PokerHand.Name.THREE_OF_A_KIND, List.of(ACE), List.of(3, 2))),
        arguments(
            Card.parseMany("♠J,♠3,♦J,♥A,♦3"),
            Evaluation.of(PokerHand.Name.TWO_PAIR, List.of(J, 3), List.of(ACE))),
        arguments(
            Card.parseMany("♠5,♠3,♦5,♥Q,♦6"),
            Evaluation.of(PokerHand.Name.ONE_PAIR, List.of(5), List.of(Q, 6, 3))),
        arguments(
            Card.parseMany("♦2,♠A,♠7,♦8,♥4"),
            Evaluation.of(PokerHand.Name.HIGH_CARD, List.of(), List.of(ACE, 8, 7, 4, 2))));
  }
}
