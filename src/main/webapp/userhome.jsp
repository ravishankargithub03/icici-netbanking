<%@page import="java.sql.ResultSet"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Home</title>
<link rel="stylesheet" href="userhomestyle.css" >
</head>
<body>
		<div class="div1">
			<div class="menu">
				<ul>
				<li><a href="userhome.html">Home</a></li>
				<li><a href="profile">Profile</a></li>
				<li><a>Account</a>
					<div class="submenu1">
						<ul>
						<li><a href="mobile.html">Mobile</a></li>
						<li><a href="email.html">Email</a></li>
						<li><a href="newpass.html">Password</a></li>
						<li><a href="address.html">Address</a></li>
						<li><a href="nominee.html">Nominee</a></li>
						</ul>
					</div>
				</li>
				<li><a>Option</a>
				<div class="submenu2">
					<ul>
					<li><a href="amount">Balance</a></li>
					<li><a href="withdrawal.html">withdrawal</a></li>
					<li><a href="deposit.html">deposit</a></li>
					<li><a href="statement.html">statement</a></li>
					<li><a href="login.html">Logout</a></li>
					</ul>
				</div>
				</li>
				<li><a href="help.html">Help</a></li>
				</ul>
			</div>
			
			<div class="name"> <h2><%=request.getAttribute("firstname") %> 
			<%=request.getAttribute("lastname")%></h2> </div>
			
			<div class="image"> <img class="img" alt="" src="https://images.unsplash.com/photo-1575936123452-b67c3203c357?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80"> </div>
		</div>
	</body>
</html>