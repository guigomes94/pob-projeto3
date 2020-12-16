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
import model.User;

public class UserListController implements Initializable, DataChangeListener {

	private ObservableList<User> obsList;

	@FXML
	private TableView<User> tableViewUser;

	@FXML
	private TableColumn<User, String> tableColumnName;

	@FXML
	private TableColumn<User, String> tableColumnEmail;

	@FXML
	private TableColumn<User, String> tableColumnPhone;

	@FXML
	private TableColumn<User, User> tableColumnEdit;

	@FXML
	private TableColumn<User, User> tableColumnRemove;

	@FXML
	private Button btnNew;

	@FXML
	public void onBtnNewAction(ActionEvent e) {
		Stage parentStage = Utils.currentStage(e);
		var obj = new User();
		createModalForm(obj, "/gui/UserForm.fxml", parentStage);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		iniatializeNodes();
	}

	private void iniatializeNodes() {
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

		var stage = (Stage) Main.getMainScene().getWindow();
		tableViewUser.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {

		Facade.inicializar();
		List<User> list = Facade.listUsers();
		obsList = FXCollections.observableArrayList(list);
		tableViewUser.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
		Facade.finalizar();
	}

	private void createModalForm(User obj, String url, Stage parentStage) {

		try {

			var loader = new FXMLLoader(getClass().getResource(url));
			Pane pane = loader.load();

			UserFormController controller = loader.getController();
			controller.setUser(obj);
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			var modalStage = new Stage();
			modalStage.setTitle("Enter User data");
			modalStage.setScene(new Scene(pane));
			modalStage.setResizable(false);
			modalStage.initOwner(parentStage);
			modalStage.initModality(Modality.WINDOW_MODAL);
			modalStage.showAndWait();

		} catch (IOException e) {
			Alerts.showAlert("IO Exception", null, e.getMessage(), AlertType.ERROR);
		}

	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	private void initEditButtons() {
		tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEdit.setCellFactory(param -> new TableCell<User, User>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(User obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createModalForm(obj, "/gui/UserForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemove.setCellFactory(param -> new TableCell<User, User>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(User obj, boolean empty) {
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

	private void removeEntity(User obj) {

		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");

		if (result.get() == ButtonType.OK) {

			try {
				Facade.inicializar();
				Facade.removeUser(obj.getName());
				updateTableView();
			} catch (Exception e) {
				Alerts.showAlert("Database Error", null, e.getMessage(), AlertType.ERROR);
			} finally {
				Facade.finalizar();
			}
		}

	}

}
