/**
 * @author Aaron Miller
 */

import java.io.IOException;
import java.util.Arrays;

public class MidgameStarter extends Starter {
	/**
	 * Runs the core game from an intermediate starting point
	 * 
	 * @param  args                 Meant to be in the form {@code robot|human p1=<firstPlayerPiecesList> p2=<secondPlayerPiecesList> k1=<firstPlayerKingsList> k2=<secondPlayerKingsList> [<robotColor> [<humanColor> [robot=<yes|pseudo> [official]]]]}
	 * @throws InterruptedException [description]
	 */
	public static void main(String[] args) throws InterruptedException {

		if (!args[0].equals("robot") && !args[0].equals("human")) {
			System.err.println("Illegal first argument to determine turn");
			return;
		}

		if (!args[1].matches("p1\\=([0-7][0-7])*")) {
			System.err.println("Illegal p1 list");
			return;
		}
		if (!args[2].matches("p2\\=([0-7][0-7])*")) {
			System.err.println("Illegal p2 list");
			return;
		}
		if (!args[3].matches("k1\\=([0-7][0-7])*")) {
			System.err.println("Illegal p1 kings list");
			return;
		}
		if (!args[4].matches("k2\\=([0-7][0-7])*")) {
			System.err.println("Illegal p2 kings list");
			return;
		}

		int[][] p1Locations = new int[(args[1].length()-3)/2][];
		int[][] p2Locations = new int[(args[2].length()-3)/2][];
		int[][] p1Kings = new int[(args[3].length()-3)/2][];
		int[][] p2Kings = new int[(args[4].length()-3)/2][];

		for (int i=0; i<p1Locations.length; i++) {
			p1Locations[i] = new int[] {Integer.parseInt(args[1].substring(3+2*i, 4+2*i)), Integer.parseInt(args[1].substring(4+2*i, 5+2*i))};
		}
		for (int i=0; i<p2Locations.length; i++) {
			p2Locations[i] = new int[] {Integer.parseInt(args[2].substring(3+2*i, 4+2*i)), Integer.parseInt(args[2].substring(4+2*i, 5+2*i))};
		}
		for (int i=0; i<p1Kings.length; i++) {
			p1Kings[i] = new int[] {Integer.parseInt(args[3].substring(3+2*i, 4+2*i)), Integer.parseInt(args[3].substring(4+2*i, 5+2*i))};
		}
		for (int i=0; i<p2Kings.length; i++) {
			p2Kings[i] = new int[] {Integer.parseInt(args[4].substring(3+2*i, 4+2*i)), Integer.parseInt(args[4].substring(4+2*i, 5+2*i))};
		}

		Robot r;
		if (args.length>7 && args[7].equalsIgnoreCase("robot=pseudo")) {
			r = new PseudoRobot();
		} else if (args.length>7 && args[7].equalsIgnoreCase("robot=none")) {
			r = null;
		} else {
			r = new Robot();
		}

		try {
			if (r != null) {
				r.connect();
			}

			Game theGame;

			Player humanPlayer;
			Player robotPlayer;
			if (args.length > 6) {
				humanPlayer = new Human('o', false, new MultithreadedAI(), r, args[6]);
			} else {
				humanPlayer = new Human('o', false, new MultithreadedAI(), r, Robot.DARK_COLOR);
			}

			if (args.length > 5) {
				robotPlayer = new SimPlayer('x', true, new MultithreadedAI(), r, args[5]);
			} else {
				robotPlayer = new SimPlayer('x', true, new MultithreadedAI(), r, Robot.LIGHT_COLOR);
			}

			if (args[0].equals("robot")) {
				theGame = new Game(robotPlayer,
								   humanPlayer,
								   p1Locations,
								   p2Locations,
								   p1Kings,
								   p2Kings,
								   (args.length>8 && args[8].equals("official")),
								   r
				);
			} else {
				theGame = new Game(humanPlayer,
								   robotPlayer,
								   p1Locations,
								   p2Locations,
								   p1Kings,
								   p2Kings,
								   (args.length>8 && args[8].equals("official")),
								   r
				);
			}

			int[][] squaresToCalibrate = new int[3][];
			Player[] players = theGame.getPlayers();
			for (Player p : players) {
				if (p.getColor().equals("gray")) {
					squaresToCalibrate[0] = p.getPlayerPieces(theGame.getGameBoard())[0].getLocation();
				} else {
					squaresToCalibrate[2] = p.getPlayerPieces(theGame.getGameBoard())[0].getLocation();
				}
			}
			for (int i = 0; i<8; i++) {
				for (int j = 0; j<8; j++) {
					if (theGame.getGameBoard().getPieceAtLocation(new int[] {i, j})==null) {
						squaresToCalibrate[1] = new int[] {i, j};
					}
				}
			}

			if (r != null) {
				r.calibrate(squaresToCalibrate);
				r.resetPosition();
			}

			Player winner = theGame.play();

			if (winner == null) {
				System.out.println("TIE");
			} else {
				System.out.println("WINNER:  " + winner.getXO());
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
