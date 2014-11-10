// ALL CORBIN

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class CRandomAI extends AIEngine {

	public Move[] rankBestMove (Move[] unsortedMoves, Game g, Player p, int recursionDepth) {
		Game[] ga = new Game[unsortedMoves.length];
		double[] va = new double[unsortedMoves.length];
		for (int i=0;i<unsortedMoves.length;i++) {
			ga[i] = new Game(g,unsortedMoves[i]);
			va[i] = this.minimax(ga[i],recursionDepth-1,true,p);
			//System.out.println(Arrays.deepToString(unsortedMoves[i].getWaypoints()));
		}
		Double[] sortedva = new Double[unsortedMoves.length];
		for (int i=0;i<unsortedMoves.length;i++) {
			sortedva[i] = va[i];
			//logs unsorted move values
			//System.out.println("                 2:" + Double.toString(va[i]));
		}
		Move[] result = new Move[unsortedMoves.length];
		
		Arrays.sort(sortedva, Collections.reverseOrder());
		
		int searchNumber=0;
		boolean stillSearching = true;
		while (stillSearching) {
			if (searchnumber+1<sortedva.length) {
				if (sortedva[searchNumber] == sortedva[searchNumber+1]) {
						searchNumber+=1;
				}
				else {stillSearching =false;}
			}
			else {stillSearching=false;}
		}
		//working here
		
		for (int i=0;i<unsortedMoves.length;i++) {
			//logs sorted move values

			int index = ArraysHelper.find(va,sortedva[i]);
			va[index] = 1000000;
			result[i] = unsortedMoves[index];
			//System.out.println(Arrays.deepToString(unsortedMoves[index].getWaypoints()));
			//System.out.println(Arrays.deepToString(unsortedMoves[index].getWaypoints())+"  "+Double.toString(new Board(this.getBoard(),unsortedMoves[index]).calculateValue()));				
		}			
		//System.out.println();
		
		return result;
	}

	private double minimax(Game g, int recursionDepth, boolean isOpponentNode, Player p) {
	
		if (recursionDepth == 0) {
			double value = g.getGameBoard().calculateValue(p);
			//System.out.println("                0:" + Double.toString(value));
			return value;
		}
		
		Move[] unsortedMoves;
		if (isOpponentNode) {
			unsortedMoves = g.getOtherPlayer(p).getAllMoves(g);
		} else {
			unsortedMoves = p.getAllMoves(g);
		}

		if (g.isDraw()) {
			return 0;
		}
		
		if (unsortedMoves.length == 0) {
			return -36;
		}
		
		else {
			Game[] ga = new Game[unsortedMoves.length];
			double[] va = new double[unsortedMoves.length];
			for (int i=0;i<unsortedMoves.length;i++) {
				ga[i] = new Game(g,unsortedMoves[i]);
				va[i] = this.minimax(ga[i],recursionDepth-1,!isOpponentNode,p);
				//System.out.println(Arrays.deepToString(unsortedMoves[i].getWaypoints()));

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
			//if (recursionDepth==2) {
			//	System.out.println("                               2: "+Double.toString(value));
			//} if (recursionDepth==1) {
			//	System.out.println("                     1: "+Double.toString(value));
			//}
			return value;
		}
	}
}
