public class Human extends Player {
	
	public Human(boolean startsOnZeroSide) {
		super(startsOnZeroSide);
	}
	
	public Human (boolean startsOnZeroSide, Robot startGameRobot) {
		super(startsOnZeroSide, startGameRobot);
	}

	public void takeTurn() {
		super.performMove(this.inputMove());
	}

	public Move inputMove() {
		Move[] possibleMoves = this.rankBestMoves();
		for (Move m : possibleMoves) {
			String color = this.gameRobot.examineLocation(/*add location here*/);
			//finish implementation
		}
	}
}