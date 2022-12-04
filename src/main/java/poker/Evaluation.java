package poker;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static poker.PokerHand.Name.ROYAL_FLUSH;

record Evaluation(PokerHand.Name name, List<Integer> ofWhat, List<Integer> kicker)
    implements Comparable<Evaluation> {

  static Evaluation royalFlush() {
    return Evaluation.of(ROYAL_FLUSH, List.of());
  }

  static Evaluation of(final PokerHand.Name name, final List<Integer> ofWhat) {
    return Evaluation.of(name, ofWhat, List.of());
  }

  static Evaluation of(final PokerHand.Name name, final List<Integer> ofWhat, final List<Integer> kicker) {
    return new Evaluation(name, ofWhat, kicker);
  }

  @Override
  public String toString() {
    return "%s%s%s".formatted(this.name.toHumanString(), this.ofWhatToString(), this.kickerToString());
  }

  private String ofWhatToString() {
    return switch (this.ofWhat.size()) {
      case 0 -> "";
      case 1 -> " of " + Values.toString(this.ofWhat.get(0));
      default -> " of "
          + this.ofWhat.stream().map(Values::toString).collect(Collectors.joining(",", "[", "]"));
    };
  }

  private String kickerToString() {
    return (this.kicker.isEmpty()) ? "" : " with kicker=" + this.kicker;
  }

  @Override
  public int compareTo(final Evaluation o) {
    return Comparator.comparing(Evaluation::name)
        .thenComparing(Evaluation::ofWhat, Evaluation::compareLists)
        .thenComparing(Evaluation::kicker, Evaluation::compareLists)
        .compare(this, o);
  }

  private static int compareLists(final List<Integer> u, final List<Integer> v) {
    for (int i = 0; i < u.size(); i++) {
      final int cmp = Integer.compare(u.get(i), v.get(i));
      if (0 == cmp) {
        continue;
      }
      return cmp;
    }
    return 0;
  }
}
