package pharmastock.service;

import pharmastock.model.*;
import pharmastock.exception.InsufficientStockException;

import java.util.ArrayList;
import java.util.Collections;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InventoryManager {

    private ArrayList<Medication> medicineList;

    // File will be saved in user's home directory — always works
    private static final String FILE_PATH =
            System.getProperty("user.home") + File.separator + "pharmacy_db.txt";

    public InventoryManager() {
        this.medicineList = new ArrayList<>();
    }

    // Method Overloading - Version 1: Prescription
    public void addMedicine(String id, String name, double price, int stock,
                            boolean licenseRequired, int scheduleLevel) {
        PrescriptionDrug drug = new PrescriptionDrug(id, name, price, stock, licenseRequired, scheduleLevel);
        medicineList.add(drug);
    }

    // Method Overloading - Version 2: OTC
    public void addMedicine(String id, String name, double price, int stock,
                            String symptom, double discountRate) {
        OverTheCounter otc = new OverTheCounter(id, name, price, stock, symptom, discountRate);
        medicineList.add(otc);
    }

    public ArrayList<Medication> getMedicineList() {
        return medicineList;
    }

    public void sortInventory() {
        Collections.sort(medicineList);
    }

    public void reduceStock(String id, int amount) throws InsufficientStockException {
        for (Medication med : medicineList) {
            if (med.getId().equals(id)) {
                if (med.getStockCount() < amount) {
                    throw new InsufficientStockException(
                            "Not enough stock for: " + med.getName() +
                                    ". Available: " + med.getStockCount() +
                                    ", Requested: " + amount
                    );
                }
                med.setStockCount(med.getStockCount() - amount);
                return;
            }
        }
    }

    // Polymorphism: displayInfo() calls the correct subclass version at runtime
    public String getAllMedicinesAsText() {
        if (medicineList.isEmpty()) {
            return "No medicines in inventory.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("===== PHARMACY INVENTORY =====\n");
        sb.append("Total Items: ").append(medicineList.size()).append("\n");
        sb.append("------------------------------\n");

        for (Medication med : medicineList) {
            sb.append(med.displayInfo()).append("\n");
        }

        sb.append("==============================");
        return sb.toString();
    }

    private static final String BILLS_FILE_PATH =
            System.getProperty("user.home") + File.separator + "pharmacy_bills.txt";

    public void saveBill(String billText) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter(BILLS_FILE_PATH, true));
            writer.println("Date/Time: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            writer.println(billText);
            writer.println();
            System.out.println("Bill saved at: " + BILLS_FILE_PATH);
        } catch (Exception e) {
            System.out.println("Error saving bill: " + e.getMessage());
        } finally {
            if (writer != null) writer.close();
        }
    }

    // Returns file path so UI can show user where file is saved
    public String getFilePath() {
        return FILE_PATH;
    }

    public void saveFile() {
        PrintWriter writer = null;
        try {
            File file = new File(FILE_PATH);
            writer = new PrintWriter(file);

            // Polymorphism: each subclass handles its own serialization via toFileString()
            // No instanceof needed — clean OOP design
            for (Medication med : medicineList) {
                writer.println(med.toFileString());
            }

            System.out.println("File saved at: " + FILE_PATH);

        } catch (Exception e) {
            System.out.println("Error saving file: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public void loadFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("No saved file found at: " + FILE_PATH);
            return;
        }

        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
            medicineList.clear();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                // Limit split to 7 parts — protects against commas inside medicine names
                String[] parts = line.split(",", 7);

                if (parts[0].equals("PRESCRIPTION") && parts.length == 7) {
                    addMedicine(parts[1], parts[2],
                            Double.parseDouble(parts[3]),
                            Integer.parseInt(parts[4]),
                            Boolean.parseBoolean(parts[5]),
                            Integer.parseInt(parts[6])
                    );
                } else if (parts[0].equals("OTC") && parts.length == 7) {
                    addMedicine(parts[1], parts[2],
                            Double.parseDouble(parts[3]),
                            Integer.parseInt(parts[4]),
                            parts[5],
                            Double.parseDouble(parts[6])
                    );
                }
            }

            System.out.println("File loaded from: " + FILE_PATH);

        } catch (Exception e) {
            System.out.println("Error loading file: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }
}