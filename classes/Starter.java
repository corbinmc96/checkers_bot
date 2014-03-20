public class Starter implements Runnable {
	private String[] _args;
	private ThreadListener _parent;

	public Starter(String[] args, ThreadListener parent) {
		this._args = args;
		this._parent = parent;
	}

	public void run() {
		try {
			Game theGame = new Game(new SimPlayer(_args.length>0 ? _args[0] : "x",true, new MultithreadedAI()),
									new SimPlayer(_args.length>1 ? _args[1] : "o",false, new MultithreadedAI()),
									(_args.length>3 && _args[3].equals("official"))
			);
			Player winner = theGame.play();
			if (winner == null) {
				System.out.println("TIE");
			} else {
				System.out.println("WINNER:  " + winner.getXO());
			}
		} catch(Exception e) {
		} finally {
			if (this._parent != null)
				this._parent.onThreadCompletion();
		}
	}

	public static void main(String[] args) {
		Starter s = new Starter(args, null);
		s.run();
	}
}
