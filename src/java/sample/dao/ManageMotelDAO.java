/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import sample.dto.RoomDTO;
import sample.dto.OrderDTO;

import sample.utils.DBUtils;

/**
 *
 * @author Huydqse62353
 */
public class ManageMotelDAO implements Serializable {

    public boolean checkLogin(String username, String password) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = DBUtils.makeConnection();
            if (con != null) {
                String sql = "Select * From tbl_account Where username = ? AND password = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, username);
                stm.setString(2, password);

                rs = stm.executeQuery();
                if (rs.next()) {
                    return true;
                }
            }

        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }

        return false;
    }

    public List<RoomDTO> listRoom;

    public List<RoomDTO> getListRoom() {
        return listRoom;
    }

    public void searchRoom(String searchValue) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = DBUtils.makeConnection();
            if (con != null) {
                String sql;
                if (searchValue.trim().equalsIgnoreCase("")) {
                    sql = "Select * From tbl_room";
                    stm = con.prepareStatement(sql);
                } else {
                    sql = "Select * From tbl_room Where floor = ?";
                    stm = con.prepareStatement(sql);
                    stm.setInt(1, Integer.parseInt(searchValue));

                }
                rs = stm.executeQuery();
                while (rs.next()) {
                    String mobileId = rs.getString("roomID");
                    String description = rs.getString("description");
                    float hourPrice = rs.getFloat("hourPrice");
                    float dayPrice = rs.getFloat("dayPrice");

                    RoomDTO dto = new RoomDTO(mobileId, description, hourPrice, dayPrice);
                    if (this.listRoom == null) {
                        this.listRoom = new ArrayList<>();
                    }
                    this.listRoom.add(dto);
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    public boolean checkRoomValid(String roomId, Date fromDate, int checkHour) throws SQLException, NamingException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = "";
        Calendar cal = Calendar.getInstance();
        Date[] fromDateBooked  = new Date[10];
        int[] hourOrDay = new int[10];
        boolean[] checkHourOrDay = new boolean[10];
        int count = 0;
        try {
            con = DBUtils.makeConnection();

            sql = "Select o.fromDate, (oD.total / oD.price) as hourOrDay, oD.hourPrice as checkHourOrDay\n" +
            "From tbl_orderDetail as oD join tbl_order as o ON o.orderId = oD.orderId Where oD.roomId = ?";
            stm = con.prepareStatement(sql);
            stm.setString(1, roomId);
            rs = stm.executeQuery();
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     
            while (rs.next()) {
                fromDateBooked[count] = parser.parse(rs.getString("fromDate"));
                hourOrDay[count] = rs.getInt("hourOrDay");
                checkHourOrDay[count] = rs.getBoolean("checkHourOrDay");
                count++;
            }
            
            for (int i = 0; i < count; i++){
                cal.setTime(fromDateBooked[i]); // Lấy thời gian fromDate của phòng đã được book
                if (checkHourOrDay[i]){
                    cal.add(Calendar.HOUR_OF_DAY, hourOrDay[i]);
                }else {
                    cal.add(Calendar.DAY_OF_MONTH, hourOrDay[i]);
                }
                Date toDateBooked = cal.getTime(); // Tìm ra thời gian toDate cụ thể của phòng đã được book
                
                cal.setTime(fromDate); // Lấy thời gian fromDate của phòng đang check
                if (checkHour < 0){
                    cal.add(Calendar.HOUR_OF_DAY, -checkHour);    
                } else {
                    cal.add(Calendar.DAY_OF_MONTH, checkHour);  
                }
                Date toDate = cal.getTime(); // Tìm ra thời gian toDate cụ thể của phòng đang check
                
                if ((fromDate.compareTo(fromDateBooked[i]) >= 0 && fromDate.compareTo(toDateBooked) <= 0)
                        || (toDate.compareTo(fromDateBooked[i]) >= 0 && toDate.compareTo(toDateBooked) <= 0)){
                    return false;
                }
            }
            
//            while (rs.next()) {
//                fromDateBooked[count] = parser.parse(rs.getString("fromDate"));
//                toDateBooked[count] = parser.parse(rs.getString("toDate"));
//                checkHourOrDay[count] = rs.getBoolean("hourPrice");
//                count++;
//            }
            
//            for (int i = 0; i < count; i++){
//                if ((fromDate.compareTo(fromDateBooked[i]) >= 0 && fromDate.compareTo(toDateBooked[i]) <= 0)
//                        || (toDate.compareTo(fromDateBooked[i]) >= 0 && toDate.compareTo(toDateBooked[i]) <= 0)){                   
//                    return false;
//                }
//            }
            
            
        } catch (ParseException ex) {
            Logger.getLogger(ManageMotelDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return true;
    }

    public boolean addOrderDetail(String roomId, float total,
            boolean hourPrice, String orderId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = "";
        float price = 0;
        try {
            con = DBUtils.makeConnection();
            if (hourPrice) {
                sql = "Select hourPrice as price From tbl_room Where roomID = ?";
            } else {
                sql = "Select dayPrice as price From tbl_room Where roomID = ?";
            }
            stm = con.prepareStatement(sql);
            stm.setString(1, roomId);
            rs = stm.executeQuery();
            if (rs.next()) {
                price = rs.getFloat("price");
            }

            sql = "Insert into tbl_orderDetail(price,total,hourPrice,orderID,roomID) values (?,?,?,?,?)";
            stm = con.prepareStatement(sql);
            stm.setFloat(1, price);
            stm.setFloat(2, total);
            stm.setBoolean(3, hourPrice);
            stm.setString(4, orderId);
            stm.setString(5, roomId);
            int row = stm.executeUpdate();
            if (row > 0) {
                return true;
            }

        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return false;
    }

    public String takeOrder(String orderDate, String fromDate, String toDate,
            float total, String cusName, String cardID) throws SQLException, NamingException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBUtils.makeConnection();
            String sql = "Select orderId From tbl_order";

            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            String id = null;
            String orderId = "ODID001";
            while (rs.next()) {
                id = rs.getString("orderId");
            }
            if (id != null) {
                String idNumber = "" + ((Double.parseDouble(id.substring(4)) + 1) / 1000);
                idNumber = idNumber.substring(2);
                while (!idNumber.matches("[0-9]{3}")) {
                    idNumber = idNumber + "0";
                }
                orderId = "ODID" + idNumber;
            }

            sql = "Insert into tbl_order values (?,?,?,?,?,?,?)";
            stm = con.prepareStatement(sql);
            stm.setString(1, orderId);
            stm.setString(2, orderDate);
            stm.setString(3, fromDate);
            stm.setString(4, toDate);
            stm.setFloat(5, total);
            stm.setString(6, cusName);
            stm.setString(7, cardID);
            int row = stm.executeUpdate();
            if (row > 0) {
                return orderId;
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }

        return "";
    }
    public List<OrderDTO> listOrder;

    public List<OrderDTO> getListOrder() {
        return listOrder;
    }

    public void showOrder(String orderId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean check = false;
        OrderDTO dto = null;
        try {
            con = DBUtils.makeConnection();
            String sql = "Select * From tbl_order Where OrderID = ?";
            stm = con.prepareStatement(sql);
            stm.setString(1, orderId);
            rs = stm.executeQuery();
            if (rs.next()) {
                String orderDate = rs.getString("orderDate");
                String fromDate = rs.getString("fromDate");
                String toDate = rs.getString("toDate");
                float totalPrice = rs.getFloat("totalPrice");
                String cusName = rs.getString("customerName");
                String cardId = rs.getString("cardID");
                dto = new OrderDTO(cusName, orderDate, fromDate, toDate, totalPrice, cardId);
                check = true;
            }
            if (check) {
                sql = "Select roomId, total From tbl_orderDetail Where orderID = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, orderId);
                rs = stm.executeQuery();
                while (rs.next()) {
                    String roomId = rs.getString("roomId");
                    float price = rs.getFloat("total");
                    dto.addRoomToOrder(roomId, price);
                }
                if (this.listOrder == null) {
                    this.listOrder = new ArrayList<>();
                }
                this.listOrder.add(dto);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }
}
