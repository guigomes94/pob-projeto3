package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.utils.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemLastLoans;

	@FXML
	private MenuItem menuItemLastReservations;

	@FXML
	private MenuItem menuItemNextDevolutions;

	@FXML
	private MenuItem menuItemLoans;

	@FXML
	private MenuItem menuItemReservations;

	@FXML
	private MenuItem menuItemUser;

	@FXML
	private MenuItem menuItemAuthor;

	@FXML
	private MenuItem menuItemGenre;

	@FXML
	private MenuItem menuItemBook;

	@FXML
	private MenuItem menuItemBooksByAuthor;

	@FXML
	private MenuItem menuItemBooksByGenre;

	@FXML
	private MenuItem menuItemBooksByTitle;

	@FXML
	private MenuItem menuItemUsersByName;

	@FXML
	private MenuItem menuItemUsersByEmail;

	@FXML
	private MenuItem menuItemUsersById;

	@FXML
	private MenuItem menuItemReservationById;

	@FXML
	private MenuItem menuItemLoanById;

	@FXML
	private MenuItem menuItemReportLoans;

	@FXML
	private MenuItem menuItemReportReservations;

	@FXML
	private MenuItem menuItemAbout;

	@FXML
	public void onMenuItemLastLoansAction() {
		loadView("/gui/LastLoansList.fxml", (LastLoansListController controller) -> {
			controller.updateTableView();
		});
	}


	@FXML
	public void onMenuItemNextDevolutionsAction() {
		loadView("/gui/NextDevolutionsList.fxml", (NextDevolutionsListController controller) -> {
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemLoanAction() {
		loadView("/gui/LoanList.fxml", (LoanListController controller) -> {
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemReservationAction() {
		loadView("/gui/ReservationList.fxml", (ReservationListController controller) -> {
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemUserAction() {
		loadView("/gui/UserList.fxml", (UserListController controller) -> {
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemAuthorAction() {
		loadView("/gui/AuthorList.fxml", (AuthorListController controller) -> {
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemGenreAction() {
		loadView("/gui/GenreList.fxml", (GenreListController controller) -> {
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemBookAction() {
		loadView("/gui/BookList.fxml", (BookListController controller) -> {
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemBookByAuthorAction() {
		System.out.println("BookByAuthor");
	}

	@FXML
	public void onMenuItemBookByGenreAction() {
		System.out.println("BookByGenre");
	}

	@FXML
	public void onMenuItemBookByTitleAction() {
		System.out.println("BookByTitle");
	}

	@FXML
	public void onMenuItemUserByNameAction() {
		System.out.println("UserByName");
	}

	@FXML
	public void onMenuItemUserByEmailAction() {
		System.out.println("UserByEmail");
	}

	@FXML
	public void onMenuItemUserByIdAction() {
		loadView("/gui/NotImplemented.fxml", x -> {
		});
	}

	@FXML
	public void onMenuItemLoanByIdAction() {
		System.out.println("Loan");
	}

	@FXML
	public void onMenuItemReservationByIdAction() {
		System.out.println("Reservation");
	}

	@FXML
	public void onMenuItemReportLoansAction() {
		loadView("/gui/NotImplemented.fxml", x -> {
		});
	}

	@FXML
	public void onMenuItemReportReservationsAction() {
		loadView("/gui/NotImplemented.fxml", x -> {
		});
	}

	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml", x -> {
		});
	}

	private synchronized <T> void loadView(String url, Consumer<T> initializingAction) {
		try {
			var loader = new FXMLLoader(getClass().getResource(url));
			VBox newVBox = loader.load();

			var mainScene = Main.getMainScene();
			var mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

			var mainMenu = mainVBox.getChildren().get(0);

			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());

			T controller = loader.getController();
			initializingAction.accept(controller);

		} catch (IOException e) {
			Alerts.showAlert("IO Exception", null, e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {

	}

}
