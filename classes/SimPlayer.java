public class SimPlayer extends Player {

	// public SimPlayer(String startColor, boolean startsOnZeroSide) {
	// 	super(startColor, startsOnZeroSide);
	// }

	public SimPlayer (String startColor, boolean startsOnZeroSide, Robot startGameRobot) {
		super(startColor, startsOnZeroSide, startGameRobot);
	}

	public SimPlayer (String startXO, boolean startOnZeroSide, int startValueFactor) {
		super(startXO, startOnZeroSide, startValueFactor);
	}

	public void takeTurn(Game g) {
		this.performMove(this.calculateBestMove(2), super.getBoard());
	}
		
	public void performMove(Move myMove, Board myBoard, Robot myRobot) {
		Player.performMove(myMove,this.getBoard());
		//tests if game is using the robot
		if (myRobot!=null) {
			//move arm over moving piece
			myRobot.moveToXY(myMove.getSource());
			//pcks up the piece
			myRobot.pickUpPiece();
			//moves piece to the destination
			myRobot.moveToXY(myMove.getDestination());
			//drops the piece
			myRobot.dropPiece();
			//iterates over all jumped pieces
			for (Piece deadPiece : myMove.calculatePiecesToJump(myBoard)) {
				//move arm over the piece that was jumped
				myRobot.moveToXY(deadPiece.getLocation());
				//picks up jumped piece
				myRobot.pickUpPiece();
				//moves arm over drop point for dead pieces
				myRobot.moveToXY(myMove.getDestination());
				//drops the piece
				myRobot.dropPiece();
			}
			//resets arm after all pieces have been moved.
			myRobot.resetPosition();
		}
	}
}