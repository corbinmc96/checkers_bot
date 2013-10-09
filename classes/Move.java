import java.util.ArrayList;

public class Move {

	private Piece movePiece;
	//waypoints are in order and include the starting point and the destination
	private byte[][] waypoints;
	//number of jumps contained
	private int jumpsContained;
	//is the move valid
	private boolean isValid;


	public Move (Piece startPiece, byte[][] startWaypoints) {
		//instantiate all the variables
		this.movePiece = startPiece;
		this.waypoints=startWaypoints;
		//calculate the number of pieces to be jumped
		this.jumpsContained = startWaypoints.length - 2;
		//implementation here to check if move is valid.
	}
	
	//get the souce location: the position the moving Piece is originating from
	public byte[] getSource () {
		return this.waypoints[0];
	}

	//get the final destination of the moving piece
	public byte[] getDestination () {
		return this.waypoints[this.waypoints.length-1];
	}

	//get an array of every waypoint the moving piece will stop at
	public byte[][] getWaypoints () {
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
				byte[] midpoint = {((byte) (this.waypoints[i][0]+this.waypoints[i-1][0])/2), ((byte) (this.waypoints[i][1]+this.waypoints[i-1][1])/2)};
				result.add(this.movePiece.getPlayer().getBoard().getPieceAtLocation(midpoint));
			}
			return result.toArray(new Piece[result.size()]);
		}
	}

	public boolean calculateIsValid () {
		Player thePlayer = this.movePiece.getOwningPlayer();
		Board theBoard = thePlayer.getBoard();
		for (int i=0; i<startWaypoints-1;i++) {
			byte[] start = startWaypoints[i];
			byte[] end = startWaypoints[i+1];
			byte[] midpoint = new byte[]{(end[0]+start[0])/2,(end[1]+start[1])/2}
			byte[] displacement = new byte[] {end[0]-start[0],end[1]-start[1]};
			if (!this.movePiece.getIsKing()) {
				if (this.movePiece.getOwningPlayer().getIsOnZeroSide()) {
					if (Arrays.asList(new byte[][]{new byte[]{1,1}, new byte[]{-1,1}}).contains(displacement)&&theBoard.getPieceAtLocation(end)==null&&Board.locationIsInBounds(end)) {
						return true;
					}
					else if (Arrays.asList(new byte[][]{new byte[]{2,2}, new byte[]{-2,2}}).contains(displacement)&&theBoard.getPieceAtLocation(end)==null&&theBoard.getPieceAtLocation(midpoint)!=null&&theBoard.getPieceAtLocation(midpoint).getOwningPlayer()!=thePlayer&&Board.locationIsInBounds(end)) {
						return true;
					}
					else {
						return false;
					}
				}
				else {
					if (Arrays.asList(new byte[][]{new byte[]{1,-1}, new byte[]{-1,-1}}).contains(displacement)&&theBoard.getPieceAtLocation(end)==null&&Board.locationIsInBounds(end)) {
						return true;
					}
					else if (Arrays.asList(new byte[][]{new byte[]{2,-2}, new byte[]{-2,-2}}).contains(displacement)&&theBoard.getPieceAtLocation(end)==null&&theBoard.getPieceAtLocation(midpoint)!=null&&theBoard.getPieceAtLocation(midpoint).getOwningPlayer()!=thePlayer&&Board.locationIsInBounds(end)) {
						return true;
					}
					else {
						return false;
					}
				}
			}
			else {
					if (Arrays.asList(new byte[][]{new byte[]{1,1}, new byte[]{-1,1}, new byte[] {1,-1}, new byte[] {-1,-1}}).contains(displacement)&&theBoard.getPieceAtLocation(end)==null&&Board.locationIsInBounds(end)) {
						return true;
					}
					else if (Arrays.asList(new byte[][]{new byte[]{2,2}, new byte[]{-2,2},new byte[] {2,-2}, new byte[] {-2,-2}}).contains(displacement)&&theBoard.getPieceAtLocation(end)==null&&theBoard.getPieceAtLocation(midpoint)!=null&&theBoard.getPieceAtLocation(midpoint).getOwningPlayer()!=thePlayer&&Board.locationIsInBounds(end)) {
						return true;
					}
					else {
						return false;
				}
			}
		}
	}
}
