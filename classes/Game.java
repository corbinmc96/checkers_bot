public class Game {

	private Board gameBoard;
	private Player player1;
	private Player player2;

	public Game () {
		this.gameBoard = new Board();
		this.player1, this.player2 = new Player(), new Player();
	}

	public Game (Robot startGameRobot) {
		this.Game();
		this.gameRobot = startGameRobot;
		//indicates robot arm will be used. this must be passed into any simplayers so they know to use the robot arm. 
	}

	public void play() {
		//implementation will be done later
	}

	public Board getGameBoard () {
		return this.gameBoard;
	}

	public Player[] getPlayers () {
		return Player[2]{this.player1,this.player2};
	}
}