import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String prefix = "";
        String newPath = "";
        boolean fullStat = false;
        boolean shortStat = false;
        boolean existingFile = false;
        List<Path> paths = new ArrayList<>();

        if(args.length == 0) {
            System.out.println("Нет аргументов!");
            return;
        }

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-f":
                    fullStat = true;
                    break;
                case "-s":
                    shortStat = true;
                    break;
                case "-o":
                    if(i + 1 < args.length)
                        newPath = args[++i] + "\\";
                    else {
                        System.out.println("Ошибка: должен быть путь!");
                        return;
                    }
                    break;
                case "-p":
                    if(i + 1 < args.length)
                        prefix = args[++i];
                    else {
                        System.out.println("Ошибка: должен быть префикс!");
                        return;
                    }
                    break;
                case "-a":
                    existingFile = true;
                    break;
                default: paths.add(Paths.get(args[i]));
            }
        }

        FileProcessor fileProcessor = new FileProcessor(prefix, newPath, fullStat, shortStat, existingFile);
        List<String> lines = fileProcessor.readFiles(paths);

        Path outputFileInteger = Path.of("integer.txt");
        Path outputFileDouble = Path.of("double.txt");
        Path outputFileString = Path.of("string.txt");

        fileProcessor.filterLines(lines, outputFileInteger, outputFileDouble, outputFileString);
    }
}
