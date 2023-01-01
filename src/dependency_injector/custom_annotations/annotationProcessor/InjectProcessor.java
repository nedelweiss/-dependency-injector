package dependency_injector.custom_annotations.annotationProcessor;

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
    private final Logger logger = Logger.getLogger(InjectProcessor.class.getName());

    public List<Object> inject(List<Class<?>> classes) {
        List<Object> objects = new ArrayList<>();
        for (Class<?> c : classes) {
            List<Constructor<?>> constructors = getWithAnnotation(c.getDeclaredConstructors());
            List<Field> fields = getWithAnnotation(c.getDeclaredFields());

            Object instanceByConstructor = createInstanceByConstructor(constructors);
            Object instanceByField = createInstanceByField(fields);

            objects.add(instanceByConstructor);
            objects.add(instanceByField);
        }
        return objects;
    }

    private Object createInstanceByConstructor(List<Constructor<?>> constructors) {
        Object instance = null;
        try {
            Constructor<?> constructor = processConstructors(constructors.toArray(new Constructor<?>[0]));
            instance = constructor.newInstance();
        } catch (Exception e) {
            logger.info("No constructors found");
        }
        return instance;
    }

    private Object createInstanceByField(List<Field> fields) {
        Object instance = null;
        for (Field field : fields) {
            try {
                Constructor<?> constructorWithoutParameters = processConstructors(field.getType().getConstructors());
                instance = constructorWithoutParameters.newInstance();
            } catch (Exception e) {
                logger.info("Not the only constructor found");
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

        // process the remaining constructors - check for annotation: @Inject
        List<Constructor<?>> constructorsWithAnnotation = getWithAnnotation(constructors);
        if (constructorsWithAnnotation.size() != 1) {
            throw new NotOnlyConstructorException("Unable to define constructor");
        }
        return constructorsWithAnnotation.get(0);
    }

    private <T extends AccessibleObject> List<T> getWithAnnotation(T[] objects) {
        return Arrays.stream(objects)
                .filter(object -> Optional.ofNullable(object.getAnnotation(Inject.class)).isPresent())
                .collect(Collectors.toList());
    }

    private static class NotOnlyConstructorException extends Exception {
        public NotOnlyConstructorException(String message) {
            super(message);
        }
    }
}

