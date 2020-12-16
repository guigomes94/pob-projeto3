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
import model.Author;

public class AuthorListController implements Initializable, DataChangeListener {

	private ObservableList<Author> obsList;

	@FXML
	private TableView<Author> tableViewAuthor;

	@FXML
	private TableColumn<Author, String> tableColumnName;

	@FXML
	private TableColumn<Author, Author> tableColumnEdit;

	@FXML
	private TableColumn<Author, Author> tableColumnRemove;

	@FXML
	private Button btnNew;

	@FXML
	public void onBtnNewAction(ActionEvent e) {
		Stage parentStage = Utils.currentStage(e);
		var obj = new Author();
		createModalForm(obj, "/gui/AuthorForm.fxml", parentStage);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		iniatializeNodes();
	}

	private void iniatializeNodes() {
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));

		var stage = (Stage) Main.getMainScene().getWindow();
		tableViewAuthor.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {

		Facade.inicializar();
		List<Author> list = Facade.listAuthors();
		obsList = FXCollections.observableArrayList(list);
		tableViewAuthor.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
		Facade.finalizar();
	}

	private void createModalForm(Author obj, String url, Stage parentStage) {

		try {

			var loader = new FXMLLoader(getClass().getResource(url));
			Pane pane = loader.load();

			AuthorFormController controller = loader.getController();
			controller.setAuthor(obj);
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			var modalStage = new Stage();
			modalStage.setTitle("Enter Author data");
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
		tableColumnEdit.setCellFactory(param -> new TableCell<Author, Author>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Author obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> Alerts.showAlert("Error", null, "Operation Not Allowed!", AlertType.ERROR));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemove.setCellFactory(param -> new TableCell<Author, Author>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Author obj, boolean empty) {
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

	private void removeEntity(Author obj) {

		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");

		if (result.get() == ButtonType.OK) {

			try {
				Facade.inicializar();
				Facade.removeAuthor(obj.getName());
				updateTableView();
			} catch (Exception e) {
				Alerts.showAlert("Database Error", null, e.getMessage(), AlertType.ERROR);
			} finally {
				Facade.finalizar();
			}
		}

	}

}
