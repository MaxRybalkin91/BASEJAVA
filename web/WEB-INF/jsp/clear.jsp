<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="get">
        <% String action = ""; %>
        <% String word = ""; %>
        <c:if test="${param.action.equals('submitClear') }">
            <% action = "clear";%>
            <% word = "все";%>
        </c:if>

        <c:if test="${param.action.equals('submitDelete') }">
            <% action = "delete";%>
            <% word = "данное";%>
        </c:if>
        <h1>Вы действительно желаете удалить <%=word%> резюме? Отменить операцию будет невозможно!</h1>

        <input type="hidden" name="uuid" value="${param.get('uuid')}"/>
        <input type="hidden" name="action" value=<%=action%>>
        <button type="submit">Подтвердить удаление</button>
        <button type="reset" onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>