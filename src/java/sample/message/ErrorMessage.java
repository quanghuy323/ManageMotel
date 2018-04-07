package sample.message;

import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Huydqse62353
 */
public class ErrorMessage implements Serializable{
    private String searchError;
    private String bookError;
    private String inputHourDateError;
    private String roomBookedError;
    private String bookRoomValidTimeError;
    private String inputNameCartError;

    public String getInputNameCartError() {
        return inputNameCartError;
    }

    public void setInputNameCartError(String inputNameCartError) {
        this.inputNameCartError = inputNameCartError;
    }
    

    public String getBookRoomValidTimeError() {
        return bookRoomValidTimeError;
    }

    public void setBookRoomValidTimeError(String bookRoomValidTimeError) {
        this.bookRoomValidTimeError = bookRoomValidTimeError;
    }
    

    public String getRoomBookedError() {
        return roomBookedError;
    }

    public void setRoomBookedError(String roomBookedError) {
        this.roomBookedError = roomBookedError;
    }
    

    public String getInputHourDateError() {
        return inputHourDateError;
    }

    public void setInputHourDateError(String inputHourDateError) {
        this.inputHourDateError = inputHourDateError;
    }

    public String getSearchError() {
        return searchError;
    }

    public String getBookError() {
        return bookError;
    }

    public void setBookError(String bookError) {
        this.bookError = bookError;
    }

    public void setSearchError(String searchError) {
        this.searchError = searchError;
    }

    public ErrorMessage() {
    }

    
    
}
