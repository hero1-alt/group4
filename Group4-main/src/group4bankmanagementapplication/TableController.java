/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group4bankmanagementapplication;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author EBEN
 */
public class TableController {

    private final SimpleStringProperty thCatCol, thStatusCol, thDateCol;
    private final SimpleDoubleProperty thAmountCol;

    TableController(String thCatCol, Double thAmountCol, String thStatusCol, String thDateCol) {
        this.thCatCol = new SimpleStringProperty(thCatCol);
        this.thAmountCol = new SimpleDoubleProperty(thAmountCol);
        this.thStatusCol = new SimpleStringProperty(thStatusCol);
        this.thDateCol = new SimpleStringProperty(thDateCol);
    }

    public String getCategory() {
        return thCatCol.get();
    }
    public Double getAmount() {
        return thAmountCol.get();
    }
    public String getStatus() {
        return thStatusCol.get();
    }
    public String getDate() {
        return thDateCol.get();
    }
    
    public void setCategory(String Category) {
        this.thCatCol.set(Category);
    }

    public void setAmount(Double Amount) {
        this.thAmountCol.set(Amount);
    }
    
    public void setStatus(String Status) {
        this.thStatusCol.set(Status); }
    
    public void setDate(String Date) {
        this.thDateCol.set(Date);
    }
    

}
