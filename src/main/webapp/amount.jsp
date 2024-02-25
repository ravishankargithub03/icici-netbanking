<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<style>
body
{
background: #b3d9d9;
color: #804040;
text-align: center;
}
</style>
</head>
<body>
<h2> <br><br>
Total Amount : <%=request.getAttribute("amount")%>
</h2>
<br><br>
<form action="userhome.jsp">
<input type="submit" value="Home">
</form>
</body>
</html>