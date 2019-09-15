<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
<%@ page import="ru.javawebinar.basejava.util.DateUtil" %>
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
            <h3>${sectionType.title}</h3>
            <c:set var="name" value="${sectionType.name()}"/>
            <c:set var="section" value="${resume.getSection(sectionType)}"/>
            <c:set var="body" value="${section.toString()}"/>
            <c:set var="textarea"><c:if test="${not empty body}">${body}</c:if></c:set>

            <c:choose>
                <%-- TextSection / ListSection --%>
                <c:when test="${sectionType.equals(SectionType.PERSONAL) || sectionType.equals(SectionType.OBJECTIVE)
                || sectionType.equals(SectionType.ACHIEVEMENT) || sectionType.equals(SectionType.QUALIFICATION)}">
                    <dd>
                        <textarea rows="4" cols="150" name="${name}">${textarea}</textarea>
                    </dd>
                </c:when>

                <%-- Experience / Education --%>
                <c:when test="${sectionType.equals(SectionType.EXPERIENCE) || sectionType.equals(SectionType.EDUCATION)}">

                    <dd>
                    <c:forEach var="org" items="${resume.getSection(sectionType).getOrganizations()}"
                               varStatus="orgCounter">

                        <c:set var="orgName">${name}_${orgCounter.count}</c:set>
                        <%-- Organization --%>
                        <dl>
                            <dt><b>Название*:</b></dt>
                            <dd>
                                <input type="text" size="50"
                                       placeholder="Оставьте это поле пустым для удаления организации"
                                       name="${name}_name"
                                       value="${org.link.name}"/>
                            </dd>
                        </dl>
                        <dl>
                            <dt>Ссылка:</dt>
                            <dd>
                                <input type="text" size="50"
                                       name="${orgName}_url"
                                       value="${org.link.url}"/>
                            </dd>
                        </dl>
                        <br>

                        <%-- Organization periods --%>
                        <c:forEach var="stage" items="${org.getStages()}">
                            <ul>
                            <dl>
                                <dt>Начало*:</dt>
                                <dd>
                                    <input type="text" size="10"
                                           placeholder="ММ-ГГГГ"
                                           name="${orgName}_start"
                                           value="${stage.start.format(DateUtil.FORMATTER)}">
                                </dd>
                            </dl>
                            <dl>
                                <dt>Окончание*:</dt>
                                <dd>
                                    <input type="text" size="10"
                                           placeholder="ММ-ГГГГ"
                                           name="${orgName}_end"
                                           value="${stage.end.format(DateUtil.FORMATTER)}">
                                </dd>
                            </dl>

                            <c:set var="position">
                                <c:choose>
                                    <c:when test="${sectionType.equals(SectionType.EXPERIENCE)}">Должность</c:when>
                                    <c:otherwise>Факультет</c:otherwise>
                                </c:choose>
                            </c:set>

                            <dl>
                                <dt>${position}*:</dt>
                                <dd>
                                    <input type="text" size="50"
                                           placeholder="Оставьте это поле пустым для удаления периода"
                                           name="${orgName}_position"
                                           value="${stage.position}">
                                </dd>
                            </dl>

                            <c:set var="duties">
                                <c:set var="dutyName">name="${orgName}_duty"</c:set>
                                <c:choose>
                                    <c:when test="${sectionType.equals(SectionType.EXPERIENCE)}">
                                        <dt>Описание:</dt>
                                        <dd><textarea rows="4" cols="150" ${dutyName}>${stage.duties}</textarea>
                                        </dd>
                                    </c:when>
                                    <c:otherwise>
                                        <dt>Уровень подготовки:</dt>
                                        <input type="text" size="50" ${dutyName} value="${stage.duties}">
                                    </c:otherwise>
                                </c:choose>
                            </c:set>

                            <dl>
                                    ${duties}
                            </dl>
                            </ul>
                        </c:forEach>

                        <%-- Add new stage --%>
                        <ul>
                        <h4>Добавить новый период в организации ${org.link.name}:</h4>
                            <dl>
                                <dt>Начало*:</dt>
                                <dd>
                                    <input type="text" size="10"
                                           placeholder="ММ-ГГГГ"
                                           name="${orgName}_NEW_start">
                                </dd>
                            </dl>
                            <dl>
                                <dt>Окончание*:</dt>
                                <dd>
                                    <input type="text" size="10"
                                           placeholder="ММ-ГГГГ"
                                           name="${orgName}_NEW_end">
                                </dd>
                            </dl>
                            <dl>
                                <dt>${position}:</dt>
                                <dd>
                                    <input type="text" size="50"
                                           placeholder="Оставьте это поле пустым для удаления периода"
                                           name="${orgName}_NEW_position">
                                </dd>
                            </dl>

                            <c:set var="newDuties">
                                <c:set var="newDuty">name="${orgName}_NEW_duty"</c:set>
                                <c:choose>
                                    <c:when test="${sectionType.equals(SectionType.EXPERIENCE)}">
                                        <dt>Описание:</dt>
                                        <dd><textarea rows="4" cols="150" ${newDuty}></textarea>
                                        </dd>
                                    </c:when>
                                    <c:otherwise>
                                        <dt>Уровень подготовки:</dt>
                                        <input type="text" size="50" ${newDuty}>
                                    </c:otherwise>
                                </c:choose>
                            </c:set>

                            <dl>
                                    ${newDuties}
                            </dl>
                        </ul>
                    </c:forEach>
                    <br>

                    <%-- Add new organization --%>
                    <c:set var="orgType"
                           value='<%=sectionType.equals(SectionType.EXPERIENCE) ? "место работы" : "образовательное учреждение"%>'/>

                    <h4>Добавить ${orgType}:</h4>
                    <dl>
                        <dt><b>Название*:</b></dt>
                        <dd>
                            <input type="text" size="50"
                                   placeholder="Оставьте это поле пустым для удаления организации"
                                   name="${name}_NEW_name"
                                   value="${org.link.name}"/>
                        </dd>
                    </dl>

                    <dl>
                        <dt>Ссылка:</dt>
                        <dd>
                            <input type="text" size="50"
                                   name="${name}_NEW_url"
                                   value="${org.link.url}"/>
                        </dd>
                    </dl>
                    <br>
                    <dl>
                        <dt>Начало*:</dt>
                        <dd>
                            <input type="text" size="10"
                                   placeholder="ММ-ГГГГ"
                                   name="${name}_NEW_start">
                        </dd>
                    </dl>
                    <dl>
                        <dt>Окончание*:</dt>
                        <dd>
                            <input type="text" size="10"
                                   placeholder="ММ-ГГГГ"
                                   name="${name}_NEW_end">
                        </dd>
                    </dl>
                    <dl>
                        <dt>${position}:</dt>
                        <dd>
                            <input type="text" size="50"
                                   name="${name}_NEW_position">
                        </dd>
                    </dl>
                    <dl>
                            ${newDuties}
                    </dl>
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