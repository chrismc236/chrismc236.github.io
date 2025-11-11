package a00310267;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class BookView extends JFrame implements ActionListener{

	// declaration of GUI elements and global variables
	JFrame window = new JFrame("Book Manager");

	private JButton showAllButton = new JButton("Show All");
	private JButton addNewButton = new JButton("Add");
	private JButton deleteButton = new JButton("Delete");
	private JButton editButton = new JButton("Edit");
	private JButton updateButton = new JButton("Update");
	private JButton clearButton = new JButton("Clear Fields");

	String numOfPagesString = "";
	private JLabel titleLabel = new JLabel("Title: ");
	private JLabel authorLabel = new JLabel("Author: ");
	private JLabel pageLabel = new JLabel("Pages: ");

	private JTextField titleField = new JTextField(20);
    private JTextField authorField = new JTextField(20);
    private JTextField pagesField = new JTextField(20);

    private JTextArea bookListTextArea = new JTextArea(15, 40);
    private JScrollPane bookListScrollPane;

    private JTextField userField = new JTextField(20);

	private JPanel formPanel = new JPanel();
	private JPanel detailsPanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	private JPanel deletePanel = new JPanel();
	private JPanel bottomPanel = new JPanel();

	// constructor for book view, creates all the gui
	public BookView() {
		// window settings
		window.setSize(750, 750);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // IMPORTANT
		window.setLayout(new BorderLayout()); // uses borderlayout for entire window

		// form panel contains the creation fields and labels for making a new object
		formPanel.setLayout(new GridLayout(4, 2)); // this specifc panel uses grid layout

		// adds content to form panel
        formPanel.add(new JLabel("Title:"));
        formPanel.add(titleField);
        formPanel.add(new JLabel("Author:"));
        formPanel.add(authorField);
        formPanel.add(new JLabel("Number of Pages:"));
        formPanel.add(pagesField);
        formPanel.add(new JLabel());

        // adds content to detail panel
		detailsPanel.add(titleLabel);
		detailsPanel.add(authorLabel);
		detailsPanel.add(pageLabel);

		// add action listeners
		showAllButton.addActionListener(this);
		addNewButton.addActionListener(this);
		deleteButton.addActionListener(this);
		editButton.addActionListener(this);
		updateButton.addActionListener(this);
		clearButton.addActionListener(this);

		// set button sizes
		showAllButton.setPreferredSize(new Dimension(75, 25));
		addNewButton.setPreferredSize(new Dimension(75, 25));
		deleteButton.setPreferredSize(new Dimension(75, 25));
		editButton.setPreferredSize(new Dimension(75, 25));
		updateButton.setPreferredSize(new Dimension(75, 25));
		clearButton.setPreferredSize(new Dimension(75, 25));

		// adds buttons to panel, does exactly what is says on the tin
		buttonPanel.add(showAllButton);
		buttonPanel.add(addNewButton);
		buttonPanel.add(deleteButton);
		buttonPanel.add(editButton);
		buttonPanel.add(updateButton);
		buttonPanel.add(clearButton);

		// adds user entry field, delete panel and bottom row buttons
		deletePanel.add(userField);
		bottomPanel.add(deletePanel);
		bottomPanel.add(buttonPanel);

		// settings for the area where the book list will go
		bookListTextArea.setEditable(false);
		bookListTextArea.setLineWrap(true);
		bookListTextArea.setWrapStyleWord(true);
		bookListScrollPane = new JScrollPane(bookListTextArea);

		// additional window layout settings, added panels, etc.
		window.add(formPanel, BorderLayout.NORTH);
		window.add(bookListScrollPane, BorderLayout.CENTER);
		window.add(bottomPanel, BorderLayout.SOUTH);
		window.setVisible(true);
	}

	// inherited from Action Listener, handles the button clicks, probably should be
	// in the controller class instead
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(showAllButton)) {
			System.out.println("Show All Button Clicked");
			try {
				showAll();
			}
			catch(Exception showAllException) {
				showAllException.printStackTrace();
			}
		}
		else if(e.getSource().equals(addNewButton)) {
			System.out.println("Add New Button Clicked");
			try {
				addNew();
			} catch (Exception addNewException) {
				addNewException.printStackTrace();
			}
		}
		else if(e.getSource().equals(deleteButton)) {
			System.out.println("Delete Button Clicked");
			deleteBook();
		}
		else if(e.getSource().equals(editButton)) {
			System.out.println("Edit Button Clicked");
			editBook();
		}
		else if(e.getSource().equals(updateButton)) {
			System.out.println("Update Button Clicked");
			try {
				updateBook();
			} catch (RemoteException updateException) {
				updateException.printStackTrace();
			}
		}
		else if(e.getSource().equals(clearButton)) {
			clearFields();
		}
	}

	// displays the current book list in the bookListTextArea, appending all booksto the field
	public void showAll() throws RemoteException {
		ArrayList<BookInterface> bookList = BookClient.getBookList();
		bookListTextArea.setText(""); // Clear existing content
        if (bookList == null || bookList.isEmpty()) {
        	bookListTextArea.append("No books available. \n");
        	return;
        }
        for (BookInterface b : bookList) {
        	try {
	        	bookListTextArea.append("Title: " + b.getBookTitle() + "\n");
	        	bookListTextArea.append("Author: " + b.getBookAuthor() + "\n");
	        	bookListTextArea.append("Number of Pages: " + b.getBookNumOfPages() + "\n");
	        	bookListTextArea.append("-----------------------------\n");
        	}
        	catch(Exception e) {
    	        JOptionPane.showMessageDialog(window, "Books could not be displayed");
        		e.printStackTrace();
        	}
        }
	}

	// clears all entry fields and book list field
	public void clearFields() {
		bookListTextArea.setText("");
		titleField.setText("");
		titleField.setBackground(Color.white);
		authorField.setText("");
		authorField.setBackground(Color.white);
		pagesField.setText("");
		pagesField.setBackground(Color.white);
		userField.setText("");
		System.out.println("All Fields Cleared");
	}

	// should probably be in the BookController file
	// adds book to list
	public void addNew() throws Exception {
		titleField.setBackground(Color.green);
		authorField.setBackground(Color.green);
		pagesField.setBackground(Color.green);

		// gets the values from the input fields and assigns them to variables
	    String title = titleField.getText().trim();
	    String author = authorField.getText().trim();
	    String pagesText = pagesField.getText().trim();

	    // Input validation
	    if (title.isEmpty() || author.isEmpty() || pagesText.isEmpty()) {
	        JOptionPane.showMessageDialog(window, "All fields must be filled!");
	        clearFields();
	        return;
	    }
	    // input validation for number of pages field
	    int numOfPages;
	    try {
	        numOfPages = Integer.parseInt(pagesText);
	        if (numOfPages <= 0)
			 {
				throw new NumberFormatException(); // Pages must be positive
			}
	    } catch (NumberFormatException ex) {
	        JOptionPane.showMessageDialog(window, "Number of pages must be a valid positive integer!", "Input Error", JOptionPane.ERROR_MESSAGE);
	        return;
	    }
	    // Create and add the book
	    BookModel newBook = new BookModel(title, author, numOfPages);
	    BookController.addBook(newBook);
	    BookClient.addBook(newBook);
	    JOptionPane.showMessageDialog(window, "Book added: "+title+" by: "+author);
	    clearFields();
	    showAll();
	    }

	// should probably be in the BookController file
	// edits a selected book from the list
	public void editBook() {
		// book is selected from title match of the user field input
		String titleToEdit = userField.getText().trim();
		if (titleToEdit.isBlank() || titleToEdit.isEmpty()) {
	        JOptionPane.showMessageDialog(window, "Please Enter a title!");
	        return;
		}
		try {
			// looks through created list for a book with the title matching what the user input
			ArrayList<BookInterface> bookList = BookClient.getBookList();
			BookInterface bookToEdit = null;
	        for (BookInterface book : bookList) {
	            if (book.getBookTitle().equalsIgnoreCase(titleToEdit)) {
	                bookToEdit = book;
	                break;
	            }
	        }
	        // if there is no book in the list, show an error window
	        if (bookToEdit == null) {
	            JOptionPane.showMessageDialog(window, "Book not found!");
	            clearFields();
	            return;
	        }
	        // loads the selected book into the edit feels to be updated
			titleField.setText(bookToEdit.getBookTitle());
	        authorField.setText(bookToEdit.getBookAuthor());
	        pagesField.setText(String.valueOf(bookToEdit.getBookNumOfPages()));
			JOptionPane.showMessageDialog(window, "Book ready to be updated");
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(window, "Failed to update");
			e.printStackTrace();
		}
	}

	// should also probably be in the BookController class
	// once new inputs are after the edit function, changes selected book to new values
	public void updateBook() throws RemoteException{
		String oldTitle = userField.getText().trim();
		String newTitle = titleField.getText().trim();
		String newAuthor = authorField.getText().trim();
		String newPages = pagesField.getText().trim();
		if (newTitle.isEmpty() || newAuthor.isEmpty() || newPages.isEmpty()) {
			JOptionPane.showMessageDialog(window, "All fields must be filled!");
			return;
		}
		// input validation for number of pages field
		int numOfPages;
		try {
			numOfPages = Integer.parseInt(newPages);
			if (numOfPages < 0) {
				throw new NumberFormatException();
			}
		}
		catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(window, "Number of pages must be a valid integer!");
			return;
		}
		try {
			BookModel updatedBook = new BookModel(newTitle, newAuthor, numOfPages);
			BookClient.updateBook(oldTitle, updatedBook);
			JOptionPane.showMessageDialog(window, "Book updated successfully!");
			clearFields();
			showAll();
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(window, "Failed to update Book");
			e.printStackTrace();
		}
	}

	// should also probably be in the BookController class
	// if entered book title exists in the list, deletes it from the list and updates list
	public void deleteBook() {
		String titleToDelete = userField.getText().trim();
		if (titleToDelete.isBlank() || titleToDelete.isEmpty()) {
	        JOptionPane.showMessageDialog(window, "Please Enter a title!");
	        clearFields();
	        return;
		}
		try {
			BookClient.deleteBook(titleToDelete);
	        JOptionPane.showMessageDialog(window, "Book Deleted from List!");
	        ArrayList<BookInterface> updatedList = BookClient.getBookList();
	        showUpdatedList(updatedList);
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(window, "Failed to delete");
			e.printStackTrace();
		}
	}

	// this could be in the right place
	// clears what is currently on the screen and reloads list from file
	public void showUpdatedList(ArrayList<BookInterface> updatedList) {
		clearFields();
		for(BookInterface book: updatedList) {
			try {
				bookListTextArea.append("Title: " + book.getBookTitle() + "\n");
	        	bookListTextArea.append("Author: " + book.getBookAuthor() + "\n");
	        	bookListTextArea.append("Number of Pages: " + book.getBookNumOfPages() + "\n");
	        	bookListTextArea.append("-----------------------------\n");
			}
			catch(Exception e) {
				JOptionPane.showMessageDialog(window, "Failed to show updated list");
				e.printStackTrace();
			}
		}
	}

	// logging method that was used to check if methods were right, kept in project for nostalgia
	public void printBookDetails(String title, String author, int numOfPages) {
		System.out.println("Book: ");
		System.out.println("Title: " + title);
		System.out.println("Author: " + author);
		System.out.println("Number of Pages: " + numOfPages);
	}
}
