package dependency_injector.custom_annotations.annotation_processor;

import dependency_injector.custom_annotations.Inject;
import dependency_injector.utils.TreeNode;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class InjectProcessor {

    private final Logger LOGGER = Logger.getLogger(InjectProcessor.class.getName());

    private final ComponentProcessor componentProcessor;

    public InjectProcessor(ComponentProcessor componentProcessor) {
        this.componentProcessor = componentProcessor;
    }

    public List<TreeNode<InjectableMetadata>> inject(final List<Class<?>> classesToInject) {
        final List<TreeNode<InjectableMetadata>> tree = new ArrayList<>();
        for (Class<?> classToInject : classesToInject) {
//            List<Constructor<?>> constructors = getWithAnnotation(classToInject.getDeclaredConstructors());
//            Object instanceByConstructor = createInstanceByConstructor(constructors);
//            result.add(instanceByConstructor);
            try {
                tree.add(buildInjectableFieldsTree(classToInject, null));
            } catch (Exception e) {
                LOGGER.info("Couldn't handle class: " + classesToInject);
            }
        }
        return tree;
    }

    private TreeNode<InjectableMetadata> buildInjectableFieldsTree(
        final Class<?> classToInject,
        final Field fieldToInject
    ) {
        TreeNode<InjectableMetadata> root = new TreeNode<>(new InjectableMetadata(fieldToInject, classToInject));

        List<Field> withAnnotation = getWithAnnotation(classToInject.getDeclaredFields());
        for (Field field : withAnnotation) {
            Class<?> fieldClass = field.getType();
            try {
                root.addChild(buildInjectableFieldsTree(fieldClass, field));
            } catch (Exception e) {
                LOGGER.info("Couldn't handle the next field: " + fieldClass);
            }
        }

        LOGGER.info("Injectable fields have been collected");
        return root;
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

    private Constructor<?> processConstructors(final Constructor<?>[] constructors) throws NotOnlyConstructorException {
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() == 0) {
                return constructor;
            }
        }
        // process the remaining constructors - check for @Inject annotation
        List<Constructor<?>> constructorsWithInjectAnnotation = getWithAnnotation(constructors);
        if (constructorsWithInjectAnnotation.size() != 1) {
            throw new NotOnlyConstructorException("Unable to define constructor");
        }
        return constructorsWithInjectAnnotation.get(0);
    }

    private <T extends AccessibleObject> List<T> getWithAnnotation(final T[] injectableClassItems) {
        return Arrays.stream(injectableClassItems)
            .filter(object -> Optional.ofNullable(object.getAnnotation(Inject.class)).isPresent())
            .toList();
    }

    private static final class NotOnlyConstructorException extends RuntimeException {

        NotOnlyConstructorException(String message) {
            super(message);
        }
    }
}

