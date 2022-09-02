/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group4bankmanagementapplication;

import static group4bankmanagementapplication.DashboardController.accountNumber;
import static group4bankmanagementapplication.DashboardController.momoNumber;
import static group4bankmanagementapplication.DashboardController.user;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author EBEN
 */
public class ConnectionController implements Initializable {

    @FXML
    private Label status;
    @FXML
    private ProgressBar progress;
    @FXML
    private Button trnsBtnGoBack;
    @FXML
    private ComboBox selectBank;
    @FXML
    private TextField bankAccountNo;
    @FXML
    private PasswordField bankPin;
    @FXML
    private ComboBox operator;
    @FXML
    private TextField momoNumber;
    @FXML
    private PasswordField momoPin;
    @FXML
    private Button toTransactions;
    @FXML
    private Button btnConnectBank;
    @FXML
    private Button btnConnectMoMo;

    public static String user = null;

    private Scene scene;
    private Stage stage;
    private Parent root;
    public static boolean momoConneccted, bankConnected;
    public static String momoNo, accountNo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> bankList = FXCollections.observableArrayList("GCB", "Ecobank", "ABSA", "Fedelity Bank", "ADB", "SG", "CBG");
        selectBank.setItems(bankList);
        ObservableList<String> momoAgentList = FXCollections.observableArrayList("MTN", "AirtelTigo", "Vodafone", "G Money");
        operator.setItems(momoAgentList);
    }

    @FXML
    private void Logout(ActionEvent event) throws IOException {
        FXMLLoader loader = null;
        loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
        root = loader.load();
        FXMLDocumentController welcomePage = loader.getController();
        welcomePage.setStatus("you're Logged out.");
        stage = (Stage) trnsBtnGoBack.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Welcome - Login");
        stage.show();
    }

    @FXML
    private void GoBackHandler(ActionEvent event) throws IOException, SQLException {
        
        FXMLLoader loader = null;
        loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
        root = loader.load();
        DashboardController dashboard = loader.getController();
        dashboard.display(user);
        stage = (Stage) trnsBtnGoBack.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Dashboard");
        stage.show();
    }

    @FXML
    private void toTransactionsHandler(ActionEvent event) throws IOException {
        System.out.println("Going to transactions");
        stage = (Stage) toTransactions.getScene().getWindow();
        FXMLLoader loader = null;
        loader = new FXMLLoader(getClass().getResource("Transactions.fxml"));
        root = loader.load();
        System.out.println("Bank Connected? "+bankConnected);
        System.out.println("Momo connected? "+momoConneccted);
        TransactionsController.momoConnected = momoConneccted;
        TransactionsController.bankConnected = bankConnected;
        TransactionsController.user = user;
        TransactionsController.momoNumber = momoNo;
        TransactionsController.accountNo = accountNumber;
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Transactions");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void connectBankHandler(ActionEvent event) throws SQLException, InterruptedException {
        System.out.println("current user" + user);
        String bankAgent = selectBank.getSelectionModel().getSelectedItem().toString();
        String accounNo = bankAccountNo.getText();
        String pin = bankPin.getText();
        Random rand = new Random();
        Integer balance = rand.nextInt(1000) + 1000;
        if (bankAgent.isEmpty()) {
            status.setText("Please select a bank");
            status.setTextFill(Color.BLUE);
            status.setVisible(true);
        } else if (accounNo.isEmpty()) {
            status.setText("Account Number is required");
            status.setTextFill(Color.BLUE);
            status.setVisible(true);
        } else if (pin.isEmpty()) {
            status.setText("Secret Pin is required. Contact " + bankAgent + " for your secret pin");
            status.setTextFill(Color.BLUE);
            status.setVisible(true);
        } else {
            System.out.println("connecting to Bank");
            status.setText("Innitilizing connection to " + bankAgent + "...");
            status.setTextFill(Color.GREEN);
            status.setVisible(true);
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/g4tb", "root", "");
            String query = "Insert into wallet (`username`,`connectedBank`,`bankBalance`,`bankPin`,`accountNumber`) Values(?,?,?,?,?)";
            Statement statement;
            try {
                statement = conn.createStatement();
                System.out.println("Database Connecion is ok");
            } catch (Exception e) {
                status.setText("Database Server is down. contact Administrator for help");
                status.setTextFill(Color.YELLOW);
                status.setVisible(true);
                System.out.println("Database Not Connected");
                System.out.println(e);
                return;
            }
            try {
                Thread.sleep(10000);
                status.setText("Authentication...");
                status.setTextFill(Color.BLUE);
                status.setVisible(true);
                String insertquery = "select username from `wallet` where username = '" + user + "' and accountNumber='" + accounNo + "'";
                ResultSet result = statement.executeQuery(insertquery);
                while (result.next()) {
                    status.setText("Account is already connected");
                    status.setTextFill(Color.BLUE);
                    status.setVisible(true);
                    result.close();
                    return;
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
            System.out.println("MoMo is connected " + momoConneccted);
            if (momoConneccted) {
                System.out.println("Momo is connectted updating bank fields");
                try {
                    String insertquery = "UPDATE `wallet` set `connectedBank`='" + bankAgent + "',`bankBalance`='" + balance.toString() + "',`bankPin`='" + pin + "',`accountNumber`='" + accounNo + "' WHERE momoNumber = '" + momoNo + "'";
                    statement.executeUpdate(insertquery);
                    System.out.println("Updated");
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
                bankConnected = true;
                Thread.sleep(10000);
                status.setText(bankAgent + " connected succesfully");
                status.setTextFill(Color.BLUE);
                status.setVisible(true);
                System.out.println("Bank fields updated");
                System.out.println("Bank connected? " + bankConnected);
            } else {
                System.out.println("Momo is not connected creating new fields in wallet");
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, user);
                    stmt.setString(2, bankAgent);
                    stmt.setString(3, balance.toString());
                    stmt.setString(4, pin);
                    stmt.setString(5, accounNo);
                    stmt.executeUpdate();
                    Thread.sleep(10000);
                    status.setText(bankAgent + " is connected successfully");
                    status.setTextFill(Color.BLUE);
                    status.setVisible(true);
                    bankConnected = true;
                    System.out.println("New fields created");
                    System.out.println("Bank connected? "+bankConnected);

                } catch (SQLException e) {
                    status.setText("Error connecting account, contact Administrator for support");
                    status.setTextFill(Color.BLUE);
                    status.setVisible(true);
                    System.out.println(e);
                }

            }
        }

    }

    @FXML
    private void connectMoMoHandler(ActionEvent event) throws SQLException, InterruptedException {

        String momoAgent = operator.getSelectionModel().getSelectedItem().toString();
        String phone = momoNumber.getText();
        String pin = momoPin.getText();
        Random rand = new Random();
        Integer balance = rand.nextInt(1000) + 1000;
        status.setText("Innitilizing connection to " + momoAgent + "...");
        status.setTextFill(Color.GREEN);
        status.setVisible(true);
        if (momoAgent.isEmpty()) {
            status.setText("Please select a network operator");
            status.setTextFill(Color.BLUE);
            status.setVisible(true);
        } else if (phone.isEmpty()) {
            status.setText("MoMo Number is required");
            status.setTextFill(Color.BLUE);
            status.setVisible(true);
        } else if (pin.isEmpty()) {
            status.setText("Your momo pin is required");
            status.setTextFill(Color.BLUE);
            status.setVisible(true);
        } else {
            System.out.println("connecting to momo");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/g4tb", "root", "");
            String query = "Insert into wallet (`username`,`connectedMoMo`,`momoBalance`,`momoPin`,`momoNumber`) Values(?,?,?,?,?)";
            Statement statement;
            try {
                status.setText("Establishing connection to " + momoAgent + "...");
                status.setTextFill(Color.BLUE);
                status.setVisible(true);
                Thread.sleep(5000);
                statement = conn.createStatement();
                System.out.println("Database Connection is ok");
            } catch (InterruptedException | SQLException e) {
                status.setText("Database Server is down. contact Administrator for help");
                status.setTextFill(Color.YELLOW);
                status.setVisible(true);
                System.out.println("Database Not Connected");
                System.out.println(e);
                return;
            }
            System.out.println("Bank is connected " + bankConnected);
            if (bankConnected) {
                System.out.println("Bank is connected, updating momo fielsds in database");
                try {
                    String insertquery = "UPDATE `wallet` set `connectedMoMo`='" + momoAgent + "',`momoBalance`='" + balance.toString() + "',`momoPin`='" + pin + "',`momoNumber`='" + phone + "' WHERE accountNumber = '" + accountNo + "'";
                    statement.executeUpdate(insertquery);
                    System.out.println("Updated");
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
                momoConneccted = true;
                Thread.sleep(10000);
                status.setText(momoAgent + " is connected successfully");
                status.setTextFill(Color.BLUE);
                status.setVisible(true);
                System.out.println("Momo Fields updated");
                System.out.println("Momo connected? " + momoConneccted);
            } else {
                System.out.println("Bank is not connectected, Creating new fiealds in database");
                try {
                    status.setText("Connecting to " + momoAgent + " Authenticationg...");
                    status.setTextFill(Color.BLUE);
                    status.setVisible(true);
                    Thread.sleep(4000);
                    String insertquery = "select username from `wallet` where username = '" + user + "' and connectedMoMo='" + momoNumber + "'";
                    ResultSet result = statement.executeQuery(insertquery);
                    while (result.next()) {
                        status.setText("Account is already connected");
                        status.setTextFill(Color.BLUE);
                        status.setVisible(true);
                        result.close();
                        return;
                    }
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, user);
                    stmt.setString(2, momoAgent);
                    stmt.setString(3, balance.toString());
                    stmt.setString(4, pin);
                    stmt.setString(5, phone);
                    stmt.executeUpdate();
                    momoConneccted = true;
                    status.setText(momoAgent + " Connected Succesfully");
                    status.setTextFill(Color.BLUE);
                    status.setVisible(true);
                    System.out.println("New fields created in wallet, momo connected");
                    System.out.println("Momo connected? " + momoConneccted);
                } catch (SQLException e) {
                    status.setText("Error connecting account, contact Administrator for support");
                    status.setTextFill(Color.BLUE);
                    status.setVisible(true);
                    System.out.println(e);
                }
            }
        }
    }

    static void setUser(String user) {
        ConnectionController.user = user;
    }

}
