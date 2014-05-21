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
     	private static final double DISTANCE_PRIORITY = 1;
     	private static final double SPLIT_PRIORITY = 1;

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
		
			ArrayList<Move> all_moves = new ArrayList<Move>(Arrays.asList(this.getAllMoves(g)));
			ArrayList<int[]> scanned_locations = new ArrayList<int[]>();
			ArrayList<String> scanned_values = new ArrayList<String>();
			int[] current_location = new int[] {0,0};

			while (all_moves.size()>1) {

				ArrayList<int[]> critical_points = new ArrayList<int[]>();
				for (Move m : all_moves) {
					for (int[] critical_point : m.getCriticalPoints()) {
						critical_points.add(critical_point);	
					}
				}

				double[] point_scan_values = new double[critical_points.size()];

				for (int i=0; i<critical_points.size(); i++) {
					int[] point = critical_points.get(i);

					int occurrences = 0;
					double percent_occurence;
					ArrayList<Double> move_values = new ArrayList<>();
					double average_move_value;
					double distance;

					for (Move m : all_moves) {
						double move_value = this.getBrain().valueOfMoves(new Game(g, m), this, 3, true, -(4)*Board.MAX_BOARD_VALUE);
						move_values.add(move_value);
						if (Arrays.asList(m.getCriticalPoints()).contains(point)) {
							occurrences+=1;
						}
					}
					percent_occurence = occurrences/all_moves.size();

					double sum = 0;
					for (int j=0; j<move_values.size(); j++) {
						sum += move_values.get(j);
					}
					average_move_value = sum/move_values.size();

					distance = Math.pow(Math.pow(current_location[0]-point[0], 2) + Math.pow(current_location[1] - point[1], 2), .5);

					point_scan_values[i] = Math.pow(average_move_value, Human.OPPORTUNE_PRIORITY) / Math.pow(distance, Human.DISTANCE_PRIORITY) / Math.pow(Math.abs(percent_occurence-0.5), Human.SPLIT_PRIORITY);
				}
				
				double max_value = 0;
				for (double value : point_scan_values) {
					if (value > max_value) {
						max_value = value;
					}
				}
				int[] best_scan_location = critical_points.get(critical_points.indexOf(max_value));
			
				String point_color = r.examineLocation(best_scan_location);
				
				ArrayList<Move> impossible_moves = new ArrayList<Move>();
				for (Move m : all_moves) {
					int[][] m_waypoints = m.getWaypoints();
					if (point_color==this.getColor()) {
						if (m_waypoints[m_waypoints.length-1] != best_scan_location) {
							impossible_moves.add(m);
						}
					}
					else if (point_color==Robot.BOARD_COLOR) {
						if (m_waypoints[0] != best_scan_location) {
							impossible_moves.add(m);
						}
					}
					else {
						boolean isValidMove = true;
						for (Piece p : m.calculatePiecesToJump()) {
							if (Arrays.equals(p.getLocation(), best_scan_location)) {
								isValidMove = false;
								break;
							}
						}
						if (!isValidMove) {
							impossible_moves.add(m);
						}
					}
				}
				for (Move m : impossible_moves) {
					boolean successful = all_moves.remove(m);
					if (!successful) {
						System.out.println("move does not exist to be removes");
					}
				}
			}

			if (all_moves.size()==0) {
				System.out.println("An error/mistake occurred. no correct moves could be found.");
				r.waitForSensorPress();
				return this.inputMove(g);
			}

			return all_moves.get(0);

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
