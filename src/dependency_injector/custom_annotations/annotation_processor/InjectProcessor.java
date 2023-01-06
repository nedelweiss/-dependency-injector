package dependency_injector.custom_annotations.annotation_processor;

import dependency_injector.custom_annotations.Inject;
import dependency_injector.utils.TreeNode;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Logger;

public class InjectProcessor {

    private final Logger LOGGER = Logger.getLogger(InjectProcessor.class.getName());

    private final ComponentProcessor componentProcessor;

    public InjectProcessor(ComponentProcessor componentProcessor) {
        this.componentProcessor = componentProcessor;
    }

    public Map<Object, List<TreeNode<Field>>> inject(final List<Class<?>> classesToInject) {
        final Map<Object, List<TreeNode<Field>>> tree = new HashMap<>();
        for (Class<?> classToInject : classesToInject) {
//            List<Constructor<?>> constructors = getWithAnnotation(classToInject.getDeclaredConstructors());
//            Object instanceByConstructor = createInstanceByConstructor(constructors);
//            result.add(instanceByConstructor);

            final List<Field> fields = getWithAnnotation(classToInject.getDeclaredFields());
            final List<TreeNode<Field>> treeNodes = buildInjectableFieldsTree(fields);

            tree.put(classToInject, treeNodes);
        }
        return tree;
    }

    private List<TreeNode<Field>> buildInjectableFieldsTree(final List<Field> injectableFields) {
        final Set<TreeNode<Field>> fieldsTree = new HashSet<>();

        TreeNode<Field> root = null;
        for (Field field : injectableFields) {
            root = new TreeNode<>(field);
            final Class<?> fieldClass = field.getType();
            try {
                final List<Field> withAnnotation = getWithAnnotation(fieldClass.getDeclaredFields());
                if (!withAnnotation.isEmpty()) {
                    root.addChildren(buildInjectableFieldsTree(withAnnotation));
                } else {
                    fieldsTree.add(root);
                }
            } catch (Exception e) {
                LOGGER.info("Couldn't handle the next field: " + fieldClass);
            }
        }
        fieldsTree.add(root);
        LOGGER.info("Injectable fields have been collected");
        return new ArrayList<>(fieldsTree);
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

