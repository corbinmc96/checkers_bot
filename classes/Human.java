public class Human extends Player {
	
	public Human() {
		//nothing
	}
	
	public Human (Robot startGameRobot) {
		super(startGameRobot);
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