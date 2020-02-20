package TP2;


import java.rmi.*;

public  class HelloImpl implements Hello {

	private String message;
 
	public HelloImpl(String s) {
		message = s ;
	}

	@Override
	public String sayHello(Info_itf client) throws RemoteException {
		return client.getName() + ": " + message;
	}
}

