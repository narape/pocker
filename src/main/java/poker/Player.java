package poker;

import java.util.List;

public class Player {

  public static final int DEAL_SIZE = 7;
  public static final int FROM_SELECTION = 0x1F;
  public static final int TO_SELECTION = 0x76;
  private final int id;

  private final PokerHand bestPokerHand;

  public Player(final int id, final List<Card> cards) {
    this.id = id;

    if (DEAL_SIZE != cards.stream().distinct().count()) {
      throw new IllegalArgumentException(
          "Players must receive 7 distinct cars, but received:" + cards.size());
    }
    bestPokerHand = this.computeBestPockerHand(cards);
  }

  public PokerHand getBestPokerHand() {
    return this.bestPokerHand;
  }

  private PokerHand computeBestPockerHand(final List<Card> cards) {
    PokerHand bestSoFar = null;
    for (int selection = Player.FROM_SELECTION; TO_SELECTION >= selection; selection++) {
      if (PokerHand.SIZE != Integer.bitCount(selection)) {
        continue;
      }

      final PokerHand hand = this.getPokerHand(cards, selection);
      if (null == bestSoFar || 0 < hand.compareTo(bestSoFar)) {
        bestSoFar = hand;
      }
    }
    return bestSoFar;
  }

  private PokerHand getPokerHand(final List<Card> cards, final int selection) {
    final int[] indexes = this.idxForSelection(selection);
    final Card[] selectedCards = new Card[PokerHand.SIZE];
    for (int i = 0; i < indexes.length; i++) {
      selectedCards[i] = cards.get(indexes[i]);
    }

    return new PokerHand(List.of(selectedCards));
  }

  private int[] idxForSelection(final int combination) {
    final var indexes = new int[PokerHand.SIZE];

    for (int i = 0, j = 0; i < indexes.length; i++) {
      while (0 == (1 << j & combination)) {
        j++;
      }
      indexes[i] = j;
      j++;
    }
    return indexes;
  }

  @Override
  public String toString() {
    return "poker.Player %s: %s %s"
        .formatted(this.id, this.bestPokerHand.eval().name().toHumanString(), this.bestPokerHand);
  }
}
