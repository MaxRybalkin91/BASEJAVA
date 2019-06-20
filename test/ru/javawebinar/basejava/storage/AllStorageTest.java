package ru.javawebinar.basejava.storage;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ArrayStorageTest.class,
        FileStorageTest.class,
        JsonStorageTest.class,
        ListStorageTest.class,
        MapResumeStorageTest.class,
        MapUuidStorageTest.class,
        PathStorageTest.class,
        SortedArrayStorageTest.class,
        XmlStorageTest.class
})

public class AllStorageTest {

}