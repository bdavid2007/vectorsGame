
package game;

import java.util.Random;

public class AI {
    public enum Tactic {
        //MIDDLE_MAN, i got rid of this it was too hard :(
        BLIND_MAN,
        BAG_MAN,
        SCISSOR_MAN
    }

    Random random;
    Tactic currentTactic;
    int moveX, moveY;

    public AI() {
        random = new Random();
        currentTactic = tacticFromNumber(random.nextInt(Tactic.values().length));
    }

    // Pick a tactic by number instead of using Tactic.values() (an array).
    private Tactic tacticFromNumber(int number) {
        switch (number) {
            case 0: return Tactic.BLIND_MAN;
            case 1: return Tactic.BAG_MAN;
            case 2: return Tactic.SCISSOR_MAN;
        }

		return  Tactic.BLIND_MAN;
    }

    public void computerInput(GameBoard board) {
        int openSpots = 0;

        // Check every row of the board.
        for (int y = 0; y < board.height; y++) {
            // Check every space in the current row and count empty spaces.

            for (int x = 0; x < board.width; x++) {
                if (board.getTokenTeam(x, y) == 0) openSpots++;
            }
        }

        if (openSpots == 0)
		{
			System.out.println("Something went wrong broliochachenheimer");
		}

        // Each turn has a 1-in-3 chance to switch to another tactic.
        if (random.nextInt(3) == 0) switchTactic();

        // -1 means that a tactic has not picked a move yet.
        moveX = -1;
        moveY = -1;

        switch (currentTactic) {
            case BLIND_MAN:
                chooseBlindMan(board, openSpots);
                break;
            case BAG_MAN:
                chooseBagMan(board);
                break;
            case SCISSOR_MAN:
                chooseScissorMan(board);
                break;
        }

        // If this tactic had no useful move, choose a random empty spot.
        if (!hasTeam(board, moveX, moveY, 0)) {
            chooseBlindMan(board, openSpots);
        }

        board.writeToken(moveX, moveY, 2);
    }

    private void switchTactic() {
        // Choose one of the three other tactics, never the current one.
        int next = random.nextInt(3);

        if (next >= currentTactic.ordinal()) next++;

        currentTactic = tacticFromNumber(next);
    }

    // choose a random square brosef brofenstien
    private void chooseBlindMan(GameBoard board, int openSpots) {
        int target = random.nextInt(openSpots);
		int x, y;

        // Go through every row until we reach the chosen empty space.
        for (y = 0; y < board.height; y++) {

            // Count down only when a space is empty.
            for (x = 0; x < board.width; x++) {

                if (board.getTokenTeam(x, y) == 0) {
                    if (target == 0) {
                        moveX = x;
                        moveY = y;
                        return;
                    }

                    target--;
                }
            }
        }
    }


    private void chooseBagMan(GameBoard board) {
        int longest = 0;
        int ties = 0;
		int x, y, dir;

        // Look through every row for possible moves.
        for (y = 0; y < board.height; y++) {

            // Try each empty spot in the row.
            for (x = 0; x < board.width; x++) {
                if (!hasTeam(board, x, y, 0)) continue;

                // Check horizontal, vertical, and both diagonal directions.
                for (dir = 0; dir < 4; dir++) {
                    int dx = directionX(dir);
                    int dy = directionY(dir);

                    int forward = countLine(board, x + dx, y + dy, dx, dy, 2);
                    int backward = countLine(board, x - dx, y - dy, -dx, -dy, 2);


                    int length = forward + backward;
                    if (forward > 0 && backward > 0) length = 0;

                    if (length == 0 || length < longest) continue;

                    if (length > longest) {
                        longest = length;
                        ties = 0;
                    }

                    ties++;
                    if (random.nextInt(ties) == 0) {
                        moveX = x;
                        moveY = y;
                    }
                }
            }
        }
    }

    // evulauate where the best place to scew over the player will be, then engage screwing.
    private void chooseScissorMan(GameBoard board) {
        int biggestThreat = 0;
        int ties = 0;
		int x, y, dir;

        // Go through every row looking for blocking opportunities.
        for (y = 0; y < board.height; y++) {

            // Look at each empty space where the computer could block.
            for (x = 0; x < board.width; x++) {

				//skip if its not empty
                if (!hasTeam(board, x, y, 0)) continue;

                int threat = 0;

                // Check how many player tokens connect in each direction
                // if the player were to take this empty spot.
                for (dir = 0; dir < 4; dir++) {
                    int dx = directionX(dir);
                    int dy = directionY(dir);

                    int length = countLine(board, x + dx, y + dy, dx, dy, 1) 
						+ countLine(board, x - dx, y - dy, -dx, -dy, 1);

                    if (length > threat) threat = length;
                }

                if (threat == 0 || threat < biggestThreat) continue;

                if (threat > biggestThreat) {
                    biggestThreat = threat;
                    ties = 0;
                }

                // Randomly choose between equally useful blocking moves.
                ties++;
                if (random.nextInt(ties) == 0) {
                    moveX = x;
                    moveY = y;
                }
            }
        }
    }

    // Turn a direction number into its horizontal step
    // 0 = right, 1 = down, 2 = down-right, 3 = up-right
    private int directionX(int direction) {
        if (direction == 1) return 0;
        return 1;
    }

    // Turn a direction number into its vertical step
    private int directionY(int direction) {
        if (direction == 0) return 0;
        if (direction == 3) return -1;
        return 1;
    }

    // Count connected tokens of one team in a straight line.
    private int countLine(GameBoard board, int x, int y, int dx, int dy, int team) {
        int length = 0;

        // Keep moving in the same direction until the line ends.
        while (hasTeam(board, x, y, team)) {
            length++;
            x += dx;
            y += dy;
        }

        return length;
    }

    // Check whether a position exists and belongs to the given team.
    private boolean hasTeam(GameBoard board, int x, int y, int team) {
        return x >= 0 && x < board.width
            && y >= 0 && y < board.height
            && board.getTokenTeam(x, y) == team;
    }
}