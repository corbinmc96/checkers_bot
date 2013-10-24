public class AutoStarter {
	public static void main(String[] args) {
		Player p1 = new SimPlayer("x", true);
		Player p2 = new SimPlayer("o", false);

		Game g = new Game(p1, p2);

		Player winner = g.play();
		if (winner==p1) {
			System.out.println("Player 1 wins");
<<<<<<< HEAD
		} else {
			System.out.println("Player 2 wins");
=======
		} else  if (winner==p2) {
			System.out.println("Player 2 wins");
		} else {
			System.out.println("Draw");
>>>>>>> 84ff295458fbbc48f42f7fba1ba9efbb3481e3e4
		}
	}
}