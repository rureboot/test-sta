package org.example;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuditManagerTest {

    public static final String DEFAULT_FILE_NAME = "audit_0.csv";
    public static final String AUDIT1_FILE_NAME = "audit_1.csv";
    @TempDir
    private Path tempDir;


    private AuditDataWriter writer;

    @BeforeEach
    void setup() {
        writer = new AuditDataWriterImpl(2, tempDir.toString());
    }

    @Test
    void shouldContainsOnlyOneFile() throws IOException {
        writer.writeRecord("user1", LocalDateTime.now());
        List<Path> list = Files.list(tempDir).toList();
        assertEquals(list.size(), 1);
    }

    @Test
    void shouldContains2FilesAndCorrectFilenames() throws IOException {
        writer.writeRecord("user1", LocalDateTime.now());
        writer.writeRecord("user2", LocalDateTime.now());
        writer.writeRecord("user3", LocalDateTime.now());
        List<Path> list = Files.list(tempDir).toList();
        assertEquals(list.size(), 2);

        var firstFileActualValue = list.get(0).getFileName().toString();
        var secondFileActualValue = list.get(1).getFileName().toString();

        assertEquals(DEFAULT_FILE_NAME, firstFileActualValue);
        assertEquals(AUDIT1_FILE_NAME, secondFileActualValue);
    }

    @Test
    void shouldContainsCorrectData() throws IOException, CsvException {

        var user1 = "user1";
        var user1Date = LocalDateTime.now();
        var user2 = "user2";
        var user2Date = LocalDateTime.now();
        var user1expectedData = user1 + ";" + user1Date;
        var user2expectedData = user2 + ";" + user2Date;

        writer.writeRecord(user1, user1Date);
        writer.writeRecord(user2, user2Date);

        Path path = Files.list(tempDir).toList().get(0);

        try (CSVReader reader = new CSVReader(new FileReader(path.toFile()))) {
            var user1ActualData = reader.readNext()[0];
            var user2ActualData = reader.readNext()[0];
            assertEquals(user1ActualData, user1expectedData);
            assertEquals(user2ActualData, user2expectedData);
        }
    }

}