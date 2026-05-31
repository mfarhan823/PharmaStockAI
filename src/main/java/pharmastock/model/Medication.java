package pharmastock.model;

/**
 * Abstract base class for all medicines.
 * Implements Comparable for alphabetical sorting.
 * Implements Displayable (custom interface) for polymorphic display.
 */
public abstract class Medication implements Comparable<Medication>, Displayable {

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

    // From Displayable interface — each subclass provides its own version (polymorphism)
    @Override
    public abstract String displayInfo();

    // Each subclass knows how to serialize itself — no instanceof needed in saveFile()
    public abstract String toFileString();
}