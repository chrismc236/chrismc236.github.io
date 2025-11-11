package a00310267;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class BookClass extends UnicastRemoteObject implements BookInterface, Serializable{
	// may not be needed, could be what gets serialized
	private String title;
	private String author;
	private int numOfPages;
	public BookClass(String title, String author, int numOfPages) throws RemoteException {
		this.title = title;
		this.author = author;
		this.numOfPages = numOfPages;
	}
	@Override
	public String getBookTitle() {
		return this.title;
	}
	@Override
	public String getBookAuthor() {
		return this.author;
	}
	@Override
	public int getBookNumOfPages() {
		return this.numOfPages;
	}
}