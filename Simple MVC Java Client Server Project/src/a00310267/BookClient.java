package a00310267;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class BookClient {
	public static void main(String[] args) {
		// line for logging
		System.out.println("Client has started");
		try {
			// deserialize book list from BookArrayListClass
			ArrayList<BookInterface> bookList = deserializeBookList();
			if(bookList != null && !bookList.isEmpty()) {
				for(BookInterface book : bookList) {
					printBookDetails(book);
					System.out.println("==============================");
				}
			}
			else {
				System.out.println("No books in storage");
			}
			// creates instance of the GUI
			BookView bv = new BookView();
			bv.showAll();
			// sample book to be edited in demonstration
			BookModel sampleBook = new BookModel("Sample Book 1", "Sample Author 1", 200);
			addBook(sampleBook);
			// serializes current book list to "books.ser" file
			serializeBookList(getBookListFromServer());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	// uses FileOutputStream and ObjectOutputStream to write the book list to the "book.ser" file
	public static void serializeBookList(ArrayList<BookInterface> bookList) {
		 try {
			 FileOutputStream fos = new FileOutputStream("books.ser");
			 ObjectOutputStream oos = new ObjectOutputStream(fos);
			 oos.writeObject(bookList);
			 System.out.println("Book list saved locally");
			 oos.close();
		 }
		 catch (Exception e) {
			 System.out.println("Error during serialization");
		 }
	}

	 // retrieves the book list from the server file for use later on
	public static ArrayList<BookInterface> getBookListFromServer() {
		 try {
			 Registry registry = LocateRegistry.getRegistry(1234);
			 System.out.println("Registry retrieved at 1234");
			 BookArrayListInterface bookListInterface = (BookArrayListInterface) registry.lookup("listObject");
			 return bookListInterface.getList();
		 }
		 catch (Exception e) {
			 e.printStackTrace();
		 }
		 // Return empty list if there is an error
		 return new ArrayList<>();
	}

	// uses FileInputStream and ObjectInputStream to read the book list from the "book.ser" file
	public static ArrayList<BookInterface> deserializeBookList() {
		try {
			FileInputStream fis = new FileInputStream("books.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			return (ArrayList<BookInterface>) ois.readObject();
		}
		catch(Exception e) {
			System.out.println("No local book list found or error during serialization");
		}
		return null;
	}

	// logging method for checking, this time checks if the bookinterfaces were made correctly,
	// this one is not nostalgic to me
	public static void printBookDetails(BookInterface book) throws Exception {
	        System.out.println("Title: " + book.getBookTitle());
	        System.out.println("Author: " + book.getBookAuthor());
	        System.out.println("Pages: " + book.getBookNumOfPages());
	    }

	// retrieves the booklist from the registry/server
	 public static ArrayList<BookInterface> getBookList(){
		 try {
				Registry registry = LocateRegistry.getRegistry(1234);
				System.out.println("registry retrieved at 1234");
				BookArrayListInterface bookListInterface = (BookArrayListInterface)registry.lookup("listObject");
				return bookListInterface.getList();
		 }
		 catch(Exception e) {
			 e.printStackTrace();
		 }
		 // return empty list if there is an error
		 return new ArrayList<>();

	 }

	// retrieves the list and then adds the book to that server book list
	public static void addBook(BookModel newBook) throws Exception{
		try {
			Registry registry = LocateRegistry.getRegistry(1234);
			System.out.println("registry retrieved at 1234");
			BookArrayListInterface bali = (BookArrayListInterface) registry.lookup("listObject");
			// add book to server list
			bali.addToList(newBook);
			System.out.println("Book added to the server");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	// retrieves the list and then deletes the book from that server book list
	public static void deleteBook(String titleToBeDeleted) throws Exception{
		Registry registry = LocateRegistry.getRegistry(1234);
		System.out.println("Registry retrieved at 1234");
		BookArrayListInterface bali = (BookArrayListInterface) registry.lookup("listObject");
		bali.deleteFromList(titleToBeDeleted);
	}

	// retrieves the list and then updates the book from that server book list with the correct values
	public static void updateBook(String titleToEdit, BookModel updatedBook) throws Exception{
		Registry registry = LocateRegistry.getRegistry(1234);
		System.out.println("Registry retrieved at 1234");
		BookArrayListInterface bali = (BookArrayListInterface) registry.lookup("listObject");
		bali.updateBook(titleToEdit, updatedBook);
	}
}