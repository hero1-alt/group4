/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group4bankmanagementapplication;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.TreeMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author Cool IT Help
 */
public class TableViewController implements Initializable {

    @FXML
    private Label label;
    @FXML
    private TextField filterField;
    @FXML
    private TableView<Table> tableview;

    private static TreeMap<String, String> user;

    //observalble list to store data
    private final ObservableList<Table> dataList = FXCollections.observableArrayList();
    @FXML
    private TableColumn<Table, String> cat;
    @FXML
    private TableColumn<Table, String> from;
    @FXML
    private TableColumn<Table, String> to;
    @FXML
    private TableColumn<Table, String> amount;
    @FXML
    private TableColumn<Table, String> status;
    @FXML
    private ComboBox comboFilter;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        String user = DashboardController.user;
        cat.setCellValueFactory(new PropertyValueFactory<>("category"));
        from.setCellValueFactory(new PropertyValueFactory<>("From"));
        to.setCellValueFactory(new PropertyValueFactory<>("To"));
        amount.setCellValueFactory(new PropertyValueFactory<>("Status"));
        status.setCellValueFactory(new PropertyValueFactory<>("amount"));
        System.out.println("User is " + user);
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/g4tb", "root", "");
            Statement statement = conn.createStatement();
            System.out.println("Database is Connected");

            try {
                String querry = "SELECT * FROM `transactionshistory` WHERE username='" + user + "';";
                ResultSet result = statement.executeQuery(querry);
                while (result.next()) {
                    Table row = new Table(result.getString("category"), result.getString("fromm"), result.getString("too"), result.getString("status"), result.getString("amount"));
                    dataList.addAll(row);
                }
            } catch (SQLException ex) {
                System.out.println("Problem To Show Data");
            }

        } catch (Exception e) {
            System.out.print("Database Not Connected");
        }

//        dataList.addAll(emp1, emp2, emp3, emp4);
        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Table> filteredData = new FilteredList<>(dataList, b -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(table -> {
                // If filter text is empty, display all persons.

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (table.getFrom().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches first name.
                } else if (table.getTo().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                } else if (table.getCategory().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                } else if (table.getStatus().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                } else if (String.valueOf(table.getStatus()).indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else {
                    return false; // Does not match.
                }
            });
        });

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<Table> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(tableview.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tableview.setItems(sortedData);

        ObservableList<String> list = FXCollections.observableArrayList("Transfer", "Withdrawal","Bank","MoMo","Successful","Failed");
        comboFilter.setItems(list);
    }
    
    @FXML
    private void comboFilterHandler(ActionEvent event) {
        String filter = comboFilter.getSelectionModel().getSelectedItem().toString();
        filterField.setText(filter);
    }

}
