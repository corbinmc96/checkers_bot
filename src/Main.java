/**
 * @author Aaron Miller
 */

public class Main {
	public static void main(String[] args) throws InterruptedException {
		if (args.length > 0 && args[0].equalsIgnoreCase("gui")) {
			GUIStarter.main(ArraysHelper.copyOfRange(args, 1, args.length));
		} else if (args.length > 0 && args[0].equalsIgnoreCase("midgame")) {
			MidgameStarter.main(ArraysHelper.copyOfRange(args, 1, args.length));
		} else {
			Starter.main(args);
		}
	}
}
