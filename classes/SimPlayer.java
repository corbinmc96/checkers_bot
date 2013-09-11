public class SimPlayer extends Player {

	private Robot gameRobot;

	public SimPlayer () {
		//constructor implementation
	}

	public SimPlayer (Robot startGameRobot) {
		//implementation
	}

	public void takeTurn() {
		//implementation
	}

	public void performMove(Move myMove) {
		super.performMove(myMove);
		if (this.gameRobot!=null) {
			//extra code to move the Robot.
		}
	} 
}
