package dependency_injector.instance;

import dependency_injector.class_marker.processor.InjectableMetadata;
import dependency_injector.utils.TreeNode;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class InstanceBuilder {

    private static final int DEFAULT_CONSTRUCTOR_MARKER = 0;

    public List<TreeNode<Object>> build(final List<TreeNode<InjectableMetadata>> injectableDependencies) {
        final List<TreeNode<Object>> instanceTree = new ArrayList<>();

        for (TreeNode<InjectableMetadata> injectable : injectableDependencies) {
            final InjectableMetadata data = injectable.getData();

            Class<?> aClass = data.aClass();
            Class<?> injectableClass = isNull(aClass) ? data.field().getDeclaringClass() : aClass;

            TreeNode<Object> parent = new TreeNode<>(createInstance(injectableClass));
            parent.addChildren(build(injectable.getChildren()));
            instanceTree.add(parent);
        }
        return instanceTree;
    }

    private Object createInstance(final Class<?> aClass) {
        Constructor<?> singleConstructor = processConstructors(aClass.getDeclaredConstructors());
        try {
            return singleConstructor.newInstance();
        } catch (Exception e) {
            throw new InstanceCreatingErrorException("Cannot create instance for: " + singleConstructor.getName(), e);
        }
    }

    private Constructor<?> processConstructors(final Constructor<?>[] constructors) {
        for (Constructor<?> constructor : constructors) {
            // TODO: think about strategy pattern instead simple if statement
            if (constructor.getParameterCount() == DEFAULT_CONSTRUCTOR_MARKER) {
                return constructor;
            } else {
                // TODO: create instance using constructors with parameters
            }
        }
        throw new InstanceCreatingErrorException("Something went wrong...");
    }

    private static final class InstanceCreatingErrorException extends RuntimeException {

        InstanceCreatingErrorException(String message, Exception exception) {
            super(message, exception);
        }

        InstanceCreatingErrorException(String message) {
            super(message);
        }
    }
}
