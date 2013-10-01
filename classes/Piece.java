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
		//creates return ArrayList
		ArrayList<Move> result = new ArrayList<Move>();
		//gets array of all sets of waypoints
		byte[][][] allWaypoints = this.getMovesFromLocation(this.location, false);
		//iterates through each ser
		for (byte[][] theWaypoints : allWaypoints) {
			//creates move and adds to return string
			result.add(Move(this, theWaypoints));
		}
		//returns final result
		return result;
	}

	public byte[][][] getMovesFromLocation (byte[] pieceLocation, boolean mustBeJump) {
		//creates return array
		ArrayList<byte[][][]> result = new ArrayList<byte[][][]>();
		//tests if this piece is king
		if (this.isKing) {
			//tests if this move does not need to be a jump
			if (!mustBeJump) {
				//loops through all displacements
				for (byte[] displacement : new byte[][] {new byte[] {1,1}, new byte[] {1,-1}, new byte[] {-1,1}, new byte[] {-1,-1}}) {
					//finds potential destination
					byte[] testDestination = new byte[] {pieceLocation[0]+displacement[0], pieceLocation[1]+displacement[1]};
					//if destination empty and inbounds
					if (this.owningPlayer.myBoard.getPieceAtLocation(testDestination) == null && Board.locationIsInBounds(testDestination)) {
						//add waypoint set to return array
						result.add(new byte[][] {pieceLocation,testDestination});
					}
				}
			}
			//loops through jump displacements
			for (byte[] displacement : new byte[][] {new byte[] {2,2}, new byte[] {2,-2}, new byte[] {-2,2}, new byte[] {-2,-2}}) {
				//finds potential destinations
				byte[] testDestination = new byte[] {pieceLocation[0]+displacement[0], pieceLocation[1]+displacement[1]};
				//finds location being jumped over
				byte[] midpoint = new byte[] {pieceLocation[0]+displacement[0]/2, pieceLocation[1]+displacement[1]/2};
				//tests that destination is in bounds, destination is unoccupied, and opponent piece is being jumped over
				if (Board.locationIsInBounds(testDestination) && this.owningPlayer.myBoard.getPieceAtLocation(testDestination) == null && this.owningPlayer.myBoard.getPieceAtLocation() != null && this.owningPlayer.myBoard.getPieceAtLocation().owningPlayer != this.owningPlayer) {
					//adds move to return array
					result.add(new byte[][] {pieceLocation,testDestination});
					//cycles through possible multi-jump scenarios
					for (byte[][] potentialMove : this.getMovesFromLocation(testDestination,true)) {
						result.add(ArrayUtils.addAll(new byte[][] {pieceLocation, testDestination},potentialMove));
					}
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
					if (this.owningPlayer.myBoard.getPieceAtLocation(testDestination) == null && Board.locationIsInBounds(testDestination)) {
						result.add(new byte[][] {pieceLocation,testDestination});
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
				if (Board.locationIsInBounds(testDestination) && this.owningPlayer.myBoard.getPieceAtLocation(testDestination) == null && this.owningPlayer.myBoard.getPieceAtLocation() != null && this.owningPlayer.myBoard.getPieceAtLocation().owningPlayer != this.owningPlayer) {
					result.add(new byte[][] {pieceLocation,testDestination});
					for (byte[][] potentialMove : this.getMovesFromLocation(testDestination,true)) {
						result.add(ArrayUtils.addAll(new byte[][] {pieceLocation, testDestination},potentialMove));
					}
				}
			}
		}
	}
}