//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package src;

import java.time.Year;

public class Car {
    private final long id;
    private final String brand;
    private final String model;
    private final int year;
    private final double price;
    private final String vin;

    public Car(long id, String brand, String model, int year, double price, String vin) {
        if (brand == null || brand.isBlank()) {
            throw new InvalidCarDataException("brand не может быть null или пустым");
        }
        int current = Year.now().getValue();
        if (year < 1886 || year > current) {
            throw new InvalidCarDataException("year должен быть в [1886, " + current + "]: " + year);
        }
        if (!(price > 0)) {
            throw new InvalidCarDataException("price должна быть > 0: " + price);
        }
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.vin = vin;
    }

    public long getId() { return id; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public double getPrice() { return price; }
    public String getVin() { return vin; }

    @Override
    public String toString() {
        return "Car{id=" + id + ", brand=" + brand + ", model=" + model +
                ", year=" + year + ", price=" + price + ", vin=" + vin + "}";
    }
}

// checked
class CarNotFoundException extends Exception {
    public CarNotFoundException(String message) { super(message); }
    public CarNotFoundException(String message, Throwable cause) { super(message, cause); }
}

// unchecked
class InvalidCarDataException extends RuntimeException {
    public InvalidCarDataException(String message) { super(message); }
    public InvalidCarDataException(String message, Throwable cause) { super(message, cause); }
}