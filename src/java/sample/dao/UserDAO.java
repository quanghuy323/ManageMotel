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
import javax.naming.NamingException;
import sample.utils.DBUtils;

/**
 *
 * @author Huydqse62353
 */
public class UserDAO implements Serializable{
    
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
}
