package dependency_injector.instance;

import dependency_injector.DependencyInjector;
import dependency_injector.class_marker.processor.InjectProcessor;
import dependency_injector.class_marker.processor.InjectableMetadata;
import dependency_injector.utils.TreeNode;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.logging.Logger;

public class InstanceBuilder {

    private static final Logger LOGGER = Logger.getLogger(DependencyInjector.class.getName());

    private final InjectProcessor injectProcessor;

    public InstanceBuilder(InjectProcessor injectProcessor) {
        this.injectProcessor = injectProcessor;
    }

    public List<TreeNode<Object>> build(List<TreeNode<InjectableMetadata>> injectableDependencies) {
        //            List<Constructor<?>> constructors = getWithAnnotation(classToInject.getDeclaredConstructors());
//            Object instanceByConstructor = createInstanceByConstructor(constructors);
//            result.add(instanceByConstructor);
        return null;
    }

    private Object createInstance() {
//        Constructor<?> constructorWithoutParameters = processConstructors(someClass.getConstructors());
//        constructorWithoutParameters.newInstance();
        return null;
    }

    private Object createInstanceByConstructor(final List<Constructor<?>> injectableByConstructors) {
        Object instance = null;
        try {
            Constructor<?> constructor = processConstructors(injectableByConstructors.toArray(new Constructor<?>[0]));
            instance = constructor.newInstance();
        } catch (Exception e) {
            LOGGER.info("Constructors were not found"); // TODO: but what about empty constructor which always exists?
        }
        return instance;
    }

    private Constructor<?> processConstructors(final Constructor<?>[] constructors) {
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() == 0) {
                return constructor;
            }
        }
        // process the remaining constructors - check for @Inject annotation
        List<Constructor<?>> constructorsWithInjectAnnotation = injectProcessor.getWithAnnotation(constructors);
        if (constructorsWithInjectAnnotation.size() != 1) {
            throw new NotOnlyConstructorException("Unable to define constructor");
        }
        return constructorsWithInjectAnnotation.get(0);
    }

    private static final class NotOnlyConstructorException extends RuntimeException {

        NotOnlyConstructorException(String message) {
            super(message);
        }
    }
}
