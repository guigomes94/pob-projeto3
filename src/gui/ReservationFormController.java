package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import facade.Facade;
import gui.listeners.DataChangeListener;
import gui.utils.Alerts;
import gui.utils.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.Book;
import model.Reservation;
import model.User;

public class ReservationFormController implements Initializable {

	private Reservation entity;

	private ObservableList<User> obsUserList;
	
	private ObservableList<Book> obsBookList;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private DatePicker dpReservationDate;

	@FXML
	private ComboBox<User> comboBoxUser;
	
	@FXML
	private ComboBox<Book> comboBoxBook;

	@FXML
	private Button btnSave;

	@FXML
	private Button btnCancel;

	@FXML
	public void onBtnSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		try {
			entity = getFormData();
			Facade.inicializar();
			Facade.saveOrUpdateReservation(entity.getId(), entity.getLoanDate(), entity.getUser(), entity.getBook());
			Facade.finalizar();
			notifyDataChangeListerners();
			Utils.currentStage(event).close();

		} catch (Exception e) {
			Alerts.showAlert("Database Error", null, e.getMessage(), AlertType.ERROR);

		}
	}

	private void notifyDataChangeListerners() {
		dataChangeListeners.forEach(listener -> listener.onDataChanged());
	}

	private Reservation getFormData() {
		var obj = new Reservation();
		
		// obj.setId(txtId.getText());
		
		var instant = Instant.from(dpReservationDate.getValue().atStartOfDay(ZoneId.systemDefault()));
		
		obj.setLoanDate(LocalDate.ofInstant(instant, ZoneId.systemDefault()));
		
		obj.setUser(comboBoxUser.getValue());
		
		obj.setBook(comboBoxBook.getValue());

		return obj;
	}

	@FXML
	public void onBtnCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	public void setReservation(Reservation entity) {
		this.entity = entity;
	}


	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();

	}

	private void initializeNodes() {
		Utils.formatDatePicker(dpReservationDate, "dd/MM/yyyy");
		
		initializeComboBoxUser();
		initializeComboBoxBook();
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}

		Locale.setDefault(Locale.US);
		
		if (entity.getId() != null) {
			txtId.setText(entity.getId().toString());
		}
		
		if (entity.getLoanDate() != null) {
			dpReservationDate.setValue(entity.getLoanDate());
		}
		
		if (entity.getUser() == null) {
			comboBoxUser.getSelectionModel().selectFirst();
		} else {
			comboBoxUser.setValue(entity.getUser());	
		}
		
		if (entity.getBook() == null) {
			comboBoxBook.getSelectionModel().selectFirst();
		} else {
			comboBoxBook.setValue(entity.getBook());	
		}

	}

	public void loadAssociatedObjects() {
		Facade.inicializar();
		List<User> userList = Facade.listUsers();
		List<Book> bookList = Facade.listBooks();
		
		obsUserList = FXCollections.observableArrayList(userList);
		comboBoxUser.setItems(obsUserList);
		
		obsBookList = FXCollections.observableArrayList(bookList);
		comboBoxBook.setItems(obsBookList);
	}

	private void initializeComboBoxUser() {
		Callback<ListView<User>, ListCell<User>> factory = lv -> new ListCell<User>() {
			@Override
			protected void updateItem(User item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxUser.setCellFactory(factory);
		comboBoxUser.setButtonCell(factory.call(null));
	}
	
	private void initializeComboBoxBook() {
		Callback<ListView<Book>, ListCell<Book>> factory = lv -> new ListCell<Book>() {
			@Override
			protected void updateItem(Book item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getTitle());
			}
		};
		comboBoxBook.setCellFactory(factory);
		comboBoxBook.setButtonCell(factory.call(null));
	}
}
