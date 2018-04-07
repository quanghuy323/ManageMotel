/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import sample.room.RoomDAO;
import sample.message.ErrorMessage;
import sample.cart.CartObj;

/**
 *
 * @author Huydqse62353
 */
@WebServlet(name = "RentServlet", urlPatterns = {"/RentServlet"})
public class RentServlet extends HttpServlet {

    private final String rentError = "viewBooking.jsp";
    private final String confirmPage = "confirmCustomer.jsp";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String total = (String) request.getParameter("total");
        String fromDate = request.getParameter("fromDate");
        String fromHour = request.getParameter("fromHour");
        ErrorMessage error = new ErrorMessage();
        int daysss = 0, hourrr = 0, count = 0;
        ArrayList<Integer> checkHour = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        Date ord = cal.getTime();
        String orderDate = dateFormat.format(ord);
        String url = rentError;
        boolean bError = false;
        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                if (!fromDate.equals("")) {
                    CartObj cart = (CartObj) session.getAttribute("CARTTT");
                    if (cart != null) {
                        float totalPrice = Float.parseFloat(total);
                        Map hourDay = cart.getHourDay();
                        Set set = hourDay.keySet();
                        for (Object key : set) {
                            checkHour.add((int) hourDay.get(key));
                            if (checkHour.get(count) > daysss) {
                                daysss = checkHour.get(count);
                            }
                            count++;
                        }

                        String[] time = fromDate.split("-");
                        fromHour = fromHour.substring(0, 2);
                        cal.set(Calendar.SECOND, 0);
                        cal.set(Calendar.MINUTE, 0);
                        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(fromHour));
                        cal.set(Calendar.YEAR, Integer.parseInt(time[0]));
                        cal.set(Calendar.MONTH, Integer.parseInt(time[1]) - 1);
                        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(time[2]));
                        Date from = cal.getTime();
                        fromDate = dateFormat.format(from);
                        if (from.compareTo(ord) <= 0){
                            error.setBookRoomValidTimeError("Please input a date from now to future !!!");
                            bError = true;
                        }
                        if (daysss > 0) {
                            cal.add(Calendar.DAY_OF_MONTH, daysss);
                        } else {
                            for (int i = 0; i < count; i++) {
                                if (checkHour.get(i) < hourrr) {
                                    hourrr = checkHour.get(i);
                                }
                            }
                            cal.add(Calendar.HOUR_OF_DAY, -hourrr);
                        }
                        Date to = cal.getTime();
                        String toDate = dateFormat.format(to);
                        RoomDAO dao = new RoomDAO();
                        count = 0;
                        for (Object key : set) {
                            boolean result = dao.checkRoomValid((String) key, from, checkHour.get(count));
                            if (!result) {
                                error.setRoomBookedError("room " + key + " have already booked. "
                                        + "Please choose another room or another date !");
                                bError = true;
                                break;
                            }
                            count++;
                        }

                        if (bError) {
                            request.setAttribute("ERROR", error);
                        } else {
                            session.setAttribute("fromHour", fromHour);
                            session.setAttribute("orderDateeee", orderDate);
                            session.setAttribute("fromDateeee", fromDate);
                            session.setAttribute("toDateeee", toDate);
                            session.setAttribute("totalPriceeee", totalPrice);
                            session.setAttribute("checkHourrr", checkHour);
                            if (session.getAttribute("ERROR") != null){
                                session.removeAttribute("ERROR");
                            }                            
                            url = confirmPage;
                        }
                    }
                } else {
                    error.setInputHourDateError("Please input hour and date you will take room !!!");
                    request.setAttribute("ERROR", error);
                }

            }
        } catch (SQLException ex) {
            log("RentServlet - SQL: " + ex.getMessage());
        } catch (NamingException ex) {
            log("RentServlet - Naming: " + ex.getMessage());
        } finally {
            RequestDispatcher rd = request.getRequestDispatcher(url);
            rd.forward(request, response);
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
