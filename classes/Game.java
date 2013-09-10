public class Game {

	private Board gameBoard;
	private Player player1;
	private Player player2;

	public Game () {
		//constructor implementation
	}

	public Game (Robot startGameRobot) {
		//constructor implementation
		//indicates robot arm will be used. this must be passed into any simplayers so they know to use the robot arm. 
	}

	public void play() {
		//implementation
	}

	public Board getGameBoard () {
		//implementation
	}

	public Player[] getPlayers () {
		//implementation
	}
}