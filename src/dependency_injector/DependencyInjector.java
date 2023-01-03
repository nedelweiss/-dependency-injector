package dependency_injector;

import dependency_injector.custom_annotations.annotation_processor.ComponentProcessor;
import dependency_injector.custom_annotations.annotation_processor.InjectProcessor;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class DependencyInjector {

    private static final String CURRENT_DIR_KEY = "user.dir";
    private static final String PATH_TO_TEST_PACKAGE = "\\src\\dependency_injector\\test_package";

    public static void main(String[] args) {
        PackageScanner packageScanner = new PackageScanner();

        Path path = Paths.get(System.getProperty(CURRENT_DIR_KEY), PATH_TO_TEST_PACKAGE);
        File directory = path.toFile();

        List<Class<?>> scannedClasses = packageScanner.classScanner(directory, "dependency_injector.test_package");

        ComponentProcessor componentProcessor = new ComponentProcessor();
        List<Class<?>> classesWithComponentAnnotation = componentProcessor.process(scannedClasses);

        List<Object> injectedDependencies = new InjectProcessor().inject(classesWithComponentAnnotation);
    }
}
