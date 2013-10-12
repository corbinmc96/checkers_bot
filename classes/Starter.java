public class Starter {

	public static void main(String[] args) {
		Game theGame = new Game(new Human("x",true), new SimPlayer("o",false));
		Player winner = theGame.play();
		
	}
}