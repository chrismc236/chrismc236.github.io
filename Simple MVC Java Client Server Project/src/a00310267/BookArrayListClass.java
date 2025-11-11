package a00310267;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;

public class BookArrayListClass extends UnicastRemoteObject implements BookArrayListInterface, Iterable{
	// creates array list that takes in book interfaces to be used in methods later on
	private ArrayList<BookInterface> bookList;

	// constructor for class that creates an empty list if no booklist can be loaded from the serialized file
	public BookArrayListClass() throws RemoteException{
		bookList = loadFromFile();
		if(bookList == null) {
			bookList = new ArrayList<>();
		}
	}

	// deserializes books.ser file as an object and returns the result after being casted to an ArrayList<BookInterface
	// otherwise prints out error message and returns null
	private ArrayList<BookInterface> loadFromFile() {
		try {
			FileInputStream fis = new FileInputStream("books.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			System.out.println("Books loaded from file");
			Object result = ois.readObject();
			return(ArrayList<BookInterface>) result;
		}
		catch(Exception e) {
			System.out.println("No previous data found or issue with deserialization");
			return null;
		}
	}

	// inherited from the BookArrayListInterface class, used for other methods later on
	@Override
	public ArrayList<BookInterface> getList(){
		return bookList;
	}

	// inherited from the BookArrayListInterface class, adds parameter book to the bookList variable
	@Override
	public void addToList(BookModel b) throws RemoteException{
		bookList.add(b);
		System.out.println("Book Added to List: " +
				b.getBookTitle() + " " +
				b.getBookAuthor() + " " +
				b.getBookNumOfPages());
	}

	// inherited method, needed for editBook and deleteBook method to find specific book from title
	@Override
	public Iterator iterator() {
		// Unimplemented method from Iterable
		return null;
	}

	// inherited method, checks if book exists in list, deletes it and updates the list
	@Override
	public void deleteFromList(String titleToBeDeleted) throws RemoteException {
		Iterator<BookInterface> iterator = bookList.iterator();
		boolean bookFound = false;
		while(iterator.hasNext()) {
			BookInterface bi = iterator.next();
			if(bi.getBookTitle().equalsIgnoreCase(titleToBeDeleted)) {
				iterator.remove();
				bookFound = true;
				System.out.println("Book deleted: " + titleToBeDeleted);
				saveToFile();
				break;
			}
		}
		if(bookFound == false) {
			System.out.println("Book not found: " + titleToBeDeleted);
			throw new RemoteException("Book not found: "+titleToBeDeleted);
		}
	}

	// inherited method, does the same as the delete method but updates the selected book to an updatedBook object then updates the list
	@Override
	public void updateBook(String oldTitle, BookModel updatedBook) throws RemoteException {
		for (int i = 0; i < bookList.size(); i++) {
	        if (bookList.get(i).getBookTitle().equalsIgnoreCase(oldTitle)) {
	            bookList.set(i, updatedBook);
	            saveToFile();
	            return;
	        }
	    }
	    throw new RemoteException("Book not found!");
	}

	// serializes the list to the "books.ser" file, called after delete or update methods
	private void saveToFile() {
		try {
			FileOutputStream fos = new FileOutputStream("books.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(bookList);
			System.out.println("Books saved to file");
		}
		catch(Exception e) {
			System.out.println("Issue with serialization");
		}
	};
}