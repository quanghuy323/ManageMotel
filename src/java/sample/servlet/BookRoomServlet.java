/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import sample.message.ErrorMessage;
import sample.cart.CartObj;

/**
 *
 * @author Huydqse62353
 */
@WebServlet(name = "BookRoomServlet", urlPatterns = {"/BookRoomServlet"})
public class BookRoomServlet extends HttpServlet {

    private final String searchPage = "search.jsp";

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
        String roomId = request.getParameter("txtRoomID");
        String hourPrice = request.getParameter("txtHourPrice");
        String dayPrice = request.getParameter("txtDayPrice");
        String hour = request.getParameter("cboHour");
        String day = request.getParameter("cboDay");
        String type = request.getParameter("chkType");
        String lastSearchValue = request.getParameter("lastSearchValue");
        
        ErrorMessage error = new ErrorMessage();
        boolean bError = false;
        float price = 0;
        String urlRewriting = searchPage;
        try {
            HttpSession session = request.getSession();
            CartObj cart = (CartObj) session.getAttribute("CARTTT");
            if (cart == null) {
                cart = new CartObj();
            }
            if (type.equals("typeHour")) {
                price = Float.parseFloat(hourPrice) * Integer.parseInt(hour);
                if (price > 0){
                    cart.addHourOrDayToOrder(roomId, - Integer.parseInt(hour));
                }
            } else {
                price = Float.parseFloat(dayPrice) * Integer.parseInt(day);
                if (price > 0){
                    cart.addHourOrDayToOrder(roomId, Integer.parseInt(day));
                }
            }
            if (price == 0) {
                error.setBookError("Please input expect time do you want to book first !!!");
                bError = true;
            }
            if (bError) {
                request.setAttribute("ERRORRR", error);
            } else {
                cart.addRoomToOrder(roomId, price);                
                session.setAttribute("CARTTT", cart);
            }
                urlRewriting = "SearchFloorServlet"
                        + "?txtSearchValue=" + lastSearchValue;           
        } finally {
            RequestDispatcher rd = request.getRequestDispatcher(urlRewriting);
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
