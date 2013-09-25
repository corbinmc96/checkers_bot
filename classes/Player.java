import java.util.ArrayList;

public abstract class Player {

	private Board myBoard;
	private Robot gameRobot;

	public Player (Robot startGameRobot) {
		this.myBoard = null;
		this.gameRobot = startGameRobot;
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

	public Move calculateBestMove () {
		//implementation
	}

	public Move[] rankBestMoves () {
		//implementation
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

	public abstract void takeTurn();
}