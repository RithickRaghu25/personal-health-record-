package phr;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Vitals {
    private double bodyTemperature; // Celsius
    private int heartRate;          // BPM
    private String bloodPressure;   // e.g., "120/80"
    private LocalDateTime logTime;

    public Vitals(double bodyTemperature, int heartRate, String bloodPressure) {
        this.bodyTemperature = bodyTemperature;
        this.heartRate = heartRate;
        this.bloodPressure = bloodPressure;
        this.logTime = LocalDateTime.now();
    }

    // Constructor used when loading historical data from text files
    public Vitals(double bodyTemperature, int heartRate, String bloodPressure, String isoTimeString) {
        this.bodyTemperature = bodyTemperature;
        this.heartRate = heartRate;
        this.bloodPressure = bloodPressure;
        this.logTime = LocalDateTime.parse(isoTimeString);
    }

    public double getBodyTemperature() { return bodyTemperature; }
    public int getHeartRate() { return heartRate; }
    public String getBloodPressure() { return bloodPressure; }
    public LocalDateTime getLogTime() { return logTime; }

    public String generateHealthStatusSummary() {
        StringBuilder status = new StringBuilder();
        
        // Dynamic Condition Evaluation
        if (bodyTemperature > 37.5) status.append("[Fever Alert] ");
        else if (bodyTemperature < 35.0) status.append("[Hypothermia Alert] ");
        
        if (heartRate > 100) status.append("[Tachycardia Alert] ");
        else if (heartRate < 60) status.append("[Bradycardia Alert] ");
        
        try {
            String[] bpParts = bloodPressure.split("/");
            int systolic = Integer.parseInt(bpParts[0].trim());
            int diastolic = Integer.parseInt(bpParts[1].trim());
            if (systolic >= 130 || diastolic >= 80) status.append("[High BP Alert] ");
        } catch (Exception e) {
            status.append("[Invalid BP format] ");
        }

        return status.length() == 0 ? "Normal / Healthy" : status.toString().trim();
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("[%s] Temp: %.1f°C | HR: %d BPM | BP: %s -> Status: %s", 
            logTime.format(formatter), bodyTemperature, heartRate, bloodPressure, generateHealthStatusSummary());
    }
}
