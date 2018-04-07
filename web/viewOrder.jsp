<%-- 
    Document   : viewOrderDetail
    Created on : Oct 17, 2017, 9:17:40 PM
    Author     : Huydqse62353
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Order Details</h1>
        <c:set var="listOrder" value="${sessionScope.LISTORDERRRR}"/>
        <c:if test="${not empty listOrder}">
            <c:forEach var="dto" items="${listOrder}">
                Customer Name: ${dto.cusname}<br/>
                Order Date: ${dto.orderdate}<br/>
                From Date: ${dto.fromdate}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                To Date: ${dto.todate}<br/>
                Details<br/>
                <table border="2">
                    <thead>
                        <tr>
                            <th>No.</th>
                            <th>Room</th>
                            <th>Price</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:set var="items" value="${dto.items}"/>
                        <c:if test="${not empty items}">
                            <c:forEach var="item" items="${items}" varStatus="counter">
                                <tr>
                                    <td>${counter.count}</td>
                                    <td>${item.key}</td>
                                    <td>${item.value}</td>
                                </tr>
                            </c:forEach>                       
                        </c:if>         
                    </tbody>
                </table>
            </c:forEach>            
        </c:if>
        <a href="search.jsp">Click here to book more !</a>
    </body>
</html>
