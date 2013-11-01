public class Starter {

	public static void main(String[] args) {
		Game theGame = new Game(new Human("x",true,new CorbinAI()), new SimPlayer("o",false,new AaronAI()));
		Player winner = theGame.play();
		System.out.println("WINNER:  " + winner.getXO());
	}
}