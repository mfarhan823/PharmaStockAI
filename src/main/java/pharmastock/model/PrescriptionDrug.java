package pharmastock.model;

public class PrescriptionDrug extends Medication {

    private boolean doctorLicenseRequired;
    private int scheduleLevel;

    public PrescriptionDrug(String id, String name, double price, int stockCount,
                            boolean doctorLicenseRequired, int scheduleLevel) {
        super(id, name, price, stockCount);
        this.doctorLicenseRequired = doctorLicenseRequired;
        this.scheduleLevel = scheduleLevel;
    }

    public boolean isDoctorLicenseRequired() { return doctorLicenseRequired; }
    public void setDoctorLicenseRequired(boolean doctorLicenseRequired) { this.doctorLicenseRequired = doctorLicenseRequired; }

    public int getScheduleLevel() { return scheduleLevel; }
    public void setScheduleLevel(int scheduleLevel) { this.scheduleLevel = scheduleLevel; }

    @Override
    public String displayInfo() {
        return "[PRESCRIPTION] " +
                "ID: " + getId() + " | " +
                "Name: " + getName() + " | " +
                "Price: Rs." + String.format("%.2f", getPrice()) + " | " +
                "Stock: " + getStockCount() + " | " +
                "License Required: " + (doctorLicenseRequired ? "Yes" : "No") + " | " +
                "Schedule Level: " + scheduleLevel;
    }
}
