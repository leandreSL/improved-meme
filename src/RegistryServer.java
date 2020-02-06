import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class RegistryServer {
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Usage: java EchoServer <port number>");
			System.exit(1);
		}

		int portNumber = Integer.parseInt(args[0]);

		try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
				Socket clientSocket = serverSocket.accept();
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {
			String inputLine;
			Registry r = new Registry();
			String argsR [];
			while ((inputLine = in.readLine()) != null) {
				argsR = inputLine.split(" ");
				if (argsR.length == 0) {
					out.println("You must enter at least one argument");
					continue;
				}
				switch (argsR[0]) {
					case "add":
						if (argsR.length != 3) {
							out.println("You must enter 3 arguments");
							continue;
						}
						else {
							Person p = new Person(argsR[1],argsR[2]);
							r.add(p);
							out.println("Person " + p.name + " has been added to the database");
						}
						break;
					case "getPhone":
						if (argsR.length != 2) {
							out.println("You must enter 2 arguments");
							continue;
						}
						else {
							String phone;
							if ((phone = r.getPhone(argsR[1])) != null) {
								out.println("The phone number is : " + phone);
							}
							else {
								out.println("The person with the name " + argsR[1] + " is not in the database");
							}
						}
						break;
					case "getAll":
						if (argsR.length != 1) {
							out.println("You must enter 1 argument");
							continue;
						}
						else {
							String res = "";
							Iterable<Person> ps = r.getAll();
							for (Person p : ps) {
								res += p.name + " " + p.phoneNumber + "; ";
							}
							if (res == "") {
								out.println("The database is empty");
							}else {
								out.println(res);
							}
						}
						break;
					case "search":
						if (argsR.length != 2) {
							out.println("You must enter 2 arguments");
							continue;
						}
						else {
							Person p;
							if ((p = r.search(argsR[1])) != null) {
								out.println("Name : " + p.name + " Phone Number : " + p.phoneNumber);
							}
							else {
								out.println("The person with the name " + argsR[1] + " is not in the database");
							}
						}
						break;
					default:
						out.println("Unsupported operation : \"" + inputLine + "\"");
				}
			}

			System.out.println("I'm ending...");
		} catch (IOException e) {
			System.out.println(
					"Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
			System.out.println(e.getMessage());
		}
	}
}
