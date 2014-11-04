// ALL CORBIN, AARON CONTRIBUTED TO inputMove

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Collections;

public class Human extends Player {
	//stores the Scanner object for taking input
	private Scanner in;

	//scanning priority constants -- higher number denotes higher priority
	private static final double OPPORTUNE_PRIORITY = 1;
     	private static final double DISTANCE_PRIORITY = 2;
     	private static final double SPLIT_PRIORITY = 2;
	private static final int SCANNING_RECURSION_DEPTH = 3;

	public Human (String startXO, boolean startOnZeroSide, AIEngine startBrain) {
		//calls the Player constructor with the same arguments
		super(startXO, startOnZeroSide, startBrain);
		//creates a Scanner object to take input from the command line
		this.in = new Scanner(System.in);
	}

	public Move takeTurn(Game g) {
		//gets the last move made by the human player
		Move m = this.inputMove(g);
		//performs the move on the game board
		super.performMove(m, g.getGameBoard());
		//returns the move that was made
		return m;
	}

	public Move inputMove(Game g) {
		boolean moveEntered = false;
		Move inputtedMove = null;
		while (!moveEntered) {
			System.out.println("Enter Move:");
			String inputLine = this.in.nextLine();
			if ((inputLine.length()+1)%3==0) {
				inputLine = inputLine + " ";
			}
			if (inputLine.length()%3==0 && inputLine.length()>=5) {

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
				}
			}
		}
		return inputtedMove;
	}

	public static Integer[] convert(int[] old) {
		Integer[] result = new Integer[old.length];
		for (int i = 0; i<old.length; i++) {
			result[i] = Integer.valueOf(old[i]);
		}
		return result;
	}
}
