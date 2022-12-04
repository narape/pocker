package poker;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Deck {
  public static final int NEW_DECK_SIZE = 52;
  private final List<Card> cards;

  public Deck() {
    cards =
        Stream.of(Suit.values())
            .flatMap(s -> Values.stream().mapToObj(v -> new Card(s, v)))
            .collect(Collectors.toCollection(LinkedList::new));
  }

  public Card deal() {
    if (this.isEmpty()) {
      throw new IllegalStateException("Deck is empty, cannot deal one more card");
    }

    return this.cards.remove(0);
  }

  public List<Card> deal(final int howMany) {
    if (0 > howMany) {
      throw new IllegalArgumentException(
          "How many should be a positive number: %s".formatted(howMany));
    } else if (this.cards.size() < howMany) {
      throw new IllegalArgumentException(
          "Not enough cards in the deck to deal %s cards".formatted(howMany));
    }

    return IntStream.range(0, howMany).mapToObj(__ -> this.cards.remove(0)).toList();
  }

  public int size() {
    return this.cards.size();
  }

  public boolean isEmpty() {
    return this.cards.isEmpty();
  }

  public void shuffle() {
    Collections.shuffle(this.cards);
  }

  @Override
  public String toString() {
    return this.cards.toString();
  }
}
