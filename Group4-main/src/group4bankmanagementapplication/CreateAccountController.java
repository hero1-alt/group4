/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group4bankmanagementapplication;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.Random;
import javafx.scene.control.Label;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author EBEN
 */
public class CreateAccountController implements Initializable {

    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private DatePicker accTxtDOB;
    @FXML
    private TextField accTxtPhone;
    @FXML
    private TextField accTxtUsername;
    @FXML
    private PasswordField password;
    @FXML
    private TextField accTxtNationality;
    @FXML
    private TextField accTxtAddress;
    @FXML
    private Button logout;
    @FXML
    private Label LabelFeedBack;
    @FXML
    private Button CreateAccount;

    Statement statement;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
//        ObservableList<String> list = FXCollections.observableArrayList("GCB", "Ecobank", "ABSA", "Fedelity Bank", "ADB", "SG", "CBG");
//
//        accBank.setItems(list);

    }

    @FXML
    private void caccBtnCreateAccountHandler(ActionEvent event) throws IOException, SQLException {
        String first_Name = firstName.getText();
        String last_Name = lastName.getText();
        LocalDate dob = accTxtDOB.getValue();
        String phone = accTxtPhone.getText();
        String username = accTxtUsername.getText();
        String pass = password.getText();
        String nationality = accTxtNationality.getText();
        String address = accTxtAddress.getText();

        if (first_Name.isEmpty()) {
            LabelFeedBack.setText("Please provide first name");
            LabelFeedBack.setTextFill(Color.BLUE);
            LabelFeedBack.setVisible(true);
            return;
        } else if (last_Name.isEmpty()) {
            LabelFeedBack.setText("Please provide last name");
            LabelFeedBack.setTextFill(Color.BLUE);
            LabelFeedBack.setVisible(true);
            return;
        } else if (dob.toString().isEmpty()) {
            LabelFeedBack.setText("Please provide a valid date of birth");
            LabelFeedBack.setTextFill(Color.BLUE);
            LabelFeedBack.setVisible(true);
            return;
        } else if (username.isEmpty()) {
            LabelFeedBack.setText("Please provide a unique username");
            LabelFeedBack.setTextFill(Color.BLUE);
            LabelFeedBack.setVisible(true);
            return;
        } else if (pass.isEmpty()) {
            LabelFeedBack.setText("Please provide a strong password");
            LabelFeedBack.setTextFill(Color.BLUE);
            LabelFeedBack.setVisible(true);
            return;
        }

        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/g4tb", "root", "");
        String query = "Insert into registration (`firstName`, `lastName`, `dob`, `phone`, `username`, `pass`, `nationality`, `address`) Values(?,?,?,?,?,?,?,?)";

        try {
            statement = conn.createStatement();
            System.out.print("Database Connected");
        } catch (Exception e) {
            LabelFeedBack.setText("Database Server is down. contact Administrator for help");
            LabelFeedBack.setTextFill(Color.YELLOW);
            LabelFeedBack.setVisible(true);
            System.out.println("Database Not Connected");
            System.out.println(e);
            return;
        }
        try {
            String insertquery = "select username from `registration` where username = '" + username + "'";
            ResultSet result = statement.executeQuery(insertquery);
            while (result.next()) {
                LabelFeedBack.setText("Username already exist, try logging in");
                LabelFeedBack.setTextFill(Color.BLUE);
                LabelFeedBack.setVisible(true);
                result.close();
                return;
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, first_Name);
            stmt.setString(2, last_Name);
            stmt.setString(3, dob.toString());
            stmt.setString(4, phone);
            stmt.setString(5, username);
            stmt.setString(6, pass);
            stmt.setString(7, nationality);
            stmt.setString(8, address);
            stmt.executeUpdate();
            FXMLLoader loader = null;
            loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            Parent root = loader.load();
            DashboardController dashboard = loader.getController();
            dashboard.display(username);
            Stage stage = (Stage) CreateAccount.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Dashboard");
            stage.show();
        } catch (SQLException | IOException e) {
            LabelFeedBack.setText("Error creating account, contact Administrator");
            LabelFeedBack.setTextFill(Color.BLUE);
            LabelFeedBack.setVisible(true);
            System.out.println(e);
        }
    }

    @FXML
    private void logout(ActionEvent event) throws IOException {
        FXMLLoader loader = null;
        loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
        Parent root = loader.load();
        FXMLDocumentController welcomePage = loader.getController();
        welcomePage.setStatus("you're Logged out.");
        Stage stage = (Stage) logout.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Welcome - Login");
        stage.show();
    }

    @FXML
    private void setUsername(InputMethodEvent event) {
        Random ran = new Random();
        int num = ran.nextInt(100 - 1) + 1;
        String random_username = firstName.getText() + num;
        accTxtUsername.setText(random_username);
    }

}
