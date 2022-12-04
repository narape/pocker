package poker;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static poker.Values.ACE;
import static poker.Values.J;
import static poker.Values.K;
import static poker.Values.Q;

class ValuesTest {
  @ParameterizedTest
  @CsvSource({"-1,false", "0,false", "2,true", "13,true", "14,true", "15,false"})
  void testIsValid(final int value, final boolean expected) {
    assertEquals(expected, Values.isValid(value));
  }

  @ParameterizedTest
  @ValueSource(ints = {2, 3, 7, K, ACE})
  void testEnsureValidWhenValid(final int value) {
    assertDoesNotThrow(() -> Values.ensureValid(value));
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 1, ACE + 1})
  void testEnsureValidWhenOutOfRange(final int value) {
    assertThrows(IllegalArgumentException.class, () -> Values.ensureValid(value));
  }

  @ParameterizedTest
  @MethodSource
  void testParse(final String str, final int expectedValue) {
    assertEquals(expectedValue, Values.parse(str));
  }

  public static Stream<Arguments> testParse() {
    return Stream.of(
        arguments("2", 2),
        arguments("3", 3),
        arguments("7", 7),
        arguments("J", J),
        arguments("Q", Q),
        arguments("K", K),
        arguments("A", ACE));
  }
}
