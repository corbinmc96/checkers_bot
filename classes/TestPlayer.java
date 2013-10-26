import java.util.Arrays;

public class TestPlayer extends SimPlayer {
	public TestPlayer (String startXO, boolean startOnZeroSide) {
		super(startXO, startOnZeroSide);
	}

	public double valueOfBestMove(Game g, int recursionDepth) {
		if (g.isDraw()) {
			// System.out.println(Arrays.deepToString(g.getLastFewMoves()));
			// System.out.println("Detected possible draw");
			return 0;
		}

		//creates array to hold all possible moves
		Move[] moves = this.getAllMoves(g.getGameBoard());

		if (moves.length==0) {
			return -2*Board.maxBoardValue;
		}

		//creates array to hold values of boards
		double[] boardValues = new double[moves.length];

		//if recursionDepth is one, calculate direct values of moves
		if (recursionDepth==1) {
			//iterates over all moves and calculates values to put in boardValues
			for (int i = 0; i<moves.length; i++) {
				boardValues[i] = (new Board(g.getGameBoard(), moves[i])).calculateValue(this);
			}
		
		//recursionDepth must be greater than one, so get values of the best opponent moves for each possible move
		} else {
			//iterates over all moves and calculates value based on best opponent move
			for (int i = 0; i<moves.length; i++) {
				boardValues[i] = -g.getOtherPlayer(this).valueOfBestMove(new Game(g, moves[i]), recursionDepth-1);
			}
		}

		//creates array to hold sorted values from lowest to highest
		double[] boardValuesSorted = Arrays.copyOf(boardValues, boardValues.length);
		Arrays.sort(boardValuesSorted);

		//creates variable to hold result value
		double result = boardValuesSorted[boardValuesSorted.length-1] * 9;
		//iterates over all values except the last
		for (int i = 0; i<boardValuesSorted.length-1; i++) {
			result += boardValuesSorted[i] * 1/(boardValuesSorted.length-1);
		}

		//logs the values for debugging
		// g.getGameBoard().printBoard();
		//Move[] sortedMoves = new Move[moves.length];
		//int index = 0;
		//for (int i = 0; i<boardValuesSorted.length; i++) {
		//	index = ArraysHelper.find(boardValues, boardValuesSorted[i]);
		//	boardValues[index] = Board.maxBoardValue*1000;
		//	sortedMoves[moves.length-i-1] = moves[index];
		//}
		//for (Move m : sortedMoves) {
		//	System.out.println(Arrays.deepToString(m.getWaypoints()));
		//}
		// System.out.print("" + recursionDepth + " ");
		// System.out.println(Arrays.toString(boardValuesSorted));
		// System.out.println(result / 10);

		return result / 10;
	}

}