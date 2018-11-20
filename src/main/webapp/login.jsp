<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false" %>
<html>
<head>
<meta charset="UTF-8">
<title>Login page</title>
</head>
<body>
<h1 >Login</h1>
<form name="f" action="login_check" method="post">
	<input type="text" name="usname" placeholder="Username"><br><br>
	<input type="password" name="password" placeholder="Password"><br><br>
	<input type="checkbox" name="remember-me" checked hidden ><br><br>
	<button type="submit">Login</button>
</form>
</body>
</html>
