<%@ page import="ru.javawebinar.basejava.model.ListSection" %>
<%@ page import="ru.javawebinar.basejava.model.Organization.Stage" %>
<%@ page import="ru.javawebinar.basejava.model.OrganizationSection" %>
<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
<%@ page import="ru.javawebinar.basejava.model.TextSection" %>
<%@ page import="ru.javawebinar.basejava.util.DateUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
    <h2>${resume.fullName}
        <a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"></a>
        <a href="resume?uuid=${resume.uuid}&action=submitDelete"><img src="img/delete.png"></a>
    </h2>

    <%-- Contacts--%>
    <h2>Контакты:</h2>
    <c:forEach var="contactEntry" items="${resume.contacts}">
        <jsp:useBean id="contactEntry"
                     type="java.util.Map.Entry<ru.javawebinar.basejava.model.ContactType, java.lang.String>"/>
        <% String value = contactEntry.getValue(); %>
        <c:if test="<%=value != null && value.trim().length() != 0%>">
            <%=contactEntry.getKey().toHtml(value)%><br/>
        </c:if>
    </c:forEach>

    <%-- Sections--%>
    <c:forEach items="${resume.sections}" var="sectionEntry">
        <jsp:useBean id="sectionEntry"
                     type="java.util.Map.Entry<ru.javawebinar.basejava.model.SectionType,
                     ru.javawebinar.basejava.model.Section>"/>
        <% String value = sectionEntry.getValue().toString(); %>
        <c:if test="<%=value != null && value.trim().length() != 0%>">
            <h3>${sectionEntry.key.title}</h3>
            <c:choose>
                <c:when test="${sectionEntry.key.equals(SectionType.PERSONAL) || sectionEntry.key.equals(SectionType.OBJECTIVE)}">
                    <%= ((TextSection) sectionEntry.getValue()).getText() %>
                </c:when>

                <c:when test="${sectionEntry.key.equals(SectionType.ACHIEVEMENT) || sectionEntry.key.equals(SectionType.QUALIFICATION)}">
                    <c:forEach var="skill" items="<%=((ListSection) sectionEntry.getValue()).getValues()%>">
                        <p>-${skill}</p>
                    </c:forEach>
                </c:when>

                <c:when test="${sectionEntry.key.equals(SectionType.EDUCATION) || sectionEntry.key.equals(SectionType.EXPERIENCE)}">
                    <c:forEach var="organization"
                               items="<%=((OrganizationSection) sectionEntry.getValue()).getOrganizations()%>">
                        <jsp:useBean id="organization" type="ru.javawebinar.basejava.model.Organization"/>

                        <c:choose>
                            <c:when test="${organization.link.url == null}">${organization.link.name}</c:when>
                            <c:otherwise>
                                <a href=${organization.link.url}>${organization.link.name}</a>
                            </c:otherwise>
                        </c:choose>
                        <c:forEach var="Stage" items="<%=organization.getStages()%>">
                            <jsp:useBean id="Stage" type="ru.javawebinar.basejava.model.Organization.Stage"/>
                            <ul>
                                ${Stage.start.format(DateUtil.FORMATTER)} - ${Stage.end.format(DateUtil.FORMATTER)} <br/>
                                <b>${Stage.position}</b> <br/>
                                    ${Stage.duties}<br/>
                            </ul>
                        </c:forEach>
                    </c:forEach>
                </c:when>
            </c:choose>
        </c:if>
    </c:forEach>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>