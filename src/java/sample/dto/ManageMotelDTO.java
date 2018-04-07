/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.dto;

import java.io.Serializable;

/**
 *
 * @author Huydqse62353
 */
public class ManageMotelDTO implements Serializable{
    private String roomID;
    private String description;
    private float hourPrice;
    private float dayPrice;

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getHourPrice() {
        return hourPrice;
    }

    public void setHourPrice(float hourPrice) {
        this.hourPrice = hourPrice;
    }

    public float getDayPrice() {
        return dayPrice;
    }

    public void setDayPrice(float dayPrice) {
        this.dayPrice = dayPrice;
    }

    public ManageMotelDTO(String roomID, String description, float hourPrice, float dayPrice) {
        this.roomID = roomID;
        this.description = description;
        this.hourPrice = hourPrice;
        this.dayPrice = dayPrice;
    }

    public ManageMotelDTO() {
    }
    
    
    
}
