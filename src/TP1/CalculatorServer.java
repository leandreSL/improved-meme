package TP1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class CalculatorServer {
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
			int arg1 = 0, arg2 = 0;
			Calculator c = new Calculator();
			
			while ((inputLine = in.readLine()) != null) {
				try {
					arg1 = Integer.valueOf(in.readLine());
				}
				catch (NumberFormatException e) {
					out.println("Operand 1 must be an integer");
					in.readLine();
					continue;
				}
				try {
					arg2 = Integer.valueOf(in.readLine());
				}
				catch (NumberFormatException e) {
					out.println("Operand 2 must be an integer");
					continue;
				}
				
				switch (inputLine) {
					case "add":
						out.println(c.plus(arg1, arg2));
						break;
					case "sub":
						out.println(c.minus(arg1, arg2));
						break;
					case "div":
						try {
							out.println(c.divide(arg1, arg2));
						}
						catch (Exception e) {
							out.println("Division by 0 is forbidden");
						}
						break;
					case "mult":
						out.println(c.multiply(arg1, arg2));
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
