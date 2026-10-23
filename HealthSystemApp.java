package phr;

import java.util.Map;
import java.util.Scanner;

public class HealthSystemApp {
    private static Map<String, HealthRecord> recordDatabase;
    private static Scanner consoleInput = new Scanner(System.in);

    public static void main(String[] args) {
        recordDatabase = StorageManager.loadRecords();
        System.out.println("=================================================");
        System.out.println("   PERSONAL HEALTH RECORD SYSTEM INITIALIZED     ");
        System.out.println("=================================================");

        while (true) {
            System.out.println("\n--- MAIN MONITORING INTERFACE ---");
            System.out.println("1. Create New Patient Health Profile");
            System.out.println("2. Record New Daily Vitals Entry");
            System.out.println("3. View Complete Historical Patient Dashboard");
            System.out.println("4. Exit and Save System Progress");
            System.out.print("Select a menu option parameter (1-4): ");
            
            String selection = consoleInput.nextLine();
            switch (selection) {
                case "1" -> createProfile();
                case "2" -> captureVitals();
                case "3" -> renderDashboard();
                case "4" -> {
                    StorageManager.saveRecords(recordDatabase);
                    System.out.println("All transaction states committed to local file storage. Shutting down.");
                    return;
                }
                default -> System.out.println("Invalid input token. Please designate valid control parameter.");
            }
        }
    }

    private static void createProfile() {
        System.out.print("Enter unique Alpha-Numeric Identification Key (ID): ");
        String id = consoleInput.nextLine().trim().toUpperCase();
        if (recordDatabase.containsKey(id)) {
            System.out.println("Operational Error: Profile key already mapped within dataset index mapping.");
            return;
        }
        System.out.print("Enter Full Legal Biological Identification Name: ");
        String name = consoleInput.nextLine();
        System.out.print("Enter Age: ");
        int age = Integer.parseInt(consoleInput.nextLine());
        System.out.print("Enter Blood Type Classification (e.g., O+, A-): ");
        String blood = consoleInput.nextLine();

        HealthRecord newRecord = new HealthRecord(id, name, age, blood);
        recordDatabase.put(id, newRecord);
        System.out.println("Profile successfully written to memory registry.");
    }

    private static void captureVitals() {
        System.out.print("Enter Patient Identification Key: ");
        String id = consoleInput.nextLine().trim().toUpperCase();
        HealthRecord record = recordDatabase.get(id);
        if (record == null) {
            System.out.println("Identification Lookup Error: Match target missing inside current execution scope.");
            return;
        }

        System.out.print("Input Patient Temperature (Celsius - e.g., 36.8): ");
        double temp = Double.parseDouble(consoleInput.nextLine());
        System.out.print("Input Pulse Frequency (BPM): ");
        int hr = Integer.parseInt(consoleInput.nextLine());
        System.out.print("Input Systemic Arterial Pressure Profile (Systolic/Diastolic e.g. 120/80): ");
        String bp = consoleInput.nextLine();

        Vitals entry = new Vitals(temp, hr, bp);
        record.addNewLog(entry);
        System.out.println("Biometric entry processing complete.");
    }

    private static void renderDashboard() {
        System.out.print("Enter Patient Identification Key: ");
        String id = consoleInput.nextLine().trim().toUpperCase();
        HealthRecord record = recordDatabase.get(id);
        if (record == null) {
            System.out.println("Identification Lookup Error: Match target missing inside current execution scope.");
            return;
        }

        System.out.println("\n=================================================");
        System.out.printf(" HEALTH MANAGEMENT SYSTEM PORTAL FOR: %s \n", record.getPatientName().toUpperCase());
        System.out.println("=================================================");
        System.out.printf("Patient Unique ID : %s\n", record.getRecordId());
        System.out.printf("Biological Age     : %d Years\n", record.getAge());
        System.out.printf("Blood Phenotype   : %s\n", record.getBloodType());
        System.out.println("-------------------------------------------------");
        System.out.println("HISTORICAL METRIC LOG TIMELINE:");
        
        if (record.getVitalsHistory().isEmpty()) {
            System.out.println("   [Empty Timeline Set: No data logged to date]");
        } else {
            for (Vitals v : record.getVitalsHistory()) {
                System.out.println(" " + v.toString());
            }
        }
        System.out.println("=================================================");
    }
}
