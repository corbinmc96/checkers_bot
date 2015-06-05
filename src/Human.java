import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

/**
 * @author Corbin McNeill
 * @author Aaron Miller
 */

public class Human extends Player {
	//stores the Scanner object for taking input
	private Scanner in = new Scanner(System.in);

	//scanning priority constants -- higher number denotes higher priority
	private static final double OPPORTUNE_PRIORITY = 1;
	private static final double DISTANCE_PRIORITY = 2;
	private static final double SPLIT_PRIORITY = 2;
	private static final int SCANNING_RECURSION_DEPTH = 3;
	private static final String INSTRUCTIONS_PROMPTS[] = {"instructions", "instruction", "help"};
	private static final String INSTRUCTIONS = "these are instructions.";

	public Human (String startColor, boolean startsOnZeroSide, Robot startGameRobot, AIEngine startBrain) {
		//calls the Player constructor with the same arguments
		super(startColor, startsOnZeroSide, startGameRobot, startBrain);
	}

	public Human (String startXO, boolean startOnZeroSide, AIEngine startBrain) {
		//calls the Player constructor with the same arguments
		super(startXO, startOnZeroSide, startBrain);
	}

	@Override
	public Move takeTurn(Game g) throws InterruptedException {
		//gets the last move made by the human player
		Move m = this.inputMove(g);
		//performs the move on the game board
		Player.performMove(m, g.getGameBoard());
		//returns the move that was made
		return m;
	}

	private Move inputMove(Game g) throws InterruptedException {
		Robot r = this.getRobot();
		if (r != null) {
			return r.detectLastMove(g, this);
		} else {
			boolean moveEntered = false;
			Move inputtedMove = null;
			while (!moveEntered) {
				System.out.println(Colors.ANSI_GREEN + "Enter Move:" + Colors.ANSI_RESET);
				String inputLine = this.in.nextLine();
				if (Arrays.asList(INSTRUCTIONS_PROMPTS).contains(inputLine)) {
					System.out.println("\n" + INSTRUCTIONS + "\n");
					continue;
				}
				if (inputLine.equals("exit")) {
					System.out.println("\n" + "Goodbye!" + "\n");
					System.exit(0);
				}
				if ((inputLine.length()+1)%3==0) {
					inputLine = inputLine + " ";
				}

				try {
					int numberOfWaypoints = inputLine.length()/3;
					String[] waypointStrings = new String[numberOfWaypoints];
					int[][] allWaypoints = new int[numberOfWaypoints][];
					for (int i=0; i<numberOfWaypoints; i++) {
						waypointStrings[i] = inputLine.substring(3*i,3*(i+1));
					}
				
					for (int i=0; i<numberOfWaypoints; i++) {
						allWaypoints[i] = new int[] {Integer.parseInt(inputLine.substring(3*i, 3*i+1)),Integer.parseInt(inputLine.substring(3*i+1, 3*i+2))};
					}
					inputtedMove = new Move(this.getBoard().getPieceAtLocation(allWaypoints[0]), allWaypoints);
					if (inputtedMove.getMovePiece()!=null && inputtedMove.getMovePiece().getPlayer()==this && inputtedMove.calculateIsValid(g)) {
						moveEntered = true;
						break;
					}
				} 
				//These exceptions will be triggered by incorrectly formatted input. They are expected and not handled.
				catch (NumberFormatException expected) {}
				catch (ArrayIndexOutOfBoundsException expected) {}

				System.out.println("Sorry. That does not appear to be a valid move!");
			}
			return inputtedMove;
		}
	}

	private static Integer[] convert(int[] old) {
		Integer[] result = new Integer[old.length];
		for (int i = 0; i<old.length; i++) {
			result[i] = Integer.valueOf(old[i]);
		}
		return result;
	}
}
