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

import static java.util.Objects.isNull;

public class PackageScanner {

    private final Pattern CLASS_NAME_PATTERN = Pattern.compile((".+?(?=.java)"));
    private final Logger LOGGER = Logger.getLogger(PackageScanner.class.getName());

    List<Class<?>> scan(final File directory, final String packageName) {
        File[] files = directory.listFiles();

        final List<Class<?>> classes = new ArrayList<>();
        if (!Files.exists(Paths.get(directory.getPath())) || isNull(files)) {
            return classes;
        }

        for (File file : files) {
            final String parentPackage = packageName + FileUtils.PACKAGE_SEPARATOR;

            if (file.isDirectory()) {
                classes.addAll(scan(file, parentPackage + file.getName()));
            } else {
                try {
                    final Optional<String> fileName = processFileName(file.getName());
                    if (fileName.isEmpty()) continue;

                    classes.add(Class.forName(parentPackage + fileName.get()));
                } catch (ClassNotFoundException e) {
                    LOGGER.info("Class isn't exists: " + e.getException().getClass().toString());
                }
            }
        }
        return classes;
    }

    private Optional<String> processFileName(final String fileName) {
        Matcher matcher = CLASS_NAME_PATTERN.matcher(fileName);
        if (matcher.find()) {
            return Optional.of(matcher.group());
        }
        return Optional.empty();
    }
}

