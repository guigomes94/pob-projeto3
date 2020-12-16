package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import facade.Facade;
import gui.listeners.DataChangeListener;
import gui.utils.Alerts;
import gui.utils.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Book;

public class BookListController implements Initializable, DataChangeListener {

	private ObservableList<Book> obsList;

	@FXML
	private TableView<Book> tableViewBook;

	@FXML
	private TableColumn<Book, String> tableColumnTitle;
	
	@FXML
	private TableColumn<Book, String> tableColumnAuthor;
	
	@FXML
	private TableColumn<Book, String> tableColumnGenre;

	@FXML
	private TableColumn<Book, String> tableColumnPages;
	
	@FXML
	private TableColumn<Book, String> tableColumnAvailable;

	@FXML
	private TableColumn<Book, Book> tableColumnEdit;

	@FXML
	private TableColumn<Book, Book> tableColumnRemove;

	@FXML
	private Button btnNew;

	@FXML
	public void onBtnNewAction(ActionEvent e) {
		Stage parentStage = Utils.currentStage(e);
		var obj = new Book();
		createModalForm(obj, "/gui/BookForm.fxml", parentStage);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		iniatializeNodes();
	}

	private void iniatializeNodes() {
		tableColumnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
		tableColumnPages.setCellValueFactory(new PropertyValueFactory<>("pages"));
		tableColumnAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
		tableColumnGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
		tableColumnAvailable.setCellValueFactory(new PropertyValueFactory<>("available"));

		var stage = (Stage) Main.getMainScene().getWindow();
		tableViewBook.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {

		Facade.inicializar();
		List<Book> list = Facade.listBooks();
		obsList = FXCollections.observableArrayList(list);
		tableViewBook.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
		Facade.finalizar();
	}

	private void createModalForm(Book obj, String url, Stage parentStage) {

		try {

			var loader = new FXMLLoader(getClass().getResource(url));
			Pane pane = loader.load();

			BookFormController controller = loader.getController();
			controller.setBook(obj);
			controller.loadAssociatedObjects();
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			var modalStage = new Stage();
			modalStage.setTitle("Enter Book data");
			modalStage.setScene(new Scene(pane));
			modalStage.setResizable(false);
			modalStage.initOwner(parentStage);
			modalStage.initModality(Modality.WINDOW_MODAL);
			modalStage.showAndWait();

		} catch (IOException e) {
			System.out.println(e.getMessage());
			Alerts.showAlert("IO Exception", null, e.getMessage(), AlertType.ERROR);
		}

	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	private void initEditButtons() {
		tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEdit.setCellFactory(param -> new TableCell<Book, Book>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Book obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createModalForm(obj, "/gui/BookForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemove.setCellFactory(param -> new TableCell<Book, Book>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Book obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Book obj) {

		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");

		if (result.get() == ButtonType.OK) {

			try {
				Facade.inicializar();
				Facade.removeBook(obj.getTitle());
				updateTableView();
			} catch (Exception e) {
				Alerts.showAlert("Database Error", null, e.getMessage(), AlertType.ERROR);
			} finally {
				Facade.finalizar();
			}
		}

	}

}
