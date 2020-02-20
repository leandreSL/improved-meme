package TP2;


import java.rmi.*;

public  class HelloImpl implements Hello, Hello2 {

	private String message;
 
	public HelloImpl(String s) {
		message = s ;
	}

	@Override
	public String sayHello(Info_itf client) throws RemoteException {
		return client.getName() + ": " + message;
	}

	@Override
	public int sayHello(Accounting_itf client) throws RemoteException {
		String clientId = ((HelloClient) client).getName();
		HelloServer hs = HelloServer.getInstance();
		hs.setNumberOfCalls(clientId, hs.getNumberOfCalls(clientId) + 1);
		return hs.getNumberOfCalls(clientId);
	}
}

