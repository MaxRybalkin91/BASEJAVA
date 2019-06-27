package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ru.javawebinar.basejava.model.SectionType.getSectionType;

public class DataStreamSerializer implements StreamSerializer {
    @Override
    public void writeToStorage(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());

            dos.writeInt(resume.getContacts().size());
            for (Map.Entry<ContactType, Link> entry : resume.getContacts().entrySet()) {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue().toString());
            }

            for (Map.Entry<SectionType, AbstractSection> entry : resume.getSections().entrySet()) {
                SectionType sectionType = entry.getKey();
                if (sectionType != null) {
                    switch (sectionType) {
                        case OBJECTIVE:
                        case PERSONAL:
                            writeTextSection(dos, resume, sectionType);
                            break;
                        case ACHIEVEMENT:
                        case QUALIFICATIONS:
                            writeListSection(dos, resume, sectionType);
                            break;
                        case EXPERIENCE:
                        case EDUCATION:
                            writeOrganizationSection(dos, resume, sectionType);
                            break;
                    }
                }
            }
        }
    }

    @Override
    public Resume readFromStorage(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);

            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                resume.setContacts(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }

            for (Map.Entry<SectionType, AbstractSection> entry : resume.getSections().entrySet()) {
                SectionType sectionType = entry.getKey();
                switch (sectionType) {
                    case OBJECTIVE:
                    case PERSONAL:
                        readTextSection(dis, resume);
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        readListSection(dis, resume);
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        readOrganizationSection(dis, resume);
                        break;
                }
            }

            return resume;
        }
    }

    private void writeTextSection(DataOutputStream dos, Resume resume, SectionType sectionType) throws IOException {
        TextSection textSection = (TextSection) resume.getSection(sectionType);
        dos.writeUTF(sectionType.name());
        dos.writeUTF(textSection.getValue());
    }

    private void writeListSection(DataOutputStream dos, Resume resume, SectionType sectionType) throws IOException {
        ListSection listSection = (ListSection) resume.getSection(sectionType);

        dos.writeUTF(sectionType.name());
        List<String> skills = listSection.getValues();
        dos.writeInt(skills.size());
        for (String skill : skills) {
            dos.writeUTF(skill);
        }
    }

    private void writeOrganizationSection(DataOutputStream dos, Resume resume, SectionType sectionType) throws IOException {
        OrganizationSection organizationSection = (OrganizationSection) resume.getSection(sectionType);

        dos.writeUTF(sectionType.name());
        List<Organization> organizations = organizationSection.getOrganizations();
        dos.writeInt(organizations.size());
        for (Organization organization : organizations) {
            dos.writeUTF(organization.getLink().toString());
            List<Organization.Period> periods = organization.getPeriods();
            dos.writeInt(periods.size());
            for (Organization.Period period : periods) {
                dos.writeUTF(period.getStart().toString());
                dos.writeUTF(period.getEnd().toString());
                dos.writeUTF(period.getPosition());
                dos.writeUTF(period.getDuties());
            }
        }
    }

    private void readTextSection(DataInputStream dis, Resume resume) throws IOException {
        String name = dis.readUTF();
        String value = dis.readUTF();
        resume.setSections(getSectionType(name), new TextSection(value));
    }

    private void readListSection(DataInputStream dis, Resume resume) throws IOException {
        String name = dis.readUTF();
        int size = dis.readInt();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(dis.readUTF());
        }
        if (list.size() != 0) {
            resume.setSections(getSectionType(name), new ListSection(list));
        }
    }

    private void readOrganizationSection(DataInputStream dis, Resume resume) throws IOException {
        String name = dis.readUTF();
        List<Organization> orgList = new ArrayList<>();
        int organizationSize = dis.readInt();
        for (int i = 0; i < organizationSize; i++) {
            Organization org = new Organization(new Link(dis.readUTF()), new ArrayList<>());
            int periodSize = dis.readInt();
            for (int j = 0; j < periodSize; j++) {
                org.addPeriod(new Organization.Period(
                        LocalDate.parse(dis.readUTF()),
                        LocalDate.parse(dis.readUTF()),
                        dis.readUTF(),
                        dis.readUTF()));
            }
            orgList.add(org);
        }
        resume.setSections(getSectionType(name), new OrganizationSection(orgList));
    }
}