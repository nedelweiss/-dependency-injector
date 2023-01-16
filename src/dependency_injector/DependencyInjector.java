package dependency_injector;

import dependency_injector.class_marker.processor.ComponentProcessor;
import dependency_injector.class_marker.processor.InjectProcessor;
import dependency_injector.class_marker.processor.InjectableDependenciesTreeBuilder;
import dependency_injector.class_marker.processor.InjectableMetadata;
import dependency_injector.instance.InstanceBuilder;
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

        InjectProcessor injectProcessor = new InjectProcessor();
        ComponentProcessor componentProcessor = new ComponentProcessor();

        List<TreeNode<InjectableMetadata>> injectableDependencies = new InjectableDependenciesTreeBuilder(componentProcessor, injectProcessor)
            .buildInjectableFieldsTree(scannedClasses);

        List<TreeNode<Object>> instances = new InstanceBuilder().build(injectableDependencies); // TODO: need to cover by tests

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
