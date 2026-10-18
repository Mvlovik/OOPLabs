package src;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final List<Car> cars = new ArrayList<>();

    static Car findById(long id) throws CarNotFoundException {
        for (Car c : cars) {
            if (c.getId() == id) return c;
        }
        throw new CarNotFoundException("Автомобиль с id " + id + " не найден");
    }

    public static void main(String[] args) {
        cars.add(new Car(1, "Toyota", "Camry", 2020, 2_500_000, "JTNB11HK0L3000001"));

        try {
            System.out.println(findById(1));
            findById(2);
        } catch (CarNotFoundException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        try {
            new Car(2, "", "X", 2020, 100, "VIN");
        } catch (InvalidCarDataException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}