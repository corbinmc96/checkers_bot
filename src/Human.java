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
		if (this.getRobot()!=null) {
			Robot r = this.getRobot();
			r.waitForSensorPress();
		
			ArrayList<Move> all_moves = new ArrayList<Move>(Arrays.asList(this.getAllMoves(g)));
			ArrayList<int[]> scanned_locations = new ArrayList<int[]>();
			int[] current_location = new int[] {0,0};

			while (all_moves.size()>1) {

				ArrayList<int[]> critical_points = new ArrayList<int[]>();
				for (Move m : all_moves) {
					for (int[] critical_point : m.getCriticalPoints()) {
						boolean already_used = false;
						for (int[] previous_point : scanned_locations) {
							if (!Arrays.equals(previous_point, critical_point)) {
								already_used = true;
								break;
							}
						}
						if (!already_used) {
							critical_points.add(critical_point);
						}
					}
				}

				double[] point_scan_values = new double[critical_points.size()];

				for (int i=0; i<critical_points.size(); i++) {
					int[] point = critical_points.get(i);

					int occurrences = 0;
					double percent_occurence;
					ArrayList<Double> move_values = new ArrayList<Double>();
					double average_move_value;
					double distance;

					for (Move m : all_moves) {
						if (Arrays.asList(m.getCriticalPoints()).contains(point)) {
							occurrences+=1;
							double move_value = this.getBrain().valueOfMoves(new Game(g, m), this, Human.SCANNING_RECURSION_DEPTH, true, -(Human.SCANNING_RECURSION_DEPTH)*Board.MAX_BOARD_VALUE);
							move_values.add(move_value);
						}
					}
					percent_occurence = occurrences/all_moves.size();

					double sum = 0;
					for (int j=0; j<move_values.size(); j++) {
						sum += move_values.get(j);
					}
					average_move_value = sum/move_values.size();

					distance = Math.pow(Math.pow(current_location[0]-point[0], 2) + Math.pow(current_location[1] - point[1], 2), .5);

					try {
						point_scan_values[i] = Math.pow(average_move_value, Human.OPPORTUNE_PRIORITY) / Math.pow(distance, Human.DISTANCE_PRIORITY) / Math.pow(Math.abs(percent_occurence-0.5), Human.SPLIT_PRIORITY);
					} catch (ArithmeticException e) {
						point_scan_values[i] = Double.MAX_VALUE; 
					}
				}
				
				double max_value = -(Board.MAX_BOARD_VALUE)*100;
				int max_value_index = -1;
				for (int i=0; i<point_scan_values.length; i++) {
					double value = point_scan_values[i];
					if (value > max_value) {
						max_value = value;
						max_value_index = i;
						System.out.println(max_value);
					}
				}
				int[] scan_location = critical_points.get(max_value_index);
			
				String point_color = r.examineLocation(scan_location);
				scanned_locations.add(scan_location);
				
				ArrayList<Integer> impossible_moves = new ArrayList<Integer>();
				for (int i=0; i<all_moves.size(); i++) {
					int[][] waypoints = all_moves.get(i).getWaypoints();
					if (point_color.equals(this.getColor())) {
						if (!Arrays.equals(waypoints[waypoints.length-1], scan_location)) {
							impossible_moves.add(i);
						}
					}
					else if (point_color.equals(Robot.BOARD_COLOR)) {
						if (!Arrays.equals(waypoints[0], scan_location)) {
							impossible_moves.add(i);
						}
					}
					else {
						boolean isValidMove = true;
						for (Piece p : all_moves.get(i).calculatePiecesToJump()) {
							if (Arrays.equals(p.getLocation(), scan_location)) {
								isValidMove = false;
								break;
							}
						}
						if (!isValidMove) {
							impossible_moves.add(i);
						}
					}
				}
				for (int i : impossible_moves) {
					try {
						all_moves.remove(i);
					} catch (IndexOutOfBoundsException e) {
						e.getMessage();
					}
				}
			}

			if (all_moves.size()==0) {
				System.out.println("An error/mistake occurred. no correct moves could be found.");
				r.waitForSensorPress();
				return this.inputMove(g);
			}

			return all_moves.get(0);

		// Command line input
		} else {
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

	private static Integer[] convert(int[] old) {
		Integer[] result = new Integer[old.length];
		for (int i = 0; i<old.length; i++) {
			result[i] = Integer.valueOf(old[i]);
		}
		return result;
	}
}
