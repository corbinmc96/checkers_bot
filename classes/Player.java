import java.util.ArrayList;

public class Player {

	private Board myBoard;

	public Player (Board : startBoard) {
		this.myBoard = startBoard;
		//constructor implementation
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
		//implementation
	}

	public Board getBoard () {
		returnthis.myBoard;
	}
}