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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author EBEN
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Label wlcmLabelStatus;
    @FXML
    private TextField wlcmTxtUsername;
    @FXML
    private PasswordField wlcmTxtPassword;
    @FXML
    private Button wlcmBtnLogin;
    @FXML
    private Button wlcmBtnCreateAccount;
    private Stage stage;
    private Scene scene;
    private Parent root;

    Statement statement;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void wlcmBtnLoginHandler(ActionEvent event) throws IOException, SQLException {
        String user = wlcmTxtUsername.getText().toLowerCase();
        String password = wlcmTxtPassword.getText();
        if (user.isEmpty()) {
            wlcmLabelStatus.setText("Please provide username");
            wlcmLabelStatus.setTextFill(Color.BLUE);
            wlcmLabelStatus.setVisible(true);
            return;
        } else if (password.isEmpty()) {
            wlcmLabelStatus.setText("Password field cannot be empty");
            wlcmLabelStatus.setTextFill(Color.BLUE);
            wlcmLabelStatus.setVisible(true);
            return;
        }
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/g4tb", "root", "");
        try {
            statement = conn.createStatement();
            System.out.println("Database Connected");
        } catch (Exception e) {
            wlcmLabelStatus.setText("Database Server is down. contact Administrator for help");
            wlcmLabelStatus.setTextFill(Color.YELLOW);
            wlcmLabelStatus.setVisible(true);
            System.out.println("Database Not Connected");
            System.out.println(e);
            return;
        }
        try {
            String query = "SELECT * FROM `registration` WHERE username='" + user + "';";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);
            while (result.next()) {
                String id = result.getString("username");
                String pin = result.getString("pass");
                System.out.println(id.concat(pin));
                if (!pin.equals(password)) {
                    wlcmLabelStatus.setText("Invalid username or password");
                    wlcmLabelStatus.setTextFill(Color.BLUE);
                    wlcmLabelStatus.setVisible(true);
                    result.close();
                } else {
                    FXMLLoader loader;
                    loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
                    root = loader.load();
                    DashboardController username = loader.getController();
                    username.display(id);
                    stage = (Stage) wlcmBtnLogin.getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.setTitle("Dashboard");
                    stage.show();
                    result.close();
                }
            }
        } catch (SQLException ex) {
            wlcmLabelStatus.setText("Invalid username or password, if you're sure of the username and password contact Administrator for help");
            wlcmLabelStatus.setTextFill(Color.BLUE);
            wlcmLabelStatus.setVisible(true);
            System.out.println(ex);
        }

    }

    public void setStatus(String Status) {
        wlcmLabelStatus.setVisible(true);
        wlcmLabelStatus.setText(Status);
        wlcmLabelStatus.setTextFill(Color.GREEN);
    }

    @FXML
    private void wlcmBtnCreateAccountHandler(ActionEvent event) throws IOException {
        Parent createAccount = FXMLLoader.load(getClass().getResource("CreateAccount.fxml"));
        stage = (Stage) wlcmBtnCreateAccount.getScene().getWindow();
        scene = new Scene(createAccount);
        stage.setScene(scene);
        stage.setTitle("Create New Account");
        stage.show();
    }

}
