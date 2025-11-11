package a00310267;

public class BookMVCTester {

	// old tester file that is not needed, also for nostalgia
	BookModel model = new BookModel();
	BookView view = new BookView();
	BookController controller = new BookController(model, view);

	public static void main(String[] args) {
		BookMVCTester tester = new BookMVCTester();
		tester.testCode();
	}

	public void testCode() {
		model.setTitle("Of Mice and Men");
		model.setAuthor("John Steinbeck");
		model.setNumOfPages(144);

		controller.updateView();
		controller.setBookTitle("Off Mouses and Means");
		controller.setBookAuthor("Jim Beckstein");
		controller.setBookNumOfPages(200);
	}
}