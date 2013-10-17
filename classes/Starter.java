public class Starter {

	public static void main(String[] args) {
		Game theGame = new Game(new Human("x",true,1), new SimPlayer("o",false,-1));
		Player winner = theGame.play();
		
	}
}