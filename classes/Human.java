public class Human extends Player {

	public Human () {
		super();
	}
	
	public Human (Robot startGameRobot) {
		
	}

	public void takeTurn() {
		performMove(this.inputMove());
	}

	public Move inputMove() {
		//implementation
	}
}