package dependencyInjector;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PackageScanner {
    private final Pattern PATTERN = Pattern.compile((".+?(?=.java)"));
    private final String PACKAGE_SEPARATOR = ".";
    private final Logger logger = Logger.getLogger(PackageScanner.class.getName());

    public List<Class<?>> scanClasses(File directory, String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        File[] files = directory.listFiles();

        if (!Files.exists(Paths.get(directory.getPath())) || files == null) {
            return null;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(scanClasses(file, packageName + PACKAGE_SEPARATOR + file.getName()));
            } else {
                try {
                    Optional<String> fileName = fileNameProcessing(file.getName());
                    if (fileName.isEmpty()) continue;

                    classes.add(Class.forName(packageName + PACKAGE_SEPARATOR + fileName.get()));
                } catch (ClassNotFoundException e) {
                    logger.info(e.getException().getClass().toString());
                }
            }
        }
        return classes;
    }

    public Optional<String> fileNameProcessing(String fileName) {
        Matcher matcher = PATTERN.matcher(fileName);
        if (matcher.find()) {
            return Optional.of(matcher.group());
        }
        return Optional.empty();
    }
}

