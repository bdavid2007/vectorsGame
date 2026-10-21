package game;

public class Token {
	public int x;
	public int y;
	
	// 0 is empty
	// 1 is player
	// 2 is computer
	public int team;

	public Token(int x, int y) {
		this.x = x;
		this.y = y;
		this.team = 0;
	}
}