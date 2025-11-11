package a00310267;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface BookArrayListInterface extends Remote{
	// array list interface to be serialized in the server
	public void addToList(BookModel b) throws RemoteException;
	public ArrayList<BookInterface> getList() throws RemoteException;
	public void deleteFromList(String titleToBeDeleted) throws RemoteException;
	public void updateBook(String oldTitle, BookModel updatedBook) throws RemoteException;
}