package dependency_injector.class_marker.class_marker_processor;

import java.lang.reflect.Field;

public record InjectableMetadata(Field field, Class<?> aClass) {
}
