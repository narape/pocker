package poker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static poker.Suit.CLUBS;
import static poker.Suit.DIAMONDS;
import static poker.Suit.HEARTS;
import static poker.Suit.SPADES;
import static poker.Values.ACE;
import static poker.Values.J;

class CardTest {
  @ParameterizedTest
  @MethodSource
  void testParse(final String str, final Card expected) {
    assertEquals(expected, Card.parse(str));
  }

  @Test
  void testParseInvalid() {
    assertThrows(IllegalArgumentException.class, () -> Card.parse("not correct format"));
  }

  public static Stream<Arguments> testParse() {
    return Stream.of(
        arguments("♠2", new Card(SPADES, 2)),
        arguments("♥7", new Card(HEARTS, 7)),
        arguments("♣J", new Card(CLUBS, J)),
        arguments("♦A", new Card(DIAMONDS, ACE)));
  }

  @ParameterizedTest
  @MethodSource("testParseMany")
  void testParseMany(final String str, final List<Card> expected) {
    assertEquals(expected, Card.parseMany(str));
  }

  @Test
  void testParseManyInvalid() {
    assertThrows(IllegalArgumentException.class, () -> Card.parseMany("♠2,♥7,not correct format"));
  }

  public static Stream<Arguments> testParseMany() {
    return Stream.of(
        arguments("♠2", List.of(new Card(SPADES, 2))),
        arguments("♠2,♥7", List.of(new Card(SPADES, 2), new Card(HEARTS, 7))));
  }
}
