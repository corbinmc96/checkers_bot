import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;

public class CorbinAI extends AIEngine {

	public Move[] rankBestMove (Move[] unsortedMoves, Game g, Player p, int recursionDepth) {
		Game[] ga = new Game[unsortedMoves.length];
		double[] va = new double[unsortedMoves.length];
		for (int i=0;i<unsortedMoves.length;i++) {
			ga[i] = new Game(g,unsortedMoves[i]);
			va[i] = this.minimax(ga[i],recursionDepth-1,true,p);
		}
		Double[] sortedva = new Double[unsortedMoves.length];
		for (int i=0;i<unsortedMoves.length;i++) {
			sortedva[i] = va[i];
		}
		Move[] result = new Move[unsortedMoves.length];
		
		Arrays.sort(sortedva, Collections.reverseOrder());
		
		for (int i=0;i<unsortedMoves.length;i++) {
			//logs sorted move values

			int index = ArraysHelper.find(va,sortedva[i]);
			va[index] = 1000000;
			result[i] = unsortedMoves[index];
		}			
		
		return result;
	}

	private double minimax(Game g, int recursionDepth, boolean isOpponentNode, Player p) {
	
		if (recursionDepth == 0) {
			return g.getGameBoard().calculateValue(p);
		}

		if (g.isDraw()) {
			return 0;
		}
		
		Move[] unsortedMoves;
		if (isOpponentNode) {
			unsortedMoves = g.getOtherPlayer(p).getAllMoves(g);
		} else {
			unsortedMoves = p.getAllMoves(g);
		}
		
		if (unsortedMoves.length == 0) {
			return (recursionDepth+1)*-36;
		}
		
		else {
			Game[] ga = new Game[unsortedMoves.length];
			double[] va = new double[unsortedMoves.length];
			for (int i=0;i<unsortedMoves.length;i++) {
				ga[i] = new Game(g,unsortedMoves[i]);
				va[i] = this.minimax(ga[i],recursionDepth-1,!isOpponentNode,p);

			}
			double value;
			if (isOpponentNode) { 
				value = 1000;
			} else {
				value = -1000;
			}
			for (double v : va) {
				if (isOpponentNode) {
					if (v<=value) {
						value = v;
					}
				}
				else {
					if (v>=value) {
						value = v;
					}
				}
			}
			return value;
		}
	}
}
