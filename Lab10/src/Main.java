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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Main {
    static final int THREADS = 4;
    static final long TIMEOUT_SECONDS = 30;
    static final Path BASE_DIR = Files.isDirectory(Path.of("data")) ? Path.of("").toAbsolutePath()
            : Files.isDirectory(Path.of("Lab10/data")) ? Path.of("Lab10").toAbsolutePath()
            : Path.of("").toAbsolutePath().resolve("..").normalize();
    static final Path DATA_DIR = BASE_DIR.resolve("data");

    record LoadResult(String fileName, List<Car> cars, long millis, String thread) {}
    record LoadSummary(List<Car> cars, long millis) {}

    public static void main(String[] args) throws Exception {
        List<Path> files = findFiles();
        if (files.isEmpty()) {
            System.err.println("CSV файлы не найдены в " + DATA_DIR);
            return;
        }
        System.out.println("Файлов: " + files.size());

        LoadSummary sequential = loadSequential(files);
        LoadSummary parallel = loadParallel(files);

        List<Car> unique = deduplicate(parallel.cars());
        System.out.println("После удаления дубликатов: " + unique.size());

        printGroups(unique);

        System.out.println();
        System.out.println("=== Сравнение ===");
        System.out.printf("Ускорение: %.2fx%n", sequential.millis() / (double) parallel.millis());
    }

    static List<Path> findFiles() throws IOException {
        try (var stream = Files.list(DATA_DIR)) {
            return stream
                    .filter(p -> p.getFileName().toString().toLowerCase().endsWith(".csv"))
                    .sorted()
                    .toList();
        }
    }

    static LoadSummary loadSequential(List<Path> files) throws IOException {
        System.out.println();
        System.out.println("=== Последовательная загрузка ===");
        List<Car> all = new ArrayList<>();
        long start = System.nanoTime();
        for (Path file : files) {
            long t0 = System.nanoTime();
            List<Car> cars = readCars(file);
            long millis = (System.nanoTime() - t0) / 1_000_000;
            all.addAll(cars);
            System.out.printf("%s: %d автомобилей, %d мс%n", file.getFileName(), cars.size(), millis);
        }
        long total = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Всего автомобилей: " + all.size());
        System.out.println("Время: " + total + " мс");
        return new LoadSummary(all, total);
    }

    static LoadSummary loadParallel(List<Path> files) throws InterruptedException {
        System.out.println();
        System.out.println("=== Параллельная загрузка (пул из " + THREADS + " потоков) ===");
        AtomicInteger counter = new AtomicInteger();
        ExecutorService executor = Executors.newFixedThreadPool(THREADS, r -> {
            Thread thread = new Thread(r, "поток-" + counter.incrementAndGet());
            return thread;
        });

        List<Future<LoadResult>> futures = new ArrayList<>();
        List<Car> all = new ArrayList<>();
        long start = System.nanoTime();
        try {
            for (Path file : files) {
                futures.add(executor.submit(() -> {
                    long t0 = System.nanoTime();
                    List<Car> cars = readCars(file);
                    long millis = (System.nanoTime() - t0) / 1_000_000;
                    return new LoadResult(file.getFileName().toString(), cars, millis, Thread.currentThread().getName());
                }));
            }

            for (int i = 0; i < futures.size(); i++) {
                Future<LoadResult> future = futures.get(i);
                String fileName = files.get(i).getFileName().toString();
                try {
                    LoadResult result = future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    all.addAll(result.cars());
                    System.out.printf("%s: %d автомобилей, %d мс (%s)%n",
                            result.fileName(), result.cars().size(), result.millis(), result.thread());
                } catch (TimeoutException e) {
                    future.cancel(true);
                    System.err.printf("Файл %s не загружен за %d секунд, задача отменена%n", fileName, TIMEOUT_SECONDS);
                } catch (ExecutionException e) {
                    System.err.printf("Ошибка загрузки %s: %s%n", fileName, e.getCause().getMessage());
                }
            }
        } finally {
            executor.shutdown();
            if (!executor.awaitTermination(TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        }

        long total = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Всего автомобилей: " + all.size());
        System.out.println("Время: " + total + " мс");
        return new LoadSummary(all, total);
    }

    static List<Car> readCars(Path file) throws IOException {
        List<Car> cars = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line = reader.readLine();
            if (line == null) return cars;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(",");
                if (parts.length < 6) continue;
                cars.add(new Car(
                        Integer.parseInt(parts[0].trim()),
                        parts[1].trim(),
                        parts[2].trim(),
                        Integer.parseInt(parts[3].trim()),
                        Long.parseLong(parts[4].trim()),
                        parts[5].trim()
                ));
            }
        }
        return cars;
    }

    static List<Car> deduplicate(List<Car> cars) {
        Map<Integer, Car> unique = new LinkedHashMap<>();
        for (Car car : cars) {
            unique.putIfAbsent(car.getId(), car);
        }
        return new ArrayList<>(unique.values());
    }

    static void printGroups(List<Car> cars) {
        Map<String, Long> groups = cars.stream()
                .collect(Collectors.groupingBy(Car::getBrand, TreeMap::new, Collectors.counting()));
        System.out.println();
        System.out.println("=== Группировка по бренду ===");
        groups.forEach((brand, count) -> System.out.printf("%-12s %d%n", brand, count));
    }
}
