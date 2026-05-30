package pharmastock.model;

public abstract class Medication implements Comparable<Medication> {

    private String id;
    private String name;
    private double price;
    private int stockCount;

    public Medication(String id, String name, double price, int stockCount) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stockCount = stockCount;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStockCount() { return stockCount; }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setStockCount(int stockCount) { this.stockCount = stockCount; }

    @Override
    public int compareTo(Medication other) {
        return this.name.compareToIgnoreCase(other.name);
    }


    @Override
    public String toString() {

        return this.name + " (Available Stock: " + this.stockCount + ")";
    }


    public abstract String displayInfo();
}