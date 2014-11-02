// COLLABORATION BETWEEN CORBIN AND AARON

import java.io.IOException;

public class Starter {

	public static void main(String[] args) {

		Game theGame = new Game(new Human(args.length>0 ? args[0] : "x", true, new MultithreadedAI()),
						new SimPlayer(args.length>1 ? args[1] : "o", false, new MultithreadedAI()),
						(args.length>3 && args[3].equals("official"))
		);
		Player winner = theGame.play();
		if (winner == null) {
			System.out.println("TIE");
		} else {
			System.out.println("WINNER:  " + winner.getXO());
		}
	}
}
