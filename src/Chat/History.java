package Chat;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class History {
	FileWriter historyFileWriter;
	FileReader historyFileReader;
	String filepath = "/tmp/historyMsgs.txt";
	public History() {
		try {
			historyFileWriter = new FileWriter(filepath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void addMessage(Message msg) {
		try {
			historyFileWriter.write(msg.toString() + "\n");
			historyFileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void addMessage(String msg) {
		try {
			historyFileWriter.write(msg + "\n");
			historyFileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String toString() {
		String buffer ="";
		try {
			buffer = new String(Files.readAllBytes(Paths.get(this.filepath)), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
			return "-";
		};
		return buffer;
		
		
	}
	
	
}
