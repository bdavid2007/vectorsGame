package game;

public class Vector {
	public int team;
	public Token from;
	public Token to;
	public int length;
	public char direction;
	public Token[] containedTokens;

	// Argument constructor
	public Vector(Token[][] board, char[][] CHARS, Token from, Token to, int team) {
		this.team = team;
		this.updateVector(board, CHARS, from, to);
	}

	public void updateVector(Token[][] board, char[][] CHARS, Token from, Token to) {
		this.from = from;
		this.to = to;

		int changeInX = this.to.x - this.from.x;
		int changeInY = this.to.y - this.from.y;
		this.length = Math.max(Math.abs(changeInX), Math.abs(changeInY)) + 1;

		// Determine direction of vector
		if (changeInX > 0) {
			if (changeInY > 0) {
				this.direction = CHARS[4][1]; // ↘
			}
			else if (changeInY == 0) {
				this.direction = CHARS[4][0]; // →
			}
			else {// if (changeInY < 0) {
				this.direction = CHARS[4][7]; // ↗ Impossible
			}
		}
		else if (changeInX == 0) {
			if (changeInY > 0) {
				this.direction = CHARS[4][2]; // ↓
			}
			else if (changeInY == 0) {
				this.direction = CHARS[4][8]; // . Impossible
			}
			else {// if (changeInY < 0) {
				this.direction = CHARS[4][6]; // ↑ Impossible
			}
		}
		else {
			if (changeInY > 0) {
				this.direction = CHARS[4][3]; // ↙
			}
			else if (changeInY == 0) {
				this.direction = CHARS[4][4]; // ← Impossible
			}
			else {// if (changeInY < 0) {
				this.direction = CHARS[4][5]; // ↖ Impossible
			}
		}

		// Fill containedTokens
		this.containedTokens = new Token[this.length];

		int stepX = (changeInX == 0)
			? 0
			: changeInX / Math.abs(changeInX)
		;
		int stepY = (changeInY == 0)
			? 0
			: changeInY / Math.abs(changeInY)
		;

		int currentX;
		int currentY;
		int index;
		for (index = 0; index < this.length; index++) {
			currentX = this.from.x + (index * stepX);
			currentY = this.from.y + (index * stepY);
			// Fill with references to tokens rather than literal tokens
			this.containedTokens[index] = board[currentX][currentY];
		}
	}

	public boolean isTokenInVector(Token token) {
		for (Token containedToken : containedTokens) {
			if (token.x == containedToken.x && token.y == containedToken.y) {
				return true;
			}
		}
		return false;
	}

	public int getScore(int[][] legend) {
		for (int[] item : legend) {
			if (item[0] == this.length) {
				return item[1];
			}
		}
		return 0;
	}
}