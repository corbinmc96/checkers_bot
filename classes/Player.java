import java.util.Arrays;
import java.util.ArrayList;

public abstract class Player {

	private Board myBoard;
	private Robot gameRobot;
	//true if the player is located on the side of the board marked with index 0
	private boolean isOnZeroSide;
	//contains the piece color
	private String color;

	public Player (String startColor, boolean startsOnZeroSide) {
		this.color = startColor;
		this.isOnZeroSide = startsOnZeroSide;
	}

	public Player (String startColor, boolean startsOnZeroSide, Robot startGameRobot) {
		this.color = startColor;
		this.isOnZeroSide = startsOnZeroSide;
		this.gameRobot = startGameRobot;
	}

	public boolean getIsOnZeroSide() {
		return this.isOnZeroSide;
	}

	public String getColor() {
		return this.color;
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
		Move[] moves = this.getAllMoves();
		Board[] boards = new Board[moves.lengths];
		for (int i = 0; i<moves.length; i++) {
			boards[i] = new Board(this.myBoard, moves[i]);
		}
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
		ArrayList<Move> result = new ArrayList<Move>();
		for (Piece playerPiece : this.getPlayerPieces()) {
			for (Move pieceMove : playerPiece.getMovesOfPiece()) {
				result.add(pieceMove);
			}
		}
	}

	public abstract void takeTurn();
}