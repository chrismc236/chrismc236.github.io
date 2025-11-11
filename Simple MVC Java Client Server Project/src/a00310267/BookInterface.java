package a00310267;

import java.rmi.Remote;
import java.rmi.RemoteException;

// interface to be added to array list and serialized
public interface BookInterface extends Remote{
	public String getBookTitle() throws RemoteException;
	public String getBookAuthor() throws RemoteException;
	public int getBookNumOfPages() throws RemoteException;
}
