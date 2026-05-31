package pharmastock.model;

/**
 * Subclass of Medication representing over-the-counter medicines.
 * Inherits from Medication, overrides displayInfo() and toFileString().
 */
public class OverTheCounter extends Medication {

    private String symptom;
    private double discountRate;

    public OverTheCounter(String id, String name, double price, int stockCount,
                          String symptom, double discountRate) {
        super(id, name, price, stockCount);
        this.symptom = symptom;
        this.discountRate = discountRate;
    }

    public String getSymptom() { return symptom; }
    public void setSymptom(String symptom) { this.symptom = symptom; }

    public double getDiscountRate() { return discountRate; }
    public void setDiscountRate(double discountRate) { this.discountRate = discountRate; }

    // Polymorphism: overrides Displayable.displayInfo() via Medication
    @Override
    public String displayInfo() {
        return "[OTC] " +
                "ID: " + getId() + " | " +
                "Name: " + getName() + " | " +
                "Price: Rs." + String.format("%.2f", getPrice()) + " | " +
                "Stock: " + getStockCount() + " | " +
                "Symptom: " + symptom + " | " +
                "Discount: " + discountRate + "%";
    }

    // Polymorphism: each subclass serializes itself — no instanceof needed
    @Override
    public String toFileString() {
        return "OTC," +
                getId() + "," +
                getName() + "," +
                getPrice() + "," +
                getStockCount() + "," +
                getSymptom() + "," +
                getDiscountRate();
    }
}