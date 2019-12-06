package dependencyInjector;

import dependencyInjector.customAnnotations.annotationProcessor.ComponentProcessor;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        PackageScanner packageScanner = new PackageScanner();

        String property = System.getProperty("user.dir");
        Path path = Paths.get(property, "testPackage");
        File directory = path.toFile();
        List<Class<?>> classes = packageScanner.classScanner(directory, "dependencyInjector.testPackage");

        ComponentProcessor componentProcessor = new ComponentProcessor();
        List<Object> objects = componentProcessor.process(classes);
    }
}
