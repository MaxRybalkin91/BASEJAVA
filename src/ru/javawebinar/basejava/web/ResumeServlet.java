package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.storage.SqlStorage;
import ru.javawebinar.basejava.util.DateUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResumeServlet extends HttpServlet {
    private SqlStorage sqlStorage;

    @Override
    public void init() {
        sqlStorage = new SqlStorage(Config.get().getDbUrl(),
                Config.get().getDbUser(),
                Config.get().getDbPassword());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");

        Resume resume;

        if (isEmpty(uuid)) {
            resume = new Resume(fullName);
        } else {
            resume = sqlStorage.get(uuid);
            resume.setFullName(fullName);
        }

        for (ContactType ct : ContactType.values()) {
            String value = request.getParameter(ct.name());
            resume.setContacts(ct, value);
        }
        for (SectionType st : SectionType.values()) {
            String value = request.getParameter(st.name());
            switch (st) {
                case PERSONAL:
                case OBJECTIVE:
                    resume.setSections(st, new TextSection(value));
                    break;
                case ACHIEVEMENT:
                case QUALIFICATION:
                    List<String> skills = Arrays.asList(value.split("\n"));
                    resume.setSections(st, new ListSection(skills));
                    break;
                case EDUCATION:
                case EXPERIENCE:
                    List<Organization> orgList = new ArrayList<>();
                    String[] values = request.getParameterValues(st.name() + "_name");
                    if (values != null) {
                        for (int i = 0; i < values.length; i++) {
                            if (!isEmpty(values[i])) {
                                List<Organization.Stage> stages = new ArrayList<>();
                                String prefix = st.name() + "_" + (i + 1);
                                String[] startDates = request.getParameterValues(prefix + "_start");
                                String[] endDates = request.getParameterValues(prefix + "_end");
                                String[] positions = request.getParameterValues(prefix + "_position");
                                String[] duties = request.getParameterValues(prefix + "_dutie");

                                for (int k = 0; k < positions.length; k++) {
                                    if (!isEmpty(positions[k], startDates[k], endDates[k])) {
                                        Organization.Stage stage = new Organization.Stage(DateUtil.toDate(startDates[k]),
                                                DateUtil.toDate(endDates[k]), positions[k]);
                                        if (!isEmpty(duties[k])) {
                                            stage.setDuties(duties[k]);
                                        }
                                        stages.add(stage);
                                    }
                                }

                                String newStartDate = request.getParameter(prefix + "_NEW_start");
                                String newEndDate = request.getParameter(prefix + "_NEW_end");
                                String newTitle = request.getParameter(prefix + "_NEW_position");
                                String newDescription = request.getParameter(prefix + "_NEW_dutie");
                                if (!isEmpty(newStartDate, newEndDate, newTitle)) {
                                    Organization.Stage period = new Organization.Stage(DateUtil.toDate(newStartDate),
                                            DateUtil.toDate(newEndDate), newTitle);
                                    if (!isEmpty(newDescription)) {
                                        period.setPosition(newDescription);
                                    }
                                    stages.add(period);
                                }

                                String[] urls = request.getParameterValues(prefix + "_url");
                                Link link = new Link(values[i]);
                                if (!isEmpty(urls[i])) {
                                    link.setUrl(urls[i]);
                                }
                                orgList.add(new Organization(link, stages));
                            }
                        }
                    }

                    String newName = request.getParameter(st.name() + "_NEW_name");
                    String newUrl = request.getParameter(st.name() + "_NEW_url");
                    String newStartDate = request.getParameter(st.name() + "_NEW_start");
                    String newEndDate = request.getParameter(st.name() + "_NEW_end");
                    String newTitle = request.getParameter(st.name() + "_NEW_position");
                    String newDescription = request.getParameter(st.name() + "_NEW_dutie");
                    if (!isEmpty(newName)) {
                        Link link = new Link(newName);
                        if (!isEmpty(newUrl)) {
                            link.setUrl(newUrl);
                        }
                        if (!isEmpty(newStartDate, newEndDate, newTitle)) {
                            Organization.Stage period = new Organization.Stage(DateUtil.toDate(newStartDate),
                                    DateUtil.toDate(newEndDate), newTitle);
                            if (!isEmpty(newDescription)) {
                                period.setPosition(newDescription);
                            }
                            orgList.add(new Organization(link, period));
                        }
                    }

                    resume.setSections(st, new OrganizationSection(orgList));
            }
        }

        if (isEmpty(uuid)) {
            sqlStorage.save(resume);
        } else {
            sqlStorage.update(resume);
        }
        response.sendRedirect("resume");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", sqlStorage.getSortedStorage());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }

        Resume resume = null;
        String page;

        switch (action) {
            case "submitClear":
            case "submitDelete":
                page = "/WEB-INF/jsp/clear.jsp";
                break;
            case "clear":
            case "delete":
                if (action.equals("clear")) {
                    sqlStorage.clear();
                } else {
                    sqlStorage.delete(uuid);
                }
                response.sendRedirect("resume");
                return;
            case "add":
            case "view":
            case "edit":
                resume = action.equals("add") ? new Resume() : sqlStorage.get(uuid);
                page = action.equals("view") ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp";
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", resume);
        request.getRequestDispatcher(page).forward(request, response);
    }

    private boolean isEmpty(String... values) {
        boolean isEmpty = false;
        for (String value : values) {
            if (value == null || value.trim().length() == 0) {
                isEmpty = true;
                break;
            }
        }
        return isEmpty;
    }
}