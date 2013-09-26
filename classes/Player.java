import java.util.ArrayList;

public abstract class Player {

	private Board myBoard;
	private Robot gameRobot;

	public Player (Robot startGameRobot) {
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
		//creates array list to store result
		ArrayList<Piece> result;
		//iterates through all player's pieces
		for (Piece playerPiece : this.getPlayerPieces()) {
			//test if the the current piece is a king
			if (playerPiece.getIsKing()) {
				//iterates through all possible displacements for a normal move
				for (byte[] displacement : new byte[][]{new byte[]{1,1}, new byte[] {1,-1}, new byte[] {-1,1}, new byte[] {-1,-1}) {
					//calculates the location to be moved to.
					byte[] potentialEndpoint = new byte[] {playerPiece.getLocation()[0]+displacement[0],playerPiece.getLocation()[1]+displacement[1]};
					//tests that destination is unoccupied and in bounds
					if (myBoard.getPieceAtLocation(potentialEndpoint)==null && myBoard.locationIsInBounds(potentialEndpoint)) {
						//move is valid, added to return
						result.add(Move(playerPiece,new byte[][] {playerPiece.getLocation(),potentialEndpoint}));
					}
				}
				while (true) {
					for (byte[] displacement : new byte[][]{new byte[]{2,2}, new byte[] {2,-2}, new byte[] {-2,2}, new byte[] {-2,-2}) {
						//calculates the location to be moved to.
						byte[] potentialEndpoint = new byte[] {playerPiece.getLocation()[0]+displacement[0],playerPiece.getLocation()[1]+displacement[1]};
						//tests that destination is unoccupied and in bounds
						if (myBoard.getPieceAtLocation(potentialEndpoint)==null && myBoard.locationIsInBounds(potentialEndpoint)) {
							//move is valid, added to return
							result.add(Move(playerPiece,new byte[][] {playerPiece.getLocation(),potentialEndpoint}));
						}
						else {
							break; // still working here -- corbin
						}
					}
				}
			}
			else {
				for (byte[] displacement : new byte[][]{new byte[]{1,1}, new byte[] {-1,1})
			}
		}
	}

	public abstract void takeTurn();
}