import java.util.ArrayList;

public class Player {

	private Board myBoard;

	public Player (Board : startBoard) {
		this.myBoard = startBoard;
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

	public void performMove(Move myMove) {
		myMove.getMovePiece().setLocation(myMove.getDestination());
		for (deadPiece : myMove.calculatePiecesToJump()) {
			this.myBoard.removePiece(deadPiece);
		}
	}

	public Board getBoard () {
		return this.myBoard;
	}
}