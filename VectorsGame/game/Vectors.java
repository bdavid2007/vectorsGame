package game;

import java.util.Scanner;

public class Vectors {
	public GameBoard gameBoard;
	public Scanner scanner;
	public AI ai;
	public int numRounds;

	// TODO:Q: Is building the header with asterisks mandatory?
	// No
	// TODO: Expand header with game board size
	private String LINE = "**************************************";
	private String CLEAR = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n";
	
	public Vectors() {
		this.gameBoard = new GameBoard(); // Default constructor is 8x8
		this.scanner = new Scanner(System.in);
		this.ai = new AI();
	}

	public void run() {
		System.out.println(this.CLEAR);
		System.out.println(this.LINE);
		System.out.println("- V E C T O R S                      *");
		System.out.println(this.LINE);
		this.inputRounds();
		System.out.println(this.CLEAR);
		
		// Game loop
		int currentRound;
		for (currentRound = 1; currentRound <= this.numRounds; currentRound++) {
			//this.gameBoard.dev_setBoard(false); // Toggle this function on to generate boards every turn

			this.gameBoard.scanBoard();
			System.out.println(this.CLEAR);

			this.gameBoard.calculateScores(false);
			this.printHeader(currentRound);

			this.gameBoard.printBoard();
			this.inputCoordinatePair();
			this.ai.computerInput(this.gameBoard); // Something like this
		}
		this.scanner.close();
	}

	private void printHeader(int round) {
		System.out.println(LINE);

		System.out.println("- V E C T O R S                      *");
		System.out.println(LINE);

		String user = "John";
		int highscore = 0;
		String highscoreStr = "- High Score: " + user + " - " + highscore;
		System.out.printf("%-37s*\n", highscoreStr);
		System.out.println(LINE);

		// TODO: Add colon to "Last Round"
		System.out.printf("- Player Score:%6d Last Round%4d *\n", this.gameBoard.currentPlayerScore, this.gameBoard.previousPlayerScore);
		System.out.printf("- Computer Score:%4d Last Round%4d *\n", this.gameBoard.currentComputerScore, this.gameBoard.previousComputerScore);
		System.out.println(LINE);

		String roundStr = "- Turn " + round + " of " + this.numRounds;
		System.out.printf("%-37s*\n", roundStr);
		System.out.println(LINE);
	}

	

	private int isolateInteger(String str) {
		String output = "";
		int index;

		for (index = 0; index < str.length(); index++) {
			char ch = str.charAt(index);
			
			// Clamp
			if ((ch >= '0' && ch <= '9')) {
				output += ch;
			}
		}
		if (output.equals("")) {
			return 20;
		}
		return Integer.parseInt(output);
	}

	private void inputRounds() {
		String reason;
		
		while (true) {
			reason = "Must be formatted as integer or left blank for default";
			System.out.print("Number of rounds: ");
			
			try {
				this.numRounds = this.isolateInteger(this.scanner.nextLine());

				// Clamp
				if (this.numRounds <= 0 || this.numRounds > this.gameBoard.maxRounds) {
					reason = "Must be between 1 and " + this.gameBoard.maxRounds + " (max player tokens)";
					throw new Exception();
				}
				break;
			}
			catch (Exception exception) {
				System.out.printf("[!] \"%d\" is invalid: %s\n\n", this.numRounds, reason);
			}
		}
	}

	// VERY VERY VERY FOOL-PROOF INPUT HANDLING
	private String[] isolateCoordinatePair(String str, GameBoard gameBoard) {
		String[] numbers = str.replaceAll("^\\D+", "").split("\\D+"); // "abc123def456ghi" -> {"123","456"}

		if (numbers.length == 2) return numbers; // Easy solution ("1 2")
		if (numbers.length != 1) return null;	 // Hard solution ("12")

		// Hard solution: Split numbers into every combination and take what's valid
		String digits = numbers[0];
		String[] coordinatePair = null;
		int splitIndex;

		for (splitIndex = 1; splitIndex < digits.length(); splitIndex++) {
			String rowStr = digits.substring(0, splitIndex);
			String colStr = digits.substring(splitIndex);

			// Bounds check
			int row = Integer.parseInt(rowStr);
			int col = Integer.parseInt(colStr);
			if (row >= 0 && row < gameBoard.height && col >= 0 && col < gameBoard.width) {
				// Return null if there is more than 1 possible answer
				if (coordinatePair != null) {
					return null;
				}
				coordinatePair = new String[]{rowStr, colStr};
			}
		}

		if (coordinatePair != null) {
			return coordinatePair;
		}
		if (digits.length() >= 2) {
			return new String[]{digits.substring(0, 1), digits.substring(1)};
		}
		// Only happens if there's 0 or 1 digit
		return null;
	}

	private void inputCoordinatePair() {
		String reason;
		int row = -1;
		int col = -1;

		while (true) {
			reason = "Improperly formatted";
			// TODO: Every other game in existence is notated as col-row (x,y) rather than row-col. If the game must stay row-col, can I move the row numbers to the left side of the board?
			// Yes
			System.out.print("Player(X) Turn (Row Col): ");

			try {
				// Extract row and col from string
				String[] coordinatePair = this.isolateCoordinatePair(this.scanner.nextLine(), this.gameBoard);
				if (coordinatePair == null) {
					throw new Exception();
				}
				row = Integer.parseInt(coordinatePair[0]);
				col = Integer.parseInt(coordinatePair[1]);

				// Bounds check
				if (col < 0 || col > this.gameBoard.width-1 || row < 0 || row > this.gameBoard.height-1) {
					reason = "Coordinate must be in range";
					throw new Exception();
				}
				
				// Overwriting check
				if (this.gameBoard.getTokenTeam(col, row) > 0) { 
					reason = "This space is already taken";
					throw new Exception();
				}

				this.gameBoard.writeToken(col, row, 1);
				break;
			}
			catch (Exception exception) {
				System.out.printf("[!] (%d,%d) is invalid: %s\n\n", row, col, reason);
			}
		}
	}
}