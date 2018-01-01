<%--
  Created by IntelliJ IDEA.
  User: Anlu
  Date: 2018/1/1
  Time: 19:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Welcome page</title>
    <link href="lib/bootstrap/css/bootstrap.css"  rel="stylesheet"></link>
    <link href="<c:url value='/static/css/app.css' />" rel="stylesheet"></link>
</head>
<body>
<div class="success">
    Greeting : ${greeting}
    This is a welcome page.
</div>
</body>
</html>
