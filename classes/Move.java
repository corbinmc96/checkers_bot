import java.util.ArrayList;

public class Move {

	private Piece movePiece;
	//waypoints are in order and include the starting point and the destination
	private int[][] waypoints;
	//number of jumps contained
	private int jumpsContained;
	//is the move valid

	public Move (Piece startPiece, int[][] startWaypoints) {
		//instantiate all the variables
		this.movePiece = startPiece;
		this.waypoints = startWaypoints;
		//calculate the number of pieces to be jumped
		if (Math.abs(startWaypoints[1][0]-startWaypoints[0][0])==2 && Math.abs(startWaypoints[1][1]-startWaypoints[0][1])==2) {
			this.jumpsContained = startWaypoints.length - 1;
		} else {
			this.jumpsContained = 0;
		}
		//implementation here to check if move is valid.
	}
	
	//get the souce location: the position the moving Piece is originating from
	public int[] getSource () {
		return this.waypoints[0];
	}

	//get the final destination of the moving piece
	public int[] getDestination () {
		return this.waypoints[this.waypoints.length-1];
	}

	//get an array of every waypoint the moving piece will stop at
	public int[][] getWaypoints () {
		return this.waypoints;
	}

	//get the piece the that is moving
	public Piece getMovePiece () {
		return this.movePiece;
	}

	//get the number of jumps that the piece will perform
	public int getJumpsContained () {
		return this.jumpsContained;
	}
	
	//calculate an array of all the Piece objects that will be jumped during the move
	public Piece[] calculatePiecesToJump () {
		//if no jumps, return empty list
		if (this.jumpsContained == 0) {
			return new Piece[0];
		}
		else {
			ArrayList<Piece> result = new ArrayList<Piece>();
			//cycle through all the movements the piece makes
			for (int i=1; i<=this.waypoints.length-1;i++) {
				//find the location that is being jumped over
				int[] midpoint = {(this.waypoints[i][0]+this.waypoints[i-1][0])/2, (this.waypoints[i][1]+this.waypoints[i-1][1])/2};
				result.add(this.movePiece.getParentBoard().getPieceAtLocation(midpoint));
			}
			return result.toArray(new Piece[result.size()]);
		}
	}

	public boolean calculateIsValid () {
		Player thePlayer = this.movePiece.getPlayer();
		Board theBoard = this.movePiece.getParentBoard();

		Move[] validMoves = thePlayer.getAllMoves(theBoard);
		for (Move validMove : validMoves) {

			if (validMove.getJumpsContained() != this.getJumpsContained()) {
				continue;
			}

			for (int waypoint = 0; waypoint<validMove.getJumpsContained()+1; waypoint++) {
				if (this.waypoints[waypoint][0]!=validMove.getWaypoints()[waypoint][0] || this.waypoints[waypoint][1]!=validMove.getWaypoints()[waypoint][1]) {
					continue;
				}
			}
			return true;
		}
		return false;
	}
}
