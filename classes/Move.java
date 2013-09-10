public class Move {

	private byte[] source;
	private byte[] destination;
	private byte[][] waypoints;
	private Piece movePiece;
	private int jumpsContained;
	private Piece[] piecesToJump;

	public Move (Piece startPiece, byte[] startSource, byte[] startDestination) {
		//constructor implementation
	}

	public Move (Piece startPiece, byte[] startSource, byte[] startDestination, byte[][] startWaypoints) {
		//constructor implementation
	}

	public byte[] getSource () {
		//implementation
	}

	public byte[] getDestination () {
		//implementation
	}

	public byte[][] getWaypoints () {
		//implementation
	}

	public Piece getMovePiece () {
		//implementation
	}

	public boolean getJumpsContained () {
		//implementation
	}

	public Piece getPiecesToJump () {
		//implementation
	}
}
