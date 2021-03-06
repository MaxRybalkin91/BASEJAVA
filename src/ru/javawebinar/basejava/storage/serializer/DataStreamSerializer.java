package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
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
                dos.writeUTF(entry.getValue());
            });

            writeWithException(dos, resume.getSections().entrySet(),
                    entry -> {
                        Section section = entry.getValue();
                            String sectionName = entry.getKey().name();
                            dos.writeUTF(sectionName);
                            switch (sectionName) {
                                case "PERSONAL":
                                case "OBJECTIVE":
                                    dos.writeUTF(((TextSection) section).getText());
                                    break;
                                case "ACHIEVEMENT":
                                case "QUALIFICATION":
                                    writeWithException(dos, ((ListSection) section).getValues(),
                                            dos::writeUTF);
                                    break;
                                case "EXPERIENCE":
                                case "EDUCATION":
                                    writeWithException(dos, ((OrganizationSection) section).getOrganizations(),
                                            organization -> {
                                                dos.writeUTF(organization.getLink().getName());
                                                dos.writeUTF(organization.getLink().getUrl());
                                                writeWithException(dos, organization.getStages(),
                                                        stage -> {
                                                            dos.writeUTF(stage.getStartDate());
                                                            dos.writeUTF(stage.getEndDate());
                                                            dos.writeUTF(stage.getPosition());
                                                            dos.writeUTF(stage.getDuties());
                                                        });
                                            });
                                    break;
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
                SectionType sectionType = SectionType.valueOf(sectionName);
                switch (sectionName) {
                    case "PERSONAL":
                    case "OBJECTIVE":
                        String body = dis.readUTF();
                        resume.setSections(sectionType, new TextSection(body));
                        break;

                    case "ACHIEVEMENT":
                    case "QUALIFICATION":
                        List<String> skillList = new ArrayList<>();

                        readWithException(dis, () -> skillList.add(dis.readUTF()));
                        resume.setSections(sectionType, new ListSection(skillList));
                        break;
                    case "EXPERIENCE":
                    case "EDUCATION":
                        List<Organization> orgList = new ArrayList<>();

                        readWithException(dis, () -> {
                            String linkName = dis.readUTF();
                            String linkUrl = dis.readUTF();

                            List<Organization.Stage> stages = new ArrayList<>();

                            readWithException(dis, () -> {
                                String startDate = dis.readUTF();
                                String endDate = dis.readUTF();
                                String title = dis.readUTF();
                                String description = dis.readUTF();

                                stages.add(new Organization.Stage(
                                        LocalDate.parse(startDate),
                                        LocalDate.parse(endDate),
                                        title,
                                        description));
                            });

                            Organization organization = new Organization((new Link(linkName, linkUrl)),
                                    stages);
                            orgList.add(organization);
                        });

                        resume.setSections(sectionType, new OrganizationSection(orgList));
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