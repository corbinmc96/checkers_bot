//ALL AARON

import java.io.IOException;
import java.util.Arrays;

public class MidgameStarter {
	public static void main(String[] args) {

		Robot r = new PseudoRobot();
		r.connect();

		if (!args[0].equals("robot") && !args[0].equals("human")) {
			System.err.println("Illegal first argument to determine turn");
			return;
		}

		if (!args[1].matches("p\\{([0-7][0-7])*\\}")) {
			System.err.println("Illegal p1 list");
			return;
		}
		if (!args[2].matches("p\\{([0-7][0-7])*\\}")) {
			System.err.println("Illegal p2 list");
			return;
		}
		if (!args[3].matches("k\\{([0-7][0-7])*\\}")) {
			System.err.println("Illegal p1 kings list");
			return;
		}
		if (!args[4].matches("k\\{([0-7][0-7])*\\}")) {
			System.err.println("Illegal p2 kings list");
			return;
		}

		int[][] p1Locations = new int[(args[1].length()-3)/2][];
		int[][] p2Locations = new int[(args[2].length()-3)/2][];
		int[][] p1Kings = new int[(args[3].length()-3)/2][];
		int[][] p2Kings = new int[(args[4].length()-3)/2][];

		for (int i=0; i<p1Locations.length; i++) {
			p1Locations[i] = new int[] {Integer.parseInt(args[1].substring(2+2*i, 3+2*i)), Integer.parseInt(args[1].substring(3+2*i, 4+2*i))};
		}
		for (int i=0; i<p2Locations.length; i++) {
			p2Locations[i] = new int[] {Integer.parseInt(args[2].substring(2+2*i, 3+2*i)), Integer.parseInt(args[2].substring(3+2*i, 4+2*i))};
		}
		for (int i=0; i<p1Kings.length; i++) {
			p1Kings[i] = new int[] {Integer.parseInt(args[3].substring(2+2*i, 3+2*i)), Integer.parseInt(args[3].substring(3+2*i, 4+2*i))};
		}
		for (int i=0; i<p2Kings.length; i++) {
			p2Kings[i] = new int[] {Integer.parseInt(args[4].substring(2+2*i, 3+2*i)), Integer.parseInt(args[4].substring(3+2*i, 4+2*i))};
		}

		try {
			Game theGame;
			if (args[0].equals("robot")) {
				theGame = new Game(new SimPlayer(args.length>5 ? args[5] : "x", true, r, new MultithreadedAI()),
								   new Human(args.length>6 ? args[6] : "o", false, r, new MultithreadedAI()),
								   p1Locations,
								   p2Locations,
								   p1Kings,
								   p2Kings,
								   (args.length>8 && args[8].equals("official")),
								   r
				);
			} else {
				theGame = new Game(new Human(args.length>6 ? args[6] : "o", false, r, new MultithreadedAI()),
								   new SimPlayer(args.length>5 ? args[5] : "x", true, r, new MultithreadedAI()),
								   p1Locations,
								   p2Locations,
								   p1Kings,
								   p2Kings,
								   (args.length>8 && args[8].equals("official")),
								   r
				);
			}
			Player winner = theGame.play();
			if (winner == null) {
				System.out.println("TIE");
			} else {
				System.out.println("WINNER:  " + (winner.getXO().equals(" ") ? winner.getColor() : winner.getXO()));
			}
		} finally {
			try {
				r.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}