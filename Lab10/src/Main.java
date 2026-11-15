import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class Main {
    static final int THREADS = 4;
    static final long TIMEOUT = 30;
    static final Path DATA = Path.of("data");
    static final Path DIR = Files.isDirectory(DATA) ? DATA : Path.of("Lab10/data");

    public static void main(String[] args) throws Exception {
        List<Path> files;
        try (var stream = Files.list(DIR)) {
            files = stream.filter(p -> p.toString().endsWith(".csv")).sorted().toList();
        }

        System.out.println("=== Последовательная загрузка ===");
        long start = System.nanoTime();
        List<Car> all = new ArrayList<>();
        for (Path file : files) {
            all.addAll(load(file));
        }
        long seq = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Всего автомобилей: " + all.size());
        System.out.println("Время: " + seq + " мс");

        System.out.println();
        System.out.println("=== Параллельная загрузка (пул из " + THREADS + " потоков) ===");
        ExecutorService pool = Executors.newFixedThreadPool(THREADS);
        start = System.nanoTime();
        List<Car> parallel = new ArrayList<>();
        List<Future<List<Car>>> futures = new ArrayList<>();
        for (Path file : files) {
            futures.add(pool.submit(() -> {
                List<Car> cars = load(file);
                System.out.println(file.getFileName() + ": " + cars.size()
                        + " автомобилей (" + Thread.currentThread().getName() + ")");
                return cars;
            }));
        }
        for (Future<List<Car>> future : futures) {
            try {
                parallel.addAll(future.get(TIMEOUT, TimeUnit.SECONDS));
            } catch (TimeoutException e) {
                future.cancel(true);
                System.err.println("Таймаут загрузки, задача отменена");
            } catch (ExecutionException e) {
                System.err.println("Ошибка загрузки: " + e.getCause().getMessage());
            }
        }
        pool.shutdown();
        pool.awaitTermination(TIMEOUT, TimeUnit.SECONDS);
        long par = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Всего автомобилей: " + parallel.size());
        System.out.println("Время: " + par + " мс");

        Map<Integer, Car> unique = new LinkedHashMap<>();
        for (Car car : parallel) {
            unique.putIfAbsent(car.getId(), car);
        }
        System.out.println("После удаления дубликатов: " + unique.size());

        System.out.println();
        System.out.println("=== Группировка по бренду ===");
        unique.values().stream()
                .collect(Collectors.groupingBy(Car::getBrand, TreeMap::new, Collectors.counting()))
                .forEach((brand, count) -> System.out.println(brand + ": " + count));

        System.out.println();
        System.out.println("=== Сравнение ===");
        System.out.printf("Ускорение: %.2fx%n", seq / (double) par);
    }

    static List<Car> load(Path file) throws IOException {
        List<Car> cars = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(",");
                cars.add(new Car(
                        Integer.parseInt(parts[0]),
                        parts[1],
                        parts[2],
                        Integer.parseInt(parts[3]),
                        Long.parseLong(parts[4]),
                        parts[5]
                ));
            }
        }
        return cars;
    }
}
