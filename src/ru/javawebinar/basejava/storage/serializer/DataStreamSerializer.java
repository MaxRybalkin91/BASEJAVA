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

            dos.writeInt(resume.getSections().size());
            for (Map.Entry<SectionType, AbstractSection> entry : resume.getSections().entrySet()) {
                SectionType sectionType = entry.getKey();
                if (sectionType != null) {
                    dos.writeUTF(sectionType.name());
                    switch (sectionType) {
                        case OBJECTIVE:
                        case PERSONAL:
                            TextSection textSection = (TextSection) entry.getValue();
                            dos.writeUTF(textSection.getText());
                            break;
                        case ACHIEVEMENT:
                        case QUALIFICATIONS:
                            writeListSection(dos, resume, (ListSection) entry.getValue());
                            break;
                        case EXPERIENCE:
                        case EDUCATION:
                            writeOrganizationSection(dos, resume, (OrganizationSection) entry.getValue());
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

            int contactsSize = dis.readInt();
            for (int i = 0; i < contactsSize; i++) {
                resume.setContacts(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }

            int sectionsSize = dis.readInt();
            for (int i = 0; i < sectionsSize; i++) {
                String sectionName = dis.readUTF();

                switch (sectionName) {
                    case "OBJECTIVE":
                    case "PERSONAL":
                        readTextSection(dis, resume, sectionName);
                        break;
                    case "ACHIEVEMENT":
                    case "QUALIFICATIONS":
                        readListSection(dis, resume, sectionName);
                        break;
                    case "EXPERIENCE":
                    case "EDUCATION":
                        readOrganizationSection(dis, resume, sectionName);
                        break;
                }
            }

            return resume;
        }
    }

    private void writeListSection(DataOutputStream dos, Resume resume, ListSection listSection) throws IOException {
        List<String> skills = listSection.getValues();
        dos.writeInt(skills.size());
        for (String skill : skills) {
            dos.writeUTF(skill);
        }
    }

    private void writeOrganizationSection(DataOutputStream dos, Resume resume, OrganizationSection organizationSection) throws IOException {
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

    private void readTextSection(DataInputStream dis, Resume resume, String name) throws IOException {
        String value = dis.readUTF();
        resume.setSections(getSectionType(name), new TextSection(value));
    }

    private void readListSection(DataInputStream dis, Resume resume, String name) throws IOException {
        int size = dis.readInt();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(dis.readUTF());
        }
        if (list.size() != 0) {
            resume.setSections(getSectionType(name), new ListSection(list));
        }
    }

    private void readOrganizationSection(DataInputStream dis, Resume resume, String name) throws IOException {
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