public class Move {

	private byte[] source;
	private byte[] destination;
	private byte[][] waypoints;
	private Piece movePiece;
	private int jumpsContained;
	private Piece[] piecesToJump;

	public Move (Piece startPiece, byte[] startSource, byte[] startDestination) {
		this.movePiece = startPiece;
		this.source = startSource;
		this.destination = startDestination;
		if this.destination[0] = 
	}

	public Move (Piece startPiece, byte[] startSource, byte[] startDestination, byte[][] startWaypoints) {
		this.Move(startPiece, startSource, startDestination);
	}

	public byte[] getSource () {
		return this.source;
	}

	public byte[] getDestination () {
		return this.destination;
	}

	public byte[][] getWaypoints () {
		return this.waypoints;
	}

	public Piece getMovePiece () {
		return this.movePiece;
	}

	public int getJumpsContained () {
		return this.jumpsContained;
	}

	public Piece[] getPiecesToJump () {
		return this.piecesToJump;
	}
}
