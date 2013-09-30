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
		byte[][][] allWaypoints = this.getMovesFromLocation(this.location, false);
	}

	public byte[][][] getMovesFromLocation (byte[] pieceLocation, boolean mustBeJump) {
		ArrayList<byte[][][]> result = new ArrayList<byte[][][]>();
		if (this.isKing) {
			if (!mustBeJump) {
				for (displacement : new byte[][] {new byte[] {1,1}, new byte[] {1,-1}, new byte[] {-1,1}, new byte[] {-1,-1}}) {
					byte[] testDestination = new byte[] {pieceLocation[0]+displacement[0], pieceLocation[1]+displacement[1]};
					if (this.owningPlayer.myBoard.getPieceAtLocation(testDestination) == null) {
						result.add(new byte[][] {this.location,testDestination});
					}
				}
			}
			for (displacement : new byte[][] {new byte[] {2,2}, new byte[] {2,-2}, new byte[] {-2,2}, new byte[] {-2,-2}}) {
				byte[] testDestination = new byte[] {pieceLocation[0]+displacement[0], pieceLocation[1]+displacement[1]};
				byte[] midpoint = new byte[] {pieceLocation[0]+displacement[0]/2, pieceLocation[1]+displacement[1]/2}
				if (this.owningPlayer.myBoard.getPieceAtLocation(testDestination) == null && this.owningPlayer.myBoard.getPieceAtLocation() != null && this.owningPlayer.myBoard.getPieceAtLocation().owningPlayer != this.owningPlayer) {
					result.add(new byte[][] {this.locaation,testDestination}) // still working here -- Corbin
				}
			}
		}
	}
}
