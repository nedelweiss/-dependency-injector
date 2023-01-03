package dependency_injector;

import dependency_injector.custom_annotations.annotation_processor.ComponentProcessor;
import dependency_injector.custom_annotations.annotation_processor.InjectProcessor;
import dependency_injector.utils.FileUtils;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

public class DependencyInjector {

    private static final String CURRENT_DIR_KEY = "user.dir";
    private static final String TEST_PACKAGE = "test_package";
    private static final String DEPENDENCY_INJECTOR_PACKAGE = "dependency_injector";
    private static final String PATH_TO_SCANABLE_PACKAGE = buildPathToScanablePackage();

    private static final Logger LOGGER = Logger.getLogger(DependencyInjector.class.getName());

    public static void main(String[] args) {
        List<Class<?>> scannedClasses = new PackageScanner().scan(getDirectory(), buildPackageName());

        ComponentProcessor componentProcessor = new ComponentProcessor();
        List<Class<?>> classesWithComponentAnnotation = componentProcessor.process(scannedClasses);

        List<Object> injectedDependencies = new InjectProcessor().inject(classesWithComponentAnnotation);
        LOGGER.info("Dependencies have been injected");
    }

    private static String buildPathToScanablePackage() {
        return "\\src\\" + DEPENDENCY_INJECTOR_PACKAGE + "\\" + TEST_PACKAGE;
    }

    private static String buildPackageName() {
        return DEPENDENCY_INJECTOR_PACKAGE + FileUtils.PACKAGE_SEPARATOR + TEST_PACKAGE;
    }

    private static File getDirectory() {
        return Paths.get(System.getProperty(CURRENT_DIR_KEY), PATH_TO_SCANABLE_PACKAGE).toFile();
    }
}
