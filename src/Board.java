import java.util.ArrayList;

/**
 * @author Aaron Miller 
 * @author Corbin McNeill
 */

public class Board {

	private ArrayList<Piece> piecesOnBoard;

	public static final double MAX_BOARD_VALUE = 60;

	public Board (Player[] players) {
		if (players[0].getIsOnZeroSide() == players[1].getIsOnZeroSide()) {
			throw new IllegalArgumentException("Players are on the same side of the board.");
		}
		this.piecesOnBoard = new ArrayList<Piece>();
		for (int i=0; i<3; i++) {
			for (int j=0; j<4; j++) {
				this.piecesOnBoard.add(new Piece(new int[] {(2*j+(i%2)), i}, players[players[1].getIsOnZeroSide() ? 1 : 0], this));
				this.piecesOnBoard.add(new Piece(new int[] {7 - 2*j-(i%2), 7-i}, players[players[0].getIsOnZeroSide() ? 1 : 0], this));
			}
		}
	}

	public Board (Board previousBoard, Move newMove) {
		this.piecesOnBoard = new ArrayList<Piece>();
		for (Piece p : previousBoard.getPiecesOnBoard()) {
			this.piecesOnBoard.add(p.copyToBoard(this));
		}
		Player.performMove(new Move(this.getPieceAtLocation(newMove.getSource()), newMove.getWaypoints()), this);
	}

	public Board(Player[] players, int[][] p1Locations, int[][] p2Locations, int[][] p1kings, int[][] p2kings) {
		if (players[0].getIsOnZeroSide() == players[1].getIsOnZeroSide()) {
			throw new IllegalArgumentException("Players are on the same side of the board.");
		}
		this.piecesOnBoard = new ArrayList<Piece>();
		for (int[] location : p1Locations) {
			this.piecesOnBoard.add(new Piece(location, players[0], this));
		}
		for (int[] location : p2Locations) {
			this.piecesOnBoard.add(new Piece(location, players[1], this));
		}
		for (int[] location : p1kings) {
			this.piecesOnBoard.add(new Piece(location, players[0], this));
			this.piecesOnBoard.get(piecesOnBoard.size()-1).setIsKing(true);
		}
		for (int[] location : p2kings) {
			this.piecesOnBoard.add(new Piece(location, players[1], this));
			this.piecesOnBoard.get(piecesOnBoard.size()-1).setIsKing(true);
		}
	}

	public Piece[] getPiecesOnBoard () {
		return this.piecesOnBoard.toArray(new Piece[this.piecesOnBoard.size()]);
	}

	public Piece getPieceAtLocation(int[] location) {
		for (Piece piece : this.piecesOnBoard) {
			if (piece.getLocation()[0] == location[0] && piece.getLocation()[1] == location[1]) {
				return piece;
			}
		}
		return null;
	}

	public void removePiece (Piece pieceToRemove) {
		int[] location = pieceToRemove.getLocation();
		for (Piece p : this.piecesOnBoard) {
			if (p.getLocation()[0]==location[0] && p.getLocation()[1]==location[1]) {
				this.piecesOnBoard.remove(p);
				return;
			}
		}
		System.out.println("Removal failed");
	}

	public int totalPiecesLeft(Player p) {
		int count = 0;
		for (Piece piece : this.piecesOnBoard) {
			if (piece.getPlayer()==p) {
				count++;
			}
		}
		return count;
	}

	public int normalPiecesLeft(Player p) {
		int count = 0;
		for (Piece piece : this.piecesOnBoard) {
			if (piece.getPlayer()==p && !piece.getIsKing()) {
				count++;
			}
		}
		return count;
	}

	public int kingsLeft(Player p) {
		int count = 0;
		for (Piece piece : this.piecesOnBoard) {
			if (piece.getPlayer()==p && piece.getIsKing()) {
				count++;
			}
		}
		return count;
	}

	public static boolean locationIsPlayable (int[] testLocation) {
		int[] boardValues = new int[] {0,1,2,3,4,5,6,7};
		return ArraysHelper.asArrayList(boardValues).contains(testLocation[0]) && ArraysHelper.asArrayList(boardValues).contains(testLocation[1]) && (testLocation[0] + testLocation[1])%2 == 0;
	}

	public double calculateValue(Player p) {
		double p1Value = 0;
		double p2Value = 0;
		ArrayList<Piece> p1Pieces = new ArrayList<Piece>();
		ArrayList<Piece> p2Pieces = new ArrayList<Piece>();

		//sorts ever piece as p1Piece or p2Piece
		for (Piece piece : this.piecesOnBoard) {
			if (piece.getPlayer() == p) {
				p1Pieces.add(piece);
			}
			else {
				p2Pieces.add(piece);
			}
		}

		//calculates point value of p's pieces
		for (Piece piece : p1Pieces) {
			//determines if the piece is a king
			if (piece.getIsKing()) {
				//adds 3 to the player's total value for the board
				p1Value += 5;
			//the piece is not a king
			} else {
				//determines which side of the board the player is on
				if (p.getIsOnZeroSide()) {
					//adds value based on distance down the board
					p1Value += 3 + 0.125*piece.getLocation()[1];
				//the player is on the side of the board with index 7
				} else {
					//adds value based on distance down the board
					p1Value += 3 + 0.125*(7 - piece.getLocation()[1]);
				}
			}
		} 

		//calculates point value of p's opponent's pieces
		for (Piece piece : p2Pieces) {
			//determines if the piece is a king
			if (piece.getIsKing()) {
				p2Value += 5;
			//the piece is not a king
			} else {
				//determines which side of the board the player is on
				if (piece.getPlayer().getIsOnZeroSide()) {
					//adds value based on distance down the board
					p2Value += 3 + 0.125*piece.getLocation()[1];
				//the player is on the side of the board with index 7
				} else {
					//adds value based on distance down the board
					p2Value += 3 + 0.125*(7 - piece.getLocation()[1]);
				}
			}
		}
		

		//discovers which player is winning by the current calculations above
		ArrayList<Piece> winningPlayerPieces;
		ArrayList<Piece> loserPlayerPieces;
		if (p1Value > p2Value) {
			winningPlayerPieces = p1Pieces;
			loserPlayerPieces = p2Pieces;
		} 
		else {
			winningPlayerPieces = p2Pieces;
			loserPlayerPieces = p1Pieces;
		}

		//performs extra calculations to encourage winner's kings to attack opponent's pieces
		for (Piece p1 : winningPlayerPieces) {
			if (p1.getIsKing()) {
				double distance = 20;
				for (Piece p2 : loserPlayerPieces) {
					double testDistance = this.getDistanceBetweenPieces(p1, p2);
					if (testDistance < distance) {
						distance = testDistance;
					}
				}
				if (winningPlayerPieces == p1Pieces) {
					p1Value -= 0.001*distance;
				} 
				else {
					p2Value -= 0.001*distance;
				}
			}
		}

		//returns final calculated score
		return p1Value-p2Value;
	}

	public void printBoard() {
		System.out.println();
		for (int y : new int[] {7,6,5,4,3,2,1,0}) {
			String[] theLine = new String[8];
			for (int x : new int[] {0,1,2,3,4,5,6,7}) {
				if (this.getPieceAtLocation(new int[] {x,y}) != null) {
					theLine[x] = this.getPieceAtLocation(new int[] {x,y}).getPlayer().getXO();
					if (this.getPieceAtLocation(new int[] {x,y}).getIsKing()) {
						theLine[x] = theLine[x].toUpperCase();
					}
				} else {
					theLine[x] = "-";
				}
			}
			String string_y = Integer.toString(y);
			System.out.print(Colors.ANSI_YELLOW + string_y + " " + Colors.ANSI_RESET);
			for (String s : theLine) {
				String pieceColor = Colors.ANSI_RESET;
				if (s.equals("x") || s.equals("X")) {
					pieceColor = Colors.ANSI_RED;
				} else if (s.equals("o") || s.equals("O")) {
					pieceColor = Colors.ANSI_GREEN;
				}
				System.out.print(pieceColor + s + " " + Colors.ANSI_RESET);
			}
			System.out.print(Colors.ANSI_YELLOW + string_y + Colors.ANSI_RESET);
			System.out.println();
		}
		System.out.println(Colors.ANSI_YELLOW + "  0 1 2 3 4 5 6 7" + Colors.ANSI_RESET);
		System.out.println();
	}

	public double getDistanceBetweenPieces(Piece p1, Piece p2) {
		double dif1 = p1.getLocation()[0] - p2.getLocation()[0];
		double dif2 = p1.getLocation()[1] - p2.getLocation()[1];
		if (dif1<0) {
			dif1=dif1*-1;
		} if (dif2<0) {
			dif2=dif2*-1;
		}
		return Math.pow(Math.pow(dif1,2)+Math.pow(dif2,2),0.5);
	}

}
