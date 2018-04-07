<%-- 
    Document   : viewBook
    Created on : Oct 17, 2017, 11:02:33 AM
    Author     : Huydqse62353
--%>
<%@page import="java.util.Map"%>
<%@page import="sample.cart.CartObj"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script type="text/javascript" src="https://code.jquery.com/jquery-2.1.4.min.js"></script> 
<script type="text/javascript" src="https://cdn.jsdelivr.net/webshim/1.14.5/polyfiller.js"></script>

<script>
    webshims.setOptions('forms-ext', {types: 'date'});
    webshims.polyfill('forms forms-ext');
    $.webshims.formcfg = {
        en: {
            dFormat: '-',
            dateSigns: '-',
            patterns: {
                d: "dd-mm-yy"
            }
        }
    };
</script>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Booking Page</title>
    </head>
    <body>
        <h1>Booking Details</h1>
        Your order information includes<br/>
        <c:set var="cart" value="${sessionScope.CARTTT}"/>
        <c:if test="${not empty cart}">
            <c:set var="items" value="${cart.item}"/>
            <c:if test="${not empty items}">
                <c:set var="totalPrice" value="${0}"/>
                <form action="viewBook">
                    <table border="2">
                        <thead>
                            <tr>
                                <th>No.</th>
                                <th>Room</th>
                                <th>Price</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${items}" varStatus="counter">
                                <tr>
                                    <td>${counter.count}</td>
                                    <td>${item.key}
                                        <input type="hidden" name="room" value="${item.key}" />
                                    </td>
                                    <td>${item.value}
                                    </td>
                                    <td><input type="checkbox" name="chkAction" value="${item.key}"</td>
                                        <c:set var="totalPrice" value="${totalPrice + item.value}"/>
                                </tr> 
                            </c:forEach>                    
                        </tbody>
                    </table><br/>
                    <b>Total: ${totalPrice}</b>
                    <input type="hidden" name="total" value="${totalPrice}"/><br/>     
                    <c:set var="error" value="${requestScope.ERROR}"/>
                    <c:if test="${not empty error}">
                        <font color="red">
                            ${error.roomBookedError}<br/>
                        </font>
                    </c:if>
                    Please input date you will take your room(s): 
                    <select name="fromHour">
                        <option>08:00</option>
                        <option>09:00</option>
                        <option>10:00</option>
                        <option>11:00</option>
                        <option>12:00</option>
                        <option>13:00</option>
                        <option>14:00</option>
                        <option>15:00</option>
                        <option>16:00</option>
                        <option>17:00</option>
                        <option>18:00</option>
                    </select>&nbsp; 
                    <input type="date" name="fromDate" id="d1"/><br/>
                    <c:if test="${not empty error}">
                        <font color="red">
                            ${error.inputHourDateError}<br/>
                        </font>
                        <font color="red">
                            ${error.bookRoomValidTimeError}<br/>
                        </font>
                    </c:if>
                    <input type="submit" value="Book more" name="btnAction" />&nbsp;&nbsp;
                    <input type="submit" value="Remove room" name="btnAction" />&nbsp;&nbsp;
                    <input type="submit" value="Rent" name="btnAction" />&nbsp;&nbsp;
                </form>
            </c:if>
            <c:if test="${empty items}">
                No room was book.
                <form action="viewBook">
                    <input type="submit" value="Book more" name="btnAction" />
                </form>
            </c:if>
        </c:if>
        <c:if test="${empty cart}">
            No room was book.
            <form action="viewBook">
                <input type="submit" value="Book more" name="btnAction" />
            </form>
        </c:if>
    </body>
</html>
