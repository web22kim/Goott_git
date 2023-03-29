<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ page import="com.miniproj.member.dao.DBConnection" %>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Insert title here</title>
  </head>
  <body>
    <h1>Hello, GOOT!</h1>
    <% out.println(DBConnection.dbConnect()); %>
    <a
      href="https://m.blog.naver.com/PostView.naver?blogId=ll3145ll&logNo=223035128516&proxyReferer="
    >
      을지로 따코맛집 제가 한번 가보겠습니다.dd!!!</a
    >
  </body>
</html>
