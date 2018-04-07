/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.orderdetail;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.NamingException;
import sample.utils.DBUtils;

/**
 *
 * @author Huydqse62353
 */
public class OrderDetailDAO implements Serializable {
    
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
}
