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

    public List<Object> inject(List<Class<?>> classesToInject) {
        final List<Object> result = new ArrayList<>();
        for (Class<?> classToInject : classesToInject) {
            List<Constructor<?>> constructors = getWithAnnotation(classToInject.getDeclaredConstructors());
            Object instanceByConstructor = createInstanceByConstructor(constructors);

            List<Field> fields = getWithAnnotation(classToInject.getDeclaredFields());
            Object instanceByField = createInstanceByField(fields);

            result.add(instanceByConstructor);
            result.add(instanceByField);
        }
        return result;
    }

    private Object createInstanceByConstructor(List<Constructor<?>> injectableByConstructors) {
        Object instance = null;
        try {
            Constructor<?> constructor = processConstructors(injectableByConstructors.toArray(new Constructor<?>[0]));
            instance = constructor.newInstance();
        } catch (Exception e) {
            LOGGER.info("No constructors found"); // TODO: but what about empty constructor which always exists?
        }
        return instance;
    }

    // TODO: recursive approach is needed?
    private Object createInstanceByField(List<Field> injectableFields) {
        Object instance = null;
        for (Field field : injectableFields) {
            try {
                Constructor<?> constructorWithoutParameters = processConstructors(field.getType().getConstructors());
                instance = constructorWithoutParameters.newInstance();
            } catch (Exception e) {
                LOGGER.info("Not the only constructor found");
            }
        }
        return instance;
    }

    private Constructor<?> processConstructors(Constructor<?>[] constructors) throws NotOnlyConstructorException {
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() == 0) {
                return constructor;
            }
        }

        // TODO: additional logic is needed?
        // process the remaining constructors - check for annotation: @Inject
        List<Constructor<?>> constructorsWithInjectAnnotation = getWithAnnotation(constructors);
        if (constructorsWithInjectAnnotation.size() != 1) {
            throw new NotOnlyConstructorException("Unable to define constructor");
        }
        return constructorsWithInjectAnnotation.get(0);
    }

    private <T extends AccessibleObject> List<T> getWithAnnotation(T[] injectableClassItems) {
        return Arrays.stream(injectableClassItems)
                .filter(object -> Optional.ofNullable(object.getAnnotation(Inject.class)).isPresent())
                .collect(Collectors.toList());
    }

    private static class NotOnlyConstructorException extends Exception {
        public NotOnlyConstructorException(String message) {
            super(message);
        }
    }
}

