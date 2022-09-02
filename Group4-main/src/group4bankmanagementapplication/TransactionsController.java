/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group4bankmanagementapplication;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.sql.SQLException;
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
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author EBEN
 */
public class TransactionsController implements Initializable {

    @FXML
    private Button trnsBtnGoBack;
    @FXML
    private Label trnsLblStatus1;
    @FXML
    private TextField trnsTxtWithdrawalAmount;
    @FXML
    private Button trnsBtnWithdrawal;
    @FXML
    private TextField trnsTxtTransferAmount;
    @FXML
    private Button trnsBtnTransfer;
    @FXML
    private ComboBox withdrawFrom;
    @FXML
    private PasswordField withdrwPin;
    @FXML
    private ComboBox transferFrom;
    @FXML
    private ComboBox transferTo;
    @FXML
    private PasswordField transferPin;

    public static boolean bankConnected, momoConnected;
    public static String user, accountNo, momoNumber;
    Double momoBalance, bankBalance = 0.0;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> list = FXCollections.observableArrayList("Bank", "MoMo");
        transferFrom.setItems(list);
        transferTo.setItems(list);
        withdrawFrom.setItems(list);
    }

    @FXML
    private void trnsBtnGoBackHandler(ActionEvent event) throws SQLException, IOException {
        FXMLLoader loader = null;
        loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
        Parent root = loader.load();
        DashboardController dashboard = loader.getController();
        dashboard.display(user);
        Stage stage = (Stage) trnsBtnGoBack.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Dashboard");
        stage.show();
    }

    @FXML
    private void trnsBtnWithdrawalHandler(ActionEvent event) throws SQLException {
        Double amount = Double.parseDouble(trnsTxtWithdrawalAmount.getText());
        String from = withdrawFrom.getSelectionModel().getSelectedItem().toString();
        String pin = withdrwPin.getText();
        String bankPin = null;
        String momoPin = null;
        if (from.equals("MoMo")) {
            if (!momoConnected) {
                trnsLblStatus1.setText("You have not connected your MoMo Wallet yet");
                trnsLblStatus1.setTextFill(Color.BLUE);
                trnsLblStatus1.setVisible(true);
            } else {
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/g4tb", "root", "");
                String insertquery = "select * from `wallet` where momoNumber='" + momoNumber + "'";
                Statement statement = conn.createStatement();
                ResultSet result = statement.executeQuery(insertquery);
                System.out.println("connected");
                System.out.println(result.next());
                while (result.next()) {
                    momoBalance = Double.parseDouble(result.getString("momoBalance"));
                    momoPin = result.getString("momoPin");
                    break;
                }
                if (!pin.equals(momoPin)) {
                    trnsLblStatus1.setText("Invalid Pin, use the secret code assoctiated with your " + momoNumber + " " + from + " account");
                    trnsLblStatus1.setTextFill(Color.BLUE);
                    trnsLblStatus1.setVisible(true);
                    String query = "Insert into transactionshistory (`username`,`category`,`fromm`,`too`,`amount`,`status`) Values(?,?,?,?,?,?)";
                    try (PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setString(1, user);
                        stmt.setString(2, "Withdrawal");
                        stmt.setString(3, from);
                        stmt.setString(4, "-");
                        stmt.setString(5, amount.toString());
                        stmt.setString(6, "Failed");
                        stmt.executeUpdate();
                    }
                    return;
                }

                if (amount > momoBalance) {
                    trnsLblStatus1.setText("Your MoMo balance is low");
                    trnsLblStatus1.setTextFill(Color.BLUE);
                    trnsLblStatus1.setVisible(true);
                    String query = "Insert into transactionshistory (`username`,`category`,`fromm`,`too`,`amount`,`status`) Values(?,?,?,?,?,?)";
                    try (PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setString(1, user);
                        stmt.setString(2, "Withdrawal");
                        stmt.setString(3, from);
                        stmt.setString(4, "-");
                        stmt.setString(5, amount.toString());
                        stmt.setString(6, "Failed");
                        stmt.executeUpdate();
                    }
                } else {
                    Double newBalance = momoBalance - amount;
                    try {
                        insertquery = "UPDATE `wallet` set `momoBalance`='" + newBalance + "' WHERE momoNumber='" + momoNumber + "'";
                        statement.executeUpdate(insertquery);
                        trnsLblStatus1.setText("You have succesfully withdrawn GHC" + amount + " from " + from + " account");
                        trnsLblStatus1.setTextFill(Color.GREEN);
                        trnsLblStatus1.setVisible(true);
                        String query = "Insert into transactionshistory (`username`,`category`,`fromm`,`too`,`amount`,`status`) Values(?,?,?,?,?,?)";
                    try (PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setString(1, user);
                        stmt.setString(2, "Withdrawal");
                        stmt.setString(3, from);
                        stmt.setString(4, "-");
                        stmt.setString(5, amount.toString());
                        stmt.setString(6, "Successful");
                        stmt.executeUpdate();
                    }
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                    }

                }
            }
        } else {
            System.out.println("Bank connection status" + bankConnected);
            if (!bankConnected) {
                trnsLblStatus1.setText("You have not connected your Bank yet");
                trnsLblStatus1.setTextFill(Color.BLUE);
                trnsLblStatus1.setVisible(true);
            } else {
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/g4tb", "root", "");
                String insertquery = "select * from `wallet` where accountNumber='" + accountNo + "'";
                Statement statement = conn.createStatement();
                ResultSet result = statement.executeQuery(insertquery);

                while (result.next()) {
                    bankBalance = Double.parseDouble(result.getString("bankBalance"));
                    bankPin = result.getString("bankPin");
                    break;
                }
                if (!pin.equals(bankPin)) {
                    trnsLblStatus1.setText("Invalid Pin, use the secret code assoctiated with your " + accountNo + " " + from + " account");
                    trnsLblStatus1.setTextFill(Color.BLUE);
                    trnsLblStatus1.setVisible(true);
                    String query = "Insert into transactionshistory (`username`,`category`,`fromm`,`too`,`amount`,`status`) Values(?,?,?,?,?,?)";
                    try (PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setString(1, user);
                        stmt.setString(2, "Withdrawal");
                        stmt.setString(3, from);
                        stmt.setString(4, "-");
                        stmt.setString(5, amount.toString());
                        stmt.setString(6, "Failed");
                        stmt.executeUpdate();
                    }
                    return;
                }

                if (amount > bankBalance) {
                    trnsLblStatus1.setText("Your bank balance is low");
                    trnsLblStatus1.setTextFill(Color.BLUE);
                    trnsLblStatus1.setVisible(true);
                    String query = "Insert into transactionshistory (`username`,`category`,`fromm`,`too`,`amount`,`status`) Values(?,?,?,?,?,?)";
                    try (PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setString(1, user);
                        stmt.setString(2, "Withdrawal");
                        stmt.setString(3, from);
                        stmt.setString(4, "-");
                        stmt.setString(5, amount.toString());
                        stmt.setString(6, "Failed");
                        stmt.executeUpdate();
                    }
                } else {
                    Double newBalance = bankBalance - amount;
                    try {
                        insertquery = "UPDATE `wallet` set `bankBalance`='" + newBalance + "' WHERE accountNumber='" + accountNo + "'";
                        statement.executeUpdate(insertquery);
                        trnsLblStatus1.setText("You have succesfully withdrawn GHC" + amount + " from " + from + "account");
                        trnsLblStatus1.setTextFill(Color.GREEN);
                        trnsLblStatus1.setVisible(true);
                        String query = "Insert into transactionshistory (`username`,`category`,`fromm`,`too`,`amount`,`status`) Values(?,?,?,?,?,?)";
                    try (PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setString(1, user);
                        stmt.setString(2, "Withdrawal");
                        stmt.setString(3, from);
                        stmt.setString(4, "-");
                        stmt.setString(5, amount.toString());
                        stmt.setString(6, "Successful");
                        stmt.executeUpdate();
                    }
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                    }

                }
            }

        }
    }

    @FXML
    private void TransferHandler(ActionEvent event) throws SQLException {
        Double amount = Double.parseDouble(trnsTxtTransferAmount.getText());
        String from = transferFrom.getSelectionModel().getSelectedItem().toString();
        String To = transferTo.getSelectionModel().getSelectedItem().toString();
        String pin = transferPin.getText();
        String bankPin = null;
        String momoPin = null;
        if (from.equals(To)) {
            trnsLblStatus1.setText("You cannot transfer to same wallet");
            trnsLblStatus1.setTextFill(Color.BLUE);
            trnsLblStatus1.setVisible(true);
            return;
        }
        if (from.equals("MoMo")) {
            System.out.println("Transferring from MOMO");
            System.out.println("Momo connected? " + momoConnected);
            System.out.println("Bankconnected? " + bankConnected);
            if (!momoConnected || !bankConnected) {
                System.out.println("Momo or bank not connected");
                trnsLblStatus1.setText("You have not connected your MoMo or Bank Wallet yet");
                trnsLblStatus1.setTextFill(Color.BLUE);
                trnsLblStatus1.setVisible(true);
            } else {
                System.out.println("Momo and Bank connected. proceed with transactions");
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/g4tb", "root", "");
                String insertquery = "select * from `wallet` where momoNumber='" + momoNumber + "'";
                Statement statement = conn.createStatement();
                ResultSet result = statement.executeQuery(insertquery);
                while (result.next()) {
                    momoBalance = Double.parseDouble(result.getString("momoBalance"));
                    momoPin = result.getString("momoPin");
                    bankBalance = Double.parseDouble(result.getString("bankBalance"));
                    break;
                }
                System.out.println("MoMo pin in database is " + momoPin);
                System.out.println("Momo pin provided by user is " + pin);
                if (!(pin.equals(momoPin))) {
                    trnsLblStatus1.setText("Invalid Pin, use the secret code assoctiated with your " + momoNumber + " " + from + " account");
                    trnsLblStatus1.setTextFill(Color.BLUE);
                    trnsLblStatus1.setVisible(true);
                    String query = "Insert into transactionshistory (`username`,`category`,`fromm`,`too`,`amount`,`status`) Values(?,?,?,?,?,?)";
                    try (PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setString(1, user);
                        stmt.setString(2, "Transfer");
                        stmt.setString(3, from);
                        stmt.setString(4, To);
                        stmt.setString(5, amount.toString());
                        stmt.setString(6, "Failed");
                        stmt.executeUpdate();
                    }
                    return;
                }
                if (amount > momoBalance) {
                    trnsLblStatus1.setText("Your balance is insufficient");
                    trnsLblStatus1.setTextFill(Color.BLUE);
                    trnsLblStatus1.setVisible(true);
                    String query = "Insert into transactionshistory (`username`,`category`,`fromm`,`too`,`amount`,`status`) Values(?,?,?,?,?,?)";
                    try (PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setString(1, user);
                        stmt.setString(2, "Transfer");
                        stmt.setString(3, from);
                        stmt.setString(4, To);
                        stmt.setString(5, amount.toString());
                        stmt.setString(6, "Failed");
                        stmt.executeUpdate();
                    }
                } else {
                    Double newBalance = momoBalance - amount;
                    bankBalance += amount;
                    try {
                        insertquery = "UPDATE `wallet` set `momoBalance`='" + newBalance + "',`bankBalance`='" + bankBalance + "' WHERE momoNumber='" + momoNumber + "'";
                        statement.executeUpdate(insertquery);
                        trnsLblStatus1.setText("You have succesfully transferred GHC" + amount + " from " + from + " account");
                        trnsLblStatus1.setTextFill(Color.GREEN);
                        trnsLblStatus1.setVisible(true);
                        String query = "Insert into transactionshistory (`username`,`category`,`fromm`,`too`,`amount`,`status`) Values(?,?,?,?,?,?)";
                        try (PreparedStatement stmt = conn.prepareStatement(query)) {
                            stmt.setString(1, user);
                            stmt.setString(2, "Transfer");
                            stmt.setString(3, from);
                            stmt.setString(4, To);
                            stmt.setString(5, amount.toString());
                            stmt.setString(6, "Succesful");
                            stmt.executeUpdate();
                        }
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                        String query = "Insert into transactionshistory (`username`,`category`,`fromm`,`too`,`amount`,`status`) Values(?,?,?,?,?,?)";
                        try (PreparedStatement stmt = conn.prepareStatement(query)) {
                            stmt.setString(1, user);
                            stmt.setString(2, "Transfer");
                            stmt.setString(3, from);
                            stmt.setString(4, To);
                            stmt.setString(5, amount.toString());
                            stmt.setString(6, "Failed");
                            stmt.executeUpdate();
                        }
                    }

                }
            }
        } else {
            System.out.println("Transferring from Bank");
            System.out.println("Momo connected? " + momoConnected);
            System.out.println("Bankconnected? " + bankConnected);
            if (!bankConnected || !momoConnected) {
                System.out.println("Momo or bank not connected");
                trnsLblStatus1.setText("You have not connected your Bank yet");
                trnsLblStatus1.setTextFill(Color.BLUE);
                trnsLblStatus1.setVisible(true);
            } else {
                System.out.println("Momo and Bank connected. proceed with transactions");
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/g4tb", "root", "");
                String insertquery = "select * from `wallet` where accountNumber='" + accountNo + "'";
                System.out.println("Account Number is " + accountNo);
                Statement statement = conn.createStatement();
                ResultSet result = statement.executeQuery(insertquery);

                while (result.next()) {
                    bankBalance = Double.parseDouble(result.getString("bankBalance"));
                    bankPin = result.getString("bankPin");
                    momoBalance = Double.parseDouble(result.getString("momoBalance"));
                    result.close();
                    break;
                }
                System.out.println("bank pin in database is " + bankPin);
                System.out.println("bank pin provided by user is " + pin);
                if (!pin.equals(bankPin)) {
                    trnsLblStatus1.setText("Invalid Pin, use the secret code assoctiated with your " + accountNo + " " + from + " account");
                    trnsLblStatus1.setTextFill(Color.BLUE);
                    trnsLblStatus1.setVisible(true);
                    return;
                }
                if (amount > bankBalance) {
                    trnsLblStatus1.setText("Your bank balance is low");
                    trnsLblStatus1.setTextFill(Color.BLUE);
                    trnsLblStatus1.setVisible(true);
                } else {
                    Double newBalance = bankBalance - amount;
                    momoBalance += amount;
                    try {
                        insertquery = "UPDATE `wallet` set `bankBalance`='" + newBalance + "',`momoBalance`='" + momoBalance + "' WHERE accountNumber='" + accountNo + "'";
                        statement.executeUpdate(insertquery);
                        trnsLblStatus1.setText("You have succesfully transferred GHC" + amount + " from " + from + " account");
                        trnsLblStatus1.setTextFill(Color.GREEN);
                        trnsLblStatus1.setVisible(true);
                        String query = "Insert into transactionshistory (`username`,`category`,`fromm`,`too`,`amount`,`status`) Values(?,?,?,?,?,?)";
                        try (PreparedStatement stmt = conn.prepareStatement(query)) {
                            stmt.setString(1, user);
                            stmt.setString(2, "Transfer");
                            stmt.setString(3, from);
                            stmt.setString(4, To);
                            stmt.setString(5, amount.toString());
                            stmt.setString(6, "Successful");
                            stmt.executeUpdate();
                        } catch (SQLException e) {
                            trnsLblStatus1.setText("Transaction Failed");
                            trnsLblStatus1.setTextFill(Color.BLUE);
                            trnsLblStatus1.setVisible(true);
                            System.out.println(e);
                            query = "Insert into transactionshistory (`username`,`category`,`fromm`,`too`,`amount`,`status`) Values(?,?,?,?,?,?)";
                            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                                stmt.setString(1, user);
                                stmt.setString(2, "Transfer");
                                stmt.setString(3, from);
                                stmt.setString(4, To);
                                stmt.setString(5, amount.toString());
                                stmt.setString(6, "Failed");
                                stmt.executeUpdate();
                            }
                        }
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                        String query = "Insert into transactionshistory (`username`,`category`,`fromm`,`too`,`amount`,`status`) Values(?,?,?,?,?,?)";
                        try (PreparedStatement stmt = conn.prepareStatement(query)) {
                            stmt.setString(1, user);
                            stmt.setString(2, "Transfer");
                            stmt.setString(3, from);
                            stmt.setString(4, To);
                            stmt.setString(5, amount.toString());
                            stmt.setString(6, "Failed");
                            stmt.executeUpdate();
                        }

                    }
                }

            }
        }

    }
}
