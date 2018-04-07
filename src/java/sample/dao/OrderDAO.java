/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import sample.dto.OrderDTO;
import sample.utils.DBUtils;

/**
 *
 * @author Huydqse62353
 */
public class OrderDAO implements Serializable {

    public boolean addOrderDetail(String roomId, float total,
            boolean hourPrice, String orderId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql;
        float price = 0;
        try {
            con = DBUtils.makeConnection();
            if (con != null) {
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
            if (con != null) {
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
        OrderDTO dto = new OrderDTO();
        try {
            con = DBUtils.makeConnection();
            if (con != null) {
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
