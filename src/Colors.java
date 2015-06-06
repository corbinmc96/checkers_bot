/**
 * @author Corbin McNeill
 */

public class Colors {
	
	private static final String ANSI_RESET = "\u001B[0m";
	private static final String ANSI_RED = "\u001B[31m";
	private static final String ANSI_GREEN = "\u001B[32m";
	private static final String ANSI_YELLOW = "\u001B[33m";

	public static String ansiReset() {
		if (System.getProperty("os.name").startsWith("Windows")) {
			return "";
		}
		return ANSI_RESET;
	}
	public static String ansiRed() {
		if (System.getProperty("os.name").startsWith("Windows")) {
			return "";
		}
		return ANSI_RED;
	}
	public static String ansiGreen() {
		if (System.getProperty("os.name").startsWith("Windows")) {
			return "";
		}
		return ANSI_GREEN;
	}
	public static String ansiYellow() {
		if (System.getProperty("os.name").startsWith("Windows")) {
			return "";
		}
		return ANSI_YELLOW;
	}
}
