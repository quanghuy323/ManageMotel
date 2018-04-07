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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import sample.dto.RoomDTO;
import sample.utils.DBUtils;

/**
 *
 * @author Huydqse62353
 */
public class RoomDAO implements Serializable {

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
        String sql;
        Calendar cal = Calendar.getInstance();
        ArrayList<Date> fromDateBooked = new ArrayList<>();
        ArrayList<Integer> hourOrDay = new ArrayList<>();
        ArrayList<Boolean> checkHourOrDay = new ArrayList<>();
        int count = 0;
        try {
            con = DBUtils.makeConnection();
            if (con != null) {
                sql = "Select o.fromDate, (oD.total / oD.price) as hourOrDay, oD.hourPrice as checkHourOrDay\n"
                        + "From tbl_orderDetail as oD join tbl_order as o ON o.orderId = oD.orderId Where oD.roomId = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, roomId);
                rs = stm.executeQuery();
                SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                while (rs.next()) {
                    fromDateBooked.add(parser.parse(rs.getString("fromDate")));
                    hourOrDay.add(rs.getInt("hourOrDay"));
                    checkHourOrDay.add(rs.getBoolean("checkHourOrDay"));
                    count++;
                }

                for (int i = 0; i < count; i++) {
                    cal.setTime(fromDateBooked.get(i)); // Lấy thời gian fromDate của phòng đã được book
                    if (checkHourOrDay.get(i)) {
                        cal.add(Calendar.HOUR_OF_DAY, hourOrDay.get(i));
                    } else {
                        cal.add(Calendar.DAY_OF_MONTH, hourOrDay.get(i));
                    }
                    Date toDateBooked = cal.getTime(); // Tìm ra thời gian toDate cụ thể của phòng đã được book

                    cal.setTime(fromDate); // Lấy thời gian fromDate của phòng đang check
                    if (checkHour < 0) {
                        cal.add(Calendar.HOUR_OF_DAY, -checkHour);
                    } else {
                        cal.add(Calendar.DAY_OF_MONTH, checkHour);
                    }
                    Date toDate = cal.getTime(); // Tìm ra thời gian toDate cụ thể của phòng đang check

                    if ((fromDate.compareTo(fromDateBooked.get(i)) >= 0 && fromDate.compareTo(toDateBooked) <= 0)
                            || (toDate.compareTo(fromDateBooked.get(i)) >= 0 && toDate.compareTo(toDateBooked) <= 0)) {
                        return false;
                    }
                }
            }
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
}
