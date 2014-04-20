// ALL AARON, EXCEPT CORBIN WROTE XO CODE AND getAllMoves

import java.util.Arrays;
import java.util.ArrayList;

public abstract class Player {

	private Board myBoard;
	private Robot gameRobot;
	//true if the player is located on the side of the board marked with index 0
	private boolean isOnZeroSide;
	//contains the piece color
	private String color;

	private String xo;
	private AIEngine brain;

	public Player (String startColor, boolean startsOnZeroSide, Robot startGameRobot, AIEngine startBrain) {
		this.color = startColor;
		this.isOnZeroSide = startsOnZeroSide;
		this.gameRobot = startGameRobot;
		this.brain = startBrain;
	}

	public Player(String startXO, boolean startsOnZeroSide, AIEngine startBrain) {
		this.xo = startXO;
		this.isOnZeroSide = startsOnZeroSide;
		this.brain = startBrain;
	}

	public Player (boolean startsOnZeroSide, AIEngine startBrain) {
		this.isOnZeroSide = startsOnZeroSide;
		this.brain = startBrain;
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

	public String getXO() {
		if (this.xo ==null) {
			return " ";
		}
		return this.xo;
	}
	
	public AIEngine getBrain() {
		return this.brain;
	}

	public Move calculateBestMove (Game g, int recursionDepth) {
		return this.brain.rankBestMove(this.getAllMoves(g), g, this, recursionDepth)[0];
	}

	public static void performMove(Move myMove, Board theBoard) {
		theBoard.movePiece(myMove.getSource(), myMove.getDestination());
		if (theBoard.ownerOfPieceAtLocation(myMove.getDestination()).getIsOnZeroSide()) {
			if (myMove.getDestination()[1]==7) {
				theBoard.kingPiece(myMove.getDestination());
			}
		} else {
			if (myMove.getDestination()[1]==0) {
				theBoard.kingPiece(myMove.getDestination());
			}
		}
		for (int[] deadPiece : myMove.calculatePiecesToJump(theBoard)) {
			theBoard.removePiece(deadPiece);
		}
	}
	
	public void setBoard (Board newBoard) {
		this.myBoard = newBoard;
	}

	public Board getBoard () {
		return this.myBoard;
	}

	public Move[] getAllMoves (Game g) {
		//creates an ArrayList to return later
		ArrayList<Move> result = new ArrayList<Move>();
		//creates a variable to store whether a jump has been found, only used in official version
		boolean canJump = false;
		boolean playOfficial = g.getIsOfficialVersion();

		Board b = g.getGameBoard();

		//iterates over each of the player's pieces
		for (int[] playerPiece : b.getPlayerPieces(this)) {

			//iterates over all of that piece's moves
			for (Move pieceMove : b.getMovesOfPiece(playerPiece)) {
				//adds each move to the return ArrayList
				if (pieceMove.getJumpsContained()>0) {
					result.add(0, pieceMove);
					canJump = true;
				} else {
					result.add(pieceMove);
				}
			}
		}

		if (playOfficial && canJump) {
			ArrayList<Move> filteredResult = new ArrayList<Move>();
			for (Move m : result) {
				if (m.getJumpsContained()>0) {
					filteredResult.add(m);
				} else {
					break;
				}
			}
			return filteredResult.toArray(new Move[filteredResult.size()]);
		}
		
		//returns the final result
		return result.toArray(new Move[result.size()]);
	}

	public abstract Move takeTurn(Game g);
}