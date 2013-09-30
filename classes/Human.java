import java.util.Arrays;

public class Human extends Player {
	
	public Human(String startColor, boolean startsOnZeroSide) {
		super(startColor, startsOnZeroSide);
	}
	
	public Human (String startColor, boolean startsOnZeroSide, Robot startGameRobot) {
		super(startColor, startsOnZeroSide, startGameRobot);
	}

	public void takeTurn() {
		super.performMove(this.inputMove());
	}

	public Move inputMove() {
		//creates dictionary to hold scanned values
		Hashtable<byte[], String> scannedLocations = new Hashtable<byte[], String>();
		//gets list of moves from best to worst
		Move[] possibleMoves = this.rankBestMoves();
		
		//iterates over all possible moves
		for (Move m : possibleMoves) {
			//gets all waypoints of the move
			byte[][] waypoints = m.getWaypoints();
			//declares pointColor variable
			String pointColor;
			
			//declares variable to determine if the loop needs to continue
			boolean shouldContinue = false;
			//iterates over all waypoints which should be empty
			for (byte[] waypoint : Arrays.copyOfRange(waypoints, 0, waypoints.length-1))) {
				if (/*location is in hashtable*/) {
					pointColor = /*color from hashtable*/;
				} else {
					pointColor = this.gameRobot.examineLocation(waypoint);
					//put location in hashtable
				}
				//checks if the square is not empty
				if (pointColor!=Board.color) {
					shouldContinue = true;
					break;
				}
			}
			//continue if the move failed
			if (shouldContinue) {
				continue;
			}
			
			//checks the color of the last waypoint
			if (/*location is in hashtable*/) {
				pointColor = /*color from hashtable*/;
			} else {
				pointColor = this.gameRobot.examineLocation(waypoints[waypoints.length-1]);
				//put location in hashtable
			}
			//checks if the correct piece is not on the square
			if (pointColor!=this.color) {
				continue;
			} else {
				//this must be the right move, so return it
				return m;
			}
		}
		
		//failed to find the right move
		return null;
	}
}