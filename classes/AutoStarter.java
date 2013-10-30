public class AutoStarter {
	public static void main(String[] args) {
		Player orig = new SimPlayer("x", true);
		Player test = new TestPlayer("x", true);
		Player rand = new RandomPlayer("o", false);

		int origWins = 0;
		int testWins = 0;
		int draws = 0;

		for (int i = 0; i<500; i++) {
			Game g = new Game(orig, rand);
			Player winner = g.play();
			if (winner==orig) {
				origWins++;
				System.out.println("Player 1 wins");
			} else if (winner==rand) {
				origWins--;
				System.out.println("Player 1 loses");
			} else {
				draws++;
			}

			g = new Game(test, rand);
			winner = g.play();
			if (winner==test) {
				testWins++;
				System.out.println("Player 2 wins");
			} else if (winner==rand) {
				testWins--;
				System.out.println("Player 2 loses");
			} else {
				draws++;
			}

			System.out.println(i);
		}

		orig = new SimPlayer("o", false);
		test = new TestPlayer("o", false);
		rand = new RandomPlayer("x", true);

		for (int i = 0; i<500; i++) {
			Game g = new Game(rand, orig);
			Player winner = g.play();
			if (winner==orig) {
				origWins++;
				System.out.println("Player 1 wins");
			} else if (winner==rand) {
				origWins--;
				System.out.println("Player 1 loses");
			} else {
				draws++;
			}

			g = new Game(rand, test);
			winner = g.play();
			if (winner==test) {
				testWins++;
				System.out.println("Player 2 wins");
			} else if (winner==rand) {
				testWins--;
				System.out.println("Player 2 loses");
			} else {
				draws++;
			}

			System.out.println(i+500);
		}

		System.out.println("" + origWins + ", " + testWins);
	}
}