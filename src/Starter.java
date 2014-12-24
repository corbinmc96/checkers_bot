import java.io.IOException;

/**
 * @author Corbin McNeill
 * @author Aaron Miller
 */

public class Starter {

	public static void main(String[] args) throws InterruptedException {
		Robot r = null;
		if (args.length > 2 && args[2].equals("robot=yes")) {
			r = new Robot();
		} else if (args.length > 2 && args[2].equals("robot=pseudo")) {
			r = new PseudoRobot();
		}

		try {
			if (r != null) {
				r.connect();
				r.calibrate();
				r.resetPosition();
			}
		
			Game theGame = new Game(
				new Human(args.length > 1 ? args[1] : "o", true, r, new MultithreadedAI()),
				new SimPlayer(args.length > 0 ? args[0] : "x", false, r, new MultithreadedAI()),
				(args.length > 3 && args[3].equals("official")),
				r
			);
			Player winner = theGame.play();
			if (winner == null) {
				System.out.println("TIE");
			} else {
				System.out.println("WINNER: " + winner.getXO());
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} finally {
			if (r != null) {
				System.err.print("Disconnecting robot...");
				try {
					r.disconnect();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				System.err.println("DONE");
			}
		}
	}
}
