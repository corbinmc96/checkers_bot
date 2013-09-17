public class Piece {

	private boolean isKing;
	private byte[] location;
	private Player owningPlayer; 

	public Piece (byte[] startLocation, Player startPlayer) {
		this.isKing=False;
		this.location=startLocation;
		this.owningPlayer=startPlayer;
	}

	public boolean getIsking () {
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

	public static Piece[] getAllPieces() {
		return owningPlayer.getBoard().getPiecesOnBoard();
	}

	public static Piece getPieceAtLocation(byte[] checkLocation) {
		for (checkPiece : this.getAllPieces()) {
			if (checkPiece.getLocation() == checkLocation) {
				return checkPiece;
			}
		}
	}
}