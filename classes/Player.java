import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;

public abstract class Player {

	private Board myBoard;
	private Robot gameRobot;
	//true if the player is located on the side of the board marked with index 0
	private boolean isOnZeroSide;
	//contains the piece color
	private String color;

	private String xo;

	private int valueFactor;
	private Player opponent;

	// public Player (String startColor, boolean startsOnZeroSide) {
	// 	this.color = startColor;
	// 	this.isOnZeroSide = startsOnZeroSide;
	// }

	public Player (String startColor, boolean startsOnZeroSide, Robot startGameRobot) {
		this.color = startColor;
		this.isOnZeroSide = startsOnZeroSide;
		this.gameRobot = startGameRobot;
	}

	public Player(String startXO, boolean startsOnZeroSide, int startValueFactor) {
		this.xo = startXO;
		this.isOnZeroSide = startsOnZeroSide;
		this.valueFactor = startValueFactor;
	}

	public Player (boolean startsOnZeroSide) {
		this.isOnZeroSide = startsOnZeroSide;
	}

	public int getValueFactor() {
		return this.valueFactor;
	}

	public boolean getIsOnZeroSide() {
		return this.isOnZeroSide;
	}

	public Robot getRobot() {
		return this.gameRobot;
	}

	public String getColor() {
		return this.color;
	}

	public Piece[] getPlayerPieces (Board b) {
		Piece[] result = new Piece[b.totalPiecesLeft(this)];
		int i = 0;
		for (Piece p : b.getPiecesOnBoard()) {
			if (p.getPlayer() == this) {
				result[i++] = p;
			}
		}
		return result;
	}

	public String getXO() {
		if (this.xo ==null) {
			return " ";
		}
		return this.xo;
	}

	public void setOpponent(Player p) {
		this.opponent = p;
	}

	public Move calculateBestMove (int recursionDepth) {
		return this.rankBestMoves(recursionDepth)[0];
	}

	public Move[] rankBestMoves (int recursionDepth) {
		Move[] ma = this.getAllMoves(this.getBoard());
		Board[] ba = new Board[ma.length];
		double[] va = new double[ma.length];
		for (int i=0;i<ma.length;i++) {
			ba[i] = new Board(this.getBoard());
			va[i] = this.minimax(ba[i],recursionDepth,this.valueFactor);
		}
		Double[] sortedva = new Double[ma.length];
		for (int i=0;i<ma.length;i++) {
			sortedva[i] = va[i];
			//logs unsorted move values
		}
		Move[] result = new Move[ma.length];
		if (this.valueFactor == 1) {
			Arrays.sort(sortedva, Collections.reverseOrder());
		}
		else {
			Arrays.sort(sortedva);
		}
		for (int i=0;i<ma.length;i++) {
			//logs sorted move values
			int index = ArraysHelper.find(va,sortedva[i]);
			result[i] = ma[index];				
		}			
		
		return result;
	}

	public double minimax(Board b, int recursionDepth, int minOrMax) {
		if (recursionDepth == 0) {
			return b.calculateValue();
		}
		else {
			Move[] ma = this.getAllMoves(b);
			Board[] ba = new Board[ma.length];
			double[] va = new double[ma.length];
			for (int i=0;i<ma.length;i++) {
				ba[i] = new Board(b,ma[i]);
				va[i] = this.opponent.minimax(ba[i],recursionDepth-1,minOrMax*-1);
			}
			double value = 0;
			for (double v : va) {
				if (minOrMax == 1) {
					if (v>=value) {
						value = v;
					}
				}
				else {
					if (v<=value) {
						value = v;
					}
				}
			}
			return value;
		}
	}

	public static void performMove(Move myMove, Board theBoard) {
		myMove.getMovePiece().setLocation(myMove.getDestination());
		if (myMove.getMovePiece().getPlayer().getIsOnZeroSide()) {
			if (myMove.getMovePiece().getLocation()[1]==7) {
				myMove.getMovePiece().setIsKing(true);
			}
		} else {
			if (myMove.getMovePiece().getLocation()[1]==0) {
				myMove.getMovePiece().setIsKing(true);
			}
		}
		for (Piece deadPiece : myMove.calculatePiecesToJump(theBoard)) {
			theBoard.removePiece(deadPiece);
		}
	}
	
	public void setBoard (Board newBoard) {
		this.myBoard = newBoard;
	}

	public Board getBoard () {
		return this.myBoard;
	}

	public Move[] getAllMoves (Board b) {
		//creates an ArrayList to return later
		ArrayList<Move> result = new ArrayList<Move>();
		//iterates over each of the player's pieces
		for (Piece playerPiece : this.getPlayerPieces(b)) {
			//iterates over all of that piece's moves
			for (Move pieceMove : playerPiece.getMovesOfPiece(b)) {
				//adds each move to the return ArrayList
				result.add(pieceMove);
			}
		}
		//returns the final result
		return result.toArray(new Move[result.size()]);
	}

	public abstract void takeTurn(Game g);
}