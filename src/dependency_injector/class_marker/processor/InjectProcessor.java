package dependency_injector.class_marker.processor;

import dependency_injector.class_marker.Inject;

import java.lang.reflect.AccessibleObject;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class InjectProcessor {

    public <T extends AccessibleObject> List<T> getWithAnnotation(final T[] injectableClassItems) {
        return Arrays.stream(injectableClassItems)
            .filter(object -> Optional.ofNullable(object.getAnnotation(Inject.class)).isPresent())
            .toList();
    }
}
