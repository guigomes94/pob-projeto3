package facade;

import java.time.LocalDate;
import java.util.List;

import dao.DAO;
import dao.DAOAuthor;
import dao.DAOBook;
import dao.DAOGenre;
import dao.DAOLoan;
import dao.DAOReservation;
import dao.DAOUser;
import gui.utils.Utils;
import model.Author;
import model.Book;
import model.Genre;
import model.Loan;
import model.Reservation;
import model.User;

public class Facade {

	private static DAOAuthor daoAuthor = new DAOAuthor();
	private static DAOBook daoBook = new DAOBook();
	private static DAOGenre daoGenre = new DAOGenre();
	private static DAOLoan daoLoan = new DAOLoan();
	private static DAOReservation daoReservation = new DAOReservation();
	private static DAOUser daoUser = new DAOUser();

	public static void inicializar() {
		DAO.open();
	}

	public static void finalizar() {
		DAO.close();
	}

	/* =====AUTHOR===== */

	public static Author saveOrUpdateAuthor(String name) throws Exception {
		DAO.begin();
		Author result = daoAuthor.read(name);
		if (result != null) {
			result.setName(name);
			daoAuthor.update(result);
			DAO.commit();
			return result;

		} else {
			Author author = new Author(name);
			daoAuthor.create(author);
			DAO.commit();
			return author;
		}

	}

	public static void removeAuthor(String authorName) throws Exception {
		DAO.begin();
		Author result = daoAuthor.read(authorName);
		if (result == null) {
			DAO.rollback();
			throw new Exception("Author " + authorName + " not exist!");
		}

		daoAuthor.delete(result);
		DAO.commit();
	}

	public static List<Author> listAuthors() {
		List<Author> list = daoAuthor.readAll();
		return list;
	}

	/* =====BOOK===== */

	public static Book saveOrUpdateBook(String title, Integer pages, Genre genre, Author author) throws Exception {
		DAO.begin();
		Book result = daoBook.read(title);
		if (result != null) {
			result.setPages(pages);
			result.setGenre(genre);
			result.setAuthor(author);
			daoBook.update(result);
			DAO.commit();
			return result;

		} else {
			Book book = new Book(title, pages, genre, author, true);
			daoBook.create(book);
			DAO.commit();
			return book;
		}
	}

	public static void removeBook(String bookTitle) throws Exception {
		DAO.begin();
		Book result = daoBook.read(bookTitle);
		if (result == null) {
			DAO.rollback();
			throw new Exception("Book " + bookTitle + " not exist!");
		}

		daoBook.delete(result);
		DAO.commit();
	}

	public static List<Book> listBooks() {
		List<Book> list = daoBook.readAll();
		return list;
	}

	public static List<Book> listBooksAvailable() {
		List<Book> list = daoBook.readBooksAvailable();
		return list;
	}

	public static List<Book> searchBooksByTitle(String caracteres) {
		List<Book> list = daoBook.readBooksByTitle(caracteres);
		return list;
	}

	public static List<Book> searchBooksByAuthor(String caracteres) {
		List<Book> list = daoBook.readBooksByAuthor(caracteres);
		return list;
	}

	public static List<Book> searchBooksByGenre(String caracteres) {
		List<Book> list = daoBook.readBooksByGenre(caracteres);
		return list;
	}

	/* =====GENRE===== */

	public static Genre saveOrUpdateGenre(String name) throws Exception {
		DAO.begin();
		Genre result = daoGenre.read(name);
		if (result != null) {
			result.setName(name);
			daoGenre.update(result);
			DAO.commit();
			return result;

		} else {
			Genre genre = new Genre(name);
			daoGenre.create(genre);
			DAO.commit();
			return genre;
		}
	}

	public static void removeGenre(String genreName) throws Exception {
		DAO.begin();
		Genre result = daoGenre.read(genreName);
		if (result == null) {
			DAO.rollback();
			throw new Exception("Genre " + genreName + " not exist!");
		}

		daoGenre.delete(result);
		DAO.commit();
	}

	public static List<Genre> listGenres() {
		List<Genre> list = daoGenre.readAll();
		return list;
	}

	/* =====LOAN===== */

	public static Loan saveOrUpdateLoan(String id, LocalDate loanDate, User user, Book book, LocalDate devolutionDate)
			throws Exception {
		DAO.begin();
		Loan result = daoLoan.read(id);
		Book findBook = daoBook.read(book.getTitle());
		if (result != null) {
			result.setDevolutionDate(devolutionDate);
			result.setLoanValue(Utils.calcValue(loanDate, devolutionDate));
			findBook.setAvailable(true);
			daoBook.update(findBook);
			daoLoan.update(result);
			DAO.commit();
			return result;

		} else {
			Loan loan = new Loan(loanDate, user, book, devolutionDate);
			loan.setLoanValue(Utils.calcValue(loanDate, devolutionDate));
			findBook.setAvailable(false);
			daoBook.update(findBook);
			daoLoan.create(loan);
			DAO.commit();
			return loan;
		}

	}

	public static List<Loan> listLoans() {
		List<Loan> list = daoLoan.readAll();
		return list;
	}
	
	public static Loan searchLoanById(String id) {
		Loan l = daoLoan.readLoanById(id);
		return l;
	}

	public static List<Loan> listLastLoans() {
		List<Loan> list = daoLoan.readLastLoans();
		return list;
	}

	public static List<Loan> listNextDevolutions() {
		List<Loan> list = daoLoan.readNextDevolutions();
		return list;
	}	 

	/* =====RESERVATION===== */

	public static Reservation saveOrUpdateReservation(String id, LocalDate reservationDate, User user, Book book)
			throws Exception {
		DAO.begin();
		Reservation result = daoReservation.read(id);
		if (result != null) {
			result.setLoanDate(reservationDate);
			result.setUser(user);
			result.setBook(book);
			daoReservation.update(result);
			DAO.commit();
			return result;

		} else {
			Reservation res = new Reservation(reservationDate, user, book);
			daoReservation.create(res);
			DAO.commit();
			return res;
		}

	}

	public static void removeReservation(String id) throws Exception {
		DAO.begin();
		Reservation result = daoReservation.read(id);
		if (result == null) {
			DAO.rollback();
			throw new Exception("ID NOT FOUND!");
		}

		daoReservation.delete(result);
		DAO.commit();
	}

	public static List<Reservation> listReservations() {
		List<Reservation> list = daoReservation.readAll();
		return list;
	}
	  
	public static Reservation searchReservationById(String id) {
		Reservation l = daoReservation.readReservationById(id);
		return l;
	}
	 

	/* =====USER===== */

	public static User saveOrUpdateUser(String name, String email, String phone) throws Exception {
		DAO.begin();
		User result = daoUser.read(name);
		if (result != null) {
			result.setEmail(email);
			result.setPhone(phone);
			daoUser.update(result);
			DAO.commit();
			return result;

		} else {
			User u = new User(name, email, phone);
			daoUser.create(u);
			DAO.commit();
			return u;
		}

	}

	public static void removeUser(String userName) throws Exception {
		DAO.begin();
		User result = daoUser.read(userName);
		if (result == null) {
			DAO.rollback();
			throw new Exception("User " + userName + " not exist!");
		}

		daoUser.delete(result);
		DAO.commit();
	}

	public static List<User> listUsers() {
		List<User> list = daoUser.readAll();
		return list;
	}

	public static List<User> searchUsersByName(String name) {
		List<User> list = daoUser.readUsersByName(name);
		return list;
	}

	public static List<User> searchUsersByEmail(String email) {
		List<User> list = daoUser.readUsersByEmail(email);
		return list;
	}

}
