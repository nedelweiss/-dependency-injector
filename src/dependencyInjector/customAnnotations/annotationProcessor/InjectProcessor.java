package dependencyInjector.customAnnotations.annotationProcessor;

import dependencyInjector.customAnnotations.Inject;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InjectProcessor {
    public List<Object> inject(List<Class<?>> classes) {
        for (Class c : classes) {
            List<Constructor> constructors = getWithAnnotation(c.getDeclaredConstructors());
            List<Field> fields = getWithAnnotation(c.getDeclaredFields());

            // TODO: make injection
        }
        return Collections.emptyList();
    }

    private <T extends AccessibleObject> List<T> getWithAnnotation(T[] objects) {
        return Arrays.stream(objects)
                .filter(object -> Optional.ofNullable(object.getAnnotation(Inject.class)).isPresent())
                .collect(Collectors.toList());
    }
}

