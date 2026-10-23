package phr;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class StorageManager {
    private static final String DATA_FILE = "health_records_data.txt";
    private static final String DELIMITER = ";;;";

    public static void saveRecords(Map<String, HealthRecord> database) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE))) {
            for (HealthRecord record : database.values()) {
                // Save Demographics Line
                writer.write(String.format("USER%s%s%s%d%s%s", DELIMITER, 
                    record.getRecordId(), DELIMITER, record.getAge(), DELIMITER, 
                    record.getPatientName(), DELIMITER, record.getBloodType()));
                writer.newLine();
                
                // Save linked vital timelines
                for (Vitals v : record.getVitalsHistory()) {
                    writer.write(String.format("VITAL%s%s%s%.2f%s%d%s%s%s%s", DELIMITER,
                        record.getRecordId(), DELIMITER, v.getBodyTemperature(), DELIMITER,
                        v.getHeartRate(), DELIMITER, v.getBloodPressure(), DELIMITER, v.getLogTime().toString()));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("System Alert: Failed to safely preserve database file modifications to system disk.");
        }
    }

    public static Map<String, HealthRecord> loadRecords() {
        Map<String, HealthRecord> database = new HashMap<>();
        File file = new File(DATA_FILE);
        if (!file.exists()) return database;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] segments = line.split(DELIMITER);
                if (segments[0].equals("USER")) {
                    HealthRecord record = new HealthRecord(segments[1], segments[3], Integer.parseInt(segments[2]), segments[4]);
                    database.put(record.getRecordId(), record);
                } else if (segments[0].equals("VITAL")) {
                    HealthRecord targetRecord = database.get(segments[1]);
                    if (targetRecord != null) {
                        Vitals loadedVitals = new Vitals(
                            Double.parseDouble(segments[2]), 
                            Integer.parseInt(segments[3]), 
                            segments[4], 
                            segments[5]
                        );
                        targetRecord.addNewLog(loadedVitals);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("System Alert: Data structural corruption detected. Generating clean repository baseline.");
        }
        return database;
    }
}
