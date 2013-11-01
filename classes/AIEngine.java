public abstract class AIEngine {

	int standardRecursionDepth;

	public void setRecursionDepth(int depth) {
		this.standardRecursionDepth = depth;
	}

	public Move[] rankBestMove(Move[] unrankedMoves, Board b, Player p) {
		this.rankBestMove(unrankedMoves,b,p,this.standardRecursionDepth)
	}

	public abstract Move[] rankBestMove(Move[] unrankedMoves, Board b, Player p, int recursionDepth);
}