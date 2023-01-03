package dependency_injector.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static final String PACKAGE_SEPARATOR = ".";

    private FileUtils() {
        // hide public constructor
    }

    public static List<File> allFilesFromFolder(File directory) {
        List<File> directoryContent = new ArrayList<>();
        File[] files = directory.listFiles();

        if (files == null) {
            return directoryContent;
        }

        for (final File file : files) {
            if (file.isDirectory()) {
                allFilesFromFolder(file);
            } else {
                directoryContent.add(file);
            }
        }
        return directoryContent;
    }
}
