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
	String filepath;
	String usable_filepath = "/tmp/historyMsgs.txt";
	String usable_filepath2 = "historyMsgs.txt";
	public History() {
		String base = "";
		try {
			base = new String(Files.readAllBytes(Paths.get(this.usable_filepath)), StandardCharsets.UTF_8);
			filepath = usable_filepath;
		} catch (IOException e) {
			
		}
		
		try {
			historyFileWriter = new FileWriter(usable_filepath);
			historyFileWriter.write(base);
			historyFileWriter.flush();
			filepath = usable_filepath;
		} catch (IOException e) {
			try {
				base = new String(Files.readAllBytes(Paths.get(this.usable_filepath2)), StandardCharsets.UTF_8);
				filepath = usable_filepath2;
			}
			catch (IOException e1) {
				
			}
			
			try {
				historyFileWriter = new FileWriter(usable_filepath2);
				historyFileWriter.write(base);
				historyFileWriter.flush();
				filepath = usable_filepath2;
			}
			catch (IOException e2) {
				e2.printStackTrace();
			}
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
