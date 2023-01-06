package dependency_injector.custom_annotations.annotation_processor;

import java.lang.reflect.Field;

public class InjectableMetadata {

    private final Field field;
    private final Class<?> aClass;

    public InjectableMetadata(Field field, Class<?> aClass) {
        this.field = field;
        this.aClass = aClass;
    }

    public Field getField() {
        return field;
    }

    public Class<?> getaClass() {
        return aClass;
    }
}
