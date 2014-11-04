// ALL CORBIN
public interface AIEngine {

	public Move[] rankBestMove(Move[] unrankedMoves, Game g, Player p, int recursionDepth) ;

	public double valueOfMoves(Game g, Player p, int recursionDepth, boolean isOpponentNode, double ab);
}
