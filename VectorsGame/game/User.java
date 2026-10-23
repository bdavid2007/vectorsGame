package game;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String FILE_NAME = "data.dat";
	
	public String name;
	public int score;
	
	public User() {
		this.score = 0;
	}
	
	public void loadUser() {
	    FileInputStream inFile;
	    ObjectInputStream in;
	    try {
	        inFile = new FileInputStream(User.FILE_NAME);
	        in = new ObjectInputStream(inFile);
	        
	        User storedUser = (User)in.readObject();
	        this.name = storedUser.name;
	        this.score = storedUser.score;
	        in.close();
	    
	    } catch (FileNotFoundException e) {
	        this.name = null;
	        this.score = 0;
	        this.saveUser();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    }
	}
	
	public void saveUser() {
		FileOutputStream outFile;
		ObjectOutputStream out;
		try {
			outFile = new FileOutputStream(User.FILE_NAME);
			out = new ObjectOutputStream(outFile);
			out.writeObject(this);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}