/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group4bankmanagementapplication;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author EBEN
 */
public class DashboardController implements Initializable {

    @FXML
    private Button uiBtnTransaction;
    @FXML
    private Button uiBtnTHistory;
    @FXML
    private Button Connect;
    @FXML
    private Label bankAgent;
    @FXML
    private TextField bankBalance;
    @FXML
    private Label momoAgent;
    @FXML
    private TextField momoBalance;
    @FXML
    private Button uiTxtLogOut;
    @FXML
    private Button manageAccount;

    private Scene scene;
    private Stage stage;
    private Parent root;
    private static String connectedBank = null;
    private static String bBalance = null;
    private static String connectedMoMo = null;
    private static String mBalance = null;
    public static String user = null;
    public static String momoNumber,accountNumber;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO am coming 5mins okay boss
    }

    @FXML
    private void uiBtnTransactionHandler(ActionEvent event) throws IOException {
        stage = (Stage) uiBtnTransaction.getScene().getWindow();
        FXMLLoader loader = null;
        loader = new FXMLLoader(getClass().getResource("Transactions.fxml"));
        root = loader.load();
        TransactionsController.bankConnected = connectedBank != null;
        TransactionsController.momoConnected = connectedMoMo != null;
        TransactionsController.user = user;
        TransactionsController.momoNumber = momoNumber;
        TransactionsController.accountNo = accountNumber;
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Transactions");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void uiBtnTHistoryHandler(ActionEvent event) throws IOException {
        stage = (Stage) uiBtnTHistory.getScene().getWindow();
        @SuppressWarnings("UnusedAssignment")
        FXMLLoader loader = null;
        loader = new FXMLLoader(getClass().getResource("TableView.fxml"));
        root = loader.load();
        TableViewController transact = loader.getController();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Transaction History");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void ConnectHandler(ActionEvent event) throws IOException {
        stage = (Stage) Connect.getScene().getWindow();
        @SuppressWarnings("UnusedAssignment")
        FXMLLoader loader = null;
        loader = new FXMLLoader(getClass().getResource("Connection.fxml"));
        ConnectionController.user = user;
        ConnectionController.bankConnected = connectedBank != null;
        ConnectionController.momoConneccted = connectedMoMo != null;
        System.out.println("Bank is connected "+connectedBank);
        System.out.println("Bank is connected "+connectedMoMo);
        ConnectionController.momoNo = momoNumber;
        ConnectionController.accountNo = accountNumber;
        root = loader.load();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Connect to Wallet");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void Logout(ActionEvent event) {
    }

    @FXML
    private void manageAccountHandler(ActionEvent event) {
    }

    void display(String user) throws SQLException {
        DashboardController.user = user;
        System.out.println("Dashboard user is " + user);
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/g4tb", "root", "");
        String query = "SELECT * FROM `wallet` WHERE username='" + user + "';";
        ResultSet result;
        try (Statement stmt = conn.createStatement()) {
            result = stmt.executeQuery(query);
            while (result.next()) {
                connectedBank = result.getString("connectedBank");
                bBalance = result.getString("bankBalance");
                connectedMoMo = result.getString("connectedMoMo");
                mBalance = result.getString("momoBalance");
                momoNumber = result.getString("momoNumber");
                accountNumber = result.getString("accountNumber");
                if (!(connectedBank == null)) {
                    bankAgent.setText(connectedBank);
                    bankBalance.setText(bBalance);
                }
                if (!(connectedMoMo == null)) {
                    momoAgent.setText(connectedMoMo);
                    momoBalance.setText(mBalance);
                }
                return ;
            }
        }
        result.close();

    }

}
