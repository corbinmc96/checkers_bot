import java.util.ArrayList;

public abstract class Player {

	private Board myBoard;
	private Robot gameRobot;
	//true if the player is located on the side of the board marked with index 0
	private boolean isOnZeroSide;

	public Player (boolean startsOnZeroSide) {
		this.isOnZeroSide = startsOnZeroSide;
	}

	public Player (boolean startsOnZeroSide, Robot startGameRobot) {
		this.isOnZeroSide = startsOnZeroSide;
		this.gameRobot = startGameRobot;
	}

	public boolean getIsOnZeroSide() {
		return this.isOnZeroSide;
	}

	public Piece[] getPlayerPieces () {
		Piece[] result = new Piece[this.myBoard.totalPiecesLeft(this)];
		int i = 0;
		for (Piece p : this.myBoard.getPiecesOnBoard()) {
			if (p.getPlayer() == this) {
				result[i++] = p;
			}
		}
		return result;
	}

	public Move calculateBestMove (Board b, int recursionDepth) {
		return this.rankBestMoves(b,recursionDepth)[0];
	}

	public Move[] rankBestMoves (Board b, int recursionDepth) {
		
	}

	public static void performMove(Move myMove, Board theBoard) {
		myMove.getMovePiece().setLocation(myMove.getDestination());
		for (Piece deadPiece : myMove.calculatePiecesToJump()) {
			theBoard.removePiece(deadPiece);
		}
	}
	
	public void setBoard (Board newBoard) {
		this.myBoard = newBoard;
	}

	public Board getBoard () {
		return this.myBoard;
	}

	public Move[] getAllMoves () {
		for (playerPiece : this.getPlayerPieces()) {
			playerPiece.getMovesOfPiece();
		}
	}

	public abstract void takeTurn();
}