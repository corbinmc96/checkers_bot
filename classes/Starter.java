public class Starter {

	public static void main(String[] args) {

		Game theGame = new Game(new SimPlayer(args.length>0 ? args[0] : "x",true, new CorbinAI()),
								new SimPlayer(args.length>1 ? args[1] : "o",false, new AaronAI()),
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
