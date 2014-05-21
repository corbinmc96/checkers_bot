// ALL CORBIN, AARON CONTRIBUTED TO inputMove

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;

public class Human extends Player {
	//stores the Scanner object for taking input
	private Scanner in;

	//scanning priority constants -- higher number denotes higher priority
	private final double OPPORTUNE_PRIORITY = 1;
     	private final double DISTANCE_PRIORITY = 1;
	
	public Human (String startColor, boolean startsOnZeroSide, Robot startGameRobot, AIEngine startBrain) {
		//calls the Player constructor with the same arguments
		super(startColor, startsOnZeroSide, startGameRobot, startBrain);
	}

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
		// THIS DOES NOT APPEAR TO DIFFERENTIATE BETWEEN DIFFERENT MOVES WITH SAME START AND ENDPOINTS
		if (this.getRobot()!=null) {
			final Robot r = this.getRobot();
			//starts concurrent thread waiting for button press
			Thread t = new Thread() {
				public void run() {
					r.waitForSensorPress();
				}
			};
			t.start();
		
			ArrayList<Move> all_moves = new ArrayList<Move>(Arrays.asList(this.getAllMoves()));
			ArrayList<int[]> scanned_locations = new ArrayList<int[]>();
			ArrayList<String> scanned_values = new ArrayList<String>();
			int[] current_location = new int[] {0,0}

			while (all_moves.size()>1) {

				ArrayList<int[]> critical_points = new ArrayList<int[]>();
				for (Move m : all_moves) {
					for (int[] critical_point : m.getCriticalPoints()) {
						critical_points.add(critical_point);	
					}
				}
				double[] point_scan_value = new double[critical_points.size()];

				double[] point_scan_value = new Array(critical_points.size());
				for (int i=0; i<critical_points.length; i++) {
					int[] point = critical_points[i];

					int occurrences = 0;
					double percent_occurence;
					ArrayList<double> move_values = new ArrayList<double>();
					double average_move_value;
					double distance;

					for (Move m : all_moves) {
						double move_value = this.getBrain().valueOfMoves(Game(g, m), p, 3, true, -(recursionDepth+1)*Board.MAX_BOARD_VALUE);
						move_values.add(move_value);
						if (Arrays.asList(m.getCriticalPoints()).contains(point)) {
							occurrences+=1;
						}
					}
					percent_occurence = occurrences/all_moves;

					double sum = 0;
					for (int j=0; j<move_values.size(); j++) {
						sum += move_values[j];
					}
					average_move_value = sum/move_values.size();

					distance = Math.pow(Math.pow(), .5) // WORKING HERE

					point_scan_value[i] = Math.pow(average_move_value, OPPORTUNE_PRIORITY) / Math.pow(distance, DISTANCE_PRIORITY) / percent_occurence;

				}


			}
				


		} else {
			// super.getBoard().printBoard();
			
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
	}

	public static Integer[] convert(int[] old) {
		Integer[] result = new Integer[old.length];
		for (int i = 0; i<old.length; i++) {
			result[i] = Integer.valueOf(old[i]);
		}
		return result;
	}
}
