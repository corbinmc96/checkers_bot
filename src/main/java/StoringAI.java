// ALL CORBIN
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class StoringAI implements AIEngine {

	private final String PATH_TO_SAVE_FILE = "";

	private String[] boardSaves;

	public StoringAI () {
		System.out.println("File Not Found.");
		try {
			FileReader fileReader = new FileReader(PATH_TO_SAVE_FILE);
			BufferedReader buffReader = new BufferedReader(fileReader);
			List<String> lines = new ArrayList<String>();
			String line = null;
			while ((line = buffReader.readLine()) != null) {
				lines.add(line);
			}
			buffReader.close();
			boardSaves = lines.toArray(new String[lines.size()]);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public Move[] rankBestMove (Move[] unsortedMoves, Game g, Player p, int recursionDepth) {
		Game[] ga = new Game[unsortedMoves.length];
		double[] va = new double[unsortedMoves.length];
		for (int i=0;i<unsortedMoves.length;i++) {
			ga[i] = new Game(g,unsortedMoves[i]);
			String newBoardString = generateBoardString(g.getGameBoard(), p);

			va[i] = this.valueOfMoves(ga[i],p,recursionDepth-1,true,0);
		}
		Double[] sortedva = new Double[unsortedMoves.length];
		for (int i=0;i<unsortedMoves.length;i++) {
			sortedva[i] = va[i];
		}
		Move[] result = new Move[unsortedMoves.length];
		
		Arrays.sort(sortedva, Collections.reverseOrder());
		
		for (int i=0;i<unsortedMoves.length;i++) {
			int index = ArraysHelper.find(va,sortedva[i]);
			va[index] = 1000000;
			result[i] = unsortedMoves[index];
		}			
		
		return result;
	}

	public double valueOfMoves(Game g, Player p, int recursionDepth, boolean isOpponentNode, double ab) {
	
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
				va[i] = this.valueOfMoves(ga[i], p, recursionDepth-1,!isOpponentNode, 0);

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

	private String generateBoardString(Board b, Player p) {
		String boardString = "";
		for (int[] location : Board.PLAYABLE_LOCATIONS) {
			Piece thePiece = b.getPieceAtLocation(location);
			if (p!=null) {
				boardString.concat(Integer.toString(location[0]));
				boardString.concat(Integer.toString(location[1]));
				boardString.concat(p==thePiece.getPlayer() ? "1" : "0");
				boardString.concat(thePiece.getIsKing() ? "1" : "0");
			}
		}
		return boardString;
	}

	public void close () {
		//code to close any open files
	}
}
