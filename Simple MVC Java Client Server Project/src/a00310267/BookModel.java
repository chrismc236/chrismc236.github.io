package a00310267;

import java.io.Serializable;

public class BookModel implements BookInterface, Serializable{
	// actual instance of the books being created
	private String title;
	private String author;
	private int numOfPages;

	public BookModel(String title, String author, int numOfPages) {
		super();
		this.title = title;
		this.author = author;
		this.numOfPages = numOfPages;
	}
	public BookModel() {
	}
	@Override
	public String getBookTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public String getBookAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	@Override
	public int getBookNumOfPages() {
		return numOfPages;
	}
	public void setNumOfPages(int numOfPages) {
		this.numOfPages = numOfPages;
	}
}