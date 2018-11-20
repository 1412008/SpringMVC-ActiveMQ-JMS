<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false" %>
<html>
<head>
<meta charset="UTF-8">
<title>Create User</title>
</head>
<body>
<h1 >New User</h1>
<form action="create-user" method="POST">
	<input type="text" name="userName" placeholder="Username"><br><br>
	<input type="text" name="password" placeholder="Password"><br><br>
	<input type="text" name="name" placeholder="Name"><br><br>
	<button type="submit">Create</button>
</form>
</body>
</html>
