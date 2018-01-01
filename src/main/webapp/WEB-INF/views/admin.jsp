<%--
  Created by IntelliJ IDEA.
  User: Anlu
  Date: 2018/1/1
  Time: 18:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Admin page</title>
</head>
<body>
Dear <strong>${user}</strong>, Welcome to Admin Page.

<sec:authorize access="isFullyAuthenticated()">
    <label><a href="#">Create New User</a> | <a href="#">View existing Users</a></label>
</sec:authorize>
<sec:authorize access="isRememberMe()">
    <label><a href="#">View existing Users</a></label>
</sec:authorize>

<a href="<c:url value="/logout" />">Logout</a>
</body>
</html>
