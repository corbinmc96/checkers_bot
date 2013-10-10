import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;

public class Human extends Player {
	
	private String xo;
	private String inputLine;
	private Scanner in;

	public Human(String startColor, boolean startsOnZeroSide) {
		super(startColor, startsOnZeroSide);
	}
	
	public Human (String startColor, boolean startsOnZeroSide, Robot startGameRobot) {
		super(startColor, startsOnZeroSide, startGameRobot);
	}

	public Human (String startXO, boolean startOnZeroSide) {
		super(startOnZeroSide);
		this.xo = startXO;
		this.in = Scanner(System.in);
	}

	public String getXO() {
		if (this.xo ==null) {
			return " ";
		}
		return this.xo;
	}

	public void takeTurn(Game g) {
		super.performMove(this.inputMove(g), g.getGameBoard());
	}

	public Move inputMove(Game g) {
		if (this.getRobot()!=null) {
			//creates dictionary to hold scanned values
			ArrayList<byte[]> scannedLocations = new ArrayList<byte[]>();
			ArrayList<String> locationValues = new ArrayList<String>();
			//gets list of moves from best to worst
			Move[] possibleMoves = this.rankBestMoves(g, 1);
			
			//iterates over all possible moves
			for (Move m : possibleMoves) {
				//gets all waypoints of the move
				byte[][] waypoints = m.getWaypoints();
				//declares pointColor variable
				String pointColor;
				
				//declares variable to determine if the loop needs to continue
				boolean shouldContinue = false;
				//iterates over all waypoints which should be empty
				for (byte[] waypoint : ArraysHelper.copyOfRange(waypoints, 0, waypoints.length-1)) {
					if (scannedLocations.contains(waypoint)) {
						pointColor = locationValues.get(scannedLocations.indexOf(waypoint));
					} else {
						pointColor = this.getRobot().examineLocation(waypoint);
						scannedLocations.add(waypoint);
						locationValues.add(pointColor);
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
				
				//sets last waypoint
				byte[] waypoint = waypoints[waypoints.length-1];
				//checks the color of the last waypoint
				if (scannedLocations.contains(waypoint)) {
					pointColor = locationValues.get(scannedLocations.indexOf(waypoint));
				} else {
					pointColor = this.getRobot().examineLocation(waypoints[waypoints.length-1]);
					scannedLocations.add(waypoint);
					locationValues.add(pointColor);
				}
				//checks if the correct piece is not on the square
				if (pointColor!=this.getColor()) {
					continue;
				} else {
					//this must be the right move, so return it
					return m;
				}
			}
			
			//failed to find the right move
			return null;
		
		} else {
			for (byte y : new byte[] {0,1,2,3,4,5,6,7}) {
				String[] theLine = new String[8];
				for (byte x : new byte[] {0,1,2,3,4,5,6,7}) {
					theLine[x] = super.getBoard().getPieceAtLocation(new byte[] {x,y}).getPlayer().getXO();
				}
				System.out.println(Arrays.toString(theLine));
			}
			boolean moveEntered = false;
			while (!moveEntered) {
				System.out.println("Enter Move:");
				this.inputLine = this.in.nextLine();
				int numberOfWaypoints = (this.inputLine.length+1)/3;
				byte[][] allWaypoints = new byte[][numberOfWaypoints];
				for (int i=0; i<numberOfWaypoints; i++) {
					allWaypoints[i] = new byte[] {Byte.valueOf(inputLine[3*i]),Byte.valueOf(inputLine[3*i+1])};
				}
				inputMove = Move(myBoard.getPieceAtLocation(allWaypoints[0]),allWaypoints);
				if (inputMove.calculateIfValid()) {
					moveEntered = true;
				}
			}
			super.perform(inputMove);
		}
	}
}