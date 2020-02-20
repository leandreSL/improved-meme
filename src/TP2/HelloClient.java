package TP2;

import java.io.Serializable;
import java.rmi.*;
import java.rmi.registry.*;

public class HelloClient implements Info_itf, Serializable, Accounting_itf {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7991929566753861754L;

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
			Hello2 h2 = (Hello2) registry.lookup("HelloService2");
			Registry_itf r = (Registry_itf) registry.lookup("RegistryService");

			HelloClient c = new HelloClient();
			r.register(c);
			// Remote method invocation
			int res = h2.sayHello(c);
			res = h2.sayHello(c);
			res = h2.sayHello(c);
			System.out.println(res);

		} catch (Exception e) {
			System.err.println("Error on client: " + e);
		}
	}

	@Override
	public String getName() throws RemoteException {
		return "ID" + serialVersionUID;
	}

	@Override
	public void numberOfCalls(int number) throws RemoteException {
		System.out.println("Number of calls already done: " + number);
	}
}