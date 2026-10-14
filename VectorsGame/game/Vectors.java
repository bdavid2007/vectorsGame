package game;

import java.util.Scanner;

public class Vectors {

	GameBoard board;
	Scanner sc;
	public void run() {
		sc = new Scanner(System.in);
		
		board = new GameBoard(8,8);
		board.printBoard();
		int curRound = 0;
		int maxRounds = 20;
		
		//System.out.println("What ");
		
		for(curRound = 0; curRound < maxRounds; curRound++) {
			// Player turn
			
			// AI turn
			
			// oppenhiemr;
		}
		
		sc.close();
	}
	
	
	public int[] getPlayerInput()
	{
		int[] input = {-1, -1};
		char[] inputChars;
        String unprocessedInput;
        char[] possibleInputChars = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        int curCharIndex = 0;
		
		System.out.printf("Choose a position (Enter as [x space y]. ex: 3 9): ");
		
        int i, j;
        boolean inputProvenValid = false, 
            characterProvenValid;


        
        while(!inputProvenValid) // We goin to get an input outta you boy
        {
            inputProvenValid = true;


            unprocessedInput = sc.nextLine().trim().toLowerCase();
            
            for(i = 0; i < unprocessedInput.length(); i++)
            {
            	for(j = 0; j < possibleInputChars.length; j++) {
            		if(curCharIndex >= 2) break;
            		if(unprocessedInput.charAt(i) == possibleInputChars[i])
            		{
            			//input[curCharIndex] = Integer.parseInt(unprocessedInput.charAt(i));
            		}
            	}
            }
        }
		
		
		
		
		
		return input;
	}
}
