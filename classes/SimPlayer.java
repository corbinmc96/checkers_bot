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
		super.performMove(myMove);
		if (this.gameRobot!=null) {
			//extra code to move the Robot.
		}
	} 
}
