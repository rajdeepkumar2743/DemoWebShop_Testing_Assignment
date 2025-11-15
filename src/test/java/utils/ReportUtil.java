package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReportUtil {

    private final List<String> logs = new ArrayList<>();
    private boolean testPassed = true;

    public void log(String message) {
        String timestamp = "[" + LocalDateTime.now() + "] ";
        System.out.println(timestamp + message); // console
        logs.add(timestamp + message);
    }

    public void markFailed() {
        testPassed = false;
    }

    public void writeReport(String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("========= TEST EXECUTION REPORT =========\n\n");
            for (String log : logs) {
                writer.write(log + "\n");
            }
            writer.write("\n========================================\n");
            writer.write("Overall Test Result: " + (testPassed ? "PASSED" : "FAILED") + "\n");
            writer.write("========================================\n");
            System.out.println("Report saved to: " + fileName);
        } catch (IOException e) {
            System.err.println("Error writing report: " + e.getMessage());
        }
    }
}
