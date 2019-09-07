<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ page import="ru.javawebinar.basejava.model.ListSection" %>
<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
<%@ page import="ru.javawebinar.basejava.model.TextSection" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="ru.javawebinar.basejava.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="fullName" size=50 value="${resume.fullName}"></dd>
        </dl>

        <h3>Контакты:</h3>
        <c:forEach var="contactType" items="<%=ContactType.values()%>">
            <jsp:useBean id="contactType" type="ru.javawebinar.basejava.model.ContactType"/>
            <dl>
                <dt>${contactType.title}</dt>
                <dd><input type="text" name="${contactType.name()}" size=30 value="${resume.getContact(contactType)}">
                </dd>
            </dl>
        </c:forEach>

        <c:forEach items="${SectionType.values()}" var="sectionType">
            <jsp:useBean id="sectionType" type="ru.javawebinar.basejava.model.SectionType"/>
            <c:choose>
                <%-- TextSection --%>
                <c:when test="${sectionType.equals(SectionType.PERSONAL) || sectionType.equals(SectionType.OBJECTIVE)}">
                    <dd>
                        <% TextSection section = (TextSection) resume.getSection(sectionType); %>
                        <% String body = section != null ? section.getText() : null; %>
                        <h3><%=sectionType.getTitle()%>
                        </h3>
                        <textarea rows="4" cols="150" name="<%=sectionType.name()%>"><c:if
                                test="<%=body!=null%>"><%=body%>
                        </c:if></textarea>
                    </dd>
                </c:when>
                <%-- ListSection --%>
                <c:when test="${sectionType.equals(SectionType.ACHIEVEMENT) || sectionType.equals(SectionType.QUALIFICATION)}">
                    <dd>
                        <% ListSection section = (ListSection) resume.getSection(sectionType); %>
                        <% String values = section != null ? section.toString().trim() : null; %>
                        <h3>
                            <%=sectionType.getTitle()%>
                        </h3>
                        <textarea rows="4" cols="150" name="<%=sectionType.name()%>"><c:if
                                test="<%=values!=null%>"><%=values%>
                        </c:if></textarea>
                    </dd>
                </c:when>
            </c:choose>
        </c:forEach>

        <button type="submit">Сохранить</button>
        <button type="reset" onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>