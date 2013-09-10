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
		if (gameRobot == null) {
			super.performMove()
		} else {
			//implementation
		}
	} //if game is to be simulated without a robot arm the performMove method of Player() will be called.
}