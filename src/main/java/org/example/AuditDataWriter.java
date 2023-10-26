package org.example;

import java.io.IOException;
import java.time.LocalDateTime;

public interface AuditDataWriter {

     void writeRecord(String userName, LocalDateTime eventTime) throws IOException;
}
