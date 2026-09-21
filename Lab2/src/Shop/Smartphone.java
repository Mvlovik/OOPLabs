package Shop;

public class Smartphone {
    String imei;
    String model;
    String brand;
    double price;
    int batteryCapacity;

    public static int count = 0;

    public Smartphone(String imei, String model, String brand, double price, int batteryCapacity) {
        count++;
        setBatteryCapacity(batteryCapacity);
        setBrand(brand);
        setModel(model);
        setPrice(price);
        setImei(imei);
    }
    public static Smartphone createSmartphone(String imei, String model, String brand, double price, int batteryCapacity) {
        return new Smartphone(imei, model, brand, price, batteryCapacity);
    }
    public String getDescription() {
        return brand + " " + model + " (IMEI: " + imei + ") - " +
        price + " руб." + batteryCapacity + " мА*ч.";
    }

    public String getDescription(boolean shortly) {
        if (shortly) return brand + " " + model +
                price + " руб.";
        return brand + " " + model + " (IMEI: " + imei + ") - " +
                price + " руб." + batteryCapacity + " мА*ч.";
    }

    public String getImei() {
        return imei;
    }

    public double getPrice() {
        return price;
    }

    public int getBatteryCapacity() {
        return batteryCapacity;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }
    public double calculatePrice(int sale) {
        return ((double) (100 - sale) / 100 ) * this.price;
    }
    public double calculatePrice(int sale, boolean blackFriday) {
        double salecalc = ((double) (100 - sale) / 100 );
        if (blackFriday) salecalc *= 0.5;
        return salecalc* this.price;
    }
    public void setBatteryCapacity(int batteryCapacity) {
        if (batteryCapacity < 500 || batteryCapacity > 10000) {
            System.out.println("Неверный battary capacite");
            return;
        }
        this.batteryCapacity = batteryCapacity;
    }

    public void setBrand(String brand) {
        if (brand.isEmpty())  {
            System.out.println("Barnd пуст");
            return;
        }
        this.brand = brand;
    }

    public void setImei(String imei) {
        if (imei.length() != 15) {
            System.out.println("Неверный IMEI");
            return;
        }
        this.imei = imei;
    }

    public void setModel(String model) {
        if (model.isEmpty())  {
            System.out.println("Model пуст");
            return;
        }
        this.model = model;
    }

    public void setPrice(double price) {
        if (price <= 0) {
            System.out.println("Неверный Price");
            return;
        }
        this.price = price;
    }
}
