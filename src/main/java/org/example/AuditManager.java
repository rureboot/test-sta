package org.example;

import java.io.IOException;
import java.time.LocalDateTime;

public class AuditManager {

    private final AuditDataWriter fileWriter;

    public AuditManager(AuditDataWriter fileWriter) {
        this.fileWriter = fileWriter;
    }

    public void addRecord(String userName, LocalDateTime eventTime) throws IOException {
        fileWriter.writeRecord(userName, eventTime);
    }
}
