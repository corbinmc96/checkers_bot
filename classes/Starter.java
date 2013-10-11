public class Starter {

	public static void main(String[] args) {
		Game theGame = new Game(new Human("x",false), new Human("o",true));
		Player winner = theGame.play();
		
	}
}