package dependencyInjector;

import java.io.File;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        PackageScanner packageScanner = new PackageScanner();

        File directory = new File("PATH_TO_TESTPACKAGE");
        List<Class<?>> classes = packageScanner.scanClasses(directory, "dependencyInjector.testPackage");
    }
}
