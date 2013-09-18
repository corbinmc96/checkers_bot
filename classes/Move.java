import java.util.ArrayList;

public class Move {

	private Piece movePiece;
	//waypoints are in order and include the starting point and the destination
	private byte[][] waypoints;
	private int jumpsContained;

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

	public Piece[] calculatePiecesToJump () {
		if (this.jumpsContained == 0) {
			return new Piece[0];
		}
		else {
			ArrayList<Piece> result = new ArrayList<Piece>();
			for (int i=1; i<=this.waypoints.length-1;i++) {
				byte[] midpoint = {(this.waypoints[i][0]+this.waypoints[i-1][0])/2,(this.waypoints[i][1]+this.waypoints[i-1][1])/2};
				result.add(Piece.getPieceAtLocation(midpoint));
			}
			return result.toArray();
		}
	}
}
