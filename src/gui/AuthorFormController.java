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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.Author;

public class AuthorFormController implements Initializable {

	private Author entity;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtName;

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
			Facade.saveOrUpdateAuthor(entity.getName());
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

	private Author getFormData() {
		var obj = new Author();

		obj.setName(txtName.getText()); // name

		return obj;
	}

	@FXML
	public void onBtnCancelAction(ActionEvent event) {
		Facade.finalizar();
		Utils.currentStage(event).close();
	}

	public void setAuthor(Author entity) {
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
		Constraints.setTextFieldMaxLength(txtName, 128);
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}

		Locale.setDefault(Locale.US);
		txtName.setText(entity.getName());

	}

}
