package a00310267;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class BookController{

	// code from bookClient should be in this file

	private BookModel model;
	public BookView view;
	private static ArrayList<BookModel> books = new ArrayList<>();

	public BookController() throws RemoteException{};

	public BookController(BookModel model, BookView view){
		this.model = model;
		this.view = view;
	}

	public String getBookTitle() {
		return model.getBookTitle();
	}

	public String getBookAuthor() {
		return model.getBookAuthor();
	}

	public int getBookNumOfPages() {
		return model.getBookNumOfPages();
	}

	public void setBookTitle(String bookTitle) {
		model.setTitle(bookTitle);
	}

	public void setBookAuthor(String bookAuthor) {
		model.setAuthor(bookAuthor);
	}

	public void setBookNumOfPages(int bookNumOfPages) {
		model.setNumOfPages(bookNumOfPages);
	}

	public void updateView() {
		view.printBookDetails(model.getBookTitle(), model.getBookAuthor(), model.getBookNumOfPages());
	}

	public static void addBook(BookModel newBook) {
		books.add(newBook);
		System.out.println("Book added: " + newBook.getBookTitle() + " by: " + newBook.getBookAuthor());
	}

	public ArrayList<BookModel> getAllBooks(){
		return books;
	}
}
