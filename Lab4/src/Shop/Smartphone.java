//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Shop;

import java.util.Objects;

public class Smartphone {
    String imei;
    String model;
    String brand;
    double price;
    int batteryCapacity;
    public static int count = 0;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Smartphone that = (Smartphone) o;
        return Objects.equals(imei, that.imei);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imei);
    }
    public Smartphone(String imei, String model, String brand, double price, int batteryCapacity) {
        ++count;
        this.setBatteryCapacity(batteryCapacity);
        this.setBrand(brand);
        this.setModel(model);
        this.setPrice(price);
        this.setImei(imei);
    }

    public static Smartphone createSmartphone(String imei, String model, String brand, double price, int batteryCapacity) {
        return new Smartphone(imei, model, brand, price, batteryCapacity);
    }

    public String getDescription() {
        return this.brand + " " + this.model + " (IMEI: " + this.imei + ") - " + this.price + " руб." + this.batteryCapacity + " мА*ч.";
    }

    public String getDescription(boolean shortly) {
        return shortly ? this.brand + " " + this.model + this.price + " руб." : this.brand + " " + this.model + " (IMEI: " + this.imei + ") - " + this.price + " руб." + this.batteryCapacity + " мА*ч.";
    }

    public String getImei() {
        return this.imei;
    }

    public double getPrice() {
        return this.price;
    }

    public int getBatteryCapacity() {
        return this.batteryCapacity;
    }

    public String getBrand() {
        return this.brand;
    }

    public String getModel() {
        return this.model;
    }

    public double calculatePrice(int sale) {
        return (double)(100 - sale) / (double)100.0F * this.price;
    }

    public double calculatePrice(int sale, boolean blackFriday) {
        double salecalc = (double)(100 - sale) / (double)100.0F;
        if (blackFriday) {
            salecalc *= (double)0.5F;
        }

        return salecalc * this.price;
    }

    public void setBatteryCapacity(int batteryCapacity) {
        if (batteryCapacity >= 500 && batteryCapacity <= 10000) {
            this.batteryCapacity = batteryCapacity;
        } else {
            System.out.println("Неверный battary capacite");
        }
    }

    public void setBrand(String brand) {
        if (brand.isEmpty()) {
            System.out.println("Barnd пуст");
        } else {
            this.brand = brand;
        }
    }

    public void setImei(String imei) {
        if (imei.length() != 15) {
            System.out.println("Неверный IMEI");
        } else {
            this.imei = imei;
        }
    }

    public void setModel(String model) {
        if (model.isEmpty()) {
            System.out.println("Model пуст");
        } else {
            this.model = model;
        }
    }

    public void setPrice(double price) {
        if (price <= (double)0.0F) {
            System.out.println("Неверный Price");
        } else {
            this.price = price;
        }
    }
}
