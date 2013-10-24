import java.util.ArrayList;

public class Game {

	private Board gameBoard;
	private Robot gameRobot;
	private Player player1;
	private Player player2;
<<<<<<< HEAD
	private Move[] lastFewMoves;
=======
	private ArrayList<int[][]> lastFewMoves;
>>>>>>> 84ff295458fbbc48f42f7fba1ba9efbb3481e3e4

	public Game (Player p1, Player p2) {
		this.gameBoard = new Board(new Player[]{p1, p2});
		this.player1 = p1;
		this.player2 = p2;
		this.player1.setBoard(gameBoard);
		this.player2.setBoard(gameBoard);
		this.lastFewMoves = new ArrayList<int[][]>();
	}
	
	public Game (Player p1, Player p2, Robot startGameRobot) {
		this(p1,p2);
		this.gameRobot = startGameRobot;
	}
	
	public Game (Game oldG, Board b) {
		this.player1 = oldG.getPlayers()[0];
		this.player2 = oldG.getPlayers()[1];
		this.gameBoard = b;
	}

<<<<<<< HEAD
	public Player play() {
		while (player1.getAllMoves(this.gameBoard).length>0 && player2.getAllMoves(this.gameBoard).length>0) {
			this.player1.takeTurn(this);
<<<<<<< HEAD
			if (this.gameBoard.totalPiecesLeft(this.player1)>0 && this.gameBoard.totalPiecesLeft(this.player2)>0) {
				this.player2.takeTurn(this);
=======
			// this.gameBoard.printBoard();
			if (player1.getAllMoves(this.gameBoard).length>0 && player2.getAllMoves(this.gameBoard).length>0) {
				this.player2.takeTurn(this);
				// this.gameBoard.printBoard();
>>>>>>> 3a58739ec598fffc6ffe52e3d999343b30c073e0
			}
=======
	public Game(Player p1, Player p2, int[][] p1Locations, int[][] p2Locations) {
		this.gameBoard = new Board(new Player[]{p1, p2}, p1Locations, p2Locations);
		this.player1 = p1;
		this.player2 = p2;
		this.player1.setBoard(this.gameBoard);
		this.player2.setBoard(this.gameBoard);
		this.lastFewMoves = new ArrayList<int[][]>();
	}
	
	public Game (Game oldG, Move m) {
		this.player1 = oldG.getPlayers()[0];
		this.player2 = oldG.getPlayers()[1];
		this.lastFewMoves = ArraysHelper.asArrayList(oldG.getLastFewMoves());
		this.lastFewMoves.add(m.getWaypoints());
		if (this.lastFewMoves.size()>12) {
			this.lastFewMoves.remove(0);
>>>>>>> 84ff295458fbbc48f42f7fba1ba9efbb3481e3e4
		}
		this.gameBoard = new Board(oldG.getGameBoard(), m);
	}

	public Player play() {
		while (player1.getAllMoves(this.gameBoard).length>0 && player2.getAllMoves(this.gameBoard).length>0 && !this.isDraw()) {
			this.lastFewMoves.add(this.player1.takeTurn(this).getWaypoints());
			if (this.lastFewMoves.size()>12) {
				this.lastFewMoves.remove(0);
			}
			// this.gameBoard.printBoard();
			if (player1.getAllMoves(this.gameBoard).length>0 && player2.getAllMoves(this.gameBoard).length>0 && !this.isDraw()) {
				this.lastFewMoves.add(this.player2.takeTurn(this).getWaypoints());
				if (this.lastFewMoves.size()>12) {
					this.lastFewMoves.remove(0);
				}
				// this.gameBoard.printBoard();
			}
		}
		if (this.gameBoard.totalPiecesLeft(player1)==0) {
			return this.player2;
		}
		else if (this.gameBoard.totalPiecesLeft(player2)==0) {
			return this.player1;
		} else {
			return null;
		}
	}

	public Board getGameBoard () {
		return this.gameBoard;
	}

	public Player[] getPlayers () {
		return new Player[]{this.player1,this.player2};
	}

	public Player getOtherPlayer(Player p) {
		if (this.player1==p) {
			return this.player2;
		} else {
			return this.player1;
		}
	}

	public int[][][] getLastFewMoves() {
		return this.lastFewMoves.toArray(new int[this.lastFewMoves.size()][][]);
	}

	public boolean isDraw() {
		if (this.lastFewMoves.size()<12) {
			return false;
		}

		int[][][] identicals;
		for (int moveIndex=0; moveIndex<4; moveIndex++) {
			identicals = new int[][][] {this.lastFewMoves.get(moveIndex), this.lastFewMoves.get(moveIndex+4), this.lastFewMoves.get(moveIndex+8)};
			if (identicals[0].length!=identicals[1].length || identicals[1].length!=identicals[2].length) {
				return false;
			}
			for (int i=0; i<identicals[0].length; i++) {
				if (!(identicals[0][i][0]==identicals[1][i][0] && identicals[0][i][1]==identicals[1][i][1]) || !(identicals[1][i][0]==identicals[2][i][0] && identicals[1][i][1]==identicals[2][i][1])) {
					return false;
				}
			}
		}
		return true;
	}
}