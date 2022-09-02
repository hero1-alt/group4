/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group4bankmanagementapplication;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author EBEN
 */
public class DatabaseUtilities {

    String url = "jdbc:h2:~/test";   //database specific url.
    String user = "root";
    String password = "";
    Statement statement;

    //function to connect to the xampp server      
    public void DatabaseConnect() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/g4tb", "root","");
            statement = conn.createStatement();
            System.out.print("Database Connected");
        } catch (Exception e) {
            System.out.print("Database Not Connected");
        }
    }
    
    //for inserting data
public void insert(){
    try{
        String insertquery = "INSERT INTO `tablename`(`field1`, `field2`) VALUES ('value1', 'value2'";
        statement.executeUpdate(insertquery);
        System.out.print("Inserted");
    } catch(Exception e){
        System.out.print("Not Inserted");
    }
}

 //for viewing data
 public void view(){
    try {
        String insertquery = "select * from `table_name` where field = 'value1'";
        ResultSet result = statement.executeQuery(insertquery);
        if(result.next()){
            System.out.println("Value " + result.getString(2));
            System.out.println("Value " + result.getString(3));
        }
    } catch (SQLException ex) {
        System.out.println("Problem To Show Data");
    }
 }

 //to update data
 public void update(){
    try {
        String insertquery = "UPDATE `table_name` set `field`='value',`field2`='value2' WHERE field = 'value'";
        statement.executeUpdate(insertquery);
        System.out.println("Updated");
    } catch (SQLException ex) {
        System.out.println(ex.getMessage());
    }
 }

//to delete data
public void delete(){
     try {
        String insertquery = "DELETE FROM `table_name` WHERE field = 'value'";
        statement.executeUpdate(insertquery);
        System.out.println("Deleted");
    } catch (SQLException ex) {
        System.out.println(ex.getMessage());
    }
 }
}
