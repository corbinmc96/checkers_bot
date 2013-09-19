public class Human extends Player {

	private Robot gameRobot;

	public Human (Board startBoard) {
		super(startBoard);
	}
	
	public Human (Board, startBoard, Robot startGameRobot) {
		this(startBoard);
		this.gameRobot=startGameRobot;
	}

	public void takeTurn() {
		super.performMove(this.inputMove());
	}

	public Move inputMove() {
		//implementation
	}
}