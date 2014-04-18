// ALL AARON

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Callable;

public class OldMultithreadedAI extends AIEngine {
	public Move[] rankBestMove (Move[] moves, Game g, Player p, int recursionDepth) {
		//creates array to hold values of boards
		double[] boardValues = new double[moves.length];

		//if recursionDepth is one, calculate direct values of moves
		if (recursionDepth==1) {
			//iterates over all moves and calculates values to put in boardValues
			for (int i = 0; i<moves.length; i++) {
				boardValues[i] = (new Board(g.getGameBoard(), moves[i])).calculateValue(p);
			}

		//recursionDepth must be greater than one, so get values of the best opponent moves for each possible move
		} else {
			ExecutorService service = Executors.newFixedThreadPool(8);
			
			@SuppressWarnings({"unchecked"})
			Future<Double>[] valueFutures = new Future[moves.length];
			
			for (int i = 0; i<moves.length; i++) {
				valueFutures[i] = service.submit(new ValueCalculator(new Game(g, moves[i]), recursionDepth-1, true, p));
			}
			
			//iterates over all moves and calculates value based on best opponent move
			for (int i = 0; i<moves.length; i++) {
				try {
					boardValues[i] = valueFutures[i].get();
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				} catch (ExecutionException ex) {
					ex.printStackTrace();
				}
			}
			
			service.shutdownNow();
		}

		//creates array to hold sorted values from lowest to highest
		double[] boardValuesSorted = Arrays.copyOf(boardValues, boardValues.length);
		Arrays.sort(boardValuesSorted);

		//creates array to hold final list of moves
		Move[] sortedMoves = new Move[moves.length];
		//creates variable to hold index of move in original list
		int index = 0;
		//iterates over all values of boardValuesSorted
		for (int i = 0; i<boardValuesSorted.length; i++) {
			//finds the index of the current values in the original move list
			index = ArraysHelper.find(boardValues, boardValuesSorted[i]);
			//sets the value at index to very large so that the same move is not used again, even if multiple moves have equal values
			boardValues[index] = Board.MAX_BOARD_VALUE*1000;

			//puts the correct move in the correct position in the final array
			sortedMoves[moves.length-i-1] = moves[index];
		}

		//logs values for debugging
		// g.getGameBoard().printBoard();
		// for (Move m : sortedMoves) {
		// 	System.out.println(Arrays.deepToString(m.getWaypoints()));
		// }
		// System.out.print("" + recursionDepth + " ");
		// System.out.println(Arrays.toString(boardValuesSorted));

		return sortedMoves;
	}

	private static class ValueCalculator implements Callable<Double> {
		private Game origGame;
		private int startRecursionDepth;
		private boolean startTestOpponentMoves;
		private Player player;
		private double startMax;
		private double startMin;
		
		public ValueCalculator (Game g, int recursionDepth, boolean testOpponentMoves, Player p) {
			this.origGame = g;
			this.startRecursionDepth = recursionDepth;
			this.startTestOpponentMoves = testOpponentMoves;
			this.player = p;
			this.startMax = -(recursionDepth+1)*Board.MAX_BOARD_VALUE;
			this.startMin = (recursionDepth+1)*Board.MAX_BOARD_VALUE;
		}
		
		public Double call() {
			return this.valueOfMoves(this.origGame, this.startRecursionDepth, this.startTestOpponentMoves, this.startMax, this.startMin);
		}
		
		private double valueOfMoves(Game g, int recursionDepth, boolean testOpponentMoves, double currentMax, double currentMin) {
			if (g.isDraw()) {
				// System.out.println(Arrays.deepToString(g.getLastFewMoves()));
				// System.out.println("Detected possible draw");
				return 0;
			}

			//creates array to hold all possible moves
			Move[] moves;
			if (testOpponentMoves) {
				moves = g.getOtherPlayer(this.player).getAllMoves(g);
			} else {
				moves = this.player.getAllMoves(g);
			}

			if (moves.length==0) {
				if (testOpponentMoves) {
					return (recursionDepth+1)*Board.MAX_BOARD_VALUE;
				} else {
					return -(recursionDepth+1)*Board.MAX_BOARD_VALUE;
				}
			}

			if (moves.length==1) {
				if (recursionDepth==1) {
					return (new Board(g.getGameBoard(), moves[0])).calculateValue(this.player);
				} else {
					if (!testOpponentMoves) {
						return this.valueOfMoves(new Game(g, moves[0]), recursionDepth-1, !testOpponentMoves, -(recursionDepth+1)*Board.MAX_BOARD_VALUE, currentMin);
					} else {
						return this.valueOfMoves(new Game(g, moves[0]), recursionDepth-1, !testOpponentMoves, currentMax, (recursionDepth+1*Board.MAX_BOARD_VALUE));
					}
				}
			}

			//creates array to hold values of boards
			double[] boardValues = new double[moves.length];

			//if recursionDepth is one, calculate direct values of moves
			if (recursionDepth==1) {
				//iterates over all moves and calculates values to put in boardValues
				for (byte i = 0; i<moves.length; i++) {
					boardValues[i] = (new Board(g.getGameBoard(), moves[i])).calculateValue(this.player);
					if ((testOpponentMoves && boardValues[i]<=currentMax) || (!testOpponentMoves && boardValues[i]>=currentMin)) {
						return boardValues[i];
					}
				}
			
			//recursionDepth must be greater than one, so get values of the best opponent moves for each possible move
			} else {
				if (!testOpponentMoves) {
					currentMax = -(recursionDepth+1)*Board.MAX_BOARD_VALUE;
				} else {
					currentMin = (recursionDepth+1)*Board.MAX_BOARD_VALUE;
				}
				//iterates over all moves and calculates value based on best opponent move
				for (byte i = 0; i<moves.length; i++) {
					boardValues[i] = this.valueOfMoves(new Game(g, moves[i]), recursionDepth-1, !testOpponentMoves, currentMax, currentMin);
					if ((testOpponentMoves && boardValues[i]<=currentMax) || (!testOpponentMoves && boardValues[i]>=currentMin)) {
						return boardValues[i];
					}

					if (!testOpponentMoves && boardValues[i]>currentMax) {
						currentMax = boardValues[i];
					} else if (testOpponentMoves && boardValues[i]>currentMin) {
						currentMin = boardValues[i];
					}
				}
			}

			// //creates array to hold sorted values from lowest to highest
			// double[] boardValuesSorted = Arrays.copyOf(boardValues, boardValues.length);
			// Arrays.sort(boardValuesSorted);

			// //creates variable to hold result value
			// double result = boardValues[0];
			// if (testOpponentMoves) {
			// 	result = boardValuesSorted[0]/* * 0.8 + boardValuesSorted[1] * 0.2*/; 
			// } else {
			// 	result = boardValuesSorted[boardValuesSorted.length-1];
			// }

			double result = boardValues[0];
			byte multiplier;
			if (testOpponentMoves) {
				multiplier = -1;
			} else {
				multiplier = 1;
			}

			for (byte i = 1; i<boardValues.length; i++) {
				if (multiplier*boardValues[i] > multiplier*result) {
					result = boardValues[i];
				}
			}



			//logs the values for debugging
			// g.getGameBoard().printBoard();
			// Move[] sortedMoves = new Move[moves.length];
			// int index = 0;
			// for (int i = 0; i<boardValuesSorted.length; i++) {
			// 	index = ArraysHelper.find(boardValues, boardValuesSorted[i]);
			// 	boardValues[index] = Board.MAX_BOARD_VALUE*1000;
			// 	sortedMoves[moves.length-i-1] = moves[index];
			// }
			// for (Move m : sortedMoves) {
			// 	System.out.println(Arrays.deepToString(m.getWaypoints()));
			// }
			// System.out.print("" + recursionDepth + " ");
			// System.out.println(Arrays.toString(boardValuesSorted));
			// System.out.println(result);

			return result;
		}
	}
}