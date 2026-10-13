package game;

public class GameBoard {
	private int[][] boardSlots = new int[8][8]; // 0 is empty, 1 is player, 2 is ai.
	
	public GameBoard() {}
	
	public GameBoard(int xSize, int ySize)
	{
		this.boardSlots = new int[xSize][ySize];
	}
	
	public int getBoardSlotStatus(int x, int y) {
		return boardSlots[x][y];
	}
	
	public void printBoard() {
		// NOT FINISHED
		
		for(int y = 0; y < boardSlots[0].length; y++)
		{
			for(int x = 0; x < boardSlots.length; x++)
			{
				System.out.print(GetBoardSlotStatus(x,y));
			}
			System.out.println();
		}
	}
	
	public boolean attemptPlay(int x, int y, boolean isPlayer)
	{
		boolean playSuccessful;
		int newStatus;
		
		if(isPlayer) newStatus = 1;
		else newStatus = 2;
		
		
		if(GetBoardSlotStatus(x, y) == 0)
		{
			playSuccessful = true;
			SetBoardSlot(x, y, newStatus);
			
			
		}
		else playSuccessful = false;
		
		return playSuccessful;
	}
	
	public void setBoardSlot(int x, int y, int status)
	{
		if(GetBoardSlotStatus(x, y) == status)
		{
			System.out.print("Board Slot at (" + x + ", " + y + ") is already set to " + status);
			return;
		}
		
		boardSlots[x][y] = status;
	}
}
