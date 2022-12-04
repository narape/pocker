package poker;

import java.util.Arrays;

public enum Suit {
  SPADES("♠"),
  HEARTS("♥"),
  CLUBS("♣"),
  DIAMONDS("♦");

  private final String symbol;

  Suit(final String symbol) {
    this.symbol = symbol;
  }

  public static Suit parse(final String str) {
    return Arrays.stream(values())
        .filter(suit -> suit.symbol.equals(str))
        .findAny()
        .orElseThrow(() -> new IllegalArgumentException("Not a valid suit: " + str));
  }

  @Override
  public String toString() {
    return symbol;
  }
}
