/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Huydqse62353
 */
public class OrderDTO implements Serializable{
    private String cusname;
    private String orderdate;
    private String fromdate;
    private String todate;
    private float total;
    private String cardid;
    private Map<String, Float> items;

    public OrderDTO(String cusname, String orderdate, String fromdate, String todate, float total, String cardid) {
        this.cusname = cusname;
        this.orderdate = orderdate;
        this.fromdate = fromdate;
        this.todate = todate;
        this.total = total;
        this.cardid = cardid;
    }

    public OrderDTO() {
    }

    public String getCusname() {
        return cusname;
    }

    public void setCusname(String cusname) {
        this.cusname = cusname;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public String getFromdate() {
        return fromdate;
    }

    public void setFromdate(String fromdate) {
        this.fromdate = fromdate;
    }

    public String getTodate() {
        return todate;
    }

    public void setTodate(String todate) {
        this.todate = todate;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getCardid() {
        return cardid;
    }

    public void setCardid(String cardid) {
        this.cardid = cardid;
    }

    public Map<String, Float> getItems() {
        return items;
    }

    public void setItems(Map<String, Float> items) {
        this.items = items;
    }

    

    
    public void addRoomToOrder(String roomId, float price) {
        if (this.items == null) {
            this.items = new HashMap<>();
        }
        this.items.put(roomId, price);
    }
}
