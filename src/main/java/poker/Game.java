package poker;

public enum Game {
  ;

  public static void main(final String[] args) {
    final var console = System.console();
    final String readLine = console.readLine("How many players (1-7)? ");
    final int numPlayers = Integer.parseInt(readLine);

    final Deck deck = new Deck();
    deck.shuffle();

    for (int i = 0; i < numPlayers; i++) {
      final Player player = new Player(i + 1, deck.deal(7));
      System.out.println(player);
    }
  }
}
