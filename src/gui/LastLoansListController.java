package gui;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import facade.Facade;
import gui.listeners.DataChangeListener;
import gui.utils.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Loan;

public class LastLoansListController implements Initializable, DataChangeListener {

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
		List<Loan> list = Facade.listLastLoans();
		obsList = FXCollections.observableArrayList(list);
		tableViewLoan.setItems(obsList);
		Facade.finalizar();
	}


	@Override
	public void onDataChanged() {
		updateTableView();
	}


}
