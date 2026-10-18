
import Shop.Smartphone;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        ArrayList<Smartphone> phones = new ArrayList<>();

        phones.add(new Smartphone("123456789012345", "x300", "Vivo", (double)10000.0F, 8000));
        phones.add(new Smartphone("123456789012344", "Uif", "iphones", (double)100000.0F, 7000));
        phones.add(new Smartphone("123456789012342", "Pef", "iphones", (double)10000.0F, 6000));
        phones.add(new Smartphone("123456789012341", "smef", "Vivo", (double)1000.0F, 4000));
        phones.add(new Smartphone("123456789012334", "pif", "wee", (double)2330000.0F, 5000));
        phones.add(new Smartphone("123456789012300", "paf", "uoo", (double)22000000.0F, 4600));
        phones.add(new Smartphone("123456289012300", "paf", "uoo", (double)20000.0F, 4600));
        phones.add(new Smartphone("123456289012300", "plf", "uoo", (double)660000.0F, 8000));
        phones.add(new Smartphone("123456289012300", "paaf", "uoo", (double)2500.0F, 4600));
        phones.add(new Smartphone("123456289012300", "padf", "Opop", (double)2000.0F, 7000));
        phones.sort(Comparator.comparing(Smartphone::getBrand)
                .thenComparingDouble(Smartphone::getPrice)
                .thenComparingInt(Smartphone::getBatteryCapacity));
        System.out.println("Сортировка (марка -> цена -> батарея):");
        phones.forEach(p -> System.out.println(p.getDescription()));

        // Фильтрация: цена < 50000
        System.out.println("\nЦена < 50000:");
        phones.stream()
                .filter(p -> p.getPrice() < 50000)
                .forEach(p -> System.out.println(p.getDescription(true)));

        // Группировка по марке
        System.out.println("\nГруппировка по марке");
        Map<String, List<Smartphone>> byBrand = phones.stream()
                .collect(Collectors.groupingBy(Smartphone::getBrand));
        byBrand.forEach((brand, list) -> System.out.println(brand + ": " + list.size()));


        // Средняя ёмкость батареи по марке
        System.out.println("\nСредняя ёмкость по марке");
        phones.stream()
                .collect(Collectors.groupingBy(
                        Smartphone::getBrand,
                        Collectors.averagingInt(Smartphone::getBatteryCapacity)))
                .forEach((brand, avg) -> System.out.printf("%s: %.0f мАч%n", brand, avg));

        // Set: дубликаты по IMEI
        Set<Smartphone> unique = new HashSet<>(phones);
        unique.add(new Smartphone("111111111111111", "Другой", "Samsung", 12345, 4000));
        System.out.println("\nУникальных смартфонов в Set: " + unique.size() + " (из " + (phones.size() + 1) + " добавленных)");

        System.out.println(phones.size());
    }
}
