public class Move {

	private byte[] source;
	private byte[] destination;
	private byte[][] waypoints;
	private Piece movePiece;
	//private int jumpsContained; I THOUGHT IT MIGHT BE EASIER JUST TO FIND LENGTH OF PIECESTOJUMP BUT IF YOU WANNT IT FEEL FREE TO TAKE OUT COMMENT MARKS.
	private Piece[] piecesToJump;

	public Move (Piece startPiece, byte[] startSource, byte[] startDestination) {
		//constructor implementation
	}

	public Move (Piece startPiece, byte[] startSource, byte[] startDestination, byte[][] startWaypoints) {
		//constructor implementation
	}

	public boolean getIsViable () {
		//implementation
		//see line 3 comment
	}

	public byte[] getSource () {
		//implementation
	}

	public byte[] getDestination () {
		//implementation
	}

	public Byte[][] getWaypoints () {
		//implementation
	}

	public Piece getMovePiece () {
		//implementation
	}

	//public boolean getJumpsContained () {
	//	//implementation
	//}

	public Piece getPiecesToJump () {
		//implementation
	}

	public void perform () {
		//implementation
	}
}
