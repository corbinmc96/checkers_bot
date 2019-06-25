// ALL CORBIN

import java.util.Arrays;

public class SimPlayer extends Player {

	public SimPlayer (String startColor, boolean startsOnZeroSide, Robot startGameRobot, AIEngine startBrain) {
		//calls the Player constructor with the same arguments
		super(startColor, startsOnZeroSide, startGameRobot, startBrain);
	}

	public SimPlayer (String startXO, boolean startOnZeroSide, AIEngine startBrain) {
		//calls the Player constructor with the same arguments
		super(startXO, startOnZeroSide, startBrain);
	}

	public Move takeTurn(Game g) {
		//calculates the best move to make the specified number of plies ahead
		Move m = this.calculateBestMove(g, 5);
		//performs the move on the stored (and the physical) board
		this.performMove(m);
		// System.out.println(Arrays.deepToString(m.getWaypoints()));
		return m;
	}
		
	public void performMove(Move myMove) {
		//performs the move on the stored board
		Player.performMove(myMove,this.getBoard());
		//tests if game is using the robot
		if (this.getRobot()!=null) {
			//move arm over moving piece
			this.getRobot().moveMagnetToXY(myMove.getSource());
			//pcks up the piece
			this.getRobot().pickUpPiece();
			//moves piece to the destination
			this.getRobot().moveMagnetToXY(myMove.getDestination());
			//drops the piece
			this.getRobot().dropPiece();
			//iterates over all jumped pieces
			for (int[] deadPiece : myMove.calculatePiecesToJump(this.getBoard())) {
				//move arm over the piece that was jumped
				this.getRobot().moveMagnetToXY(new int[] {deadPiece[0], deadPiece[1]});
				//picks up jumped piece
				this.getRobot().pickUpPiece();
				//moves arm over drop point for dead pieces
				this.getRobot().moveMagnetToXY(Robot.DEAD_LOCATION);
				//drops the piece
				this.getRobot().dropPiece();
			}
			//resets arm after all pieces have been moved.
			this.getRobot().resetPosition();
		}
	}
}
