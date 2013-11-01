public class Starter {

	public static void main(String[] args) {
		Game theGame = new Game(new Human("x",true,new TestAI()), new SimPlayer("o",false,new TestAI()));
		Player winner = theGame.play();
		
	}
}