<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h3>타로 테스트</h3>
	<c:if test="${taro.qNo le endPage}">
		<form action="/ncpl/taro/test?qNo=${qNo+1}" method="post">
		 	 <div>${taro.content}</div>
		 	 <button type="submit" name="op" value="ans1">${taro.ans1}</button>
		 	 <button type="submit" name="op" value="ans2">${taro.ans1}</button>
		</form>
	</c:if>
	<c:if test="${taro.qNo gt endPage}">
		<form action="/ncpl/taro/result">
		 	<button type="submit">결과보기</button>
<%-- 		 	<input type="submit" value="${taro.ans1}">
		 	<input type="submit" value="${taro.ans2}"> --%>
		</form>
	</c:if>

</body>
</html>