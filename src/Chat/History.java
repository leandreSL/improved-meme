package Chat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class History {
	FileWriter historyFileWriter;
	FileReader historyFileReader;
	String filepath = "/tmp/historyMsgs.txt";
	public void History() {
		try {
			historyFileWriter = new FileWriter(filepath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void addMessage(Message msg) {
		try {
			historyFileWriter.write(msg.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String toString() {
		String buffer ="";
		try {
			historyFileReader = new FileReader(filepath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int ch;
		try {
			ch = historyFileReader.read();
			while(ch != -1) {
			    System.out.print((char)ch);
			    buffer = buffer + ch;
			    ch = historyFileReader.read();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return buffer;
		
		
	}
	
	
}
