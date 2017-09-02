<%@ page import="com.mastek.TwitterUtils.*"%>
<html>
<head>
<title>Hello World!</title>
</head>
<body>
	<h1>Hello World!</h1>
	<p>
		It is now
		<%= new java.util.Date() %></p>
	<p>
		You are coming from 
		<%= request.getRemoteAddr()  %></p>
		
	<p>Calling MapR Method</p>
	
	var streamState = <%= TwitterStreaming.getTwitterStreamInstance().getStreamState() %>
	<p/>
	<form action="${pageContext.request.contextPath}/myservlet" method="post">
	    <input type="submit" name="start" value="Start Streaming" />
	</form>
</body>