package dependency_injector;

import dependency_injector.utils.FileUtils;

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

    private final Pattern CLASS_NAME_PATTERN = Pattern.compile((".+?(?=.java)"));
    private final Logger LOGGER = Logger.getLogger(PackageScanner.class.getName());

    List<Class<?>> scan(File directory, String packageName) {
        File[] files = directory.listFiles();

        List<Class<?>> classes = new ArrayList<>();
        if (!Files.exists(Paths.get(directory.getPath())) || files == null) {
            return classes;
        }

        for (File file : files) {
            String parentPackage = packageName + FileUtils.PACKAGE_SEPARATOR;

            if (file.isDirectory()) {
                classes.addAll(scan(file, parentPackage + file.getName()));
            } else {
                try {
                    Optional<String> fileName = fileNameProcessing(file.getName());
                    if (fileName.isEmpty()) continue;

                    classes.add(Class.forName(parentPackage + fileName.get()));
                } catch (ClassNotFoundException e) {
                    LOGGER.info(e.getException().getClass().toString());
                }
            }
        }
        return classes;
    }

    private Optional<String> fileNameProcessing(String fileName) {
        final Matcher matcher = CLASS_NAME_PATTERN.matcher(fileName);
        if (matcher.find()) {
            return Optional.of(matcher.group());
        }
        return Optional.empty();
    }
}

