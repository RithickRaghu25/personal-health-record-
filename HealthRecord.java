package phr;

import java.util.ArrayList;
import java.util.List;

public class HealthRecord {
    private String recordId;
    private String patientName;
    private int age;
    private String bloodType;
    private List<Vitals> vitalsHistory;

    public HealthRecord(String recordId, String patientName, int age, String bloodType) {
        this.recordId = recordId;
        this.patientName = patientName;
        this.age = age;
        this.bloodType = bloodType;
        this.vitalsHistory = new ArrayList<>();
    }

    public String getRecordId() { return recordId; }
    public String getPatientName() { return patientName; }
    public int getAge() { return age; }
    public String getBloodType() { return bloodType; }
    public List<Vitals> getVitalsHistory() { return vitalsHistory; }

    public void addNewLog(Vitals vitalEntry) {
        this.vitalsHistory.add(vitalEntry);
    }
}
