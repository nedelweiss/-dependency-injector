package dependency_injector.custom_annotations.annotation_processor;

import dependency_injector.custom_annotations.Inject;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class InjectProcessor {

    private final Logger LOGGER = Logger.getLogger(InjectProcessor.class.getName());

    public List<Object> inject(final List<Class<?>> classesToInject) {
        final List<Object> result = new ArrayList<>();
        for (Class<?> classToInject : classesToInject) {
//            List<Constructor<?>> constructors = getWithAnnotation(classToInject.getDeclaredConstructors());
//            Object instanceByConstructor = createInstanceByConstructor(constructors);
//            result.add(instanceByConstructor);

            List<Field> fields = getWithAnnotation(classToInject.getDeclaredFields());
            result.addAll(createInstanceByField(fields));
        }
        return result;
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

    private List<Object> createInstanceByField(final List<Field> injectableFields) {
        List<Object> createdInstances = new ArrayList<>();
        for (Field field : injectableFields) {
            Class<?> classOfField = field.getType();
            try {
                List<Field> withAnnotation = getWithAnnotation(classOfField.getDeclaredFields());
                if (!withAnnotation.isEmpty()) {
                    List<Object> instanceByField = createInstanceByField(withAnnotation);
                    createdInstances.addAll(instanceByField);
                }

                Constructor<?> constructorWithoutParameters = processConstructors(classOfField.getConstructors());
                createdInstances.add(constructorWithoutParameters.newInstance());
                LOGGER.info("The instance has been created: " + classOfField);
            } catch (Exception e) {
                LOGGER.info("Couldn't create instance of: " + classOfField);
            }
        }
        return createdInstances;
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
                .collect(Collectors.toList());
    }

    private static final class NotOnlyConstructorException extends RuntimeException {

        NotOnlyConstructorException(String message) {
            super(message);
        }
    }
}

