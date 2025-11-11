package a00310267;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class BookServer {
	public static void main(String[] args) {
		System.out.println("Server has started");
		try {
			// creates the server at port 1234
			Registry r = LocateRegistry.createRegistry(1234);
			System.out.println("Registry created at port 1234");

			// sample books to be added to server
			BookModel b1 = new BookModel("Dracula", "Bram Stoker", 418);
			System.out.println("Book 1 created");
			BookModel b2 = new BookModel("Harry Potter and the Philosopher's Stone", "J.K. Rowling", 223);
			System.out.println("Book 2 created");
			BookModel b3 = new BookModel("1984", "George Orwell", 328);
			System.out.println("Book 3 created");
			// bind only works if name is not already in use
			// will cause error if server is ran multiple times
			BookArrayListClass balc = new BookArrayListClass();
			balc.addToList(b1);
			balc.addToList(b2);
			balc.addToList(b3);
			try {
				// writes sample list to "books.ser" file
				FileOutputStream fo = new FileOutputStream("books.ser");
				ObjectOutputStream oo = new ObjectOutputStream(fo);
				oo.writeObject(balc);
				oo.close();
				fo.close();
			} catch (Exception e) {
				System.out.println("Issue with serialization");
			}
			// adds list to the server
			r.rebind("listObject", balc);
			System.out.println("Books binded");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}