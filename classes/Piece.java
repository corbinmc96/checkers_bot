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

	public Move[] getMovesOfPiece() {
		this.getMovesOfPiece(new byte[][] {this.location}, false);
	}

	public Move[] getMovesOfPiece(byte[][] waypointStack, boolean mustBeJump) {
		if (this.isKing) {
			for (byte[] displacement : new byte[][] {new byte[]{1,1}, new byte[] {1,-1}, new byte[] {-1,1}, new byte[] {}
		}
		else {
			if (!mustBeJump) {
				for byte[] displacement // still working here -- Corbin
			}
		}
	}
}
