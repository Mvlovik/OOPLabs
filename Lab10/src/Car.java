public class Car {
    private final int id;
    private final String brand;
    private final String model;
    private final int year;
    private final long price;
    private final String country;

    public Car(int id, String brand, String model, int year, long price, String country) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.country = country;
    }

    public int getId() { return id; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public long getPrice() { return price; }
    public String getCountry() { return country; }

    @Override
    public String toString() {
        return id + "," + brand + "," + model + "," + year + "," + price + "," + country;
    }
}
