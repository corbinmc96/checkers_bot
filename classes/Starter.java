public class Starter {

	public static void main(String[] args) {
		Game theGame = new Game(new SimPlayer("x",true,new CorbinAI()), new SimPlayer("o",false,new CorbinAI()));
		Player winner = theGame.play();
		System.out.println("WINNER:  " + winner.getXO());
	}
}