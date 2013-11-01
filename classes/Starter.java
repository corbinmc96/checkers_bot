public class Starter {

	public static void main(String[] args) {
		Game theGame = new Game(new Human("x",true,new AaronAI()), new SimPlayer("o",false,new CorbinAI()));
		Player winner = theGame.play();
		
	}
}