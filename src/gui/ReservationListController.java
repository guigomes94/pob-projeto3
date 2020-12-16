package gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
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
import model.Reservation;

public class ReservationListController implements Initializable, DataChangeListener {

	private ObservableList<Reservation> obsList;

	@FXML
	private TableView<Reservation> tableViewReservation;

	@FXML
	private TableColumn<Reservation, String> tableColumnId;
	
	@FXML
	private TableColumn<Reservation, LocalDate> tableColumnReservationDate;
	
	@FXML
	private TableColumn<Reservation, String> tableColumnUser;

	@FXML
	private TableColumn<Reservation, String> tableColumnBook;

	@FXML
	private TableColumn<Reservation, Reservation> tableColumnEdit;

	@FXML
	private TableColumn<Reservation, Reservation> tableColumnRemove;

	@FXML
	private Button btnNew;

	@FXML
	public void onBtnNewAction(ActionEvent e) {
		Stage parentStage = Utils.currentStage(e);
		var obj = new Reservation();
		createModalForm(obj, "/gui/ReservationForm.fxml", parentStage);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		iniatializeNodes();
	}

	private void iniatializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnReservationDate.setCellValueFactory(new PropertyValueFactory<>("loanDate"));
		Utils.formatTableColumnDate(tableColumnReservationDate, "dd/MM/yyyy");
		tableColumnUser.setCellValueFactory(new PropertyValueFactory<>("userName"));
		tableColumnBook.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));

		var stage = (Stage) Main.getMainScene().getWindow();
		tableViewReservation.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {

		Facade.inicializar();
		List<Reservation> list = Facade.listReservations();
		obsList = FXCollections.observableArrayList(list);
		tableViewReservation.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
		Facade.finalizar();
	}

	private void createModalForm(Reservation obj, String url, Stage parentStage) {

		try {

			var loader = new FXMLLoader(getClass().getResource(url));
			Pane pane = loader.load();

			ReservationFormController controller = loader.getController();
			controller.setReservation(obj);
			controller.loadAssociatedObjects();
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			var modalStage = new Stage();
			modalStage.setTitle("Enter Reservation data");
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
		tableColumnEdit.setCellFactory(param -> new TableCell<Reservation, Reservation>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Reservation obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createModalForm(obj, "/gui/ReservationForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemove.setCellFactory(param -> new TableCell<Reservation, Reservation>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Reservation obj, boolean empty) {
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

	private void removeEntity(Reservation obj) {

		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");

		if (result.get() == ButtonType.OK) {

			try {
				Facade.inicializar();
				Facade.removeReservation(obj.getId());
				updateTableView();
			} catch (Exception e) {
				Alerts.showAlert("Database Error", null, e.getMessage(), AlertType.ERROR);
			} finally {
				Facade.finalizar();
			}
		}

	}

}
