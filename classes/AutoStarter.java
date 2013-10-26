public class AutoStarter {
	public static void main(String[] args) {
		Player orig = new SimPlayer("x", true);
		Player test = new TestPlayer("o", false);

		int origWins = 0;
		int testWins = 0;
		int draws = 0;

		Game g = new Game(orig, test);
		Player winner = g.play();
		if (winner==orig) {
			origWins++;
			System.out.println("Player 1 wins");
		} else  if (winner==test) {
			testWins++;
			System.out.println("Player 2 wins");
		} else {
			draws++;
			System.out.println("Draw");
		}

		orig = new SimPlayer("o", false);
		test = new TestPlayer("x", true);

		g = new Game(test, orig);
		winner = g.play();
		if (winner==orig) {
			origWins++;
			System.out.println("Player 1 wins");
		} else  if (winner==test) {
			testWins++;
			System.out.println("Player 2 wins");
		} else {
			draws++;
			System.out.println("Draw");
		}

		System.out.println("" + origWins + ", " + testWins + ", " + draws);
	}
}