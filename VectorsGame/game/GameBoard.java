package game;

public class GameBoard {
	public int currentPlayerScore;
	public int currentComputerScore;
	public int previousPlayerScore;
	public int previousComputerScore;
	
	// 0 is empty
	// 1 is player
	// 2 is AI
	private int[][] board;
	
	// Default constructor (8x8)
	public GameBoard() {
		this.board = new int[8][8];
	}
	// Argument constructor (custom dimension)
	public GameBoard(int width, int height) {
		this.board = new int[width][height];
	}
	
	public boolean place(int x, int y, boolean isPlayer) {
		if (board[x][y] == 0) {
			board[x][y] = (isPlayer ? 1 : 2);
			return true;
		}
		return false;
	}
	
	// Scans the board and determines scores
	public void setScores() {
		// Move scores to previous place
		previousPlayerScore = currentPlayerScore;
		previousComputerScore = currentComputerScore;
		
		// Sequences of 3: +4 points
		// Sequences of 4: +6 points
		// Sequences of 5: +10 points
		// P P P P P P P P -> Sequence of 5 and sequence of 3, not 4 and 4
	}
	
	public void printBoard() {
		System.out.println("**************************************");
		System.out.println("* V E C T O R S                      *");
		System.out.println("**************************************");
		System.out.println(""); // Use Score class to get this data
		System.out.println("**************************************");
		System.out.println(""); // Call Vector score variables
		System.out.println("**************************************");
		System.out.println(""); // Call Vector turn variables
		System.out.println("**************************************");

		int y;
		int x;
		for (y = 0; y < board[0].length; y++) {
			for (x = 0; x < board.length; x++) {
				System.out.print(board[x][y]);
			}
			System.out.println();
		}
		
		/*
			**************************************
			- V E C T O R S                      *
			**************************************
			- High Score: John – 123             *
			**************************************
			- Player Score:    15 Last Round   9 *
			- Computer Score:   6 Last Round   3 *
			**************************************
			- Turn 7 of 20                       *
			**************************************
			  0   1   2   3   4   5   6   7
			---------------------------------
			|   | C |   |   | C |   |   |   | 0
			---------------------------------
			|   |   |   | X |   |   |   |   | 1
			---------------------------------
			|   |   |   | X |   |   |   |   | 2
			---------------------------------
			| C |   |   | X | X | X |   |   | 3
			---------------------------------
			|   |   |   |   |   | X |   |   | 4
			---------------------------------
			|   |   |   |   |   |   | C |   | 5
			---------------------------------
			|   |   |   |   |   |   | C |   | 6
			---------------------------------
			|   |   |   |   |   |   | C |   | 7
			---------------------------------
			Player(X) Turn (Row Col): 3 6
		 */
	}
}
