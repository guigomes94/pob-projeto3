package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import facade.Facade;
import gui.listeners.DataChangeListener;
import gui.utils.Alerts;
import gui.utils.Constraints;
import gui.utils.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Callback;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.Author;
import model.Book;
import model.Genre;

public class BookFormController implements Initializable {

	private Book entity;
	
	private ObservableList<Author> obsAuthorList;
	
	private ObservableList<Genre> obsGenreList;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtTitle;

	@FXML
	private TextField txtPages;
	
	@FXML
	private ComboBox<Author> comboBoxAuthor;
	
	@FXML
	private ComboBox<Genre> comboBoxGenre;
	

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
			Facade.saveOrUpdateBook(entity.getTitle(), entity.getPages(), entity.getGenre(), entity.getAuthor());
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

	private Book getFormData() {
		var obj = new Book();

		obj.setTitle(txtTitle.getText());

		obj.setPages(Utils.tryParseToInt(txtPages.getText()));
		
		obj.setGenre(comboBoxGenre.getValue());
		
		obj.setAuthor(comboBoxAuthor.getValue());

		return obj;
	}

	@FXML
	public void onBtnCancelAction(ActionEvent event) {
		Facade.finalizar();
		Utils.currentStage(event).close();
	}

	public void setBook(Book entity) {
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
		Constraints.setTextFieldMaxLength(txtTitle, 128);
		Constraints.setTextFieldInteger(txtPages);
		
		initializeComboBoxAuthor();
		initializeComboBoxGenre();
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}

		Locale.setDefault(Locale.US);
		txtTitle.setText(entity.getTitle());
		txtPages.setText(String.format("%d", entity.getPages()));
		
		if (entity.getAuthor() == null) {
			comboBoxAuthor.getSelectionModel().selectFirst();
		} else {
			comboBoxAuthor.setValue(entity.getAuthor());	
		}
		
		if (entity.getGenre() == null) {
			comboBoxGenre.getSelectionModel().selectFirst();
		} else {
			comboBoxGenre.setValue(entity.getGenre());	
		}

	}
	
	public void loadAssociatedObjects() {
		Facade.inicializar();
		List<Author> authorList = Facade.listAuthors();
		List<Genre> genreList = Facade.listGenres();
		
		obsAuthorList = FXCollections.observableArrayList(authorList);
		comboBoxAuthor.setItems(obsAuthorList);
		
		obsGenreList = FXCollections.observableArrayList(genreList);
		comboBoxGenre.setItems(obsGenreList);
	}

	private void initializeComboBoxAuthor() {
		Callback<ListView<Author>, ListCell<Author>> factory = lv -> new ListCell<Author>() {
			@Override
			protected void updateItem(Author item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxAuthor.setCellFactory(factory);
		comboBoxAuthor.setButtonCell(factory.call(null));
	}
	
	private void initializeComboBoxGenre() {
		Callback<ListView<Genre>, ListCell<Genre>> factory = lv -> new ListCell<Genre>() {
			@Override
			protected void updateItem(Genre item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxGenre.setCellFactory(factory);
		comboBoxGenre.setButtonCell(factory.call(null));
	}

}
