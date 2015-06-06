import java.io.IOException;

/**
 * @author Corbin McNeill
 * @author Aaron Miller
 */

public class Starter {

	/**
	 * Runs the core checkers program with robot control
	 * 
	 * @param  args                 Meant to be in the form: {@code [<robotColor> [<humanColor> [robot=<yes|pseudo> [official]]]] }
	 * @throws InterruptedException thrown if the program is interrupted while executing
	 */
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
		
			Player humanPlayer;
			Player robotPlayer;
			if (args.length > 1) {
				humanPlayer = new Human('o', false, new MultithreadedAI(), r, args[0]);
			} else {
				humanPlayer = new Human('o', false, new MultithreadedAI(), r, Robot.DARK_COLOR);
			}

			if (args.length > 2) {
				robotPlayer = new SimPlayer('x', true, new MultithreadedAI(), r, args[1]);
			} else {
				robotPlayer = new SimPlayer('x', true, new MultithreadedAI(), r, Robot.LIGHT_COLOR);
			}
			Game theGame = new Game(
				humanPlayer,
				robotPlayer,
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
