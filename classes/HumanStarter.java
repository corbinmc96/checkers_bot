public class HumanStarter {
	public static void main(String[] args) {
		Player p1 = new Human("x", true);
		Player p2 = new Human("o", false);

		Game g = new Game(p1, p2);
		Board b = new Board(g.getPlayers(), new int[][]{new int[]{2,2}, new int[]{4,2}}, new int[][]{new int[]{3,5}, new int[]{5,5}});
		g = new Game(g, b);
		p1.setBoard(b);
		p2.setBoard(b);

		Player winner = g.play();
	}
}