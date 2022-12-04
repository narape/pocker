package poker;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static poker.EvaluationTest.Comparison.EQUAL;
import static poker.EvaluationTest.Comparison.GREATER;
import static poker.EvaluationTest.Comparison.LESS;
import static poker.PokerHand.Name.THREE_OF_A_KIND;
import static poker.Values.ACE;
import static poker.Values.J;

class EvaluationTest {
  enum Comparison {
    LESS,
    EQUAL,
    GREATER
  }

  public static Stream<Arguments> testCompareTo() {
    final Evaluation royalFlush = Evaluation.royalFlush();
    final Evaluation threeOf7 = Evaluation.of(THREE_OF_A_KIND, List.of(7), List.of(ACE, 3));
    final Evaluation threeOfAce = Evaluation.of(THREE_OF_A_KIND, List.of(ACE), List.of(ACE, 3));
    final Evaluation threeOf4 = Evaluation.of(THREE_OF_A_KIND, List.of(4), List.of(ACE, 3));
    final Evaluation threeOf4WithJHigh = Evaluation.of(THREE_OF_A_KIND, List.of(4), List.of(J, 3));
    return Stream.of(
        arguments(threeOfAce, royalFlush, LESS),
        arguments(threeOf7, threeOf7, EQUAL),
        arguments(threeOf7, threeOfAce, LESS),
        arguments(threeOfAce, threeOf7, GREATER),
        arguments(threeOf4, threeOf4WithJHigh, GREATER));
  }

  @ParameterizedTest
  @MethodSource
  void testCompareTo(final Evaluation a, final Evaluation b, final Comparison cmp) {
    final boolean test =
        switch (cmp) {
          case LESS -> 0 > a.compareTo(b);
          case EQUAL -> 0 == a.compareTo(b);
          case GREATER -> 0 < a.compareTo(b);
        };

    assertTrue(test);
  }
}
