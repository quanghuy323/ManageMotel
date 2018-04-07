<%-- 
    Document   : search
    Created on : Oct 10, 2017, 12:50:15 PM
    Author     : Huydqse62353
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Search</title>
    </head>
    <body>
        <font color="red"><h3>Welcome, ${sessionScope.USERNAME}</h3></font>     
        <form action="logout">
            <input type="submit" value="Logout" name="btnAction" />
        </form>
        <h1>Search</h1>
        <form action="searchFloor">
            Floor &nbsp;&nbsp; <input type="text" name="txtSearchValue" value="" />  
            &nbsp;<input type="submit" value="Search" name="btnAction" /><br/>
            (Để trống - search tất cả)<br/>
            <c:set var="error" value="${ERRORRR}"/>
            <c:if test="${not empty error}">
                ${error.searchError}
            </c:if>
        </form>

        <br/>


        <h2>Searching result</h2>
        <c:set var="searchResult" value="${requestScope.SEARCHRESULT}"/>
        <c:set var="searchValue" value="${param.txtSearchValue}"/>
        <c:if test="${not empty searchValue}">
            <c:if test="${empty searchResult}">
                <h3>Not matching record !!!</h3>
            </c:if>
        </c:if>
        <c:set var="cart" value="${sessionScope.CARTTT}"/>
        <c:if test="${not empty searchResult}">
            <c:if test="${not empty error}">
                <font color="red" style="font-size: 30">
                ${error.bookError}
                </font>
            </c:if>
            <table border="2">
                <thead>
                    <tr>
                        <th>No.</th>
                        <th>Room</th>
                        <th>Description</th>
                        <th>Hour Price</th>
                        <th>Day Price</th>
                        <th>Hour</th>
                        <th>Day</th>
                        <th>Book</th>
                    </tr>
                </thead>
                <tbody>
                    <c:if test="${not empty cart}">
                        <c:set var="items" value="${cart.item}"/>
                        <c:if test="${not empty items}">
                            <c:forEach var="dto" items="${searchResult}" varStatus="counter">                        
                                <c:forEach var="item" items="${items}">
                                    <c:set var="roomBooked" value="${item.key}"/> 
                                    <c:if test="${roomBooked == dto.roomID}">
                                        <c:set var="check" value="checked"/>
                                    </c:if>                                    
                                </c:forEach>

                            <form action="bookRoom">
                                <tr>
                                    <td>${counter.count}</td>
                                    <td>${dto.roomID}
                                        <input type="hidden" name="txtRoomID" value="${dto.roomID}" />
                                    </td>
                                    <td>${dto.description}</td>
                                    <td>${dto.hourPrice}
                                        <input type="hidden" name="txtHourPrice" value="${dto.hourPrice}" />
                                    </td>
                                    <td>${dto.dayPrice}
                                        <input type="hidden" name="txtDayPrice" value="${dto.dayPrice}" />
                                    </td>
                                    <td>
                                        <select name="cboHour">
                                            <option>0</option>
                                            <option>1</option>
                                            <option>2</option>
                                            <option>3</option>
                                            <option>4</option>
                                            <option>5</option>
                                            <option>6</option>
                                        </select>
                                        Hour(s)
                                        <input type="radio" name="chkType" value="typeHour" checked="checked"/>
                                    </td>
                                    <td>
                                        <select name="cboDay">
                                            <option>0</option>
                                            <option>1</option>
                                            <option>2</option>
                                            <option>3</option>
                                            <option>4</option>
                                            <option>5</option>
                                            <option>6</option>
                                        </select> 
                                        Day(s)
                                        <input type="radio" name="chkType" value="typeDay" />
                                    </td>                                        
                                    <td>
                                        <c:if test="${empty check}">
                                            <input type="hidden" name="lastSearchValue" 
                                                   value="${searchValue}" />
                                            <input type="submit" value="Book" name="btnAction" />
                                        </c:if>
                                        <c:if test="${not empty check}">
                                            Booked
                                        </c:if>    
                                    </td>
                                </tr>
                            </form>                                

                            <c:if test="${not empty check}">
                                <c:set var="check" value=""/>
                            </c:if>
                        </c:forEach>
                    </c:if>
                    <c:if test="${empty items}">
                        <c:set var="cart" value=""/>
                    </c:if>
                </c:if>

                <c:if test="${empty cart}">
                    <c:forEach var="dto" items="${searchResult}" varStatus="counter">                        
                        <form action="bookRoom">
                            <tr>
                                <td>${counter.count}</td>
                                <td>${dto.roomID}
                                    <input type="hidden" name="txtRoomID" value="${dto.roomID}" />
                                </td>
                                <td>${dto.description}</td>
                                <td>${dto.hourPrice}
                                    <input type="hidden" name="txtHourPrice" value="${dto.hourPrice}" />
                                </td>
                                <td>${dto.dayPrice}
                                    <input type="hidden" name="txtDayPrice" value="${dto.dayPrice}" />
                                </td>
                                <td>
                                    <select name="cboHour">
                                        <option>0</option>
                                        <option>1</option>
                                        <option>2</option>
                                        <option>3</option>
                                        <option>4</option>
                                        <option>5</option>
                                        <option>6</option>
                                    </select>
                                    Hour(s)
                                    <input type="radio" name="chkType" value="typeHour" checked="checked" />
                                </td>
                                <td>
                                    <select name="cboDay">
                                        <option>0</option>
                                        <option>1</option>
                                        <option>2</option>
                                        <option>3</option>
                                        <option>4</option>
                                        <option>5</option>
                                        <option>6</option>
                                    </select>
                                    Day(s)
                                    <input type="radio" name="chkType" value="typeDay" />
                                </td>
                                <td>
                                    <input type="hidden" name="lastSearchValue" 
                                           value="${searchValue}" />
                                    <input type="submit" value="Book" name="btnAction" />
                                </td>
                            </tr>
                        </form>
                    </c:forEach>
                </c:if>
            </tbody>
        </table> 
    </c:if>
    <a href="viewBooking.jsp">View Booking Details</a>    
</body>
</html>
