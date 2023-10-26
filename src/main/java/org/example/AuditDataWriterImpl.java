package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

public class AuditDataWriterImpl implements AuditDataWriter {
    private static final String DEFAULT_FILE_NAME = "audit_0.csv";
    private static final String FILE_EXTENSION = ".csv";
    private static final String FILE_PREFIX = "audit_";
    private final int maxLinesPerFile;
    private final String directoryPath;

    public AuditDataWriterImpl(int maxLinesPerFile, String directoryPath) {
        this.maxLinesPerFile = maxLinesPerFile;
        this.directoryPath = directoryPath;
    }

    @Override
    public void writeRecord(String userName, LocalDateTime eventTime) throws IOException {
        List<Path> filePaths;
        try (Stream<Path> sorted = Files.list(Path.of(directoryPath)).sorted()) {
            filePaths = sorted.toList();
        }

        String newRecord = userName + ";" + eventTime + "\n";

        if (filePaths.isEmpty()) {
            createNewFileAndWriteRecord(newRecord, DEFAULT_FILE_NAME);
        } else {
            addRecord(filePaths, newRecord);
        }

    }

    private void addRecord(List<Path> filePaths, String newRecord) throws IOException {
        int lastFilePathIndex = filePaths.size() - 1;
        Path lastFilePath = filePaths.get(lastFilePathIndex);
        List<String> lines = Files.readAllLines(lastFilePath);
        if (lines.size() < maxLinesPerFile) {
            Files.write(lastFilePath, newRecord.getBytes(), StandardOpenOption.APPEND);
        } else {
            int newFileIndex = lastFilePathIndex + 1;
            String newFileName = FILE_PREFIX + newFileIndex + FILE_EXTENSION;
            createNewFileAndWriteRecord(newRecord, newFileName);
        }
    }

    private void createNewFileAndWriteRecord(String newRecord, String fileName) throws IOException {
        Path newFilePath = Path.of(directoryPath, fileName);
        Files.write(newFilePath, newRecord.getBytes());
    }

}
