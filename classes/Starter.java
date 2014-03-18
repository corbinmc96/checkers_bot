public class Starter {

	public static void main(String[] args) {
		Game theGame = new Game(new SimPlayer("x",true,new MultithreadedAI()), 
								new SimPlayer("o",false,new MultithreadedAI()), 
								(args.length>0 && args[0].equals("official"))
		);
		Player winner = theGame.play();
		if (winner == null) {
			System.out.println("TIE");
		} else {
			System.out.println("WINNER:  " + winner.getXO());
		}
	}
}
