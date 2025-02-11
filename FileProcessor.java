import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FileProcessor {
    private final String prefix;
    private final String newPath;
    private final boolean fullStat;
    private final boolean shortStat;
    private final boolean existingFile;

    public FileProcessor(String prefix, String newPath, boolean fullStat, boolean shortStat, boolean existingFile) {
        this.prefix = prefix;
        this.newPath = newPath;
        this.fullStat = fullStat;
        this.shortStat = shortStat;
        this.existingFile = existingFile;
    }

    public List<String> readFiles(List<Path> paths) {
        List<String> lines = new ArrayList<>();
        for (Path path : paths) {
            try {
                lines.addAll(Files.readAllLines(path));
            } catch (IOException e) {
                throw new RuntimeException("Ошибка при чтении файла: " + path, e);
            }
        }
        return lines;
    }

    public void filterLines(List<String> lines, Path outputFileInteger, Path outputFileDouble, Path outputFileString) {
        Pattern integerPattern = Pattern.compile("-?\\d+");
        Pattern doublePattern = Pattern.compile("-?\\d*\\.\\d+");

        Map<String, List<String>> grouped = lines.stream()
                .collect(Collectors.groupingBy(s -> {
                    if (integerPattern.matcher(s).matches()) {
                        return "Integers";
                    } else if (doublePattern.matcher(s).matches()) {
                        return "Doubles";
                    } else {
                        return "Strings";
                    }
                }));

        List<String> integers = grouped.getOrDefault("Integers", List.of());
        List<String> doubles = grouped.getOrDefault("Doubles", List.of());
        List<String> strings = grouped.getOrDefault("Strings", List.of());

        // Новый путь
        outputFileInteger = Path.of(newPath).resolve(prefix + outputFileInteger.getFileName().toString());
        outputFileDouble = Path.of(newPath).resolve(prefix + outputFileDouble.getFileName().toString());
        outputFileString = Path.of(newPath).resolve(prefix + outputFileString.getFileName().toString());

        // Вывод статистики
        if (fullStat) {
            printFullStats(strings, integers, doubles);
        }

        if (shortStat) {
            printShortStats(strings, integers, doubles);
        }

        // Запись в файлы
        writeToFile(outputFileInteger, integers);
        writeToFile(outputFileDouble, doubles);
        writeToFile(outputFileString, strings);
    }

    private void printFullStats(List<String> strings, List<String> integers, List<String> doubles) {
        if (!strings.isEmpty()) {
            String longest = Collections.max(strings, Comparator.comparingInt(String::length));
            String shortest = Collections.min(strings, Comparator.comparingInt(String::length));
            System.out.printf("Статистика для string:\n" +
                            "Количество строк: %d\n" +
                            "Самая длинная: %d символов\n" +
                            "Самая короткая: %d символов\n\n", strings.size(), longest.length(), shortest.length());
        }

        if (!integers.isEmpty()) {
            List<Integer> intValues = integers.stream().map(Integer::parseInt).toList();
            int integerMax = Collections.max(intValues);
            int integerMin = Collections.min(intValues);
            int sum = intValues.stream().mapToInt(Integer::intValue).sum();
            double average = sum / (double) intValues.size();
            System.out.printf("Статистика для integer:\n" +
                            "Количество: %d\n" +
                            "Макс: %d\n" +
                            "Мин: %d\n" +
                            "Сумма: %d\n" +
                            "Среднее: %.2f\n\n", intValues.size(), integerMax, integerMin, sum, average);
        }

        if (!doubles.isEmpty()) {
            List<Double> doubleValues = doubles.stream().map(Double::parseDouble).toList();
            double doubleMax = Collections.max(doubleValues);
            double doubleMin = Collections.min(doubleValues);
            double sumDouble = doubleValues.stream().mapToDouble(Double::doubleValue).sum();
            double averageDouble = sumDouble / doubleValues.size();
            System.out.printf("Статистика для double:\n" +
                            "Количество: %d\n" +
                            "Макс: %.2f\n" +
                            "Мин: %.2f\n" +
                            "Сумма: %.2f\n" +
                            "Среднее: %.2f\n\n", doubleValues.size(), doubleMax, doubleMin, sumDouble, averageDouble);
        }
    }

    private void printShortStats(List<String> strings, List<String> integers, List<String> doubles) {
        System.out.printf("Статистика (короткая):\n" +
                        "String: %d\n" +
                        "Integer: %d\n" +
                        "Double: %d\n\n", strings.size(), integers.size(), doubles.size());
    }

    private void writeToFile(Path filePath, List<String> data) {
        try {
            if (existingFile) {
                Files.write(filePath, data, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } else {
                Files.write(filePath, data, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка записи в файл: " + filePath, e);
        }
    }
}
