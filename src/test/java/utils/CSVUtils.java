package utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class CSVUtils {
    public static Map<String, String> readFirstRow(String csvFilePath) throws Exception {
        Path p = Path.of(csvFilePath);
        if (!Files.exists(p)) return null;
        try (Reader in = new FileReader(p.toFile())) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
            for (CSVRecord rec : records) {
                Map<String, String> map = new HashMap<>();
                rec.toMap().forEach(map::put);
                return map;
            }
        }
        return null;
    }
}
