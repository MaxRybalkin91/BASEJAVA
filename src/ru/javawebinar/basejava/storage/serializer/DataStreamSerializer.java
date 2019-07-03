package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class DataStreamSerializer implements StreamSerializer {

    private interface DataWriter<T> {
        void write(T t) throws IOException;
    }

    private interface DataReader {
        void read() throws IOException;
    }

    @Override
    public void writeToStorage(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());

            writeWithException(dos, resume.getContacts().entrySet(), entry -> {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue().getName());
            });

            writeWithException(dos, resume.getSections().entrySet(),
                    entry -> {
                        SectionType section = entry.getKey();
                        if (section != null) {
                            AbstractSection abstractSection = entry.getValue();
                            String sectionName = entry.getKey().name();
                            dos.writeUTF(sectionName);
                            switch (sectionName) {
                                case "PERSONAL":
                                case "OBJECTIVE":
                                    dos.writeUTF(((TextSection) abstractSection).getText());
                                    break;
                                case "ACHIEVEMENT":
                                case "QUALIFICATION":
                                    writeWithException(dos, ((ListSection) abstractSection).getValues(),
                                            dos::writeUTF);
                                    break;
                                case "EXPERIENCE":
                                case "EDUCATION":
                                    writeWithException(dos, ((OrganizationSection) abstractSection).getOrganizations(),
                                            organization -> {
                                                dos.writeUTF(organization.getLink().getName());
                                                writeWithException(dos, organization.getPeriods(),
                                                        period -> {
                                                            dos.writeUTF(period.getStartDate());
                                                            dos.writeUTF(period.getEndDate());
                                                            dos.writeUTF(period.getPosition());
                                                            dos.writeUTF(period.getDuties());
                                                        });
                                            });
                                    break;
                            }
                        }
                    });

        }
    }

    private <T> void writeWithException(DataOutputStream dos, Collection<T> collection, DataWriter<T> dataWriter) throws IOException {
        dos.writeInt(collection.size());
        for (T t : collection) {
            dataWriter.write(t);
        }
    }

    @Override
    public Resume readFromStorage(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);

            readWithException(dis, () -> {
                String contactType = dis.readUTF();
                String contactValue = dis.readUTF();
                resume.setContacts(ContactType.valueOf(contactType), contactValue);
            });

            readWithException(dis, () -> {
                String sectionName = dis.readUTF();
                switch (sectionName) {
                    case "PERSONAL":
                    case "OBJECTIVE":
                        String body = dis.readUTF();
                        resume.setSections(SectionType.valueOf(sectionName), new TextSection(body));
                        break;

                    case "ACHIEVEMENT":
                    case "QUALIFICATION":
                        List<String> skillList = new LinkedList<>();

                        readWithException(dis, () -> skillList.add(dis.readUTF()));
                        resume.setSections(SectionType.valueOf(sectionName), new ListSection(skillList));
                        break;
                    case "EXPERIENCE":
                    case "EDUCATION":
                        List<Organization> orgList = new LinkedList<>();

                        readWithException(dis, () -> {
                            String link = dis.readUTF();

                            List<Organization.Period> periods = new LinkedList<>();

                            readWithException(dis, () -> {
                                String startDate = dis.readUTF();
                                String endDate = dis.readUTF();
                                String title = dis.readUTF();
                                String description = dis.readUTF();

                                periods.add(new Organization.Period(
                                        LocalDate.parse(startDate),
                                        LocalDate.parse(endDate),
                                        title,
                                        description));
                            });

                            Organization organization = new Organization((new Link(link)),
                                    periods);
                            orgList.add(organization);
                        });

                        resume.setSections(SectionType.valueOf(sectionName), new OrganizationSection(orgList));
                        break;
                }
            });
            return resume;
        }
    }

    private void readWithException(DataInputStream dis, DataReader reader) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            reader.read();
        }
    }
}