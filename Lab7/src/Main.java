package src;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    record Car(int id, String brand, String model, int year, double price, String vin) {}

    static int total;
    static final Map<String, Integer> errors = new LinkedHashMap<>();
    static final Set<String> vins = new HashSet<>();

    // Разбиение строки с учётом кавычек и "" (кастомный парсер из конспекта)
    static List<String> parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') { field.append('"'); i++; }
                    else inQuotes = false;
                } else field.append(c);
            } else if (c == '"') inQuotes = true;
            else if (c == ',') { result.add(field.toString()); field.setLength(0); }
            else field.append(c);
        }
        result.add(field.toString());
        return result;
    }

    static Optional<Car> error(String reason) {
        errors.merge(reason, 1, Integer::sum);
        return Optional.empty();
    }

    static Optional<Car> parseLine(String line) {
        total++;
        List<String> f = parseCsvLine(line);
        if (f.size() < 6) return error("Недостаточно полей");
        try {
            int id = Integer.parseInt(f.get(0).trim());
            int year = Integer.parseInt(f.get(3).trim());
            if (year < 1886 || year > 2026) return error("Год вне [1886, 2026]");
            double price = Double.parseDouble(f.get(4).trim());
            if (price <= 0) return error("Цена <= 0");
            String vin = f.get(5).trim();
            if (vin.length() != 17) return error("VIN не 17 символов");
            String brand = f.get(1).trim(), model = f.get(2).trim();
            if (brand.isEmpty() || model.isEmpty()) return error("Пустой brand/model");
            return Optional.of(new Car(id, brand, model, year, price, vin));
        } catch (NumberFormatException e) {
            return error("Нечисловое поле (id/year/price)");
        }
    }

    static boolean isUnique(Car c) {
        if (vins.add(c.vin())) return true;
        error("Дубликат по VIN");
        return false;
    }

    // BufferedReader
    static List<Car> readBuffered(String file) throws IOException {
        List<Car> cars = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                parseLine(line).filter(Main::isUnique).ifPresent(cars::add);
            }
        }
        return cars;
    }

    // Files.readAllLines() + Stream API
    static List<Car> readStream(String file) throws IOException {
        return Files.readAllLines(Paths.get(file), StandardCharsets.UTF_8).stream()
                .skip(1)
                .filter(l -> !l.isBlank())
                .map(Main::parseLine)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(Main::isUnique)
                .collect(Collectors.toList());
    }

    static String quote(String s) {
        return s.matches(".*[,\"].*") ? "\"" + s.replace("\"", "\"\"") + "\"" : s;
    }

    static void write(String file, List<Car> cars) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            bw.write("id,brand,model,year,price,vin");
            bw.newLine();
            for (Car c : cars) {
                bw.write(c.id() + "," + quote(c.brand()) + "," + quote(c.model()) + "," + c.year()
                        + "," + c.price() + "," + c.vin());
                bw.newLine();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String file = args.length > 0 ? args[0] : "cars.csv";
        String brand = args.length > 1 ? args[1] : "BMW";

        List<Car> cars = readBuffered(file);
        int bad = errors.values().stream().mapToInt(Integer::intValue).sum();
        System.out.printf("Всего строк: %d, прочитано: %d, пропущено: %d %s%n",
                total, cars.size(), bad, errors);

        total = 0; errors.clear(); vins.clear();
        System.out.println("Stream API даёт тот же результат: " + cars.equals(readStream(file)));

        groupAndPrint(cars, brand);
        write("cars_clean.csv", cars);
    }

    static void groupAndPrint(List<Car> cars, String brand) {
        Map<String, List<Car>> byBrand = cars.stream()
                .collect(Collectors.groupingBy(c -> c.brand().toLowerCase()));
        byBrand.getOrDefault(brand.toLowerCase(), List.of()).forEach(System.out::println);
    }
}