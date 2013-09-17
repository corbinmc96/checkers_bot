public class Move {

	private Piece movePiece;
	//waypoints are in order and include the starting point and the destination
	private byte[][] waypoints;
	private int jumpsContained;
	private Piece[] piecesToJump;

	public Move (Piece startPiece, byte[][] startWaypoints) {
		this.movePiece = startPiece;
		this.waypoints=startWaypoints;
		this.getJumpsContained = startWaypoints[].length - 2;
	}

	public byte[] getSource () {
		return this.waypoints[0];
	}

	public byte[] getDestination () {
		return this.waypoints[this.waypoints.length-1];
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
