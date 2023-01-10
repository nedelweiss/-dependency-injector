package dependency_injector.class_marker.processor;

import java.lang.reflect.Field;

public record InjectableMetadata(Field field, Class<?> aClass) {
}
