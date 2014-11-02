// ALL CORBIN
public abstract class AIEngine {

	public abstract Move[] rankBestMove(Move[] unrankedMoves, Game g, Player p, int recursionDepth) ;

	public abstract double valueOfMoves(Game g, Player p, int recursionDepth, boolean isOpponentNode, double ab);
}
