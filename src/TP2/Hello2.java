package TP2;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Hello2 extends Remote {
	public int sayHello(Accounting_itf client) throws RemoteException;

}
