// ALL CORBIN

import java.util.Arrays;

public class SimPlayer extends Player {

	private static int CALCULATION_DEPTH = 9;

	public SimPlayer (String startXO, boolean startOnZeroSide, AIEngine startBrain) {
		//calls the Player constructor with the same arguments
		super(startXO, startOnZeroSide, startBrain);
	}

	public Move takeTurn(Game g) {
		//calculates the best move to make the specified number of plies ahead
		Move m = this.calculateBestMove(g, this.CALCULATION_DEPTH);
		//performs the move on the stored (and the physical) board
		this.performMove(m);
		// System.out.println(Arrays.deepToString(m.getWaypoints()));
		return m;
	}
		
	public void performMove(Move myMove) {
		Player.performMove(myMove,this.getBoard());
	}
}
