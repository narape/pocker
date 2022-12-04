package poker;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public record Card(Suit suit, int value) implements Comparable<Card> {

  public static final Comparator<Card> DESC_VALUE_THEN_SUIT_COMPARATOR =
      Comparator.comparingInt(Card::value).reversed().thenComparing(Card::suit);

  static List<Card> parseMany(final String str) {
    return Arrays.stream(str.split(",")).map(Card::parse).toList();
  }

  static Card parse(final String cardStr) {
    if (null == cardStr || 2 > cardStr.length()) {
      throw new IllegalArgumentException("Cannot parse into a card: " + cardStr);
    }
    Suit suit = Suit.parse(cardStr.substring(0, 1));
    int value = Values.parse(cardStr.substring(1));
    return new Card(suit, value);
  }

  @Override
  public String toString() {
    return this.suit.toString() + Values.toString(this.value);
  }

  @Override
  public int compareTo(Card o) {
    return DESC_VALUE_THEN_SUIT_COMPARATOR.compare(this, o);
  }
}
