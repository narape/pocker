package poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

public record PokerHand(List<Card> cards) implements Comparable<PokerHand> {
  static final int SIZE = 5;

  public PokerHand(final List<Card> cards) {
    if (SIZE != cards.stream().distinct().count()) {
      throw new IllegalArgumentException(
          "Poker hands should contain %s distinct cards: %s".formatted(PokerHand.SIZE, cards));
    }
    final var unsortedCards = new ArrayList<>(cards);
    Collections.sort(unsortedCards);
    this.cards = List.copyOf(unsortedCards);
  }

  @Override
  public String toString() {
    return this.cards.toString();
  }

  Evaluation eval() {
    final var allConsecutive = this.allConsecutive();
    final var allSameSuit = this.allSameSuit();
    final var highCard = this.cards.get(0);
    final var highCardValue = highCard.value();
    final var nonHighCards = this.cardsValuesOtherThan(highCard);

    if (allSameSuit && allConsecutive) {
      if (Values.ACE == highCardValue) {
        return Evaluation.of(Name.ROYAL_FLUSH, List.of(), List.of());
      }
      return Evaluation.of(Name.STRAIGHT_FLUSH, List.of(highCardValue), nonHighCards);
    } else if (allSameSuit) {
      return Evaluation.of(Name.FLUSH, List.of(highCardValue), nonHighCards);
    } else if (allConsecutive) {
      return Evaluation.of(Name.STRAIGHT, List.of(highCardValue), nonHighCards);
    }
    final var howManyOfKinds = this.computeKinds();
    if (howManyOfKinds.containsKey(4)) {
      final var fourOfAKindValue = howManyOfKinds.get(4).get(0);
      return Evaluation.of(
          Name.FOUR_OF_A_KIND,
          List.of(fourOfAKindValue),
              this.cardsValuesOtherThanValues(List.of(fourOfAKindValue)));
    } else if (howManyOfKinds.containsKey(3)) {
      final var threeOfAKindValue = howManyOfKinds.get(3).get(0);
      if (howManyOfKinds.containsKey(2)) {
        final var twoOfAKindValue = howManyOfKinds.get(2).get(0);
        return Evaluation.of(
            Name.FULL_HOUSE, List.of(threeOfAKindValue, twoOfAKindValue), List.of());
      } else {
        return Evaluation.of(
            Name.THREE_OF_A_KIND,
            List.of(threeOfAKindValue),
                this.cardsValuesOtherThanValues(List.of(threeOfAKindValue)));
      }
    } else if (howManyOfKinds.containsKey(2)) {
      final var pairs = howManyOfKinds.get(2).stream().sorted(reverseOrder()).toList();
      final var kicker = this.cardsValuesOtherThanValues(pairs);
      if (2 == pairs.size()) {
        return Evaluation.of(Name.TWO_PAIR, pairs, kicker);
      } else {
        return Evaluation.of(Name.ONE_PAIR, pairs, kicker);
      }
    }
    return Evaluation.of(Name.HIGH_CARD, List.of(), this.cards.stream().map(Card::value).toList());
  }

  private List<Integer> cardsValuesOtherThan(final Card excludedCard) {
    return this.cards.stream().filter(c -> !excludedCard.equals(c)).map(Card::value).toList();
  }

  private List<Integer> cardsValuesOtherThanValues(final List<Integer> excludeValues) {
    final var excludeValuesSet = new HashSet<>(excludeValues);
    return this.cards.stream()
        .filter(c -> !excludeValuesSet.contains(c.value()))
        .sorted()
        .map(Card::value)
        .toList();
  }

  private Map<Integer, List<Integer>> computeKinds() {
    final var countsPerKind =
            this.cards.stream()
            .map(Card::value)
            .collect(groupingBy(Function.identity(), Collectors.counting()));
    return countsPerKind.entrySet().stream()
        .collect(groupingBy(e -> e.getValue().intValue(), mapping(Map.Entry::getKey, toList())));
  }

  private boolean allConsecutive() {
    for (int i = 1; SIZE > i; i++) {
      if (1 != cards.get(i - 1).value() - cards.get(i).value()) {
        return false;
      }
    }
    return true;
  }

  private boolean allSameSuit() {
    return 1L == cards.stream().map(Card::suit).distinct().count();
  }

  @Override
  public int compareTo(final PokerHand o) {
    return Comparator.comparing(PokerHand::eval).compare(this, o);
  }

  enum Name {
    HIGH_CARD("High Card"),
    ONE_PAIR("Pair"),
    TWO_PAIR("Two Pair"),
    THREE_OF_A_KIND("3 of a Kind"),
    STRAIGHT("Straight"),
    FLUSH("Flush"),
    FULL_HOUSE("Full House"),
    FOUR_OF_A_KIND("4 of a Kind"),
    STRAIGHT_FLUSH("Straight Flush"),
    ROYAL_FLUSH("Royal Flush");

    private final String humanString;

    Name(final String humanString) {
      this.humanString = humanString;
    }

    public String toHumanString() {
      return this.humanString;
    }
  }
}
