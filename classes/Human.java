import java.util.ArrayList;
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
		Hashtable<byte[], String> scannedLocations = new Hashtable<byte[], String>();
		ArrayList<Move> possibleMoves = new ArrayList<Move>(Arrays.asList(this.rankBestMoves()));
		for (Move m : possibleMoves) {
			String startColor = this.gameRobot.examineLocation(m.getSource());
			if (startColor==this.color) {
				possibleMoves.remove(m);
				if (possibleMoves.getLength()==1) {
					break;
				}
			}
		}

		for (Move m2 : possibleMoves) {
			String endColor = this.gameRobot.examineLocation(m2.getDestination());
			if (endColor!=this.color) {
				possibleMoves.remove(m2);
				if (possibleMoves.getLength()==1) {
					break;
				}
			}
		}

		for (Move m3 : possibleMoves) {
			for (byte[] waypoint : Arrays.copyOfRange(m3.getWaypoints(), 1, m3.getJumpsContained()+1)) {
				String pointColor = this.gameRobot.examineLocation(waypoint);
				if (pointColor!=Board.color) {
					possibleMoves.remove(m3);
					if (possibleMoves.getLength()==1) {
						break;
					}
				}
			}
		}
	//finish implementation - Aaron
	}
}