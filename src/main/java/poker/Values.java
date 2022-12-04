package poker;

import java.util.stream.IntStream;

public enum Values {
  ;
  static final int J = 11;
  static final int Q = 12;
  static final int K = 13;
  static final int ACE = 14;

  static final int MIN_VALUE = 2;
  static final int MAX_VALUE = Values.ACE;

  private static final int[] VALUES = IntStream.rangeClosed(Values.MIN_VALUE, Values.MAX_VALUE).toArray();

  static boolean isValid(final int value) {
    return MIN_VALUE <= value && MAX_VALUE >= value;
  }

  static void ensureValid(final int value) {
    if (!Values.isValid(value)) {
      throw new IllegalArgumentException("Value out of range: " + value);
    }
  }

  static String toString(final int value) {
      Values.ensureValid(value);

    return switch (value) {
      case 11 -> "J";
      case 12 -> "Q";
      case 13 -> "K";
      case 14 -> "A";
      default -> String.valueOf(value);
    };
  }

  static IntStream stream() {
    return IntStream.of(Values.VALUES);
  }

  public static int parse(final String valueStr) {
    return switch (valueStr) {
      case "J" -> 11;
      case "Q" -> 12;
      case "K" -> 13;
      case "A" -> 14;
      default -> {
        final int value;
        try {
          value = Integer.parseInt(valueStr);
          if (Values.isValid(value)) {
            yield value;
          }
        } catch (final NumberFormatException e) {
          // fallthrough
        }
        throw new IllegalArgumentException("Cannot parse into a value: " + valueStr);
      }
    };
  }
}
