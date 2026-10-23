package game;

import java.util.Scanner;

public class Vectors {
	public static final boolean USE_ESCAPE_CODES = true;
	public static final boolean[] DEV_CONFIG = new boolean[]{false, false};

	// "final" is the same as "const" in JavaScript
	private final String CLEAR = (USE_ESCAPE_CODES)
		? "\033[H\033[2J\033[3J" // ACTUALLY clears the terminal!!!
		: "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n";

	private final int MINIMUM_HEADER_WIDTH = "- Computer Score:   6 Last Round   3 *".length();
	private final int DEFAULT_NUM_ROUNDS = 20;

	public static final int[][] LEGEND = {
		{5, 10},
		{4, 6},
		{3, 4}
	};

	public static final char[][] CHARS = {
		/*0*/ {' ','x','c'},
		/*1*/ {'─','│','┼','┬','┴'},
			  //0   1   2   3   4   5   6   7   8   9  10  11
		/*2*/ {'═','║','╔','╤','╗','╢','╝','╧','╚','╟','╠','╣'},
		/*3*/ {'-','\\','|','/','X'},
		/*4*/ {'→','↘','↓','↙','←','↖','↑','↗','.'}
	};

	private Scanner scanner;
	
	private int headerWidth = MINIMUM_HEADER_WIDTH;
	private String headerStr = "";

	public static GameBoard gameBoard;
	public static int currentRound;
	public static int numRounds;
	
	public User user;
	public User coolestUser;
	
	/// HEADER
	
	private String multiplyString(char c, int num) {
		String output = "";
		int count;
		for (count = 0; count < num; count++) {
			output += c;
		}
		return output;
	}

	private void printHeaderEntry(String str) {
		this.headerStr += String.format(
			"%s %-" + (this.headerWidth-4) + "s %s\n",
			Vectors.CHARS[2][1], str, Vectors.CHARS[2][1]
		);
	}

	private void printHeaderTop() {
		this.headerStr += String.format("%s%s%s\n",
			Vectors.CHARS[2][2],
			multiplyString(Vectors.CHARS[2][0], this.headerWidth-2),
			Vectors.CHARS[2][4]
		);
	}
	private void printHeaderSeparatorThick() {
		this.headerStr += String.format("%s%s%s\n",
			Vectors.CHARS[2][10],
			multiplyString(Vectors.CHARS[2][0], this.headerWidth-2),
			Vectors.CHARS[2][11]
		);
	}
	private void printHeaderBottom() {
		this.headerStr += String.format("%s%s%s\n",
			Vectors.CHARS[2][8],
			multiplyString(Vectors.CHARS[2][0], this.headerWidth-2),
			Vectors.CHARS[2][6]
		);
	}

	private void printHeaderHighscore() {
		if (coolestUser.name == null) {
			printHeaderEntry("High Score: None");
		}
		else {
			String highscoreEntry = "High Score: " + coolestUser.name + " - " + coolestUser.score;

			for (int i = 0; i < highscoreEntry.length(); i += this.headerWidth - 4) {
				int end = Math.min(i + this.headerWidth - 4, highscoreEntry.length());
				String highscoreEntrySubstring = highscoreEntry.substring(i, end);
				printHeaderEntry(highscoreEntrySubstring);
			}
		}
	}

	private void printHeaderScores() {
		// This is really, really dumb, but it works
		int scoreSeparatorX = Math.min(Math.max(this.headerWidth/2, 20), this.headerWidth - 19);
		int leftWidth = scoreSeparatorX - 17;
		int rightWidth = this.headerWidth - scoreSeparatorX - 16;

		// Top line
		this.headerStr += String.format("%s%s%s%s%s\n",
			Vectors.CHARS[2][9],
			multiplyString(Vectors.CHARS[1][0], scoreSeparatorX),
			Vectors.CHARS[1][3],
			multiplyString(Vectors.CHARS[1][0], this.headerWidth - 3 - scoreSeparatorX),
			Vectors.CHARS[2][5]
		);

		// Scores
		this.headerStr += String.format("%s Player Score:%" + (leftWidth + 2) + "d %s Last Round:%" + rightWidth + "d %s\n",
			Vectors.CHARS[2][1],
			Vectors.gameBoard.currentPlayerScore,
			Vectors.CHARS[1][1],
			Vectors.gameBoard.previousPlayerScore,
			Vectors.CHARS[2][1]
		);
		this.headerStr += String.format("%s Computer Score:%" + leftWidth + "d %s Last Round:%" + rightWidth + "d %s\n",
			Vectors.CHARS[2][1],
			Vectors.gameBoard.currentComputerScore,
			Vectors.CHARS[1][1],
			Vectors.gameBoard.previousComputerScore,
			Vectors.CHARS[2][1]
		);
		
		// Bottom line
		this.headerStr += String.format("%s%s%s%s%s\n",
			Vectors.CHARS[2][9],
			multiplyString(Vectors.CHARS[1][0], scoreSeparatorX),
			Vectors.CHARS[1][4],
			multiplyString(Vectors.CHARS[1][0], this.headerWidth - 3 - scoreSeparatorX),
			Vectors.CHARS[2][5]
		);
	}

	private void printHeaderTurns() {
		if (Vectors.currentRound > Vectors.numRounds) {
			printHeaderEntry("Game over");
		} else {
			printHeaderEntry("Turn " + Vectors.currentRound + " of " + Vectors.numRounds);
		}
	}

	private void printHeader() {
		printHeaderTop();
		printHeaderEntry("V E C T O R S");
		printHeaderSeparatorThick();
		printHeaderHighscore();
		printHeaderScores();
		printHeaderTurns();
		printHeaderBottom();
		printHeaderStr();
	}

	private void printHeaderConfig() {
		printHeaderTop();
		printHeaderEntry("Settings");
		printHeaderEntry("(Leave blank for default)");
		printHeaderBottom();
		printHeaderStr();
	}

	private void printHeaderStr() {
		System.out.println(headerStr.substring(0, headerStr.length()-1));
		this.headerStr = "";
	}

	/// GAME LOOP
	
	public Vectors() {
		this.scanner = new Scanner(System.in);
		
		this.user = new User();
		this.coolestUser = new User();
		this.coolestUser.loadUser();
		
		// Configure settings
		System.out.println(this.CLEAR);
		printHeaderConfig();
		inputBoardSize();
		inputRounds();

		// Wait a moment for dramatic effect
		try {
			System.out.print("  ");
			for (int i = 0; i < 4; i++) {
				Thread.sleep(200);
				if (i < 3) {
					System.out.print(".");
				} else {
					System.out.print(" ");
				}
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		System.out.println(this.CLEAR);

		this.headerWidth = 7 + gameBoard.width*4;
		if (this.headerWidth < this.MINIMUM_HEADER_WIDTH) {
			this.headerWidth = this.MINIMUM_HEADER_WIDTH;
		}
	}

	public void run() {
		AI ai = new AI();

		for (Vectors.currentRound = 1; Vectors.currentRound <= Vectors.numRounds+1; Vectors.currentRound++) {
			// Toggle this function on to generate boards every turn
			if (Vectors.DEV_CONFIG[0]) {
				Vectors.gameBoard.dev_setBoard(Vectors.DEV_CONFIG[1]);
			}

			Vectors.gameBoard.scanBoard();
			System.out.println(this.CLEAR);

			Vectors.gameBoard.calculateScores(false);
			this.printHeader();
			Vectors.gameBoard.printBoard();

			// Don't get input again if on final turn
			if (Vectors.currentRound < Vectors.numRounds+1) {
				inputCoordinatePair();
				ai.computerInput(Vectors.gameBoard);
			}
		}
		inputName();
		this.scanner.close();
		
		// Save score
		this.user.score = Vectors.gameBoard.currentPlayerScore - Vectors.gameBoard.currentComputerScore;
		if (this.user.score > this.coolestUser.score) {
			this.user.saveUser();
		}
	}

	/// INPUT
	
	private String isolateInteger(String str) {
		String output = "";
		int index;

		for (index = 0; index < str.length(); index++) {
			char ch = str.charAt(index);
			
			// Clamp
			if ((ch >= '0' && ch <= '9')) {
				output += ch;
			}
		}
		return output;
	}

	// VERY VERY VERY FOOL-PROOF INPUT HANDLING
	private String[] isolateStringPair(String str, boolean doBoundsCheck) {
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
			if (doBoundsCheck) {
				int row = Integer.parseInt(rowStr);
				int col = Integer.parseInt(colStr);
				if (row >= 0 && row < Vectors.gameBoard.height && col >= 0 && col < Vectors.gameBoard.width) {
					// Return null if there is more than 1 possible answer
					if (coordinatePair != null) {
						return null;
					}
					coordinatePair = new String[]{rowStr, colStr};
				}
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

	private void inputBoardSize() {
		String reason;
		int width = -1;
		int height = -1;

		while (true) {
			reason = "Improperly formatted";
			System.out.print("  Board size (Width Height): ");

			try {
				String input = this.scanner.nextLine().trim();

				// If user inputs a blank string, execute default constructor
				if (input.equals("")) {
					Vectors.gameBoard = new GameBoard();
					// Move cursor up one line and clear the line
					if (USE_ESCAPE_CODES) {
						System.out.print("\033[1A\r\033[K");
						System.out.printf("  Board size (Width Height): %dx%d\n", Vectors.gameBoard.width, Vectors.gameBoard.height);
					}
					break;
				}

				// Extract row and col from string
				String[] dimension = isolateStringPair(input, false);
				if (dimension == null) {
					throw new Exception();
				}

				width = Integer.parseInt(dimension[0]);
				height = Integer.parseInt(dimension[1]);

				// Bounds check
				if (height < 3 || height > 99 || width < 3 || width > 99) {
					reason = "Dimension must be between 3 and 99 tiles";
					throw new Exception();
				}

				Vectors.gameBoard = new GameBoard(width, height); 
				break;
			}
			catch (Exception exception) {
				System.out.printf("  [!] \"%dx%d\" is invalid: %s\n\n", width, height, reason);
			}
		}
	}

	private void inputRounds() {
		String reason;
		
		while (true) {
			reason = "Must be formatted as integer or left blank for default";
			System.out.printf("  Number of rounds (%s-%s): ", 1, Vectors.gameBoard.maxRounds);
			
			try {
				String input = this.scanner.nextLine().trim();

				// If user inputs a blank string, assign default numRounds
				if (input.equals("")) {
					Vectors.numRounds = (Vectors.gameBoard.maxRounds < this.DEFAULT_NUM_ROUNDS ? Vectors.gameBoard.maxRounds : this.DEFAULT_NUM_ROUNDS);
					// Move cursor up one line and clear the line
					if (USE_ESCAPE_CODES) {
						System.out.print("\033[1A\r\033[K");
						System.out.printf("  Number of rounds (%s-%s): %d\n", 1, Vectors.gameBoard.maxRounds, Vectors.numRounds);
					}
					break;
				}

				// Extract integer from string
				String isolatedIntegerStr = isolateInteger(input);
				if (isolatedIntegerStr == null) {
					throw new Exception();
				}
				Vectors.numRounds = Integer.parseInt(isolatedIntegerStr);

				// Clamp
				if (Vectors.numRounds <= 0 || Vectors.numRounds > Vectors.gameBoard.maxRounds) {
					reason = "Out of range";
					throw new Exception();
				}
				break;
			}
			catch (Exception exception) {
				System.out.printf("  [!]\"%d\" is invalid: %s\n\n", Vectors.numRounds, reason);
			}
		}
	}

	private void inputCoordinatePair() {
		String reason;
		int row = -1;
		int col = -1;

		while (true) {
			reason = "Improperly formatted";
			System.out.print("Player(X) Turn (Row Col): ");

			try {
				// Extract row and col from string
				String[] coordinatePair = isolateStringPair(this.scanner.nextLine(), true);
				if (coordinatePair == null) {
					throw new Exception();
				}
				row = Integer.parseInt(coordinatePair[0]);
				col = Integer.parseInt(coordinatePair[1]);

				// Bounds check
				if (col < 0 || col > Vectors.gameBoard.width-1 || row < 0 || row > Vectors.gameBoard.height-1) {
					reason = "Coordinate must be in range";
					throw new Exception();
				}
				
				// Overwriting check
				if (Vectors.gameBoard.getTokenTeam(col, row) > 0) { 
					reason = "This space is already taken";
					throw new Exception();
				}

				Vectors.gameBoard.writeToken(col, row, 1);
				break;
			}
			catch (Exception exception) {
				System.out.printf("[!] (%d,%d) is invalid: %s\n\n", row, col, reason);
			}
		}
	}

	private void inputName() {
		while (true) {
			System.out.print("  Enter your name: ");
			
			try {
				String input = this.scanner.nextLine().trim();

				// If user inputs a blank string, try again
				if (input.equals("")) {
					throw new Exception();
				}
				this.user.name = input;
				break;
			}
			catch (Exception exception) {
				System.out.println("  [!] Name not allowed\n");
			}
		}
	}
}