import java.util.Random;

/**
 * @author Aaron Miller
 */

public class RandomAI extends MultithreadedAI {
	private Random random;

	public RandomAI () {
		this.random = new Random();
	}

	@Override
	public Move[] rankBestMove (Move[] moves, Game g, Player p, int recursionDepth) throws InterruptedException {
		Move[] sortedMoves = super.rankBestMove(moves, g, p, recursionDepth);

		if (this.random.nextInt(Math.min(2, moves.length))==1) {
			Move chosenOne = sortedMoves[1];
			sortedMoves[1] = sortedMoves[0];
			sortedMoves[0] = chosenOne;
		}
		
		return sortedMoves;
	}
}
