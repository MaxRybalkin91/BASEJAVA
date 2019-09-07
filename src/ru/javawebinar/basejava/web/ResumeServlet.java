package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.storage.SqlStorage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            }
        }

        if (isEmpty(uuid)) {
            sqlStorage.save(new Resume(fullName));
        } else {
            sqlStorage.update(resume);
        }
        response.sendRedirect("resume");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
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

    private boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }
}
