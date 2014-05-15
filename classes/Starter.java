// COLLABORATION BETWEEN CORBIN AND AARON

import java.io.IOException;

public class Starter {

	public static void main(String[] args) {
<<<<<<< HEAD

		Robot r = new Robot();
		r.connect();

=======
	
		Robot r = new Robot();
		System.out.println(10);
		r.connect();
		System.out.println(12);
		
>>>>>>> ef89dbb9bd199eae7f3f8eba00e2aea2c0e463cd
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
			} catch (IOException e) {
<<<<<<< HEAD
				System.err.println(e);
=======
				e.printStackTrace();
>>>>>>> ef89dbb9bd199eae7f3f8eba00e2aea2c0e463cd
			}
		}
	}
}
