import java.util.ArrayList;

public class Piece {

	private boolean isKing;
	private byte[] location;
	private Player owningPlayer; 

	public Piece (byte[] startLocation, Player startPlayer) {
		this.isKing = false;
		this.location=startLocation;
		this.owningPlayer=startPlayer;
	}

	public boolean getIsKing () {
		return this.isKing;	
	}

	public void setIsKing (boolean changeIsKing) {
		this.isKing=changeIsKing;
	}

	public byte[] getLocation () {
		return this.location;
	}

	public void setLocation (byte[] changeLocation) {
		this.location = changeLocation;
	}

	public Player getPlayer () {
		return this.owningPlayer;
	}

	public Move[] getMovesOfPiece () {
		ArrayList<Move> result = new ArrayList<Move>();
		byte[][][] allWaypoints = this.getMovesFromLocation(this.location, false);
		for (byte[][] theWaypoints : allWaypoints) {
			result.add(Move(this, theWaypoints));
		}
		return result;
	}

	public byte[][][] getMovesFromLocation (byte[] pieceLocation, boolean mustBeJump) {
		ArrayList<byte[][][]> result = new ArrayList<byte[][][]>();
		if (this.isKing) {
			if (!mustBeJump) {
				for (byte[] displacement : new byte[][] {new byte[] {1,1}, new byte[] {1,-1}, new byte[] {-1,1}, new byte[] {-1,-1}}) {
					byte[] testDestination = new byte[] {pieceLocation[0]+displacement[0], pieceLocation[1]+displacement[1]};
					if (this.owningPlayer.myBoard.getPieceAtLocation(testDestination) == null) {
						result.add(new byte[][] {this.location,testDestination});
					}
				}
			}
			for (byte[] displacement : new byte[][] {new byte[] {2,2}, new byte[] {2,-2}, new byte[] {-2,2}, new byte[] {-2,-2}}) {
				byte[] testDestination = new byte[] {pieceLocation[0]+displacement[0], pieceLocation[1]+displacement[1]};
				byte[] midpoint = new byte[] {pieceLocation[0]+displacement[0]/2, pieceLocation[1]+displacement[1]/2};
				if (this.owningPlayer.myBoard.getPieceAtLocation(testDestination) == null && this.owningPlayer.myBoard.getPieceAtLocation() != null && this.owningPlayer.myBoard.getPieceAtLocation().owningPlayer != this.owningPlayer) {
					result.add(new byte[][] {this.locaation,testDestination});
					for (byte[][] potentialMove : this.getMovesFromLocation(testDestination,true)) {
						restult.add(potentialMove);
					}
					return result;
				}
			}
		}
		else {
			if (!mustBeJump) {
				if (this.owningPlayer.getIsOnZeroSide() == true) {
					byte[][] regularDisplacements = new byte[][] {new byte[] {1,1}, new byte[] {-1,1}};
				}
				else {
					byte[][] regularDisplacements = new byte[][] {new byte[] {1,-1}, new byte[] {1,-1}};
				}
				for (byte[] displacement : regularDisplacements) {
					byte[] testDestination = new byte[] {pieceLocation[0]+displacement[0], pieceLocation[1]+displacement[1]};
					if (this.owningPlayer.myBoard.getPieceAtLocation(testDestination) == null) {
						result.add(new byte[][] {this.location,testDestination});
					}
				}
			}
			if (this.owningPlayer.getIsOnZeroSide() == true) {
					byte[][] jumpDisplacements = new byte[][] {new byte[] {2,2}, new byte[] {-2,2}};
				}
				else {
					byte[][] jumpDisplacements = new byte[][] {new byte[] {2,-2}, new byte[] {2,-2}};
				}
			for (byte[] displacement : jumpDisplacements) {
				byte[] testDestination = new byte[] {pieceLocation[0]+displacement[0], pieceLocation[1]+displacement[1]};
				byte[] midpoint = new byte[] {pieceLocation[0]+displacement[0]/2, pieceLocation[1]+displacement[1]/2};
				if (this.owningPlayer.myBoard.getPieceAtLocation(testDestination) == null && this.owningPlayer.myBoard.getPieceAtLocation() != null && this.owningPlayer.myBoard.getPieceAtLocation().owningPlayer != this.owningPlayer) {
					result.add(new byte[][] {this.locaation,testDestination});
					for (byte[][] potentialMove : this.getMovesFromLocation(testDestination,true)) {
						restult.add(potentialMove);
					}
					return result;
				}
			}
		}
	}
}