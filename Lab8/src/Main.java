import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Main {

    static final Path BASE_DIR = Files.isDirectory(Path.of("src")) ? Path.of("").toAbsolutePath()
            : Files.isDirectory(Path.of("Lab8/src")) ? Path.of("Lab8").toAbsolutePath()
            : Path.of("").toAbsolutePath().resolve("..").normalize();
    static final Path RESOURCES = BASE_DIR.resolve("src/resources");
    static final Path JSON_FILE = RESOURCES.resolve("cars.json");
    static final Path COPY_FILE = RESOURCES.resolve("cars_copy.json");
    static final Path CAR_FILE = RESOURCES.resolve("car.json");
    static final Path XML_FILE = RESOURCES.resolve("cars.xml");

    static final ObjectMapper JSON = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .enable(SerializationFeature.INDENT_OUTPUT);

    static final XmlMapper XML = new XmlMapper();
    static {
        XML.registerModule(new JavaTimeModule());
        XML.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        XML.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static void main(String[] args) {
        try {
            Files.createDirectories(RESOURCES);
            List<Car> sample = sampleCars();

            System.out.println("Запись JSON//////////");
            JSON.writeValue(JSON_FILE.toFile(), sample);
            System.out.println("Записано машин в JSON: " + sample.size() + " (" + JSON_FILE + ")");
            System.out.println();

            System.out.println("Чтение JSON/////////////////");
            List<Car> jsonCars = JSON.readValue(JSON_FILE.toFile(), new TypeReference<List<Car>>() {});
            System.out.println("Прочитано: " + jsonCars.size());
            System.out.println("Формат: " + jsonCars.get(0).getManufactureDate());
            System.out.println("Проверка на совпадение: " + jsonCars.equals(sample));
            JSON.writeValue(COPY_FILE.toFile(), jsonCars);
            System.out.println("Записано машин в JSON: " + jsonCars.size() + " (" + COPY_FILE + ")");
            System.out.println();

            System.out.println("XML");
            XML.writeValue(XML_FILE.toFile(), new CarList(jsonCars));
            List<Car> xmlCars = XML.readValue(XML_FILE.toFile(), CarList.class).cars;
            System.out.println("Записано: " + jsonCars.size() + " (" + XML_FILE + ")");
            System.out.println("Прочитано: " + xmlCars.size());
            System.out.println("Данные из XML совпадают с JSON: " + xmlCars.equals(jsonCars));
            System.out.println();

            System.out.println("Одна запись:");
            Car car = jsonCars.get(0);
            car.setHiddenFeatures("люк, кожа, полный привод");
            JSON.writeValue(CAR_FILE.toFile(), car);
            System.out.println(JSON.writeValueAsString(car));
            System.out.println("Была ли запись? " + Files.readString(CAR_FILE).contains("hiddenFeatures"));
            System.out.println("Чтение" + JSON.readValue(CAR_FILE.toFile(), Car.class));
            System.out.println();
        } catch (IOException e) {
            System.err.println("ERR " + e.getMessage());
        }
    }

    static List<Car> sampleCars() {
        return new ArrayList<>(List.of(
                new Car(1, "Volkswagen", "Golf GTI", 2019, new BigDecimal("2850000"), "WVWXXXXX000000001", LocalDate.of(2019, 3, 14)),
                new Car(2, "Volkswagen", "Polo", 2021, new BigDecimal("1650000"), "WVWXXXXX000000002", LocalDate.of(2021, 5, 20)),
                new Car(3, "BMW", "X5 xDrive40i", 2021, new BigDecimal("7200000"), "WBAXXXXX000000006", LocalDate.of(2021, 1, 11)),
                new Car(4, "Toyota", "Camry", 2022, new BigDecimal("3300000"), "JTDXXXXX000000008", LocalDate.of(2022, 7, 4)),
                new Car(5, "Toyota", "Land Cruiser 300", 2024, new BigDecimal("9800000"), "JTDXXXXX000000011", LocalDate.of(2024, 2, 26)),
                new Car(6, "Honda", "CR-V", 2022, new BigDecimal("3500000"), "1HGXXXXX000000014", LocalDate.of(2022, 9, 9)),
                new Car(7, "Kia", "Sportage", 2023, new BigDecimal("3200000"), "KNAXXXXX000000016", LocalDate.of(2023, 4, 18)),
                new Car(8, "Skoda", "Octavia", 2021, new BigDecimal("2300000"), "TMBXXXXX000000018", LocalDate.of(2021, 11, 2)),
                new Car(9, "Mercedes-Benz", "C-Class C200", 2020, new BigDecimal("4200000"), "WDDXXXXX000000021", LocalDate.of(2020, 6, 30)),
                new Car(10, "Ford", "Mustang GT", 1969, new BigDecimal("5500000"), "1FAXXXXX000000024", LocalDate.of(1969, 8, 15)),
                new Car(11, "Tesla", "Model Y", 2024, new BigDecimal("5600000"), "5YJXXXXX000000031", LocalDate.of(2024, 5, 7)),
                new Car(12, "Ferrari", "250 GTO", 1962, new BigDecimal("50000000"), "ZFFXXXXX000000032", LocalDate.of(1962, 1, 1))
        ));
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    @JacksonXmlRootElement(localName = "car")
    public static class Car {

        @JsonProperty("id")
        private long id;

        @JsonProperty("brand")
        private String brand;

        @JsonProperty("model")
        private String model;

        @JsonProperty("year")
        private int year;

        @JsonProperty("price")
        private BigDecimal price;

        @JsonProperty("vin")
        private String vin;

        @JsonProperty("manufactureDate")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate manufactureDate;

        @JsonIgnore
        private String hiddenFeatures;

        public Car() {
            this.manufactureDate = LocalDate.now();
        }

        @JsonCreator
        public Car(@JsonProperty("id") long id, @JsonProperty("brand") String brand, @JsonProperty("model") String model,
                   @JsonProperty("year") int year, @JsonProperty("price") BigDecimal price, @JsonProperty("vin") String vin,
                   @JsonProperty("manufactureDate") @JsonFormat(pattern = "yyyy-MM-dd") LocalDate manufactureDate) {
            this.id = id;
            this.brand = brand;
            this.model = model;
            this.year = year;
            this.price = price;
            this.vin = vin;
            this.manufactureDate = manufactureDate;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public String getVin() {
            return vin;
        }

        public void setVin(String vin) {
            this.vin = vin;
        }

        public LocalDate getManufactureDate() {
            return manufactureDate;
        }

        public void setManufactureDate(LocalDate manufactureDate) {
            this.manufactureDate = manufactureDate;
        }

        @JsonIgnore
        public String getHiddenFeatures() {
            return hiddenFeatures;
        }

        public void setHiddenFeatures(String hiddenFeatures) {
            this.hiddenFeatures = hiddenFeatures;
        }

        @Override
        public String toString() {
            return "Car{id=" + id + ", brand='" + brand + "', model='" + model + "', year=" + year
                    + ", price=" + price + ", vin='" + vin + "', manufactureDate=" + manufactureDate + "}";
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Car c && id == c.id && year == c.year
                    && Objects.equals(brand, c.brand) && Objects.equals(model, c.model) && Objects.equals(price, c.price)
                    && Objects.equals(vin, c.vin) && Objects.equals(manufactureDate, c.manufactureDate);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, brand, model, year, price, vin, manufactureDate);
        }
    }

    @JacksonXmlRootElement(localName = "cars")
    public static class CarList {

        @JsonProperty("cars")
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "car")
        public List<Car> cars = new ArrayList<>();

        public CarList() {
        }

        public CarList(List<Car> cars) {
            this.cars = cars;
        }
    }
}
