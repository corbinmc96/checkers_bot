public class SimPlayer extends Player {

	private Robot gameRobot;

	public SimPlayer () {
		super();
	}

	public SimPlayer (Robot startGameRobot) {
		super();
		this.gameRobot = startGameRobot;
	}

	public void takeTurn() {
		this.performMove(super.caluclateBestMove());
	}

	public void performMove(Move myMove) {
		Player.performMove(myMove,super.getBoard());
		if (this.gameRobot!=null) {
			this.gameRobot.moveToXY(myMove.getSource());
			this.gameRobot.pickUpPiece();
			this.gameRobot.moveToXY(myMove.getDestination());
			this.gameRobot.dropPiece();
			if (myMove.) {
				for (int i=1;i>myMove.getWaypoints().length-1;i++) {
					Piece de
				} // still working here -- Corbin
			}
		}
	} 
}
