<%-- 
    Document   : confirmCustomer
    Created on : Oct 24, 2017, 5:54:48 PM
    Author     : Huydqse62353
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h2>Confirm</h2>
        <form action="viewOrder" method="POST">
            Customer Name <input type="text" name="txtCustomerName" value="" /><br/><br/>
            CardID &nbsp;&nbsp;&nbsp;<input type="text" name="txtCardID" value="" /><br/><br/>
            <c:set var="error" value="${sessionScope.ERROR}"/>
            <c:if test="${not empty error}">
                <font color="red">
                    ${error.inputNameCartError}<br/>
                </font>
            </c:if>
            <input type="submit" value="Confirm" name="btnAction" />&nbsp;&nbsp;
            <input type="submit" value="Cancel" name="btnAction" />
        </form>
    </body>
</html>
