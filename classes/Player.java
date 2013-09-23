import java.util.ArrayList;

public abstract class Player {

	private Board myBoard;
	private Robot gameRobot;

	public Player (Robot startGameRobot) {
		this.myBoard = null;
		this.gameRobot = startGameRobot;
	}

	public Piece[] getPlayerPieces () {
		ArrayList<Piece> result;
		for (Piece p : this.myBoard.getPiecesOnBoard()) {
			if (p.getPlayer() == this) {
				result.add(p);
			}
		}
		return result;
	}

	public Move calculateBestMove () {
		//implementation
	}

	public static void performMove(Move myMove, Board theBoard) {
		myMove.getMovePiece().setLocation(myMove.getDestination());
		for (deadPiece : myMove.calculatePiecesToJump()) {
			board.removePiece(deadPiece);
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