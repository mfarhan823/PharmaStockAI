package pharmastock.model;

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
}