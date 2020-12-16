package gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Loan;

public class LoanListController implements Initializable, DataChangeListener {

	private ObservableList<Loan> obsList;

	@FXML
	private TableView<Loan> tableViewLoan;

	@FXML
	private TableColumn<Loan, String> tableColumnId;
	
	@FXML
	private TableColumn<Loan, LocalDate> tableColumnLoanDate;
	
	@FXML
	private TableColumn<Loan, String> tableColumnUser;

	@FXML
	private TableColumn<Loan, String> tableColumnBook;
	
	@FXML
	private TableColumn<Loan, LocalDate> tableColumnDevolutionDate;
	
	@FXML
	private TableColumn<Loan, Double> tableColumnValue;

	@FXML
	private TableColumn<Loan, Loan> tableColumnDevolution;

	@FXML
	private Button btnNew;

	@FXML
	public void onBtnNewAction(ActionEvent e) {
		Stage parentStage = Utils.currentStage(e);
		var obj = new Loan();
		createModalForm(obj, "/gui/LoanForm.fxml", parentStage);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		iniatializeNodes();
	}

	private void iniatializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnLoanDate.setCellValueFactory(new PropertyValueFactory<>("loanDate"));
		Utils.formatTableColumnDate(tableColumnLoanDate, "dd/MM/yyyy");
		tableColumnUser.setCellValueFactory(new PropertyValueFactory<>("userName"));
		tableColumnBook.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
		tableColumnDevolutionDate.setCellValueFactory(new PropertyValueFactory<>("devolutionDate"));
		Utils.formatTableColumnDate(tableColumnDevolutionDate, "dd/MM/yyyy");
		tableColumnValue.setCellValueFactory(new PropertyValueFactory<>("loanValue"));
		Utils.formatTableColumnDouble(tableColumnValue, 2);

		var stage = (Stage) Main.getMainScene().getWindow();
		tableViewLoan.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {

		Facade.inicializar();
		List<Loan> list = Facade.listLoans();
		obsList = FXCollections.observableArrayList(list);
		tableViewLoan.setItems(obsList);
		initDevolutionButtons();
		Facade.finalizar();
	}

	private void createModalForm(Loan obj, String url, Stage parentStage) {

		try {

			var loader = new FXMLLoader(getClass().getResource(url));
			Pane pane = loader.load();

			LoanFormController controller = loader.getController();
			controller.setLoan(obj);
			controller.loadAssociatedObjects();
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			var modalStage = new Stage();
			modalStage.setTitle("Enter Loan data");
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

	private void initDevolutionButtons() {
		tableColumnDevolution.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnDevolution.setCellFactory(param -> new TableCell<Loan, Loan>() {
			private final Button button = new Button("devolution");

			@Override
			protected void updateItem(Loan obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createModalForm(obj, "/gui/LoanForm.fxml", Utils.currentStage(event)));
			}
		});
	}


}
