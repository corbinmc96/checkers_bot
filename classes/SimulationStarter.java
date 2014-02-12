public class SimulationStarter {
	public static void main(String[] args) {
		Player orig = new SimPlayer("x", true, new MultithreadedAI());
		Player test = new SimPlayer("x", true, new OldMultithreadedAI());
		Player rand = new SimPlayer("o", false, new RandomAI());
		boolean testOfficialVersion = false;

		int origWins = 0;
		int testWins = 0;
		int draws = 0;

		for (int i = 0; i<500; i++) {
			Game g = new Game(orig, rand, testOfficialVersion);
			Player winner = g.play();
			if (winner==orig) {
				origWins++;
				// System.out.println("Player 1 wins");
			} else if (winner==rand) {
				origWins--;
				// System.out.println("Player 1 loses");
			} else {
				draws++;
			}

			g = new Game(test, rand, testOfficialVersion);
			winner = g.play();
			if (winner==test) {
				testWins++;
				// System.out.println("Player 2 wins");
			} else if (winner==rand) {
				testWins--;
				// System.out.println("Player 2 loses");
			} else {
				draws++;
			}

			System.out.println(i);
			System.out.println("" + origWins + ", " + testWins);
		}

		orig = new SimPlayer("o", false, new MultithreadedAI());
		test = new SimPlayer("o", false, new OldMultithreadedAI());
		rand = new SimPlayer("x", true, new RandomAI());

		for (int i = 0; i<500; i++) {
			Game g = new Game(rand, orig, testOfficialVersion);
			Player winner = g.play();
			if (winner==orig) {
				origWins++;
				// System.out.println("Player 1 wins");
			} else if (winner==rand) {
				origWins--;
				// System.out.println("Player 1 loses");
			} else {
				draws++;
			}

			g = new Game(rand, test, testOfficialVersion);
			winner = g.play();
			if (winner==test) {
				testWins++;
				// System.out.println("Player 2 wins");
			} else if (winner==rand) {
				testWins--;
				// System.out.println("Player 2 loses");
			} else {
				draws++;
			}

			System.out.println(i+500);
			System.out.println("" + origWins + ", " + testWins);
		}

		System.out.println("" + origWins + ", " + testWins);
	}
}