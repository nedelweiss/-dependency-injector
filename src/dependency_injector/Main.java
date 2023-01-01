package dependency_injector;

import dependency_injector.custom_annotations.annotation_processor.ComponentProcessor;
import dependency_injector.custom_annotations.annotation_processor.InjectProcessor;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    private static final String CURRENT_DIR_KEY = "user.dir";
    private static final String PATH_TO_TEST_PACKAGE = "\\src\\dependencyInjector\\testPackage";

    public static void main(String[] args) {
        PackageScanner packageScanner = new PackageScanner();

        String property = System.getProperty(CURRENT_DIR_KEY);

        Path path = Paths.get(property, PATH_TO_TEST_PACKAGE);
        File directory = path.toFile();

        List<Class<?>> classes = packageScanner.classScanner(directory, "dependencyInjector.testPackage");

        ComponentProcessor componentProcessor = new ComponentProcessor();
        InjectProcessor injectProcessor = new InjectProcessor();
        List<Class<?>> collection = componentProcessor.process(classes);
        List<Object> objects = injectProcessor.inject(collection);
    }
}
