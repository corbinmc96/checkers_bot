public class SimPlayer extends Player {

	public SimPlayer(String startColor, boolean startsOnZeroSide) {
		super(startColor, startsOnZeroSide);
	}

	public SimPlayer (String startColor, boolean startsOnZeroSide, Robot startGameRobot) {
		super(startColor, startsOnZeroSide, startGameRobot);
	}

	public void takeTurn(Game g) {
		this.performMove(super.calculateBestMove(g, 4));
	}
	
	//temporary method to allow for testing
	public String getXO() {
		return null;
	}
	
	public void performMove(Move myMove) {
		Player.performMove(myMove,super.getBoard());
		//tests if game is using the robot
		if (this.getRobot()!=null) {
			//move arm over moving piece
			this.getRobot().moveToXY(myMove.getSource());
			//pcks up the piece
			this.getRobot().pickUpPiece();
			//moves piece to the destination
			this.getRobot().moveToXY(myMove.getDestination());
			//drops the piece
			this.getRobot().dropPiece();
			//iterates over all jumped pieces
			for (Piece deadPiece : myMove.calculatePiecesToJump()) {
				//move arm over the piece that was jumped
				this.getRobot().moveToXY(deadPiece.getLocation());
				//picks up jumped piece
				this.getRobot().pickUpPiece();
				//moves arm over drop point for dead pieces
				this.getRobot().moveToXY(myMove.getDestination());
				//drops the piece
				this.getRobot().dropPiece();
			}
			//resets arm after all pieces have been moved.
			this.getRobot().resetPosition();
		}
	} 
}
