package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.util.DateUtil;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResumeTestData {

    public static void main(String[] args) {
        Resume resume = new Resume("uuid", "Григорий Кислин");

        initializeResumeContactTypeSection(resume);
        initializeResumeTextTypeSection(resume);
        initializeResumeListTypeSection(resume);
        initializeResumeExperience(resume);
        initializeResumeEducation(resume);

        System.out.println(resume.getFullName() + "\n");

        printResume(resume);
    }

    private static void initializeResumeContactTypeSection(Resume resume) {
        resume.setContacts(ContactType.PHONE, "+7(921) 855-0482");
        resume.setContacts(ContactType.SKYPE, "grigory.kislin");
        resume.setContacts(ContactType.EMAIL, "gkislin@yandex.ru");
        resume.setContacts(ContactType.LINKEDIN, "LINKEDIN_URL");
        resume.setContacts(ContactType.GITHUB, "GITHUB_URL");
        resume.setContacts(ContactType.STACKOVERFLOW, "STACKOVERFLOW_URL");
        resume.setContacts(ContactType.HOMEPAGE, "HOMEPAGE_URL");
    }

    private static void initializeResumeTextTypeSection(Resume resume) {
        resume.setSections(SectionType.OBJECTIVE, new TextType("Ведущий стажировок и корпоративного обучения " +
                "по Java Web и Enterprise технологиям"));
        resume.setSections(SectionType.PERSONAL, new TextType("Аналитический склад ума, сильная логика, " +
                "креативность, инициативность. Пурист кода и архитектуры"));
    }

    private static void initializeResumeListTypeSection(Resume resume) {
        List<String> achievments = new ArrayList<>();
        List<String> qualifications = new ArrayList<>();

        achievments.add("С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", " +
                "\"Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное " +
                "взаимодействие (JMS/AKKA)\". Организация онлайн стажировок и ведение проектов. Более 1000 выпускников.");
        achievments.add("Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. " +
                "Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk.");
        achievments.add("Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM. " +
                "Интеграция с 1С, Bonita BPM, CMIS, LDAP. Разработка приложения управления окружением на стеке: " +
                "Scala/Play/Anorm/JQuery. Разработка SSO аутентификации и авторизации различных ERP модулей, интеграция " +
                "CIFS/SMB java сервера.");
        achievments.add("Реализация c нуля Rich Internet Application приложения на стеке технологий JPA, Spring, Spring-MVC, " +
                "GWT, ExtGWT (GXT), Commet, HTML5, Highstock для алгоритмического трейдинга.");
        achievments.add("Создание JavaEE фреймворка для отказоустойчивого взаимодействия слабо-связанных сервисов " +
                "(SOA-base архитектура, JAX-WS, JMS, AS Glassfish). Сбор статистики сервисов и информации о состоянии " +
                "через систему мониторинга Nagios. Реализация онлайн клиента для администрирования и мониторинга системы " +
                "по JMX (Jython/ Django).");
        achievments.add("Реализация протоколов по приему платежей всех основных платежных системы " +
                "России (Cyberplat, Eport, Chronopay, Сбербанк), Беларуcи(Erip, Osmp) и Никарагуа.");

        qualifications.add("JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2");
        qualifications.add("Version control: Subversion, Git, Mercury, ClearCase, Perforce");
        qualifications.add("DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle");
        qualifications.add("MySQL, SQLite, MS SQL, HSQLDB");
        qualifications.add("Languages: Java, Scala, Python/Jython/PL-Python, JavaScript, Groovy");
        qualifications.add("XML/XSD/XSLT, SQL, C/C++, Unix shell scripts");
        qualifications.add("Java Frameworks: Java 8 (Time API, Streams), Guava, Java Executor, MyBatis, Spring " +
                "(MVC, Security, Data, Clouds, Boot), JPA (Hibernate, EclipseLink), Guice, GWT(SmartGWT, ExtGWT/GXT), " +
                "Vaadin, Jasperreports, Apache Commons, Eclipse SWT, JUnit, Selenium (htmlelements)");
        qualifications.add("Python: Django");
        qualifications.add("JavaScript: jQuery, ExtJS, Bootstrap.js, underscore.js");
        qualifications.add("Scala: SBT, Play2, Specs2, Anorm, Spray, Akka");
        qualifications.add("Технологии: Servlet, JSP/JSTL, JAX-WS, REST, EJB, RMI, JMS, JavaMail, JAXB, StAX, SAX, DOM, " +
                "XSLT, MDB, JMX, JDBC, JPA, JNDI, JAAS, SOAP, AJAX, Commet, HTML5, ESB, CMIS, BPMN2, LDAP, OAuth1, OAuth2, JWT.");
        qualifications.add("Инструменты: Maven + plugin development, Gradle, настройка Ngnix");
        qualifications.add("Администрирование Hudson/Jenkins, Ant + custom task, SoapUI, JPublisher, Flyway, Nagios, iReport, " +
                "OpenCmis, Bonita, pgBouncer.");
        qualifications.add("Отличное знание и опыт применения концепций ООП, SOA, шаблонов проектрирования, архитектурных шаблонов, " +
                "UML, функционального программирования");
        qualifications.add("Родной русский, английский \"upper intermediate\"");

        resume.setSections(SectionType.ACHIEVEMENT, new ListType(achievments));
        resume.setSections(SectionType.QUALIFICATIONS, new ListType(qualifications));
    }

    private static void initializeResumeExperience(Resume resume) {
        List<Organization> jobOrganizations = new ArrayList<>();

        jobOrganizations.add(new Organization("Java Online Projects", DateUtil.of(2013, Month.of(10)),
                LocalDate.now(), "Автор проекта.", "Создание, организация и проведение Java онлайн проектов " +
                "и стажировок."));
        jobOrganizations.add(new Organization("Wrike", DateUtil.of(2014, Month.of(10)),
                DateUtil.of(2016, Month.of(1)), "Старший разработчик (backend).",
                "Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, Maven, " +
                        "Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, авторизация " +
                        "по OAuth1, OAuth2, JWT SSO."));
        jobOrganizations.add(new Organization("RIT Center", DateUtil.of(2012, Month.of(4)),
                DateUtil.of(2014, Month.of(10)), "Java архитектор", "Организация процесса разработки " +
                "системы ERP для разных окружений: релизная политика, версионирование, ведение CI (Jenkins), миграция базы " +
                "(кастомизация Flyway), конфигурирование системы (pgBoucer, Nginx), AAA via SSO. Архитектура БД и серверной" +
                " части системы. Разработка интергационных сервисов: CMIS, BPMN2, 1C (WebServices), сервисов общего назначения " +
                "(почта, экспорт в pdf, doc, html). Интеграция Alfresco JLAN для online редактирование из браузера документов " +
                "MS Office. Maven + plugin development, Ant, Apache Commons, Spring security, Spring MVC, Tomcat,WSO2, xcmis, " +
                "OpenCmis, Bonita, Python scripting, Unix shell remote scripting via ssh tunnels, PL/Python"));
        jobOrganizations.add(new Organization("Luxoft (Deutsche Bank)", DateUtil.of(2010, Month.of(12)),
                DateUtil.of(2012, Month.of(4)), "Ведущий программист", "Участие в проекте Deutsche Bank " +
                "CRM (WebLogic, Hibernate, Spring, Spring MVC, SmartGWT, GWT, Jasper, Oracle). Реализация клиентской и " +
                "серверной части CRM. Реализация RIA-приложения для администрирования, мониторинга и анализа результатов " +
                "в области алгоритмического трейдинга. JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Highstock, Commet, HTML5."));
        jobOrganizations.add(new Organization("Yota", DateUtil.of(2008, Month.of(6)),
                DateUtil.of(2010, Month.of(12)), "Ведущий специалист", "Дизайн и имплементация Java EE " +
                "фреймворка для отдела \"Платежные Системы\" (GlassFish v2.1, v3, OC4J, EJB3, JAX-WS RI 2.1, Servlet 2.4, " +
                "JSP, JMX, JMS, Maven2). Реализация администрирования, статистики и мониторинга фреймворка. " +
                "Разработка online JMX клиента (Python/ Jython, Django, ExtJS)"));
        jobOrganizations.add(new Organization("Enkata", DateUtil.of(2007, Month.of(3)),
                DateUtil.of(2008, Month.of(6)), "Разработчик ПО", "Реализация клиентской (Eclipse RCP) " +
                "и серверной (JBoss 4.2, Hibernate 3.0, Tomcat, JMS) частей кластерного J2EE приложения (OLAP, Data mining)."));
        jobOrganizations.add(new Organization("Siemens AG", DateUtil.of(2005, Month.of(1)),
                DateUtil.of(2007, Month.of(3)), "Разработчик ПО", "Разработка информационной модели, " +
                "проектирование интерфейсов, реализация и отладка ПО на мобильной IN платформе Siemens @vantage (Java, Unix)."));
        jobOrganizations.add(new Organization("Alcatel", DateUtil.of(1997, Month.of(9)),
                DateUtil.of(2005, Month.of(1)), "Инженер по аппаратному и программному тестированию",
                "Тестирование, отладка, внедрение ПО цифровой телефонной станции Alcatel 1000 S12 (CHILL, ASM)."));

        resume.setSections(SectionType.EXPERIENCE, new OrganizationType(jobOrganizations));
    }

    private static void initializeResumeEducation(Resume resume) {
        List<Organization> educationOrganizations = new ArrayList<>();

        educationOrganizations.add(new Organization("Coursera", DateUtil.of(2013, Month.of(3)),
                DateUtil.of(2013, Month.of(5)), "\"Functional Programming Principles in Scala\" by Martin Odersky",
                ""));
        educationOrganizations.add(new Organization("Luxoft", DateUtil.of(2011, Month.of(3)),
                DateUtil.of(2011, Month.of(4)), "Курс \"Объектно-ориентированный анализ ИС. Концептуальное " +
                "моделирование на UML.\"",
                ""));
        educationOrganizations.add(new Organization("Siemens AG", DateUtil.of(2005, Month.of(1)),
                DateUtil.of(2010, Month.of(4)), "3 месяца обучения мобильным IN сетям (Берлин)",
                ""));
        educationOrganizations.add(new Organization("Alcatel", DateUtil.of(1997, Month.of(9)),
                DateUtil.of(1998, Month.of(3)), "6 месяцев обучения цифровым телефонным сетям (Москва)",
                ""));
        educationOrganizations.add(new Organization("Санкт-Петербургский национальный исследовательский " +
                "университет информационных технологий, механики и оптики", DateUtil.of(1993, Month.of(9)),
                DateUtil.of(1996, Month.of(7)), "Аспирантура (программист С, С++)",
                ""));
        educationOrganizations.add(new Organization("", DateUtil.of(1987, Month.of(9)),
                DateUtil.of(1993, Month.of(7)), "Инженер (программист Fortran, C)",
                ""));
        educationOrganizations.add(new Organization("Заочная физико-техническая школа при МФТИ",
                DateUtil.of(1984, Month.of(9)), DateUtil.of(1987, Month.of(6)),
                "Закончил с отличием", ""));

        resume.setSections(SectionType.EDUCATION, new OrganizationType(educationOrganizations));
    }

    private static void printResume(Resume resume) {
        for (Map.Entry<ContactType, String> entry : resume.getContacts().entrySet()) {
            System.out.println(entry.getKey().getTitle() + entry.getValue());
        }

        System.out.println();

        for (Map.Entry<SectionType, AbstractSection> entry : resume.getSections().entrySet()) {
            System.out.println(entry.getKey().getTitle());
            System.out.println(entry.getValue());
        }
    }
}