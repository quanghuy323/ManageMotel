/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
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
import sample.order.OrderDAO;
import sample.cart.CartObj;
import sample.message.ErrorMessage;
import sample.orderdetail.OrderDetailDAO;

/**
 *
 * @author Huydqse62353
 */
@WebServlet(name = "TakeOrderServlet", urlPatterns = {"/TakeOrderServlet"})
public class TakeOrderServlet extends HttpServlet {

    private final String errorPage = "takeOrderFailPage.html";
    private final String errorInput = "confirmCustomer.jsp";
    private final String viewOrderPage = "ShowOrderServlet";

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
        String cusName = request.getParameter("txtCustomerName");
        String cardId = request.getParameter("txtCardID");
        String cusNameValid = "[a-zA-Z ]{2,50}";
        String cartIdValid = "[a-zA-Z0-9]{6,12}";
        String url = errorPage;
        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                CartObj cart = (CartObj) session.getAttribute("CARTTT");
                if (cart != null) {
                    if (cusName.matches(cusNameValid) && cardId.matches(cartIdValid)) {
                        float totalPrice = (float) session.getAttribute("totalPriceeee");
                        String orderDate = (String) session.getAttribute("orderDateeee");
                        String fromDate = (String) session.getAttribute("fromDateeee");
                        String toDate = (String) session.getAttribute("toDateeee");
                        ArrayList<Integer> checkHour = (ArrayList<Integer>) session.getAttribute("checkHourrr");

                        OrderDAO dao = new OrderDAO();
                        String orderId = dao.takeOrder(orderDate, fromDate, toDate, totalPrice, cusName, cardId);
                        if (!orderId.equals("")) {
                            Map ORDER = cart.getItem();
                            Set set = ORDER.entrySet();
                            Iterator i = set.iterator();
                            int count = 0;
                            boolean hourPrice;
                            boolean result = true;
                            OrderDetailDAO odao = new OrderDetailDAO();
                            while (i.hasNext() && result) {
                                Map.Entry me = (Map.Entry) i.next();
                                String roomId = (String) me.getKey();
                                float total = (float) me.getValue();
                                if (checkHour.get(count) > 0) {
                                    hourPrice = false;
                                } else {
                                    hourPrice = true;
                                }
                                result = odao.addOrderDetail(roomId, total, hourPrice, orderId);
                                count++;
                            }
                            if (result) {
                                session.setAttribute("ORDERIDDD", orderId);
                                session.removeAttribute("CARTTT");
                                url = viewOrderPage;
                            }
                        }
                    } else {
                        ErrorMessage error = new ErrorMessage();
                        error.setInputNameCartError("Your cusname or cartId is not valid ! "
                                + "(cusname: 2-50 chars, cartId: 6-12 chars)");
                        session.setAttribute("ERROR", error);
                        url = errorInput;
                    }

                }
            }

        } catch (SQLException ex) {
            log("TakeOrderServlet - SQL: " + ex.getMessage());
        } catch (NamingException ex) {
            log("TakeOrderServlet - Naming: " + ex.getMessage());
        } catch (NullPointerException ex) {
            log("TakeOrderServlet - NullPointer: " + ex.getMessage());
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
