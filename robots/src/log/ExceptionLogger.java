package log;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ExceptionLogger {
    private final static String m_directory = Paths.get("").toAbsolutePath().toString();
    private final static String m_fileName = "Robots_Crash_Log.txt";
    private final static String m_LogBegin = "[LOG BEGIN]\r\n";
    private final static String m_logEnd = "[LOG END]\r\n";

    public static void writeException(StackTraceElement[] stackTrace, String message) {
        StringBuilder builder = new StringBuilder(m_LogBegin).append(message).append("\r\n");
        for (StackTraceElement element : stackTrace) {
            builder.append(element).append("\r\n");
        }
        builder.append(m_logEnd);
        makeRecord(builder.toString());
    }

    private static void makeRecord(String logBody) {
        Path path = Paths.get(m_directory + File.separator + m_fileName);
        try {
            Files.write(path, logBody.getBytes(), StandardOpenOption.APPEND, Files.exists(path) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);

        } catch (IOException exception) {
            System.out.println("something went wrong when creating the log");
        }
    }
}
