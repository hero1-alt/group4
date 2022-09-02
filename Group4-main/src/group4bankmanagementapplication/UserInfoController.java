/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group4bankmanagementapplication;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.TreeMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author EBEN
 */
public class UserInfoController implements Initializable {

    @FXML
    private TextField uiTxtUsername;
    @FXML
    private TextField uiTxtPhone;
    @FXML
    private TextField uiTxtNationality;
    @FXML
    private TextField uiTxtAddress;
    @FXML
    private TextField uiTxtAccountNo;
    @FXML
    private Button uiBtnTransaction;
    @FXML
    private Button uiBtnTHistory;
    @FXML
    private TextField uiTxtAccountType;
    @FXML
    private TextField uiTxtBalance;
    private Scene scene;
    private Stage stage;
    private Parent root;
    public static String user;
    @FXML
    private TextField uiTxtDOB;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

    void display(TreeMap<String, String> account) throws IOException {

        uiTxtUsername.setText(account.get("username"));

        uiTxtPhone.setText(account.get("phone"));

        uiTxtNationality.setText(account.get("nationality"));

        uiTxtAccountType.setText(account.get("accountType"));

        uiTxtAddress.setText(account.get("address"));

        uiTxtDOB.setText(account.get("dob"));

        uiTxtAccountNo.setText(account.get("accountNo"));

        uiTxtBalance.setText(account.get("balance"));

    }

    @FXML
    private void uiBtnTransactionHandler(ActionEvent event) throws IOException {
        stage = (Stage)uiBtnTransaction.getScene().getWindow();
        FXMLLoader loader = null;
        loader = new FXMLLoader(getClass().getResource("Transactions.fxml"));
        root = loader.load();
        TransactionsController.user = user;
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
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Transaction History");
        stage.setScene(scene);
        stage.show();
    }

}
