import java.util.concurrent.Callable;
import java.util.Arrays;

public class ValueCalculator implements Callable<Double> {
	private Game origGame;
	private int startRecursionDepth;
	private boolean startTestOpponentMoves;
	private Player player;
	
	public ValueCalculator (Game g, int recursionDepth, boolean testOpponentMoves, Player p) {
		this.origGame = g;
		this.startRecursionDepth = recursionDepth;
		this.startTestOpponentMoves = testOpponentMoves;
		this.player = p;
	}
	
	public Double call() {
		return this.valueOfMoves(this.origGame, this.startRecursionDepth, this.startTestOpponentMoves);
	}
	
	private double valueOfMoves(Game g, int recursionDepth, boolean testOpponentMoves) {
		if (g.isDraw()) {
			// System.out.println(Arrays.deepToString(g.getLastFewMoves()));
			// System.out.println("Detected possible draw");
			return 0;
		}

		//creates array to hold all possible moves
		Move[] moves;
		if (testOpponentMoves) {
			moves = g.getOtherPlayer(this.player).getAllMoves(g.getGameBoard());
		} else {
			moves = this.player.getAllMoves(g.getGameBoard());
		}

		if (moves.length==0) {
			if (testOpponentMoves) {
				return 2*Board.maxBoardValue;
			} else {
				return -2*Board.maxBoardValue;
			}
		}

		//creates array to hold values of boards
		double[] boardValues = new double[moves.length];

		//if recursionDepth is one, calculate direct values of moves
		if (recursionDepth==1) {
			//iterates over all moves and calculates values to put in boardValues
			for (int i = 0; i<moves.length; i++) {
				boardValues[i] = (new Board(g.getGameBoard(), moves[i])).calculateValue(this.player);
			}
		
		//recursionDepth must be greater than one, so get values of the best opponent moves for each possible move
		} else {
			//iterates over all moves and calculates value based on best opponent move
			for (int i = 0; i<moves.length; i++) {
				boardValues[i] = this.valueOfMoves(new Game(g, moves[i]), recursionDepth-1, !testOpponentMoves);
			}
		}

		//creates array to hold sorted values from lowest to highest
		double[] boardValuesSorted = Arrays.copyOf(boardValues, boardValues.length);
		Arrays.sort(boardValuesSorted);

		//creates variable to hold result value
		double result;
		if (testOpponentMoves) {
			result = boardValuesSorted[0] * 0.7;
			//iterates over all values except the last
			for (int i = 1; i<boardValuesSorted.length; i++) {
				result += boardValuesSorted[i] * 0.3/(boardValuesSorted.length-1);
			}
		} else {
			result = boardValuesSorted[boardValuesSorted.length-1];
		}

		//logs the values for debugging
		// g.getGameBoard().printBoard();
		// Move[] sortedMoves = new Move[moves.length];
		// int index = 0;
		// for (int i = 0; i<boardValuesSorted.length; i++) {
		// 	index = ArraysHelper.find(boardValues, boardValuesSorted[i]);
		// 	boardValues[index] = Board.maxBoardValue*1000;
		// 	sortedMoves[moves.length-i-1] = moves[index];
		// }
		// for (Move m : sortedMoves) {
		// 	System.out.println(Arrays.deepToString(m.getWaypoints()));
		// }
		// System.out.print("" + recursionDepth + " ");
		// System.out.println(Arrays.toString(boardValuesSorted));
		// System.out.println(result / 10);

		return result;
	}
}