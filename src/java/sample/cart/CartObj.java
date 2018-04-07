/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.cart;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Huydqse62353
 */
public class CartObj implements Serializable {

    private String customerID;
    private Map<String, Float> item;
    private Map<String, Integer> hourDay;

    public Map<String, Integer> getHourDay() {
        return hourDay;
    }

    public void setHourDay(Map<String, Integer> hourDay) {
        this.hourDay = hourDay;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public Map<String, Float> getItem() {
        return item;
    }

    public void setItem(Map<String, Float> item) {
        this.item = item;
    }

    public CartObj(String customerID, Map<String, Float> item) {
        this.customerID = customerID;
        this.item = item;
    }

    public void addRoomToOrder(String roomId, float price) {
        if (this.item == null) {
            this.item = new HashMap<>();
        }
        if (this.item.containsKey(roomId)) {
            price = this.item.get(roomId) + price;
        }
        this.item.put(roomId, price);
    }

    public void removeRoomToOrder(String roomId) {
        if (this.item == null) {
            return;
        }
        if (this.item.containsKey(roomId)) {
            this.item.remove(roomId);
            if (this.item.isEmpty()) {
                this.item = null;
            }
        }
    }
    
    public void addHourOrDayToOrder(String roomId, int value){
        if (this.hourDay == null){
            this.hourDay = new HashMap<>();
        }
        this.hourDay.put(roomId, value);
    }
    
    public void removeHourDayToOrder(String roomId) {
        if (this.hourDay == null) {
            return;
        }
        if (this.hourDay.containsKey(roomId)) {
            this.hourDay.remove(roomId);
            if (this.hourDay.isEmpty()) {
                this.hourDay = null;
            }
        }
    }

    public CartObj() {
    }
}
