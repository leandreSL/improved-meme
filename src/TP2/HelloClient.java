package TP2;

import java.io.Serializable;
import java.rmi.*;
import java.rmi.registry.*;

public class HelloClient implements Info_itf, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {

		try {
			if (args.length < 1) {
				System.out.println("Usage: java HelloClient <rmiregistry host>");
				return;
			}

			String host = args[0];

			// Get remote object reference
			Registry registry = LocateRegistry.getRegistry(host);
			Hello h = (Hello) registry.lookup("HelloService");

			// Remote method invocation
			String res = h.sayHello(new HelloClient());
			System.out.println(res);

		} catch (Exception e) {
			System.err.println("Error on client: " + e);
		}
	}

	@Override
	public String getName() throws RemoteException {
		return "Client 1";
	}
}