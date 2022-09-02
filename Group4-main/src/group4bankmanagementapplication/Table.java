/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group4bankmanagementapplication;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Cool IT help
 */
public class Table {    
    private  final SimpleStringProperty category;
    private  final SimpleStringProperty From;
    private  final SimpleStringProperty To;
    private  final SimpleStringProperty amount;
    private  final SimpleStringProperty Status;
    
    Table(String category, String From, String To, String amount, String Status)
    {      
       this.category = new SimpleStringProperty(category);
       this.From = new SimpleStringProperty(From);
       this.To =  new SimpleStringProperty(To);
       this.amount =  new SimpleStringProperty(amount);
       this.Status =  new SimpleStringProperty(Status);
    
    }
    
     
    public String getCategory() {
        return category.get();
    }

    public void setCategory(String category) {
        this.category.set(category);
    }
    
   
  
    public String getFrom() {
        return From.get();
    }

    public void setFrom(String From) {
        this.From.set(From);
    }
    
    

    public String getTo() {
        return To.get();
    }

    public void setTo(String To) {
        this.To.set(To);
    }
    
    
    public String getAmount() {
        return amount.get();
    }

    public void setAmount(String amount) {
        this.amount.set(amount);
    }
    
   

    public String getStatus() {
        return Status.get();
    }

    public void setStatus(String Status) {
        this.Status.set(Status);
    }
    
    
}
    
