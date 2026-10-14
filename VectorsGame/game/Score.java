package game;

import java.io.File;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;

public class Score {
    public int highscore;
    private static final String FILE_NAME = "scores.txt";

    public int getHighscore() {
    	int highestScore = 0;
    	try {
            File file = new File(FILE_NAME);
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                int score = Integer.parseInt(sc.nextLine());
                if (score > highestScore) {
                	highestScore = score;
                }
            }
            sc.close();
        }
		catch (IOException exception) {
        	exception.printStackTrace();
        }
        return highestScore;
    }

    public void addScoreToMemory(int score) {
        try {
        	FileWriter fileWriter = new FileWriter(FILE_NAME, true); // 'true' enables append mode
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bufferedWriter);
        	printWriter.println(score);
        }
        catch (IOException exception) {
        	exception.printStackTrace();
        }
    }
}