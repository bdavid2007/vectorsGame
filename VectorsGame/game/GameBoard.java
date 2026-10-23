package game;

import java.util.ArrayList;
import java.util.Random;

public class GameBoard {
	private Token[][] board;
	private char[][] output;
	public ArrayList<Vector> vectors = new ArrayList<>();
	public int width;
	public int height;
	public int maxRounds;

	public int currentPlayerScore;
	public int currentComputerScore;
	public int previousPlayerScore;
	public int previousComputerScore;

	// Default constructor
	public GameBoard() {
		this.width = 8;
		this.height = 8;
		initBoard();
	}
	// Argument constructor
	public GameBoard(int width, int height) {
		this.width = width;
		this.height = height;
		initBoard();
	}

	private void initBoard() {
		this.maxRounds = this.width * this.height / 2; // Rounds down

		this.currentPlayerScore = 0;
		this.currentComputerScore = 0;
		this.previousPlayerScore = 0;
		this.previousComputerScore = 0;

		this.board = new Token[this.width][this.height];
		this.output = new char[this.height*2 + 1][this.width*4 + 1];
		int y;
		int x;
		for (y = 0; y < this.height; y++) {
			for (x = 0; x < this.width; x++) {
				this.board[x][y] = new Token(x, y);
			}
		}
	}

	private boolean tokenCannotBePlaced(char currentVectorDirection, Token token) {
		for (Vector vector : this.vectors) {
			if (vector.isTokenInSelf(token) && currentVectorDirection == vector.direction) {
				return true;
			}
		}
		return false;
	}

	private boolean examineNeighbor(Token from, char direction, int stepX, int stepY, int targetLength) {
		int currentX = from.x;
		int currentY = from.y;
		Token to = from;
		int t;

		for (t = 0; t < targetLength; t++) {
			// Bounds check
			if (currentX < 0 || currentX >= this.width || currentY < 0 || currentY >= this.height) {
				return false;
			}
			Token activeToken = board[currentX][currentY];
			
			// If token is occupied by a vector AND the vector is the same direction, skip building current vector
			if (activeToken.team != from.team || tokenCannotBePlaced(direction, activeToken)) {
				return false;
			}
			to = activeToken;

			currentX += stepX;
			currentY += stepY;
		}
		this.vectors.add(new Vector(this.board, from, to, from.team));
		return true;
	}

	public void scanBoard() {
		// Allow vectors to optimize between turns by clearing the array
		this.vectors.clear();

		// Scan the board every time for each possible vector length, guaranteeing that max length vectors are prioritized
		int index;
		for (index = 0; index < Vectors.LEGEND.length; index++) {
			int targetLength = Vectors.LEGEND[index][0];
			int y;
			int x;
			for (y = 0; y < this.height; y++) {
				for (x = 0; x < this.width; x++) {
					if (board[x][y].team > 0) {
						examineNeighbor(board[x][y],  '→',1, 0, targetLength); // →
						examineNeighbor(board[x][y],  '↘',1, 1, targetLength); // ↘
						examineNeighbor(board[x][y],  '↓',0, 1, targetLength); // ↓
						examineNeighbor(board[x][y],  '↙', -1, 1, targetLength); // ↙
					}
				}
			}
		}
	}

	public void writeToken(int x, int y, int team) {
		this.board[x][y].team = team;
	}
	public int getTokenTeam(int x, int y) {
		return this.board[x][y].team;
	}

	public void printBoard() {
		// Draw border
		int y;
		int x;
		int row = 0;
		int col;
		for (y = 1; y < this.output.length; y += 2) {
			col = 0;
			for (x = 2; x < this.output[0].length; x += 4) {
				// Middle
				this.output[y][x-1] = Vectors.CHARS[0][0];
				this.output[y][x+1] = Vectors.CHARS[0][0];
				if (board[col][row].team == 1) {
					this.output[y][x] = Vectors.CHARS[0][1]; // x
				} else if (board[col][row].team == 2) {
					this.output[y][x] = Vectors.CHARS[0][2]; // c
				} else {
					this.output[y][x] = Vectors.CHARS[0][0];
				}

				// Top-left
				if (row == 0 && col == 0) {
					this.output[y-1][x-2] = Vectors.CHARS[2][2]; // ╔
				} else if (row == 0 && col > 0) {
					this.output[y-1][x-2] = Vectors.CHARS[2][3]; // ╤
				} else if (col == 0) {
					this.output[y-1][x-2] = Vectors.CHARS[2][9]; // ╟
				} else {
					this.output[y-1][x-2] = Vectors.CHARS[1][2]; // ┼
				}

				// Top
				if (row == 0) {
					// ═
					this.output[y-1][x-1] = Vectors.CHARS[2][0];
					this.output[y-1][x]   = Vectors.CHARS[2][0];
					this.output[y-1][x+1] = Vectors.CHARS[2][0];
				} else {
					// ─
					this.output[y-1][x-1] = Vectors.CHARS[1][0];
					this.output[y-1][x]   = Vectors.CHARS[1][0];
					this.output[y-1][x+1] = Vectors.CHARS[1][0];
				}

				// Top-right
				if (row == 0 && col == this.width-1) {
					this.output[y-1][x+2] = Vectors.CHARS[2][4]; // ╗
				} else if (row > 0 && col == this.width-1) {
					this.output[y-1][x+2] = Vectors.CHARS[2][5]; // ╢
				} else {
					this.output[y-1][x+2] = Vectors.CHARS[1][2]; // ┼
				}

				// Right
				if (col == this.width-1) {
					this.output[y][x+2] = Vectors.CHARS[2][1]; // ║
				} else {
					this.output[y][x+2] = Vectors.CHARS[1][1]; // │
				}

				// Bottom-right
				if (row == this.height-1 && col == this.width-1) {
					this.output[y+1][x+2] = Vectors.CHARS[2][6]; // ╝
				} else if (row == this.height-1 && col < this.width-1) {
					this.output[y+1][x+2] = Vectors.CHARS[2][7]; // ╧
				} else {
					this.output[y+1][x+2] = Vectors.CHARS[1][2]; // ┼
				}

				// Bottom
				if (row == this.height-1) {
					// ═
					this.output[y+1][x+1] = Vectors.CHARS[2][0];
					this.output[y+1][x]   = Vectors.CHARS[2][0];
					this.output[y+1][x-1] = Vectors.CHARS[2][0];
				} else {
					// ─
					this.output[y+1][x+1] = Vectors.CHARS[1][0];
					this.output[y+1][x]   = Vectors.CHARS[1][0];
					this.output[y+1][x-1] = Vectors.CHARS[1][0];
				}

				// Bottom-left
				if (row == this.height-1 && col == 0) {
					this.output[y+1][x-2] = Vectors.CHARS[2][8]; // ╚
				} else if (row < this.height-1) {
					this.output[y+1][x-2] = Vectors.CHARS[1][2]; // ┼
				}
				
				// Left
				if (col == 0) {
					this.output[y][x-2] = Vectors.CHARS[2][1]; // ║
				} else {
					this.output[y][x-2] = Vectors.CHARS[1][1]; // │
				}

				col++;
			}
			row++;
		}

		// Draw vector indicators
		if (true) {
			for (Vector vector : this.vectors) {
				for (Token token : vector.containedTokens) {
					int centerX = token.x*4 + 2;
					int centerY = token.y*2 + 1;

					if (vector.direction == Vectors.CHARS[4][0]) { // →, -
						if (token == vector.from) {
							this.output[centerY][centerX+1] = Vectors.CHARS[3][0];
							this.output[centerY][centerX+2] = Vectors.CHARS[3][0];
						} else if (token == vector.to) {
							this.output[centerY][centerX-1] = Vectors.CHARS[3][0];
							this.output[centerY][centerX-2] = Vectors.CHARS[3][0];
						} else {
							this.output[centerY][centerX-1] = Vectors.CHARS[3][0];
							this.output[centerY][centerX+1] = Vectors.CHARS[3][0];
							this.output[centerY][centerX+2] = Vectors.CHARS[3][0];
						}
					}

					if (vector.direction == Vectors.CHARS[4][1]) { // ↘, ╲
						if (token == vector.from) {
							assignDiagonal(Vectors.CHARS[3][1], centerY+1, centerX+2);
						} else if (token == vector.to) {
							assignDiagonal(Vectors.CHARS[3][1], centerY-1, centerX-2);
						} else {
							assignDiagonal(Vectors.CHARS[3][1], centerY+1, centerX+2);
						}
					}

					if (vector.direction == Vectors.CHARS[4][2]) { // ↓, |
						if (token == vector.from) {
							this.output[centerY+1][centerX] = Vectors.CHARS[3][2];
						} else if (token == vector.to) {
							this.output[centerY-1][centerX] = Vectors.CHARS[3][2];
						} else {
							this.output[centerY-1][centerX] = Vectors.CHARS[3][2];
							this.output[centerY+1][centerX] = Vectors.CHARS[3][2];
						}
					}

					if (vector.direction == Vectors.CHARS[4][3]) { // ↙, ╱
						if (token == vector.from) {
							assignDiagonal(Vectors.CHARS[3][3], centerY+1, centerX-2);
						} else if (token == vector.to) {
							assignDiagonal(Vectors.CHARS[3][3], centerY-1, centerX+2);
						} else {
							assignDiagonal(Vectors.CHARS[3][3], centerY+1, centerX-2); 
						}
					}
				}
			}
		}

		// Print column numbers
		col = 0;
		System.out.print("   ");
		for (row = 0; row < this.width; row++) {
			System.out.printf(" %2d ", row);
		}
		System.out.println();

		for (y = 0; y < this.output.length; y++) {
			// Print row number
			if ((y+1) % 2 == 0) {
				System.out.printf("%2d ", col);
				col++;
			} else {
				System.out.printf("   ", col);
			}
			// Print grid
			for (x = 0; x < this.output[0].length; x++) {
				System.out.print(this.output[y][x]);
			}
			System.out.println();
		}
	}

	private void assignDiagonal(char glyph, int y, int x) {
		if (this.output[y][x] != Vectors.CHARS[1][2] /*┼*/ && this.output[y][x] != glyph) {
			this.output[y][x] = Vectors.CHARS[3][4]; // ╳
		} else {
			this.output[y][x] = glyph;
		}
	}

	public void calculateScores(boolean printVectors) {
		int playerScore = 0;
		int computerScore = 0;

		for (Vector vector : this.vectors) {
			if (vector.team == 1) {
				playerScore += vector.getScoreFromLength();
			}
			else if (vector.team == 2) {
				computerScore += vector.getScoreFromLength();
			}

			if (printVectors) {
				System.out.printf("Vector [%s] (%s):\n    ", (vector.team == 1 ? "Player" : "Computer"), vector.direction);
				for (Token token : vector.containedTokens) {
					String header = "";
					if (token.x == vector.from.x && token.y == vector.from.y) header = "F";
					if (token.x == vector.to.x && token.y == vector.to.y) header = "T";
					System.out.printf("%s(%d,%d) ", header, token.x, token.y);
				}
				System.out.printf("\n    Length: %d (+%d)\n", vector.length, vector.getScoreFromLength());
			}
		}

		this.previousPlayerScore = this.currentPlayerScore;
		this.previousComputerScore = this.currentComputerScore;
		this.currentPlayerScore = playerScore;
		this.currentComputerScore = computerScore;
	}

	public void dev_setBoard(boolean manualMode) {
		String[] inputBoard;

		// Manually create a board state
		if (manualMode) {
			inputBoard = new String[] {
				"x......x",
				".x....x.",
				"..x..x..",
				"...xx...",
				"...xx..x",
				".......x",
				".......x",
				"..xxxx.x",
			};
		}
		// Randomly create a board state
		else {
			// Clear previous board state to shuffle again
			this.vectors.clear();
			
			inputBoard = new String[this.height];
			Random rand = new Random();
			String row;
			double randomDouble;
			int y;
			int x;
			for (y = 0; y < this.height; y++) {
				row = "";
				for (x = 0; x < this.width; x++) {
					char tokenChar;
					randomDouble = rand.nextDouble();
					if (randomDouble < (1.0/3.0)) {
						tokenChar = '.';
					} else if (randomDouble < (2.0/3.0)) {
						tokenChar = Vectors.CHARS[0][1]; // x
					} else {
						tokenChar = Vectors.CHARS[0][2]; // c
					}
					row += tokenChar;
				}
				inputBoard[y] = row;
			}
		}

		// Apply board state to board
		int y;
		int x;
		for (y = 0; y < inputBoard.length; y++) {
			for (x = 0; x < inputBoard[0].length(); x++) {
				String tokenStr = inputBoard[y].substring(x, x+1);
				if (tokenStr.equals(""+Vectors.CHARS[0][1])) { // x
					this.board[x][y].team = 1;
				} else if (tokenStr.equals(""+Vectors.CHARS[0][2])) { // c
					this.board[x][y].team = 2;
				} else {
					this.board[x][y].team = 0;
				}
			}
		}
	}
}