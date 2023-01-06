package dependency_injector;

import dependency_injector.custom_annotations.annotation_processor.ComponentProcessor;
import dependency_injector.custom_annotations.annotation_processor.InjectProcessor;
import dependency_injector.custom_annotations.annotation_processor.InjectableMetadata;
import dependency_injector.utils.FileUtils;
import dependency_injector.utils.TreeNode;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

public class DependencyInjector {

    private static final String CURRENT_DIR_KEY = "user.dir";
    private static final String TEST_PACKAGE = "test_package";
    private static final String DEPENDENCY_INJECTOR_PACKAGE = "dependency_injector";
    private static final String PATH_TO_SCANNABLE_PACKAGE = buildPathToScannablePackage();

    private static final Logger LOGGER = Logger.getLogger(DependencyInjector.class.getName());

    public static void main(String[] args) {
        List<Class<?>> scannedClasses = new PackageScanner().scan(getDirectory(), buildPackageName());

        ComponentProcessor componentProcessor = new ComponentProcessor(); // TODO: inline
        List<Class<?>> classesWithComponentAnnotation = componentProcessor.process(scannedClasses); // TODO: get rid of this

        List<TreeNode<InjectableMetadata>> injectedDependencies = new InjectProcessor(componentProcessor)
            .inject(classesWithComponentAnnotation);

//        InstanceBuilder instanceBuilder = new InstanceBuilder();
//        instanceBuilder.build(injectedDependencies);

        LOGGER.info("Dependencies have been injected");
    }

    private static String buildPathToScannablePackage() {
        return "\\src\\" + DEPENDENCY_INJECTOR_PACKAGE + "\\" + TEST_PACKAGE;
    }

    private static String buildPackageName() {
        return DEPENDENCY_INJECTOR_PACKAGE + FileUtils.PACKAGE_SEPARATOR + TEST_PACKAGE;
    }

    private static File getDirectory() {
        return Paths.get(System.getProperty(CURRENT_DIR_KEY), PATH_TO_SCANNABLE_PACKAGE).toFile();
    }
}
