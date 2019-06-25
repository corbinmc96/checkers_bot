// COLLABORATION BETWEEN CORBIN AND AARON

import java.io.IOException;

public class Starter {

	public static void main(String[] args) {
	
		Robot r = new Robot();
		r.connect();
		
		try {
			Game theGame = new Game(new SimPlayer(args.length>0 ? args[0] : "x",true, r, new AaronAI()),
									new Human(args.length>1 ? args[1] : "o",false, r, new AaronAI()),
									(args.length>3 && args[3].equals("official")),
									r
			);
			Player winner = theGame.play();
			if (winner == null) {
				System.out.println("TIE");
			} else {
				System.out.println("WINNER:  " + winner.getXO());
			}
		} finally {
			try {
				r.disconnect();
			} catch {IOException e) {
				e.printStackTrace();
			}
		)
	}
}
