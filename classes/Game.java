public class Game {

	private Board gameBoard;
	private Robot gameRobot;
	private Player player1;
	private Player player2;

	public Game (Player p1, Player p2) {
		this.gameBoard = new Board();
		this.player1, this.player2 = p1, p2;
	}
	
	public Game (Robot startGameRobot) {
		this();
		this.gameRobot = startGameRobot;
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